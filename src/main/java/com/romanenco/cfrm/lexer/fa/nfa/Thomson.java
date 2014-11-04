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

import com.romanenco.cfrm.lexer.fa.EpsilonTransition;
import com.romanenco.cfrm.lexer.fa.Regex;
import com.romanenco.cfrm.lexer.fa.State;
import com.romanenco.cfrm.lexer.fa.Transition;

public final class Thomson {

    private Thomson() {
        // nothing
    }

    public static Regex symbolRegex(char symbol) {
        final State start = new State();
        final State stop = new State();
        start.addTransition(new Transition(symbol, stop));
        return new Regex(start, stop);
    }

    public static Regex orRegex(Regex regex1, Regex regex2) {
        final State start = new State();
        final State stop = new State();
        start.addTransition(new EpsilonTransition(regex1.getStartState()));
        start.addTransition(new EpsilonTransition(regex2.getStartState()));
        regex1.getFinalState().addTransition(new EpsilonTransition(stop));
        regex2.getFinalState().addTransition(new EpsilonTransition(stop));
        return new Regex(start, stop);
    }

    public static Regex andRegex(Regex regex1, Regex regex2) {
        final State start = regex1.getStartState();
        final State stop = regex2.getFinalState();
        regex1.getFinalState().addTransition(new EpsilonTransition(regex2.getStartState()));
        return new Regex(start, stop);
    }

    public static Regex kleeneStarRegex(Regex reg) {
        final State start = new State();
        final State stop = new State();
        start.addTransition(new EpsilonTransition(stop));
        start.addTransition(new EpsilonTransition(reg.getStartState()));
        reg.getFinalState().addTransition(new EpsilonTransition(start));
        reg.getFinalState().addTransition(new EpsilonTransition(stop));
        return new Regex(start, stop);
    }

}
