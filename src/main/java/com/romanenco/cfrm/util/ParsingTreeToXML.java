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

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.romanenco.cfrm.ParsingTreeNode;
import com.romanenco.cfrm.grammar.NonTerminal;
import com.romanenco.cfrm.grammar.Production;

public final class ParsingTreeToXML extends AbstractXmlBuilder<ParsingTreeNode> {

    public static final String TERM = "Term";
    public static final String NON_TERM = "NonTerm";
    public static final String ATTR_COLUMN = "column";
    public static final String ATTR_LINE = "line";
    public static final String NODE_VALUE = "Value";
    public static final String ATTR_SYMBOL = "symbol";
    public static final String ATTR_PRODUCTION = "production";
    public static final String TREE_ROOT = "ParsingTree";

    private ParsingTreeToXML(ParsingTreeNode node) {
        super(TREE_ROOT, node);
    }

    @Override
    public void buildXmlDocument() {
        addSubNodes(rootElement, data);
    }

    public static Document build(ParsingTreeNode node) {
        final ParsingTreeToXML util = new ParsingTreeToXML(node);
        util.buildXmlDocument();
        return util.getDocument();
    }

    private void addSubNodes(Element parentXMLNode, ParsingTreeNode currentTreeNode) {
        final String xmlName = currentTreeNode.getSymbol() instanceof NonTerminal ? NON_TERM : TERM;
        final Element xmlNode = document.createElement(xmlName);
        parentXMLNode.appendChild(xmlNode);
        addSymbolAttribute(xmlNode, currentTreeNode);
        addProductionAttribute(xmlNode, currentTreeNode);
        addLexemeValue(xmlNode, currentTreeNode);
        appendAllChildren(xmlNode, currentTreeNode);
    }

    private void addSymbolAttribute(Element node,
            ParsingTreeNode treeNode) {
        final Attr symbol = document.createAttribute(ATTR_SYMBOL);
        symbol.setValue(treeNode.getSymbol().getName());
        node.setAttributeNode(symbol);
    }

    private void addProductionAttribute(Element node,
            ParsingTreeNode treeNode) {
        final Production prod = treeNode.getProduction();
        if (prod != null) {
            final Attr productionAttr = document.createAttribute(ATTR_PRODUCTION);
            productionAttr.setValue(treeNode.getProduction().toString());
            node.setAttributeNode(productionAttr);
        }
    }

    private void addLexemeValue(Element terminalNode,
            ParsingTreeNode treeNode) {
        if (treeNode.getValue() == null) {
            return;
        }
        terminalNode.appendChild(document.createTextNode(treeNode.getValue().getValue()));
        addLineAndColumn(terminalNode, treeNode);
    }

    private void addLineAndColumn(Element value,
            ParsingTreeNode treeNode) {
        if (treeNode.getValue().getLine() == -1) {
            return;
        }
        addLine(value, treeNode);
        addColumn(value, treeNode);
    }

    private void addLine(Element value,
            ParsingTreeNode treeNode) {
        final Attr line = document.createAttribute(ATTR_LINE);
        line.setValue(Integer.toString(treeNode.getValue().getLine()));
        value.setAttributeNode(line);
    }

    private void addColumn(Element value,
            ParsingTreeNode treeNode) {
        final Attr column = document.createAttribute(ATTR_COLUMN);
        column.setValue(Integer.toString(treeNode.getValue().getColumn()));
        value.setAttributeNode(column);
    }

    private void appendAllChildren(Element xmlNode, ParsingTreeNode currentTreeNode) {
        for (int i = 0; i < currentTreeNode.getChildrenCount(); i++) {
            addSubNodes(xmlNode, currentTreeNode.getChild(i));
        }
    }

}
