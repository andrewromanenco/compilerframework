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

import java.io.File;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;

public final class XmlFileWriter {

    private XmlFileWriter() {
        // nothing
    }

    public static void save(Document document, String fileName) {
        try {
            final TransformerFactory factory = TransformerFactory.newInstance();
            final Transformer transformer = factory.newTransformer();
            final DOMSource source = new DOMSource(document);
            final StreamResult result = new StreamResult(new File(fileName));
            transformer.transform(source, result);
        } catch (TransformerException e) {
            throw new UtilError("Error writing XML to file", e);
        }
    }

}
