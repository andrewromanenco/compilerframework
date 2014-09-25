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

import com.romanenco.cfrm.ParsingTreeNode;
import com.romanenco.cfrm.grammar.NonTerminal;

/**
 * Visitor for S-attributed sdt.
 * Abstract visited method is called in post-order.
 *
 * @author Andrew Romanenco
 *
 */
public abstract class AbstractSAttrVisitor implements SDTVisitor {

    @Override
    public void visit(ParsingTreeNode node, ASTBuilder builder) {
        final int count = node.getChildrenCount();
        for (int i = 0; i < count; i++) {
            final ParsingTreeNode child = node.getChild(i);
            if (child.getSymbol() instanceof NonTerminal) {
                builder.build(child);
            }
        }
        visited(node);
    }

    protected abstract void visited(ParsingTreeNode node);

}
