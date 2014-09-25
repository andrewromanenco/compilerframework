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

import javax.xml.parsers.ParserConfigurationException;

import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.romanenco.cfrm.ParsingTreeNode;
import com.romanenco.cfrm.TestUtils;
import com.romanenco.cfrm.Token;
import com.romanenco.cfrm.grammar.NonTerminal;
import com.romanenco.cfrm.grammar.Terminal;

public class ParsingTreeToXMLTest {

    private static final String ID_A = "a";
    private static final String ID_B = "b";

    private final Terminal idTerm = new Terminal("id");
    private final NonTerminal plusTerm = new NonTerminal("+");

    /**
     * Tree for expression id + id
     *
     *     term(+)
     *     |     |
     * term(a)   term(b)
     *
     * @return
     */
    private ParsingTreeNode makeTreeAplusB() {
        final ParsingTreeNode root = new ParsingTreeNode(plusTerm);
        root.add(makeIdNode(ID_A, -1, -1));
        root.add(makeIdNode(ID_B, 10, 20));
        return root;
    }

    private ParsingTreeNode makeIdNode(String value, int line, int column) {
        final ParsingTreeNode  node = new ParsingTreeNode(idTerm);
        node.setValue(new Token(idTerm, value, line, column));
        return node;
    }

    @Test
    public void test() throws ParserConfigurationException {
        final ParsingTreeNode tree = makeTreeAplusB();
        final Document xmlTree = ParsingTreeToXML.build(tree);
        Assert.assertNotNull(xmlTree);
        final Element root = xmlTree.getDocumentElement();
        Assert.assertEquals("ParsingTree", root.getNodeName());
        Assert.assertEquals(1, root.getChildNodes().getLength());

        final Node nonTerm = root.getChildNodes().item(0);
        Assert.assertEquals("NonTerm", nonTerm.getNodeName());
        Assert.assertEquals("+", TestUtils.getAttr(nonTerm, "symbol"));
        Assert.assertEquals(2, nonTerm.getChildNodes().getLength());

        final Node term = nonTerm.getChildNodes().item(1);
        Assert.assertEquals("Term", term.getNodeName());
        Assert.assertEquals("id", TestUtils.getAttr(term, "symbol"));
        Assert.assertEquals("10", TestUtils.getAttr(term, "line"));
        Assert.assertEquals("20", TestUtils.getAttr(term, "column"));
        Assert.assertEquals(1, term.getChildNodes().getLength());

        final Node text = term.getChildNodes().item(0);
        Assert.assertTrue(text.getNodeType() == Node.TEXT_NODE);
        Assert.assertEquals("b", text.getTextContent());
    }

}
