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

import org.junit.Assert;
import org.junit.Test;

import com.romanenco.cfrm.lexer.fa.Regex;

public class NFAWalkTest {

    @Test
    public void test1() {
        final NFAFactory factory = new NFAFactory("(a|b)+(a|b)*+c+c");
        final Regex regex = factory.build();
        Assert.assertTrue(NFAWalk.walk(regex, "acc"));
        Assert.assertTrue(NFAWalk.walk(regex, "bcc"));
        Assert.assertTrue(NFAWalk.walk(regex, "aaacc"));
        Assert.assertTrue(NFAWalk.walk(regex, "ababacc"));
    }

    @Test
    public void test2() {
        final NFAFactory factory = new NFAFactory("a+b*+c");
        final Regex regex = factory.build();
        Assert.assertTrue(NFAWalk.walk(regex, "ac"));
        Assert.assertTrue(NFAWalk.walk(regex, "abc"));
        Assert.assertTrue(NFAWalk.walk(regex, "abbc"));
        Assert.assertTrue(NFAWalk.walk(regex, "abbbc"));
    }

    @Test
    public void test3() {
        final NFAFactory factory = new NFAFactory("a");
        final Regex regex = factory.build();
        Assert.assertTrue(NFAWalk.walk(regex, "a"));
        Assert.assertFalse(NFAWalk.walk(regex, "aa"));
        Assert.assertFalse(NFAWalk.walk(regex, "b"));
    }

    @Test
    public void test4() {
        final NFAFactory factory = new NFAFactory("a*");
        final Regex regex = factory.build();
        Assert.assertTrue(NFAWalk.walk(regex, ""));
        Assert.assertTrue(NFAWalk.walk(regex, "a"));
        Assert.assertTrue(NFAWalk.walk(regex, "aa"));
        Assert.assertTrue(NFAWalk.walk(regex, "aaa"));
        Assert.assertFalse(NFAWalk.walk(regex, "aaab"));
        Assert.assertFalse(NFAWalk.walk(regex, "baaa"));
        Assert.assertFalse(NFAWalk.walk(regex, "bb"));
    }

    @Test
    public void test5() {
        final NFAFactory factory = new NFAFactory("a|b");
        final Regex regex = factory.build();
        Assert.assertTrue(NFAWalk.walk(regex, "a"));
        Assert.assertTrue(NFAWalk.walk(regex, "b"));
        Assert.assertFalse(NFAWalk.walk(regex, "ab"));
        Assert.assertFalse(NFAWalk.walk(regex, "c"));
    }

    @Test
    public void test6() {
        final NFAFactory factory = new NFAFactory("a+b");
        final Regex regex = factory.build();
        Assert.assertTrue(NFAWalk.walk(regex, "ab"));
        Assert.assertFalse(NFAWalk.walk(regex, "a"));
        Assert.assertFalse(NFAWalk.walk(regex, "b"));
        Assert.assertFalse(NFAWalk.walk(regex, "c"));
    }

    @Test
    public void test7() {
        final NFAFactory factory = new NFAFactory("a+b*");
        final Regex regex = factory.build();
        Assert.assertTrue(NFAWalk.walk(regex, "a"));
        Assert.assertTrue(NFAWalk.walk(regex, "ab"));
        Assert.assertTrue(NFAWalk.walk(regex, "abb"));
        Assert.assertTrue(NFAWalk.walk(regex, "abbb"));
        Assert.assertFalse(NFAWalk.walk(regex, "aabbb"));
    }

    @Test
    public void test8() {
        final NFAFactory factory = new NFAFactory("t+e+s+t");
        final Regex regex = factory.build();
        Assert.assertTrue(NFAWalk.walk(regex, "test"));
        Assert.assertFalse(NFAWalk.walk(regex, "tast"));
    }

    @Test
    public void test9() {
        final NFAFactory factory = new NFAFactory("(t+e+s+t)+(t+e+s+t)*");
        final Regex regex = factory.build();
        Assert.assertTrue(NFAWalk.walk(regex, "test"));
        Assert.assertTrue(NFAWalk.walk(regex, "testtest"));
        Assert.assertTrue(NFAWalk.walk(regex, "testtesttest"));
        Assert.assertFalse(NFAWalk.walk(regex, "tast"));
    }

    @Test
    public void test10() {
        final NFAFactory factory = new NFAFactory("\\*");
        final Regex regex = factory.build();
        Assert.assertTrue(NFAWalk.walk(regex, "*"));
        Assert.assertFalse(NFAWalk.walk(regex, "a"));
    }

    @Test
    public void test11() {
        final NFAFactory factory = new NFAFactory("\\(");
        final Regex regex = factory.build();
        Assert.assertTrue(NFAWalk.walk(regex, "("));
        Assert.assertFalse(NFAWalk.walk(regex, "a"));
    }

    @Test
    public void test12() {
        final NFAFactory factory = new NFAFactory("a+a+\\)*");
        final Regex regex = factory.build();
        Assert.assertTrue(NFAWalk.walk(regex, "aa"));
        Assert.assertTrue(NFAWalk.walk(regex, "aa)"));
        Assert.assertTrue(NFAWalk.walk(regex, "aa))"));
        Assert.assertFalse(NFAWalk.walk(regex, "aa)b"));
    }

}
