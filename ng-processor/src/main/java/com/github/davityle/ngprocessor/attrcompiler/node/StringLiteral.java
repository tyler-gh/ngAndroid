package com.github.davityle.ngprocessor.attrcompiler.node;

import com.github.davityle.ngprocessor.attrcompiler.parse.Token;

public class StringLiteral extends Expression {
    public StringLiteral(Token source) {
        super(source);
    }

    @Override
    public void accept(IVisitor visitor) {
        visitor.visit(this);
    }

    public String toJavaString() {
        StringBuilder result = new StringBuilder();
        boolean insideEscape = false;

        String source = getToken().getScript();
        source = source.substring(1, source.length() - 1);

        result.append('"');

        for (char character : source.toCharArray()) {
            if (!insideEscape) {
                if (character == '\\') {
                    insideEscape = true;
                } else if (character == '"') {
                    result.append("\\\"");
                } else {
                    result.append(character);
                }
            } else {
                insideEscape = false;
                result.append('\\');
                result.append(character);
            }
        }

        result.append('"');

        return result.toString();
    }
}
