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

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.romanenco.cfrm.lexer.fa.AbstractTransition;
import com.romanenco.cfrm.lexer.fa.EpsilonTransition;
import com.romanenco.cfrm.lexer.fa.Regex;
import com.romanenco.cfrm.lexer.fa.State;
import com.romanenco.cfrm.lexer.fa.Transition;

public class NFAFactoryTest {

    @Test
    public void testSingleChar() {
        final NFAFactory factory = new NFAFactory("a");
        final Regex regex = factory.build();
        Assert.assertNotNull(regex);
        final State start = regex.getStartState();
        final State stop = regex.getFinalState();
        final List<AbstractTransition> trans = start.getTransitions();
        Assert.assertNotNull(trans);
        Assert.assertEquals(1, trans.size());
        final AbstractTransition transition = trans.get(0);
        Assert.assertTrue(transition instanceof Transition);
        Assert.assertEquals('a', ((Transition)transition).getSymbol());
        Assert.assertEquals(stop, transition.getTarget());
    }

    @Test
    public void testDoubleChar() {
        final NFAFactory factory = new NFAFactory("a+b");
        final Regex regex = factory.build();
        final State start = regex.getStartState();
        final State stop = regex.getFinalState();
        final Transition tran1 = (Transition)start.getTransitions().get(0);
        Assert.assertEquals('a', tran1.getSymbol());
        final State state1 = tran1.getTarget();
        final EpsilonTransition tran2 =
                (EpsilonTransition)state1.getTransitions().get(0);
        final State state2 = tran2.getTarget();
        final Transition tran3 = (Transition)state2.getTransitions().get(0);
        Assert.assertEquals('b', tran3.getSymbol());
        final State state3 = tran3.getTarget();
        Assert.assertEquals(stop, state3);
    }

}
