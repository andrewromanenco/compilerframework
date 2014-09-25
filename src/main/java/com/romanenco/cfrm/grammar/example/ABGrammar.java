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
 * Simple grammar for sum of two integers
 * @author Andrew Romanenco
 *
 */
public class ABGrammar extends GrammarImpl{

    public ABGrammar() {
        super();
        final GrammarJBuilder builder = new GrammarJBuilder(this);
        builder.declareNonTerminals("SUM");
        builder.declareStartSymbol("SUM");

        builder.addTerminal("int", new LexerRule("\\d+"));
        builder.addTerminal("+", new LexerRule("\\+"));

        builder.addProductions("SUM", "int + int");
    }
}
