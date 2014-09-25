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
import java.util.Set;

import org.w3c.dom.Element;

import com.romanenco.cfrm.grammar.Symbol;

public final class SetToXMLUtil extends AbstractXmlBuilder<Map<Symbol, Set<Symbol>>> {

    private static final String TREE_NODE = "Symbol";
    private static final String ATTR_NAME = "name";

    protected SetToXMLUtil(String treeRoot, Map<Symbol, Set<Symbol>> data) {
        super(treeRoot, data);
    }

    @Override
    public void buildXmlDocument() {
        for (final Map.Entry<Symbol, Set<Symbol>> entry: data.entrySet()) {
            final Element node = createSymbolElement(entry.getKey());
            rootElement.appendChild(node);
            appendChildren(node, entry.getValue());
        }
    }

    private void appendChildren(Element node, Set<Symbol> set) {
        for (final Symbol symbol: set) {
            node.appendChild(createSymbolElement(symbol));
        }
    }

    private Element createSymbolElement(Symbol key) {
        final Element node = document.createElement(TREE_NODE);
        node.setAttribute(ATTR_NAME, key.toString());
        return node;
    }

}
