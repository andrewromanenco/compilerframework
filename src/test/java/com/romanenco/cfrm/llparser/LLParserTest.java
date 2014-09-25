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

package com.romanenco.cfrm.llparser;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.romanenco.cfrm.Grammar;
import com.romanenco.cfrm.Parser;
import com.romanenco.cfrm.ParsingTreeNode;
import com.romanenco.cfrm.ParsingTreeValidator;
import com.romanenco.cfrm.Token;
import com.romanenco.cfrm.grammar.builder.GrammarJBuilder;

public class LLParserTest {

    private Grammar getGrammar() {
        final GrammarJBuilder builder = new GrammarJBuilder();

        builder.declareNonTerminals("E", "T", "E'", "T'", "F");
        builder.declareStartSymbol("E");
        builder.declareEpsilon("epsilon");

        builder.addTerminal("+");
        builder.addTerminal("*");
        builder.addTerminal("(");
        builder.addTerminal(")");
        builder.addTerminal("id");

        builder.addProductions("E", "T E'");
        builder.addProductions("E'", "+ T E'", "epsilon");
        builder.addProductions("T", "F T'");
        builder.addProductions("T'", "* F T'", "epsilon");
        builder.addProductions("F", "( E )", "id");

        return builder.getGrammar();
    }

    private Parser getParser(Grammar grammar) {
        return new LLParser(grammar);
    }

    @Test
    public void goodTest() {
        final Grammar grammar = getGrammar();
        final Parser parser = getParser(grammar);

        String input = "id + id * id";
        List<Token> tokens = ParserUtil.makeListOfTerminals(grammar, input);
        ParsingTreeNode parsingTree = parser.parse(tokens);
        Assert.assertTrue(ParsingTreeValidator.isValid(parsingTree));

        input = "id * ( id + id ) * id";
        tokens = ParserUtil.makeListOfTerminals(grammar, input);
        parsingTree = parser.parse(tokens);
        Assert.assertTrue(ParsingTreeValidator.isValid(parsingTree));
    }

    @Test(expected = ParsingError.class)
    public void badTest() {
        final Grammar grammar = getGrammar();
        final Parser parser = getParser(grammar);

        final String input = "id + id * * id";
        final List<Token> tokens = ParserUtil.makeListOfTerminals(grammar, input);
        final ParsingTreeNode parsingTree = parser.parse(tokens);
        Assert.assertTrue(ParsingTreeValidator.isValid(parsingTree));
    }

    @Test(expected = ParsingError.class)
    public void alsoBadTest() {
        final Grammar grammar = getGrammar();
        final Parser parser = getParser(grammar);

        final String input = "id * ( id + id";
        final List<Token> tokens = ParserUtil.makeListOfTerminals(grammar, input);
        final ParsingTreeNode parsingTree = parser.parse(tokens);
        Assert.assertTrue(ParsingTreeValidator.isValid(parsingTree));
    }

}
