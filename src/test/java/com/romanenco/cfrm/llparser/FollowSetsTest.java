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
import com.romanenco.cfrm.grammar.example.DragonLLGrammar;

public class FollowSetsTest {

    @Test
    public void test() {
        final Grammar grammar = new DragonLLGrammar();
        final FirstSets firstSets = new FirstSets(grammar);
        final FollowSets followSets = new FollowSets(grammar, firstSets);

        Set<Symbol> set;

        set = followSets.getFollowSet(grammar.getSymbol("T"));
        Assert.assertEquals(3, set.size());
        Assert.assertTrue(set.contains(grammar.getSymbol("+")));
        Assert.assertTrue(set.contains(grammar.getSymbol(")")));
        Assert.assertTrue(set.contains(grammar.getEOF()));

        set = followSets.getFollowSet(grammar.getSymbol("T'"));
        Assert.assertEquals(3, set.size());
        Assert.assertTrue(set.contains(grammar.getSymbol("+")));
        Assert.assertTrue(set.contains(grammar.getSymbol(")")));
        Assert.assertTrue(set.contains(grammar.getEOF()));

        set = followSets.getFollowSet(grammar.getSymbol("F"));
        Assert.assertEquals(4, set.size());
        Assert.assertTrue(set.contains(grammar.getSymbol("+")));
        Assert.assertTrue(set.contains(grammar.getSymbol("*")));
        Assert.assertTrue(set.contains(grammar.getSymbol(")")));
        Assert.assertTrue(set.contains(grammar.getEOF()));
    }

}
