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

import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

import com.romanenco.cfrm.Grammar;
import com.romanenco.cfrm.grammar.Symbol;
import com.romanenco.cfrm.grammar.Terminal;
import com.romanenco.cfrm.grammar.example.DragonLLGrammar;

public class FirstSetsTest {

    @Test
    public void test() {
        final Grammar grammar = new DragonLLGrammar();
        final FirstSets firsts = new FirstSets(grammar);
        for (final Terminal term: grammar.getTerminals()) {
            final Set<Symbol> set = firsts.getFirstSet(term);
            Assert.assertEquals(1, set.size());
            Assert.assertTrue(set.contains(term));
        }
        Set<Symbol> set;

        set = firsts.getFirstSet(grammar.getSymbol("F"));
        Assert.assertEquals(2, set.size());
        Assert.assertTrue(set.contains(grammar.getSymbol("(")));
        Assert.assertTrue(set.contains(grammar.getSymbol("id")));

        set = firsts.getFirstSet(grammar.getSymbol("T"));
        Assert.assertEquals(2, set.size());
        Assert.assertTrue(set.contains(grammar.getSymbol("(")));
        Assert.assertTrue(set.contains(grammar.getSymbol("id")));

        set = firsts.getFirstSet(grammar.getSymbol("E"));
        Assert.assertEquals(2, set.size());
        Assert.assertTrue(set.contains(grammar.getSymbol("(")));
        Assert.assertTrue(set.contains(grammar.getSymbol("id")));

        set = firsts.getFirstSet(grammar.getSymbol("E'"));
        Assert.assertEquals(2, set.size());
        Assert.assertTrue(set.contains(grammar.getSymbol("+")));
        Assert.assertTrue(set.contains(grammar.getEpsilon()));

        set = firsts.getFirstSet(grammar.getSymbol("T'"));
        Assert.assertEquals(2, set.size());
        Assert.assertTrue(set.contains(grammar.getSymbol("*")));
        Assert.assertTrue(set.contains(grammar.getEpsilon()));
    }

}
