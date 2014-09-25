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

package com.romanenco.cfrm.util;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import com.romanenco.cfrm.TestUtils;
import com.romanenco.cfrm.grammar.NonTerminal;
import com.romanenco.cfrm.grammar.Symbol;

public class SetToXMLUtilTest {

    private static final Symbol A_SYMBOL = new NonTerminal("A");
    private static final Symbol B_SYMBOL = new NonTerminal("B");
    private static final Symbol C_SYMBOL = new NonTerminal("C");

    @Test
    public void test() {
        final Map<Symbol, Set<Symbol>> input = buildTestInput();
        final SetToXMLUtil util = new SetToXMLUtil("ROOT", input);
        util.buildXmlDocument();
        final Document result = util.getDocument();
        Assert.assertNotNull(result);
        final Node root = result.getChildNodes().item(0);
        Assert.assertEquals("ROOT", root.getNodeName());
        Assert.assertEquals(1, root.getChildNodes().getLength());

        final Node nodeA = root.getChildNodes().item(0);
        Assert.assertEquals("A", TestUtils.getAttr(nodeA, "name"));
        Assert.assertEquals(2, nodeA.getChildNodes().getLength());

        final Node nodeB = nodeA.getChildNodes().item(0);
        Assert.assertEquals("B", TestUtils.getAttr(nodeB, "name"));
    }

    private Map<Symbol, Set<Symbol>> buildTestInput() {
        final Map<Symbol, Set<Symbol>> map = new HashMap<>();
        final Set<Symbol> set1 = new HashSet<>();
        set1.add(B_SYMBOL);
        set1.add(C_SYMBOL);
        map.put(A_SYMBOL, set1);
        return map;
    }

}
