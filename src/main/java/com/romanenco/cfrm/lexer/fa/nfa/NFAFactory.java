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

package com.romanenco.cfrm.lexer.fa.nfa;

import java.util.Deque;
import java.util.LinkedList;

import com.romanenco.cfrm.lexer.fa.Regex;

public class NFAFactory {

    private static final char CLOSE_PARAN = ')';
    private static final char OPEN_PARAN = '(';
    private static final char STAR = '*';
    private static final char UNION = '|';
    private static final char CONCAT = '+';
    private static final char BACKSL = '\\';

    private final String pattern;
    private int index;

    public NFAFactory(String pattern) {
        this.pattern = pattern;
        this.index = 0;
    }

    public Regex build() {
        final Deque<Regex> regStack = new LinkedList<>();
        final Deque<Operation> opsStack = new LinkedList<>();
        while (index < pattern.length()) {
            if (pattern.charAt(index) == CLOSE_PARAN) {
                index++;
                break;
            }
            final Object next = getNext();
            if (next instanceof Regex) {
                regStack.push((Regex)next);
            } else {
                final Operation oper = (Operation)next;
                if (!opsStack.isEmpty()
                        &&(opsStack.getFirst().getPriority()>= oper.getPriority())) {
                    executeStack(regStack, opsStack);
                }
                opsStack.push(oper);
            }
        }
        executeStack(regStack, opsStack);
        return regStack.pop();
    }

    private void executeStack(Deque<Regex> regStack,
            Deque<Operation> opsStack) {
        while (!opsStack.isEmpty()) {
            final Operation oper = opsStack.pop();
            final Regex reg1;
            final Regex reg2;
            switch (oper) {
            case STAR:
                regStack.push(Thomson.kleeneStarRegex(regStack.pop()));
                break;
            case AND:
                reg2 = regStack.pop();
                reg1 = regStack.pop();
                regStack.push(Thomson.andRegex(reg1, reg2));
                break;
            case OR:
                reg2 = regStack.pop();
                reg1 = regStack.pop();
                regStack.push(Thomson.orRegex(reg1, reg2));
                break;
            default:
                break;
            }
        }
        assert regStack.size() == 1;
        assert opsStack.isEmpty();
    }

    private Object getNext() {
        final char nextInput = pattern.charAt(index++);
        switch (nextInput) {
        case UNION:
            return Operation.OR;
        case STAR:
            return Operation.STAR;
        case CONCAT:
            return Operation.AND;
        case OPEN_PARAN:
            return build();
        case BACKSL:
            return Thomson.symbolRegex(pattern.charAt(index++));
        default:
            return Thomson.symbolRegex(nextInput);
        }
    }

    enum Operation {

        STAR(3),
        AND(2),
        OR(1);

        private final int priority;

        private Operation(int priority) {
            this.priority = priority;
        }

        public int getPriority() {
            return priority;
        }
    }

}