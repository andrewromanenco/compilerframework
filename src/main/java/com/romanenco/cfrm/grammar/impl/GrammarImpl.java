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

package com.romanenco.cfrm.grammar.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.romanenco.cfrm.Grammar;
import com.romanenco.cfrm.grammar.EOF;
import com.romanenco.cfrm.grammar.Epsilon;
import com.romanenco.cfrm.grammar.GrammarException;
import com.romanenco.cfrm.grammar.NonTerminal;
import com.romanenco.cfrm.grammar.Production;
import com.romanenco.cfrm.grammar.Symbol;
import com.romanenco.cfrm.grammar.Terminal;

/**
 * Context-free grammar
 *
 */
public class GrammarImpl implements Grammar {

    private final Map<String, Symbol> symbolsMap = new LinkedHashMap<>();
    private final Set<Production> productions = new HashSet<>();

    private NonTerminal startSymbol;
    private Epsilon epsilon;
    private final EOF eof = new EOF();

    @Override
    public Set<NonTerminal> getNonTerminals() {
        final Set<NonTerminal> result = new HashSet<>();
        for (final Symbol symbol:symbolsMap.values()) {
            if (symbol instanceof NonTerminal) {
                result.add((NonTerminal)symbol);
            }
        }
        return result;
    }

    @Override
    public Set<Terminal> getTerminals() {
        final Set<Terminal> result = new HashSet<>();
        for (final Symbol symbol:symbolsMap.values()) {
            if (symbol instanceof Terminal) {
                result.add((Terminal)symbol);
            }
        }
        return result;
    }

    @Override
    public List<Terminal> getTerminalsAsList() {
        final List<Terminal> result = new ArrayList<>();
        for (final Symbol symbol:symbolsMap.values()) {
            if (symbol instanceof Terminal) {
                result.add((Terminal)symbol);
            }
        }
        return result;
    }

    @Override
    public Set<Production> getProductions() {
        return productions;
    }

    @Override
    public NonTerminal getStartSymbol() {
        return this.startSymbol;
    }

    @Override
    public Epsilon getEpsilon() {
        return this.epsilon;
    }

    @Override
    public EOF getEOF() {
        return this.eof;
    }

    @Override
    public Symbol getSymbol(String symbol) {
        return symbolsMap.get(symbol);
    }

    @Override
    public Production getProduction(String production) {
        for (final Production prod: this.productions) {
            if (prod.toString().equals(production)) {
                return prod;
            }
        }
        throw new GrammarException("No such production in the grammar: " + production);
    }

    public void addSymbol(Symbol symbol) {
        this.symbolsMap.put(symbol.getName(), symbol);
    }

    public void setEpsilon(Epsilon epsilon) {
        if (this.epsilon != null) {
            throw new GrammarException("Duplicate epsilon " + epsilon);
        }
        this.epsilon = epsilon;
        addSymbol(epsilon);
    }

    public void setStartSymbol(NonTerminal symbol) {
        if (this.startSymbol != null) {
            throw new GrammarException("Start symbol is already set to " + this.startSymbol);
        }
        this.startSymbol = symbol;
    }

    public void addProduction(Production production) {
        if (this.productions.contains(production)) {
            throw new GrammarException("Duplicate production: " + production);
        }
        this.productions.add(production);
    }

}
