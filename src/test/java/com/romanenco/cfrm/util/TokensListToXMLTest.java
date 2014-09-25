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

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.romanenco.cfrm.TestUtils;
import com.romanenco.cfrm.Token;
import com.romanenco.cfrm.grammar.Terminal;

public class TokensListToXMLTest {

    private static final Terminal TERM_A = new Terminal("A");
    private static final Terminal TERM_B = new Terminal("B");

    @Test
    public void test() {
        final List<Token> input = buildInput();
        final Document xml = TokensListToXML.build(input);
        Assert.assertNotNull(xml);
        final Node root = xml.getChildNodes().item(0);
        Assert.assertEquals("Tokens", root.getNodeName());
        final NodeList children = root.getChildNodes();
        Assert.assertEquals(2, children.getLength());
        Assert.assertEquals("AAA", children.item(0).getTextContent());
        Assert.assertEquals("BBB", children.item(1).getTextContent());
        Assert.assertEquals("30", TestUtils.getAttr(children.item(1), "line"));
        Assert.assertEquals("40", TestUtils.getAttr(children.item(1), "column"));
        Assert.assertEquals("B", TestUtils.getAttr(children.item(1), "term"));
    }

    private List<Token> buildInput() {
        final List<Token> result = new ArrayList<>();
        result.add(new Token(TERM_A, "AAA", 10, 20));
        result.add(new Token(TERM_B, "BBB", 30, 40));
        return result;
    }

}
