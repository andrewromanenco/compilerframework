/*
 * Copyright 2014 Andrew Romanenco
 * www.romanenco.com
 * andrew@romanenco.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.romanenco.cfrm;

import org.junit.Assert;
import org.junit.Test;

import com.romanenco.cfrm.grammar.builder.GrammarJBuilder;
import com.romanenco.cfrm.lexer.LexerError;
import com.romanenco.cfrm.lexer.LexerRule;
import com.romanenco.cfrm.lexer.RegLexer;
import com.romanenco.cfrm.llparser.LLParser;
import com.romanenco.cfrm.llparser.ParsingError;

public class LexerLLParserTest {

    private Grammar getGrammar() {
        final GrammarJBuilder builder = new GrammarJBuilder();

        builder.declareNonTerminals("E", "T", "E'", "T'", "F");
        builder.declareStartSymbol("E");
        builder.declareEpsilon("epsilon");

        builder.addTerminal("id", new LexerRule("\\d+"));
        builder.addTerminal("+", new LexerRule("\\+"));
        builder.addTerminal("*", new LexerRule("\\*"));
        builder.addTerminal("(", new LexerRule("\\("));
        builder.addTerminal(")", new LexerRule("\\)"));
        builder.addTerminal("WS", new LexerRule(" +", LexerRule.TYPE.IGNORE));

        builder.addProductions("E", "T E'");
        builder.addProductions("E'", "+ T E'", "epsilon");
        builder.addProductions("T", "F T'");
        builder.addProductions("T'", "* F T'", "epsilon");
        builder.addProductions("F", "( E )", "id");

        return builder.getGrammar();
    }

    private Lexer getLexer(Grammar grammar) {
        return new RegLexer(grammar);
    }

    private Parser getParser(Grammar grammar) {
        return new LLParser(grammar);
    }

    @Test
    public void goodTest() {
        final Grammar grammar = getGrammar();
        final Lexer lexer = getLexer(grammar);
        final Parser parser = getParser(grammar);

        final String input = "(45+12) * 45 + 45 + (5 + 5*6)";

        final ParsingTreeNode parsingTree = parser.parse(lexer.tokenize(input));
        Assert.assertTrue(ParsingTreeValidator.isValid(parsingTree));
    }

    @Test(expected = LexerError.class)
    public void badLexerTest() {
        final Grammar grammar = getGrammar();
        final Lexer lexer = getLexer(grammar);
        final Parser parser = getParser(grammar);

        final String input = "44*X+12";

        final ParsingTreeNode parsingTree = parser.parse(lexer.tokenize(input));
        Assert.assertTrue(ParsingTreeValidator.isValid(parsingTree));
    }

    @Test(expected = ParsingError.class)
    public void badParserTest() {
        final Grammar grammar = getGrammar();
        final Lexer lexer = getLexer(grammar);
        final Parser parser = getParser(grammar);

        final String input = "44 ++ 45";

        final ParsingTreeNode parsingTree = parser.parse(lexer.tokenize(input));
        Assert.assertTrue(ParsingTreeValidator.isValid(parsingTree));
    }

}
