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

import com.romanenco.cfrm.lexer.LexerRule;
import com.romanenco.cfrm.lexer.LexerRule.TYPE;

public class Terminal extends AbstractSymbol {

    private LexerRule lexerRule;

    public Terminal(String name) {
        super(name);
    }

    public Terminal(String name, LexerRule lexerRule) {
        super(name);
        this.lexerRule = lexerRule;
    }

    public String getMatch(String input, int start) {
        return lexerRule.getNextValue(input, start);
    }

    public boolean shouldParse() {
        return lexerRule.getType() == TYPE.VALID;
    }

    public LexerRule getLexerRule() {
        return lexerRule;
    }

}