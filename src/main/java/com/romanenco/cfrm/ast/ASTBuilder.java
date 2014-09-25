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

package com.romanenco.cfrm.ast;

import java.util.HashMap;
import java.util.Map;

import com.romanenco.cfrm.ParsingTreeNode;
import com.romanenco.cfrm.grammar.Production;

public class ASTBuilder {

    private final Map<Production, SDTVisitor> map = new HashMap<>();

    public void addSDTHandler(Production production, SDTVisitor visitor) {
        if (map.containsKey(production)) {
            throw new ASTBuilderError("Duplicate production: " + production);
        }
        map.put(production, visitor);
    }

    public void build(ParsingTreeNode node) {
        final SDTVisitor visitor = map.get(node.getProduction());
        if (visitor == null) {
            throw new ASTBuilderError("No visitor for " + node.getProduction());
        }
        visitor.visit(node, this);
    }
}
