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

public class LexerError extends RuntimeException {

    private static final long serialVersionUID = 2359227499848624081L;

    private final int errorLine;
    private final int errorColumn;

    public LexerError(String message) {
        this(message, -1, -1);
    }

    public LexerError(String nextInput, int errorLine, int errorColumn) {
        super(nextInput);
        this.errorLine = errorLine;
        this.errorColumn = errorColumn;
    }

    @Override
    public String toString() {
        return String.format("Lexer error [%d, %d]: %s",
                errorLine, errorColumn, getMessage());
    }

}
