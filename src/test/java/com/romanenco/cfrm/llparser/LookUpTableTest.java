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

import org.junit.Assert;
import org.junit.Test;

import com.romanenco.cfrm.Grammar;
import com.romanenco.cfrm.grammar.NonTerminal;
import com.romanenco.cfrm.grammar.Production;
import com.romanenco.cfrm.grammar.Terminal;
import com.romanenco.cfrm.grammar.example.DragonLLGrammar;

public class LookUpTableTest {

    @Test
    public void test() {
        final Grammar grammar = new DragonLLGrammar();
        final FirstSets firsts = new FirstSets(grammar);
        final FollowSets follows = new FollowSets(grammar, firsts);
        final LookUpTable table = new LookUpTable(grammar, firsts, follows);

        final NonTerminal nonTerminal = (NonTerminal)grammar.getSymbol("E'");
        final Terminal termPlus = (Terminal)grammar.getSymbol("+");
        final Terminal termMult = (Terminal)grammar.getSymbol("*");

        Production production = table.getTableCell(nonTerminal, termPlus);
        Assert.assertNotNull(production);
        Assert.assertEquals(nonTerminal, production.getLeft());
        Assert.assertEquals(3, production.getRight().size());

        production = table.getTableCell(nonTerminal, termMult);
        Assert.assertNull(production);
    }

}
