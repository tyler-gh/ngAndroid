package com.github.davityle.ngprocessor.attrcompiler.node;

import com.github.davityle.ngprocessor.attrcompiler.parse.ParseException;
import com.github.davityle.ngprocessor.attrcompiler.parse.Token;

import java.util.HashMap;
import java.util.Map;

public abstract class SpecialIdentifier extends Expression {
    private static Map<String, Resolver<?>> identifierMap = new HashMap<String, Resolver<?>>(){{
        put("$view", new Resolver<ViewIdentifier>() {
            @Override
            public ViewIdentifier create(Token token) {
                return new ViewIdentifier(token);
            }
        });
    }};

    public static SpecialIdentifier getSpecialIdentifier(Token token) throws ParseException {
        if(identifierMap.containsKey(token.getScript())) {
            return identifierMap.get(token.getScript()).create(token);
        }
        throw new ParseException(token, String.format("Invalid special identifier '%s'",token.getScript()));
    }

    protected SpecialIdentifier(Token token) {
        super(token);
    }

    public abstract String getCompiledSource();

    public interface Resolver<T extends SpecialIdentifier> {
        T create(Token token);
    }

}
