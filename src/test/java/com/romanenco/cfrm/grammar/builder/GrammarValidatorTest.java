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

import org.junit.Assert;
import org.junit.Test;

import com.romanenco.cfrm.Grammar;
import com.romanenco.cfrm.grammar.NonTerminal;
import com.romanenco.cfrm.grammar.example.DragonLLGrammar;
import com.romanenco.cfrm.grammar.impl.GrammarImpl;

public class GrammarValidatorTest {

    @Test ()
    public void goodTest() {
        final Grammar grammar = new DragonLLGrammar();
        GrammarValidator.validateGrammar(grammar);
        Assert.assertNotNull(grammar);  // makes pmd happy
    }

    @Test( expected = GrammarBuilderError.class )
    public void noStartSymbolTest() {
        final GrammarImpl grammar = new GrammarImpl();
        grammar.addSymbol(new NonTerminal("T"));
        GrammarValidator.validateGrammar(grammar);
    }

    @Test( expected = GrammarBuilderError.class )
    public void noProductionBodyForANonTermTest() {
        final GrammarImpl grammar = new GrammarImpl();
        final NonTerminal nonTerm = new NonTerminal("T");
        grammar.addSymbol(nonTerm);
        grammar.setStartSymbol(nonTerm);
        GrammarValidator.validateGrammar(grammar);
    }

}
