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

import java.util.HashSet;
import java.util.Set;

import com.romanenco.cfrm.lexer.fa.AbstractTransition;
import com.romanenco.cfrm.lexer.fa.EpsilonTransition;
import com.romanenco.cfrm.lexer.fa.Regex;
import com.romanenco.cfrm.lexer.fa.State;
import com.romanenco.cfrm.lexer.fa.Transition;

public final class NFAWalk {

    private NFAWalk() {
        // nothing
    }

    public static boolean walk(Regex regex, String input) {
        Set<State> current = new HashSet<>();
        current.add(regex.getStartState());
        addEpsilons(current, regex.getStartState());
        for (final char c: input.toCharArray()) {
            final Set<State> next = statesForInput(c, current);
            if (next.isEmpty()) {
                return false;
            }
            current = next;
        }
        for (final State s: current) {
            if (s == regex.getFinalState()) {
                return true;
            }
        }
        return false;
    }

    private static Set<State> statesForInput(char nextInput, Set<State> current) {
        final Set<State> next = new HashSet<>();
        for (final State state: current) {
            for (final AbstractTransition tran: state.getTransitions()) {
                if ((tran instanceof Transition)
                        &&(((Transition)tran).getSymbol() == nextInput)) {
                    next.add(tran.getTarget());
                    addEpsilons(next, tran.getTarget());
                }
            }
        }
        return next;
    }

    private static void addEpsilons(Set<State> next, State target) {
        for (final AbstractTransition tran: target.getTransitions()) {
            if ((tran instanceof EpsilonTransition)
                    && !next.contains(tran.getTarget())) {
                next.add(tran.getTarget());
                addEpsilons(next, tran.getTarget());
            }
        }
    }

}
