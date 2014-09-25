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
import java.util.Map;
import java.util.Set;

import com.romanenco.cfrm.Grammar;
import com.romanenco.cfrm.grammar.NonTerminal;
import com.romanenco.cfrm.grammar.Production;
import com.romanenco.cfrm.grammar.Symbol;
import com.romanenco.cfrm.grammar.Terminal;

public class LookUpTable {

    private final Map<NonTerminal, Map<Terminal, Production>> parsingTable;

    public LookUpTable(Grammar grammar, FirstSets firstSets,
            FollowSets followSets) {
        final LookUpTableBuilder builder = new LookUpTableBuilder(grammar,
                firstSets, followSets);
        parsingTable = builder.build();
    }

    public Production getTableCell(NonTerminal nonTerm, Terminal term) {
        final Map<Terminal, Production> row = parsingTable.get(nonTerm);
        return row.get(term);
    }

    public Map<NonTerminal, Map<Terminal, Production>> getTable() {
        return parsingTable;
    }

    private static class LookUpTableBuilder {

        private final Grammar grammar;
        private final FirstSets firstSets;
        private final FollowSets followSets;
        private final Map<NonTerminal, Map<Terminal, Production>> parsingTable = new HashMap<>();

        protected LookUpTableBuilder(Grammar grammar, FirstSets firstSets,
                FollowSets followSets) {
            this.grammar = grammar;
            this.firstSets = firstSets;
            this.followSets = followSets;
        }

        protected Map<NonTerminal, Map<Terminal, Production>> build() {
            initTableWithEmptyRows();
            for (final Production production : grammar.getProductions()) {
                initTableForProduction(production);
            }
            return parsingTable;
        }

        private void initTableWithEmptyRows() {
            for (final NonTerminal nonTerm: grammar.getNonTerminals()) {
                parsingTable.put(nonTerm,
                        new HashMap<Terminal, Production>());
            }
        }

        private void initTableForProduction(Production production) {
            final Set<Symbol> first = firstSets.getFirstSet(
                    production.getRight(), 0);
            final Map<Terminal, Production> row = parsingTable.get(production
                    .getLeft());
            handleAllFirsts(production, row, first);
            if (first.contains(grammar.getEpsilon())) {
                handleEpsilonProduction(production, row);
            }
        }

        private void handleAllFirsts(Production production,
                Map<Terminal, Production> row, Set<Symbol> first) {
            for (final Symbol symbol : first) {
                handleTerminal(symbol, production, row);
            }
        }

        private void handleEpsilonProduction(Production production,
                Map<Terminal, Production> row) {
            for (final Symbol symbol : followSets.getFollowSet(production
                    .getLeft())) {
                if (symbol == grammar.getEpsilon()) {
                    continue;
                }
                handleTerminal(symbol, production, row);
            }
        }

        private void handleTerminal(Symbol symbol, Production production,
                Map<Terminal, Production> row) {
            if (symbol instanceof Terminal) {
                if (row.containsKey(symbol)) {
                    handleAmbiguity(row.get(symbol), production);
                }
                row.put((Terminal) symbol, production);
            }
        }

        private void handleAmbiguity(Production current, Production production) {
            throw new ParsingError("LLTable ambiguity:\n" + current + "\n"
                    + production);
        }

    }

}
