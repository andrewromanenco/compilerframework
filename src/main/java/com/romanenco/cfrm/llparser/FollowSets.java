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
import java.util.Map;
import java.util.Set;

import com.romanenco.cfrm.Grammar;
import com.romanenco.cfrm.grammar.NonTerminal;
import com.romanenco.cfrm.grammar.Production;
import com.romanenco.cfrm.grammar.Symbol;
import com.romanenco.cfrm.grammar.Terminal;

public class FollowSets {

    private final Map<Symbol, Set<Symbol>> sets;

    public FollowSets(Grammar grammar, FirstSets firstSets) {
        final FSBuilder fsb = new FSBuilder(grammar, firstSets);
        sets = fsb.getSets();
    }

    public Set<Symbol> getFollowSet(Symbol symbol) {
        return sets.get(symbol);
    }

    public Map<Symbol, Set<Symbol>> getFollowSets() {
        return sets;
    }

    private static class FSBuilder {

        private final Map<Symbol, Set<Symbol>> sets = new HashMap<>();

        FSBuilder(Grammar grammar, FirstSets firstSets) {
            preInitSets(grammar.getTerminals(), grammar.getNonTerminals());
            addToFollowSet(grammar.getStartSymbol(), grammar.getEOF());
            boolean changed = true;
            while (changed) {
                changed = false;
                for (final Production production : grammar.getProductions()) {
                    changed = processProduction(production, grammar, firstSets)
                            | changed;
                }
            }
        }

        private void preInitSets(Set<Terminal> terminals,
                Set<NonTerminal> nonTerminals) {
            for (final Symbol symbol : terminals) {
                sets.put(symbol, new HashSet<Symbol>());
            }
            for (final Symbol symbol : nonTerminals) {
                sets.put(symbol, new HashSet<Symbol>());
            }
        }

        private boolean processProduction(Production production, Grammar grammar,
                FirstSets firstSets) {
            if (production.isEpsilonMove()) {
                return false;
            }
            boolean result = false;
            int index = 0;
            for (final Symbol symbol : production.getRight()) {
                final Set<Symbol> follow = sets.get(symbol);
                final Set<Symbol> first = firstSets.getFirstSet(
                        production.getRight(), index + 1);
                if ((first.remove(grammar.getEpsilon())
                        || (index == production.getRight().size() - 1))
                            && follow.addAll(sets.get(production.getLeft()))) {
                        result = true;
                }
                if (follow.addAll(first)) {
                    result = true;
                }
                index++;
            }
            return result;
        }

        private void addToFollowSet(Symbol symbol, Symbol follower) {
            sets.get(symbol).add(follower); 
        }

        public Map<Symbol, Set<Symbol>> getSets() {
            return sets;
        }
    }

}
