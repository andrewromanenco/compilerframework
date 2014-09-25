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

import com.romanenco.cfrm.Grammar;
import com.romanenco.cfrm.grammar.NonTerminal;
import com.romanenco.cfrm.grammar.Production;

final class GrammarValidator {

    private GrammarValidator() {
        // nothing
    }

    public static void validateGrammar(Grammar grammar) {
        confirmStartSymbolPresents(grammar);
        for (final NonTerminal nonTerm: grammar.getNonTerminals()) {
            confirmNonTerminalHasProduction(grammar, nonTerm);
        }
    }

    private static void confirmStartSymbolPresents(Grammar grammar) {
        if (grammar.getStartSymbol() == null) {
            throw new GrammarBuilderError("StartSymbol is not declared");
        }
    }

    private static void confirmNonTerminalHasProduction(Grammar grammar, NonTerminal nonTerm) {
        for (final Production production: grammar.getProductions()) {
            if (production.getLeft().equals(nonTerm)) {
                return;
            }
        }
        throw new GrammarBuilderError("NonTerminal has no productions: " + nonTerm);
    }
}
