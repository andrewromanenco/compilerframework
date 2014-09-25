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

import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.romanenco.cfrm.Token;

public final class TokensListToXML extends AbstractXmlBuilder<List<Token>> {

    private static final String ATTR_COLUMN = "column";
    private static final String ATTR_LINE = "line";
    private static final String ATTR_TERM = "term";
    private static final String ROOT = "Tokens";
    private static final String TOKEN_NODE = "Token";

    private TokensListToXML(List<Token> data) {
        super(ROOT, data);
    }

    public static Document build(List<Token> tokens) {
        final TokensListToXML util = new TokensListToXML(tokens);
        util.buildXmlDocument();
        return util.getDocument();
    }

    @Override
    public void buildXmlDocument() {
        for (final Token token: data) {
            appendTokenNode(token);
        }
    }

    private void appendTokenNode(Token token) {
        final Element node = document.createElement(TOKEN_NODE);
        node.appendChild(document.createTextNode(token.getValue()));
        addTerminalToNode(node, token);
        addLineAndColumnToNode(node, token);
        rootElement.appendChild(node);
    }

    private void addTerminalToNode(Element node, Token token) {
        node.setAttribute(ATTR_TERM, token.getTerminal().toString());
    }

    private void addLineAndColumnToNode(Element node, Token token) {
        node.setAttribute(ATTR_LINE, Integer.toString(token.getLine()));
        node.setAttribute(ATTR_COLUMN, Integer.toString(token.getColumn()));
    }

}
