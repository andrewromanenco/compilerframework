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

package com.romanenco.cfrm.grammar.example;

import com.romanenco.cfrm.grammar.builder.GrammarJBuilder;
import com.romanenco.cfrm.grammar.impl.GrammarImpl;
import com.romanenco.cfrm.lexer.LexerRule;

/**
 * Sample grammar from Dragon book
 * 4.28
 * page: 217
 *
 * Accepts integers and "+", "-", "(", ")"
 *
 */
public class DragonLLGrammar extends GrammarImpl {

    private static final String EPSILON = "epsilon";

    public DragonLLGrammar() {
        super();

        final GrammarJBuilder builder = new GrammarJBuilder(this);

        builder.declareNonTerminals("E", "T", "E'", "T'", "F");
        builder.declareStartSymbol("E");
        builder.declareEpsilon(EPSILON);

        builder.addTerminal("id", new LexerRule("\\d+"));
        builder.addTerminal("+", new LexerRule("\\+"));
        builder.addTerminal("*", new LexerRule("\\*"));
        builder.addTerminal("(", new LexerRule("\\("));
        builder.addTerminal(")", new LexerRule("\\)"));
        builder.addTerminal("WS", new LexerRule(" +", LexerRule.TYPE.IGNORE));

        builder.addProductions("E", "T E'");
        builder.addProductions("E'", "+ T E'", EPSILON);
        builder.addProductions("T", "F T'");
        builder.addProductions("T'", "* F T'", EPSILON);
        builder.addProductions("F", "( E )", "id");
    }

}
