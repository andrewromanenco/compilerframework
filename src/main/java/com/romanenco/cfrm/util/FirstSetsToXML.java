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

import com.romanenco.cfrm.llparser.FirstSets;

public final class FirstSetsToXML {

    private static final String ROOT = "FirstSets";

    private FirstSetsToXML() {
        // nothing
    }

    public static Document build(FirstSets firstSets) {
        final SetToXMLUtil util = new SetToXMLUtil(ROOT, firstSets.getFirstSet());
        util.buildXmlDocument();
        return util.getDocument();
    }

}
