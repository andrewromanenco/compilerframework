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

package com.romanenco.cfrm.grammar;

import org.junit.Assert;
import org.junit.Test;

public class AbstractSymbolTest {

    private static final String NAME = "NAME";
    private static final String OTHER = "OTHER";

    static class TestSymbol extends AbstractSymbol {

        public TestSymbol(String name) {
            super(name);
        }

    }

    @Test
    public void testEquals() {
        final TestSymbol symbol = new TestSymbol(NAME);
        final TestSymbol same = new TestSymbol(NAME);
        final TestSymbol other = new TestSymbol(OTHER);
        Assert.assertTrue(symbol.equals(same));
        Assert.assertTrue(symbol.equals(symbol));
        Assert.assertFalse(symbol.equals(other));
    }

    @Test
    public void testHashCode() {
        final TestSymbol symbol = new TestSymbol(NAME);
        final TestSymbol same = new TestSymbol(NAME);
        final TestSymbol other = new TestSymbol(OTHER);
        Assert.assertTrue(symbol.hashCode() == same.hashCode());
        Assert.assertFalse(symbol.hashCode() == other.hashCode());
    }

}
