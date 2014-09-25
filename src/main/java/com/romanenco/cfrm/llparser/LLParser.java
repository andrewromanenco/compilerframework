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

package com.romanenco.cfrm.llparser;

import java.util.Arrays;
import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.romanenco.cfrm.Grammar;
import com.romanenco.cfrm.Parser;
import com.romanenco.cfrm.ParsingTreeNode;
import com.romanenco.cfrm.Token;
import com.romanenco.cfrm.grammar.NonTerminal;
import com.romanenco.cfrm.grammar.Production;
import com.romanenco.cfrm.grammar.Symbol;
import com.romanenco.cfrm.grammar.Terminal;

public class LLParser implements Parser {

    private static final Logger LOGGER = Logger.getLogger(LLParser.class.getName());

    private final Grammar grammar;
    private final LookUpTable lookUpTable;

    public LLParser(Grammar grammar) {
        this.grammar = grammar;
        final FirstSets firstSets = new FirstSets(grammar);
        final FollowSets followSets = new FollowSets(grammar, firstSets);
        this.lookUpTable = new LookUpTable(grammar, firstSets, followSets);
    }

    @Override
    public ParsingTreeNode parse(List<Token> tokens) {
        if ((tokens == null)||tokens.isEmpty()) {
            throw new ParsingError("Empty input");
        }
        final Parser parser = new Parser(tokens);
        return parser.parse();
    }

    @Override
    public Grammar getGrammar() {
        return grammar;
    }

    private class Parser {

        private final ParsingTreeNode root = new ParsingTreeNode(grammar.getStartSymbol());
        private final Deque<Symbol> stack = new LinkedList<>();
        private final Deque<ParsingTreeNode> nodes = new LinkedList<>();
        private final List<Token> input;
        private int index;

        Parser(List<Token> input) {
            this.input = input;
            stack.push(grammar.getEOF());
            stack.push(grammar.getStartSymbol());
            nodes.push(root);
            index = 0;
        }

        public ParsingTreeNode parse() {
            Symbol symbol = stack.peek();
            while (symbol != grammar.getEOF()) {
                if (LOGGER.isLoggable(Level.FINE)) {
                    LOGGER.fine("Next: " + input.get(index).getTerminal()
                            +" Stack: " + Arrays.toString(stack.toArray()));
                }
                if (symbol instanceof Terminal) {
                    processTerminal(symbol);
                } else {
                    processNonTerminal(symbol);
                }
                symbol = stack.peek();
            }
            return root;
        }

        private void processTerminal(Symbol symbol) {
            final Token next = input.get(index);
            if (symbol == next.getTerminal()) {
                final ParsingTreeNode current = nodes.pop();
                current.setValue(next);
                stack.pop();
                index++;
            } else {
                reportError(next, index);
            }
        }

        private void processNonTerminal(Symbol symbol) {
            final Token next = input.get(index);
            final Production production = lookUpTable.getTableCell(
                    (NonTerminal)symbol,
                    (Terminal)next.getTerminal());
            if (production == null) {
                reportError(next, index);
            }
            stack.pop();
            processProduction(production);
        }

        private void processProduction(Production production) {
            final ParsingTreeNode current = nodes.pop();
            current.setProduction(production);
            if (production.isEpsilonMove()) {
                current.add(new ParsingTreeNode(grammar.getEpsilon()));
            } else {
                final ListIterator<Symbol> listIter = production.getRight()
                        .listIterator(production.getRight().size());
                while (listIter.hasPrevious()) {
                    final Symbol prev = listIter.previous();
                    stack.push(prev);
                    nodes.push(new ParsingTreeNode(prev));
                }
                final Iterator<ParsingTreeNode> iter = nodes.iterator();
                for (int i = 0; i < production.getRight().size(); i++) {
                    current.add(iter.next());
                }
            }
        }

        private void reportError(Token next, int index) {
            throw new ParsingError("Parsing error for token ["
                    + index
                    + "] (type, value, line, col) -> "
                    + next.getTerminal().getName()
                    + ", "
                    + next.getValue()
                    + ", "
                    + next.getLine()
                    + ", "
                    + next.getColumn());

        }
    }

}
