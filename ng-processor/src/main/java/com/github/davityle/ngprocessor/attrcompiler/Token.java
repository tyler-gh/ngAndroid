
/*
 * Copyright 2015 Tyler Davis
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.github.davityle.ngprocessor.attrcompiler;

/**
* Created by davityle on 1/15/15.
*/
public final class Token {
    private final TokenType tokenType;
    private final String script;

    Token(TokenType tokenType, String script) {
        this.tokenType = tokenType;
        this.script = script;
    }

    @Override
    public String toString() {
        return tokenType.toString() + "::" + script;
    }

    public TokenType getTokenType() {
        return tokenType;
    }

    public String getScript() {
        return script;
    }
}
