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

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;

import com.romanenco.cfrm.Grammar;
import com.romanenco.cfrm.grammar.NonTerminal;
import com.romanenco.cfrm.grammar.Production;
import com.romanenco.cfrm.grammar.Symbol;

public class FirstSets {

    private final Map<Symbol, Set<Symbol>> sets;
    private final Symbol epsilon;

    public FirstSets(Grammar grammar) {
        final FirstSetsBuilder fsb = new FirstSetsBuilder(grammar);
        sets = fsb.build();
        this.epsilon = grammar.getEpsilon();
    }

    public Set<Symbol> getFirstSet(Symbol symbol) {
        return sets.get(symbol);
    }

    public Map<Symbol, Set<Symbol>> getFirstSet() {
        return sets;
    }

    public Set<Symbol> getFirstSet(List<Symbol> production, int start) {
        final Set<Symbol> first = new HashSet<>();
        final ListIterator<Symbol> iter = production.listIterator(start);
        while (iter.hasNext()) {
            final Set<Symbol> firstSet = getFirstSet(iter.next());
            first.addAll(firstSet);
            if (!firstSet.contains(epsilon)) {
                break;
            }
        }
        return first;
    }

    private static class FirstSetsBuilder {

        private final Map<Symbol, Set<Symbol>> sets = new HashMap<>();
        private final Grammar grammar;

        private final Set<Symbol> recursionSentinel = new HashSet<>();

        FirstSetsBuilder(Grammar grammar) {
            this.grammar = grammar;
        }

        public Map<Symbol, Set<Symbol>> build() {
            touchToInit();
            return sets;
        }

        private void touchToInit() {
            for (final Symbol symbol : grammar.getTerminals()) {
                getFirstSet(symbol);
            }
            for (final Symbol symbol : grammar.getNonTerminals()) {
                getFirstSet(symbol);
            }
        }

        private Set<Symbol> getFirstSet(Symbol symbol) {
            Set<Symbol> first = sets.get(symbol);
            if (recursionSentinel.equals(first)) {
                throw new ParsingError(
                        "Fail building first set. Recursion detected for " + symbol);
            }
            if (first != null) {
                return first;
            }
            first = new HashSet<>();
            sets.put(symbol, recursionSentinel);
            if (symbol instanceof NonTerminal) {
                handleNonTerminal((NonTerminal)symbol, first);
            } else {
                first.add(symbol);
            }
            sets.put(symbol, first);
            return first;
        }

        private void handleNonTerminal(NonTerminal symbol,  Set<Symbol> first) {
            for (final Production production : grammar.getProductions()) {
                if (production.getLeft() == symbol) {
                    processRightSide(production.getRight(), first);
                }
            }
        }

        private void processRightSide(List<Symbol> right, Set<Symbol> first) {
            for (final Symbol prodSymbol : right) {
                final Set<Symbol> firstSet = getFirstSet(prodSymbol);
                first.addAll(firstSet);
                if (!hasEpsilon(firstSet)) {
                    break;
                }
            }
        }

        private boolean hasEpsilon(Set<Symbol> symbols) {
            return symbols.contains(grammar.getEpsilon());
        }

    }

}
