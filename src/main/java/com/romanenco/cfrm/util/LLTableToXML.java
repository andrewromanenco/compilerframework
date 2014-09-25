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

import java.util.Map;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.romanenco.cfrm.grammar.NonTerminal;
import com.romanenco.cfrm.grammar.Production;
import com.romanenco.cfrm.grammar.Terminal;
import com.romanenco.cfrm.llparser.LookUpTable;

public final class LLTableToXML extends AbstractXmlBuilder<LookUpTable> {

    private static final String ROOT = "LLTable";
    private static final String TERMINAL = "Terminal";
    private static final String NON_TERMINAL = "NonTerminal";
    private static final String SYMBOL = "symbol";

    private LLTableToXML(LookUpTable table) {
        super(ROOT, table);
    }

    public static Document build(LookUpTable table) {
        final LLTableToXML util = new LLTableToXML(table);
        util.buildXmlDocument();
        return util.getDocument();
    }

    @Override
    public void buildXmlDocument() {
        final Map<NonTerminal, Map<Terminal, Production>> table = data.getTable();
        for (final Map.Entry<NonTerminal, Map<Terminal, Production>> entry:
            table.entrySet()) {
            addFirstLevelNode(entry.getKey(), entry.getValue());
        }
    }

    private void addFirstLevelNode(NonTerminal key,
            Map<Terminal, Production> items) {
        final Element nonTerm = document.createElement(NON_TERMINAL);
        nonTerm.setAttribute(SYMBOL, key.toString());
        for (final Map.Entry<Terminal, Production> entry: items.entrySet()) {
            addSecondLevel(nonTerm, entry.getKey(), entry.getValue());
        }

        rootElement.appendChild(nonTerm);
    }

    private void addSecondLevel(Element nonTerm, Terminal key, Production production) {
        final Element termElem = document.createElement(TERMINAL);
        termElem.setAttribute(SYMBOL, key.toString());
        termElem.appendChild(document.createTextNode(production.toString()));
        nonTerm.appendChild(termElem);
    }

}
