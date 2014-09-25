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

import org.junit.Test;

import com.romanenco.cfrm.Grammar;
import com.romanenco.cfrm.Parser;
import com.romanenco.cfrm.Token;
import com.romanenco.cfrm.grammar.builder.GrammarJBuilder;

public class LLParserAmbigousTest {

    private Grammar getGrammar() {
        final GrammarJBuilder builder = new GrammarJBuilder();

        builder.declareNonTerminals("S", "S'", "E");
        builder.declareStartSymbol("S");
        builder.declareEpsilon("epsilon");

        builder.addTerminal("if");
        builder.addTerminal("then");
        builder.addTerminal("statement");
        builder.addTerminal("else");
        builder.addTerminal("expression");

        builder.addProductions("S", "if E then S S'", "statement");
        builder.addProductions("S'", "else S", "epsilon");
        builder.addProductions("E", "expression");

        return builder.getGrammar();
    }

    private Parser getParser(Grammar grammar) {
        return new LLParser(grammar);
    }

    @Test( expected = ParsingError.class )
    public void test() {
        final LLParserAmbigousTest test = new LLParserAmbigousTest();
        final Grammar grammar = test.getGrammar();
        final Parser parser = test.getParser(grammar);

        final String input = "if expression then if expression then statement else statement";
        final List<Token> tokens = ParserUtil.makeListOfTerminals(grammar, input);
        parser.parse(tokens);
    }

}
