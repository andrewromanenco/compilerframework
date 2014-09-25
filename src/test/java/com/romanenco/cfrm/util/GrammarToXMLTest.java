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

import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.romanenco.cfrm.Grammar;
import com.romanenco.cfrm.TestUtils;
import com.romanenco.cfrm.grammar.example.DragonLLGrammar;

public class GrammarToXMLTest {

    @Test
    public void test() {
        final Grammar grammar = new DragonLLGrammar();
        final Document xml = GrammarToXML.build(grammar);
        final Element root = xml.getDocumentElement();
        Assert.assertEquals("Grammar", root.getNodeName());
        Assert.assertEquals("com.romanenco.cfrm.grammar.example.DragonLLGrammar",
                TestUtils.getAttr(root, "class"));
        Assert.assertEquals(5, root.getChildNodes().getLength());

        final Node start = root.getChildNodes().item(0);
        Assert.assertEquals("E", TestUtils.getAttr(start, "name"));

        final Node epsilon = root.getChildNodes().item(1);
        Assert.assertEquals("epsilon", TestUtils.getAttr(epsilon, "name"));

        final Node nonTerminals = root.getChildNodes().item(2);
        Assert.assertEquals(5, nonTerminals.getChildNodes().getLength());

        final Node terminals = root.getChildNodes().item(3);
        Assert.assertEquals(6, terminals.getChildNodes().getLength());

        final Node productions = root.getChildNodes().item(4);
        Assert.assertEquals(8, productions.getChildNodes().getLength());
    }

}
