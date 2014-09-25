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

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.romanenco.cfrm.Grammar;
import com.romanenco.cfrm.Lexer;
import com.romanenco.cfrm.Token;
import com.romanenco.cfrm.grammar.builder.GrammarJBuilder;

public class RegLexerTest {

    private Grammar getGrammar() {
        final GrammarJBuilder builder = new GrammarJBuilder();
        builder.addTerminal("int", new LexerRule("\\-?\\d+"));
        builder.addTerminal("+", new LexerRule("\\+"));
        builder.addTerminal("-", new LexerRule("\\-"));
        builder.addTerminal("*", new LexerRule("\\*"));
        builder.addTerminal("WS", new LexerRule(" ", LexerRule.TYPE.IGNORE));
        return builder.getGrammar();
    }

    private Lexer getLexer(Grammar grammar) {
        return new RegLexer(grammar);
    }

    @Test
    public void goodTest() {
        final Lexer lexer = getLexer(getGrammar());
        String input;
        List<Token> tokens;

        input = "44 + 123 * -89";
        tokens = lexer.tokenize(input);
        Assert.assertEquals(6, tokens.size());

        input = "44*123*89*5+90";
        tokens = lexer.tokenize(input);
        Assert.assertEquals(10, tokens.size());

        input = "44+4--56";
        tokens = lexer.tokenize(input);
        Assert.assertEquals(6, tokens.size());

        //these are valid inputs for lexer
        input = "****";
        tokens = lexer.tokenize(input);
        Assert.assertEquals(5, tokens.size());

        input = "*+ 56 ++ 67";
        tokens = lexer.tokenize(input);
        Assert.assertEquals(7, tokens.size());
    }

    @Test(expected = LexerError.class)
    public void badTest() {
        final Lexer lexer = getLexer(getGrammar());
        final String input = "44+X*89";
        lexer.tokenize(input);
    }

}
