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
import com.romanenco.cfrm.grammar.example.DragonLLGrammar;
import com.romanenco.cfrm.lexer.RegLexer;
import com.romanenco.cfrm.llparser.LLParser;

public class LAttributedTest {

    private static final String ATTR_VALUE = "value";
    private static final String ATTR_INHERITED = "inherited";

    private static final Lexer LEXER;  
    private static final Parser PARSER;  
    private static final ASTBuilder BUILDER;  

    static {
        final DragonLLGrammar grammar = new DragonLLGrammar();
        LEXER = new RegLexer(grammar);
        PARSER = new LLParser(grammar);
        BUILDER = new ASTBuilder();
        initASTBuilder(grammar);
    }

    @Test
    public void test1() {
        final ParsingTreeNode parsingTree = parse("2*(1 + 2) + 4*2");
        BUILDER.build(parsingTree);
        Assert.assertEquals(14, parsingTree.getAttribute(ATTR_VALUE));
    }

    @Test
    public void test2() {
        final ParsingTreeNode parsingTree = parse("1 + 2 + 3");
        BUILDER.build(parsingTree);
        Assert.assertEquals(6, parsingTree.getAttribute(ATTR_VALUE));
    }

    @Test
    public void test3() {
        final ParsingTreeNode parsingTree = parse("2 * 3 * 4");
        BUILDER.build(parsingTree);
        Assert.assertEquals(24, parsingTree.getAttribute(ATTR_VALUE));
    }

    private static ParsingTreeNode parse(String input) {
        return PARSER.parse(LEXER.tokenize(input));
    }

    private static void initASTBuilder(DragonLLGrammar grammar) {
        BUILDER.addSDTHandler(grammar.getProduction("T' -> * F T'"), new SDTVisitor() {
            @Override
            public void visit(ParsingTreeNode node, ASTBuilder builder) {
                final ParsingTreeNode nonTermF = node.getChild(1);
                final ParsingTreeNode nonTermTPrime = node.getChild(2);
                builder.build(nonTermF);
                final int left = (int)node.getAttribute(ATTR_INHERITED);
                final int right = (int)nonTermF.getAttribute(ATTR_VALUE);
                final int product = left * right;
                nonTermTPrime.setAttribute(ATTR_INHERITED, product);
                builder.build(nonTermTPrime);
                node.setAttribute(ATTR_VALUE, nonTermTPrime.getAttribute(ATTR_VALUE));
            }
        });

        BUILDER.addSDTHandler(grammar.getProduction("E -> T E'"), new SDTVisitor() {
            @Override
            public void visit(ParsingTreeNode node, ASTBuilder builder) {
                final ParsingTreeNode nonTermT = node.getChild(0);
                final ParsingTreeNode nonTermEPrime = node.getChild(1);
                builder.build(nonTermT);
                nonTermEPrime.setAttribute(ATTR_INHERITED, nonTermT.getAttribute(ATTR_VALUE));
                builder.build(nonTermEPrime);
                node.setAttribute(ATTR_VALUE, nonTermEPrime.getAttribute(ATTR_VALUE));
            }
        });

        BUILDER.addSDTHandler(grammar.getProduction("F -> id"), new SDTVisitor() {
            @Override
            public void visit(ParsingTreeNode node, ASTBuilder builder) {
                final ParsingTreeNode idChild = node.getChild(0);
                final String value = idChild.getValue().getValue();
                final int number = Integer.parseInt(value);
                node.setAttribute(ATTR_VALUE, number);
            }
        });

        BUILDER.addSDTHandler(grammar.getProduction("T' -> epsilon"), new SDTVisitor() {
            @Override
            public void visit(ParsingTreeNode node, ASTBuilder builder) {
                node.setAttribute(ATTR_VALUE, node.getAttribute(ATTR_INHERITED));
            }
        });

        BUILDER.addSDTHandler(grammar.getProduction("E' -> epsilon"), new SDTVisitor() {
            @Override
            public void visit(ParsingTreeNode node, ASTBuilder builder) {
                node.setAttribute(ATTR_VALUE, node.getAttribute(ATTR_INHERITED));
            }
        });

        BUILDER.addSDTHandler(grammar.getProduction("T -> F T'"), new SDTVisitor() {
            @Override
            public void visit(ParsingTreeNode node, ASTBuilder builder) {
                final ParsingTreeNode nonTermF = node.getChild(0);
                final ParsingTreeNode nonTermTPrime = node.getChild(1);
                builder.build(nonTermF);
                nonTermTPrime.setAttribute(ATTR_INHERITED, nonTermF.getAttribute(ATTR_VALUE));
                builder.build(nonTermTPrime);
                node.setAttribute(ATTR_VALUE, nonTermTPrime.getAttribute(ATTR_VALUE));
            }
        });

        BUILDER.addSDTHandler(grammar.getProduction("E' -> + T E'"), new SDTVisitor() {
            @Override
            public void visit(ParsingTreeNode node, ASTBuilder builder) {
                final ParsingTreeNode nonTermT = node.getChild(1);
                final ParsingTreeNode nonTermEPrime = node.getChild(2);
                builder.build(nonTermT);
                final int left = (int)node.getAttribute(ATTR_INHERITED);
                final int right = (int)nonTermT.getAttribute(ATTR_VALUE);
                final int sum = left + right;
                nonTermEPrime.setAttribute(ATTR_INHERITED, sum);
                builder.build(nonTermEPrime);
                node.setAttribute(ATTR_VALUE, nonTermEPrime.getAttribute(ATTR_VALUE));
            }
        });

        BUILDER.addSDTHandler(grammar.getProduction("F -> ( E )"), new SDTVisitor() {
            @Override
            public void visit(ParsingTreeNode node, ASTBuilder builder) {
                final ParsingTreeNode nonTermE = node.getChild(1);
                builder.build(nonTermE);
                node.setAttribute(ATTR_VALUE, nonTermE.getAttribute(ATTR_VALUE));
            }
        });
    }
}
