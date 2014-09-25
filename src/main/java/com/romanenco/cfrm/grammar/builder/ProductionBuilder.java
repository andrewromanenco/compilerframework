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

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.romanenco.cfrm.grammar.Production;
import com.romanenco.cfrm.grammar.Symbol;
import com.romanenco.cfrm.grammar.impl.GrammarImpl;

final class ProductionBuilder {

    private ProductionBuilder() {
        // nothing
    }

    public static List<Production> addProductions(GrammarImpl grammar, String leftSide,
            String... rightSides) {
        final Symbol left = leftSideLookup(grammar, leftSide);
        if (left == null) {
            throw new GrammarBuilderError("No symbol for left side: " + leftSide);
        }
        final List<Production> result = new ArrayList<>(rightSides.length);
        initProductions(grammar, result, left, rightSides);
        return result;
    }

    private static Symbol leftSideLookup(GrammarImpl grammar, String leftSide) {
        return grammar.getSymbol(leftSide);
    }

    private static void initProductions(GrammarImpl grammar, List<Production> result, Symbol left,
            String[] rightSides) {
        for (final String rule : rightSides) {
            final List<Symbol> right = initSymbols(grammar, rule);
            final Production production = new Production(grammar, left,
                    right.toArray(new Symbol[right.size()]));
            grammar.addProduction(production);
            result.add(production);
        }
    }

    private static List<Symbol> initSymbols(GrammarImpl grammar, String rule) {
        final List<Symbol> result = new LinkedList<>();
        for (final String name : rule.split(" ")) {
            result.add(initSymbol(grammar, name));
        }
        return result;
    }

    private static Symbol initSymbol(GrammarImpl grammar, String name) {
        final Symbol symbol = grammar.getSymbol(name);
        if (symbol != null) {
            return symbol;
        }
        throw new GrammarBuilderError("Symbol was not yet declared: " + name);
    }

}
