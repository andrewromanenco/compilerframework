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

package com.romanenco.cfrm;

import com.romanenco.cfrm.grammar.Symbol;
import com.romanenco.cfrm.grammar.Terminal;

public class Token {

    private final Terminal terminal;
    private final String value;
    private final int line;
    private final int column;

    public Token(Terminal terminal) {
        this(terminal, null, -1, -1);
    }

    public Token(Terminal terminal, String value, int line, int column) {
        this.terminal = terminal;
        this.value = value;
        this.line = line;
        this.column = column;
    }

    public Symbol getTerminal() {
        return terminal;
    }

    public String getValue() {
        return value;
    }

    public int getLine() {
        return line;
    }

    public int getColumn() {
        return column;
    }

    @Override
    public String toString() {
        return terminal.getName()
                + '['
                + value
                + ']';
    }

}
