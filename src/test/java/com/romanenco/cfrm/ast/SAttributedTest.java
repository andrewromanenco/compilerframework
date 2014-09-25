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

import com.romanenco.cfrm.Lexer;
import com.romanenco.cfrm.Parser;
import com.romanenco.cfrm.ParsingTreeNode;
import com.romanenco.cfrm.grammar.example.ABGrammar;
import com.romanenco.cfrm.lexer.RegLexer;
import com.romanenco.cfrm.llparser.LLParser;

public class SAttributedTest {

    private static final String SUM_ATTR = "sum";

    @Test
    public void test() {
        final ABGrammar grammar = new ABGrammar();
        final Lexer lexer = new RegLexer(grammar);
        final Parser parser = new LLParser(grammar);

        final ParsingTreeNode parsingTree = parser.parse(lexer.tokenize("1+2"));
        Assert.assertNotNull(parsingTree);

        final ASTBuilder builder = new ASTBuilder();
        builder.addSDTHandler(grammar.getProduction("SUM -> int + int"), new SumVisitor());

        builder.build(parsingTree);

        final Object attribute = parsingTree.getAttribute(SUM_ATTR);
        Assert.assertNotNull(attribute);
        Assert.assertEquals(3, attribute);
    }

    static class SumVisitor extends AbstractSAttrVisitor {

        @Override
        protected void visited(ParsingTreeNode node) {
            final int left = getLeftInt(node);
            final int right = getRightInt(node);
            final int sum = left + right;
            // System.out.println("Sum = " + sum);  you could just print it
            node.setAttribute(SUM_ATTR, sum);
        }

        private int getLeftInt(ParsingTreeNode node) {
            return getIntFromChild(node, 0);
        }

        private int getRightInt(ParsingTreeNode node) {
            return getIntFromChild(node, 2);
        }

        private int getIntFromChild(ParsingTreeNode node, int index) {
            final String value = node.getChild(index).getValue().getValue();
            return Integer.parseInt(value);
        }
    }
}
