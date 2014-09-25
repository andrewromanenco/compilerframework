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

import org.junit.Assert;
import org.junit.Test;

import com.romanenco.cfrm.Grammar;
import com.romanenco.cfrm.ParsingTreeNode;
import com.romanenco.cfrm.grammar.Production;
import com.romanenco.cfrm.grammar.Terminal;
import com.romanenco.cfrm.grammar.example.DragonLLGrammar;

public class ASTBuilderTest {

    @Test
    public void goodTest() {
        final Grammar grammar = new DragonLLGrammar();
        final ParsingTreeNode node = new ParsingTreeNode(grammar.getSymbol("E"));
        final Production production = grammar.getProduction("E -> T E'");
        node.setProduction(production);

        final ASTBuilder builder = new ASTBuilder();
        final Visitor visitor = new Visitor();
        builder.addSDTHandler(production, visitor);
        builder.build(node);

        Assert.assertEquals(node, visitor.node);
    }

    static class Visitor implements SDTVisitor {

        public ParsingTreeNode node;

        @Override
        public void visit(ParsingTreeNode node, ASTBuilder builder) {
            this.node = node;
        }

    }

    @Test( expected = ASTBuilderError.class )
    public void badTest() {
        final ParsingTreeNode node = new ParsingTreeNode(new Terminal("term"));
        final ASTBuilder builder = new ASTBuilder();
        builder.build(node);
    }

}
