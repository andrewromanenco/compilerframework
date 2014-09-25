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

import java.util.ArrayList;
import java.util.List;

import com.romanenco.cfrm.Grammar;
import com.romanenco.cfrm.Token;
import com.romanenco.cfrm.grammar.Symbol;
import com.romanenco.cfrm.grammar.Terminal;
import com.romanenco.cfrm.lexer.LexerError;

public final class ParserUtil {

    private static final String SYMBOL_SEPARATOR = " ";

    private ParserUtil() {
        // nothing
    }

    public static List<Token> makeListOfTerminals(Grammar grammar, String input) {
        final List<Token> tokens = new ArrayList<>();
        for (final String terminalName : input.split(SYMBOL_SEPARATOR)) {
            final Symbol terminal = grammar.getSymbol(terminalName);
            if (terminal instanceof Terminal) {
                tokens.add(new Token((Terminal) terminal));
            } else {
                throw new LexerError("Not a terminal symbol: " + terminalName);
            }
        }
        tokens.add(new Token(grammar.getEOF()));
        return tokens;
    }

}
