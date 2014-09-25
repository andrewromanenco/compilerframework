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

package com.romanenco.cfrm.grammar;

import java.util.ArrayList;
import java.util.List;

import com.romanenco.cfrm.Grammar;

public class Production {

    private final Symbol left;
    private final List<Symbol> right;
    private final boolean epsilonMove;

    public Production(Grammar grammar, Symbol left, Symbol... right) {
        if (left == null) {
            throw new GrammarException("Left side is null");
        }
        if ((right == null) || (right.length == 0)) {
            throw new GrammarException("Empty rule for " + left);
        }
        this.left = left;
        this.right = new ArrayList<>(right.length);

        processRightSide(right);

        if ((right.length == 1) && (right[0] == grammar.getEpsilon())) {
            this.epsilonMove = true;
        } else {
            this.epsilonMove = false;
        }
    }

    private void processRightSide(Symbol[] right) {
        for (final Symbol rightSymbol : right) {
            this.right.add(rightSymbol);
        }
    }

    public Symbol getLeft() {
        return left;
    }

    public List<Symbol> getRight() {
        return right;
    }

    public boolean isEpsilonMove() {
        return epsilonMove;
    }

    @Override
    public int hashCode() {
        return right.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Production) {
            final Production production = (Production) obj;
            return left.equals(production.left)
                    && right.equals(production.right);
        }
        return false;
    }

    @Override
    public String toString() {
        final StringBuilder result = new StringBuilder();
        result.append(left.getName()).append(" ->");
        for (final Symbol symbol : right) {
            result.append(' ').append(symbol.getName());
        }
        return result.toString();
    }

}