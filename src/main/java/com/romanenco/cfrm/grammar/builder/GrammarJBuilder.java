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

package com.romanenco.cfrm.grammar.builder;

import java.util.List;

import com.romanenco.cfrm.Grammar;
import com.romanenco.cfrm.grammar.Epsilon;
import com.romanenco.cfrm.grammar.NonTerminal;
import com.romanenco.cfrm.grammar.Production;
import com.romanenco.cfrm.grammar.Symbol;
import com.romanenco.cfrm.grammar.Terminal;
import com.romanenco.cfrm.grammar.impl.GrammarImpl;
import com.romanenco.cfrm.lexer.LexerRule;

public class GrammarJBuilder {

    private final GrammarImpl grammar;

    public GrammarJBuilder() {
        grammar = new GrammarImpl();
    }

    public GrammarJBuilder(GrammarImpl grammar) {
        this.grammar = grammar;
    }

    public void declareNonTerminals(String... nonTerminals) {
        for (final String name: nonTerminals) {
            grammar.addSymbol(new NonTerminal(name));
        }
    }

    public void declareEpsilon(String symbol) {
        grammar.setEpsilon(new Epsilon(symbol));
    }

    public void declareStartSymbol(String symbol) {
        final Symbol nonTerm = grammar.getSymbol(symbol);
        if (nonTerm instanceof NonTerminal) {
            grammar.setStartSymbol((NonTerminal)nonTerm);
        } else {
            throw new GrammarBuilderError("Start symbol has to be a Non Terminal");
        }
    }

    public void addTerminal(String symbol) {
        grammar.addSymbol(new Terminal(symbol));
    }

    public void addTerminal(String symbol, LexerRule lexerRule) {
        grammar.addSymbol(new Terminal(symbol, lexerRule));
    }

    public List<Production> addProductions(String leftSide,
            String... rightSides) {
        return ProductionBuilder.addProductions(grammar, leftSide, rightSides);
    }

    public void validateGrammar() {
        GrammarValidator.validateGrammar(grammar);
    }

    public Grammar getGrammar() {
        return grammar;
    }

}
