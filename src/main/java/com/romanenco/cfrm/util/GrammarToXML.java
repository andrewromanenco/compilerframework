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

package com.romanenco.cfrm.util;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.romanenco.cfrm.Grammar;
import com.romanenco.cfrm.grammar.NonTerminal;
import com.romanenco.cfrm.grammar.Production;
import com.romanenco.cfrm.grammar.Symbol;
import com.romanenco.cfrm.grammar.Terminal;

public final class GrammarToXML extends AbstractXmlBuilder<Grammar> {

    private static final String ROOT = "Grammar";
    private static final String EPSILON = "Epsilon";
    private static final String START_SYMBOL = "StartSymbol";
    private static final String SYMBOL = "Symbol";
    private static final String ATTR_NAME = "name";
    private static final String TERMINALS = "Terminals";
    private static final String NON_TERMINALS = "NonTerminals";
    private static final String PRODUCTIONS = "Productions";
    private static final String PRODUCTION = "Production";


    private GrammarToXML(Grammar grammar) {
        super(ROOT, grammar);
    }

    public static Document build(Grammar grammar) {
        final GrammarToXML util = new GrammarToXML(grammar);
        util.buildXmlDocument();
        return util.getDocument();
    }

    @Override
    public void buildXmlDocument() {
        addClassNameToRoot();
        addStartSymbol();
        addEpsilon();
        addNonTerminals();
        addTerminals();
        addProductions();
    }

    private void addStartSymbol() {
        final Element startSymbol = document.createElement(START_SYMBOL);
        startSymbol.setAttribute(ATTR_NAME, data.getStartSymbol().getName());
        rootElement.appendChild(startSymbol);
    }

    private void addEpsilon() {
        final Element epsilon = document.createElement(EPSILON);
        epsilon.setAttribute(ATTR_NAME, data.getEpsilon().getName());
        rootElement.appendChild(epsilon);
    }

    private void addClassNameToRoot() {
        rootElement.setAttribute("class", data.getClass().getName());
    }

    private void addNonTerminals() {
        final Element nonTerminals = document.createElement(NON_TERMINALS);
        for (final NonTerminal symbol: data.getNonTerminals()) {
            nonTerminals.appendChild(symbolElement(symbol));
        }
        rootElement.appendChild(nonTerminals);
    }

    private void addTerminals() {
        final Element terminals = document.createElement(TERMINALS);
        for (final Terminal symbol: data.getTerminals()) {
            terminals.appendChild(symbolElement(symbol));
        }
        rootElement.appendChild(terminals);
    }

    private Element symbolElement(Symbol symbol) {
        final Element element = document.createElement(SYMBOL);
        element.setAttribute(ATTR_NAME, symbol.toString());
        return element;
    }

    private void addProductions() {
        final Element productions = document.createElement(PRODUCTIONS);
        for (final Production production: data.getProductions()) {
            final Element element = document.createElement(PRODUCTION);
            element.appendChild(document.createTextNode(production.toString()));
            productions.appendChild(element);
        }
        rootElement.appendChild(productions);
    }

}
