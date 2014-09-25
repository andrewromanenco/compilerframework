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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

import org.w3c.dom.Document;

import com.romanenco.cfrm.grammar.example.DragonLLGrammar;
import com.romanenco.cfrm.lexer.LexerError;
import com.romanenco.cfrm.lexer.RegLexer;
import com.romanenco.cfrm.llparser.LLParser;
import com.romanenco.cfrm.llparser.ParsingError;
import com.romanenco.cfrm.util.ParsingTreeToXML;
import com.romanenco.cfrm.util.XmlFileWriter;

/**
 * Sample console parser for Dragon grammar.
 * Accepts expressions like: 13 + 34 * (45 + 67)
 */
public final class ConsoleLLParser {

    private ConsoleLLParser() {
        // nothing
    }

    public static void main(String[] args) throws IOException {

        final Grammar dragonGrammar = new DragonLLGrammar();
        final Lexer lexer = new RegLexer(dragonGrammar);
        final Parser parser = new LLParser(dragonGrammar);

        final BufferedReader reader = new BufferedReader(new InputStreamReader(
                System.in, Charset.defaultCharset()));
        while (true) {
            print("Enter expression: ");
            final String input = reader.readLine();
            if ((input == null)||input.isEmpty()) {
                break;
            }
            processInput(input, lexer, parser);
        }
    }

    private static void processInput(String input, Lexer lexer, Parser parser) {
        try{
            final ParsingTreeNode tree = parser.parse(lexer.tokenize(input));
            print("Accepted!");
            saveTreeToFile(tree);
        } catch (LexerError | ParsingError e) {
            print("ERROR: " + e.getMessage());
        }
    }

    private static void print(String string) {
        System.out.println(string);  // NOPMD NOSONAR
    }

    private static void saveTreeToFile(ParsingTreeNode tree) {
        final Document xmlTree = ParsingTreeToXML.build(tree);
        XmlFileWriter.save(xmlTree, "output.xml");
    }

}
