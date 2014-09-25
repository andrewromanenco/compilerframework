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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LexerRule {

    public enum TYPE {
        IGNORE,
        VALID;
    }

    private final Pattern regex;
    private final TYPE type;

    public LexerRule(String pattern) {
        regex = Pattern.compile(pattern);
        this.type = TYPE.VALID;
    }

    /**
     * @param pattern
     * @param sendToParser - if false token is ignored
     */
    public LexerRule(String pattern, TYPE type) {
        regex = Pattern.compile(pattern);
        if (type == null) {
            this.type = TYPE.VALID;
        } else {
            this.type = type;
        }
    }

    public String getNextValue(String input, int start) {
        final Matcher matcher = regex.matcher(input);
        if (matcher.find(start) && (matcher.start() == start)) {
            return input.substring(matcher.start(), matcher.end());
        }
        return null;
    }

    public TYPE getType() {
        return type;
    }

}
