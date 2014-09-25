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

package com.romanenco.cfrm.lexer;

import java.util.Iterator;
import java.util.List;

import com.romanenco.cfrm.Token;
import com.romanenco.cfrm.grammar.EOF;
import com.romanenco.cfrm.grammar.Terminal;

class TerminalTokenazier implements Iterable<Token>, Iterator<Token> {

    private static final char NEW_LINE = '\n';

    private final String input;
    private final List<Terminal> terminals;
    private final EOF eof;
    private int line;
    private int column;
    private int index;

    public TerminalTokenazier(String input, List<Terminal> terminals, EOF eof) {
        this.input = input;
        this.terminals = terminals;
        this.eof = eof;
    }

    public boolean hasMore() {
        return index < input.length();
    }

    @Override
    public Token next() {
        Token token = getNextToken();
        while (!((Terminal) token.getTerminal()).shouldParse()) { 
            token = getNextToken();
        }
        return token;
    }

    private Token getNextToken() {
        if (index == input.length()) {
            index ++;
            return makeToken(eof, null);
        } else {
            return iterateTillNext();
        }
    }

    private Token iterateTillNext() {
        Terminal terminal = null;
        String value = "";
        for (final Terminal term : this.terminals) {
            final String candidate = term.getMatch(input, index);
            if ((candidate != null)
                    && (value.length() < candidate
                            .length())) {
                terminal = term;
                value = candidate;
            }
        }
        if (terminal == null) {
            throw new LexerError(getInputChunk(), line, column);
        }
        if (value.isEmpty()) {
            throw new LexerError("Terminal matches empty string: "
                    + terminal);
        }
        moveTextPointer(value);
        return makeToken(terminal, value);
    }

    private String getInputChunk() {
        return input.length() - index > 15 ? input.substring(index,
                index + 15) : input.substring(index);
    }

    private Token makeToken(Terminal terminal, String value) {
        return new Token(terminal, value, line, column);
    }

    private void moveTextPointer(String value) {
        index += value.length();
        for (int i = 0; i < value.length(); i++) {
            if (value.charAt(i) == NEW_LINE) {
                line++;
                column = 0;
            } else {
                column++;
            }
        }
    }

    @Override
    public boolean hasNext() {
        return index <= input.length();
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterator<Token> iterator() {
        index = 0;
        line = 1;
        column = 0;
        return this;
    }

}
