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
import org.w3c.dom.NodeList;

import com.romanenco.cfrm.Grammar;
import com.romanenco.cfrm.grammar.example.DragonLLGrammar;
import com.romanenco.cfrm.llparser.FirstSets;
import com.romanenco.cfrm.llparser.FollowSets;
import com.romanenco.cfrm.llparser.LookUpTable;

public class LLTableToXMLTest {

    @Test
    public void test() {
        final Grammar grammar = new DragonLLGrammar();
        final FirstSets firstSets = new FirstSets(grammar);
        final FollowSets followSets = new FollowSets(grammar, firstSets);
        final LookUpTable table = new LookUpTable(grammar, firstSets, followSets);

        final Document xml = LLTableToXML.build(table);

        final Element root = xml.getDocumentElement();
        Assert.assertEquals("LLTable", root.getNodeName());
        final NodeList list = root.getChildNodes();
        Assert.assertEquals(5, list.getLength());
    }
}
