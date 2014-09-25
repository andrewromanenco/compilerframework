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

package com.romanenco.cfrm;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.romanenco.cfrm.grammar.Production;
import com.romanenco.cfrm.grammar.Symbol;

public class ParsingTreeNode {

    private final Symbol symbol;
    private Production production;
    private Token token;
    private final List<ParsingTreeNode> list = new LinkedList<ParsingTreeNode>();
    private Map<String, Object> attributes;

    public ParsingTreeNode(Symbol symbol) {
        this.symbol = symbol;
    }

    public void add(ParsingTreeNode node) {
        list.add(node);
    }

    public Symbol getSymbol() {
        return symbol;
    }

    public void setProduction(Production production) {
        this.production = production;
    }

    public Production getProduction() {
        return production;
    }

    public Token getValue() {
        return token;
    }

    public void setValue(Token value) {
        this.token = value;
    }

    public ParsingTreeNode getChild(int index) {
        return list.get(index);
    }

    public int getChildrenCount() {
        return list.size();
    }

    public boolean hasChildren() {
        return !list.isEmpty();
    }

    public void setAttribute(String key, Object value) {
        if (this.attributes == null) {
            this.attributes = new HashMap<>();
        }
        this.attributes.put(key, value);
    }

    public Object getAttribute(String key) {
        if (attributes == null) {
            return null;
        }
        return attributes.get(key);
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("Node: ")
                .append(symbol.getName())
                .append(" =>");
        appendChildren(builder);
        return builder.toString();
    }

    private void appendChildren(StringBuilder builder) {
        if (list == null) {
            builder.append(" *NONE*");
        } else {
            for (final ParsingTreeNode node: list) {
                builder.append(" [");
                builder.append(node.getSymbol());
                builder.append(" | ");
                builder.append(node.getValue());
                builder.append(']');
            }
        }
    }

}
