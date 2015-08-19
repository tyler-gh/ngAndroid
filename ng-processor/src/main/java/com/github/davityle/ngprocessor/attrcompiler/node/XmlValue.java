package com.github.davityle.ngprocessor.attrcompiler.node;

import com.github.davityle.ngprocessor.attrcompiler.parse.ParseException;
import com.github.davityle.ngprocessor.attrcompiler.parse.Token;

import java.util.HashMap;
import java.util.Map;

public abstract class XmlValue extends Expression {

    protected XmlValue(Token token) {
        super(token);
    }

    private static Map<String, Resolver<?>> xmlValueMap = new HashMap<String, Resolver<?>>(){{
        put("@string", new Resolver<StringXmlValue>() {
            @Override
            public StringXmlValue create(Token token) {
                return new StringXmlValue(token);
            }
        });
        put("@bool", new Resolver<BoolXmlValue>() {
            @Override
            public BoolXmlValue create(Token token) {
                return new BoolXmlValue(token);
            }
        });
        put("@color", new Resolver<ColorXmlValue>() {
            @Override
            public ColorXmlValue create(Token token) {
                return new ColorXmlValue(token);
            }
        });
        put("@dimen", new Resolver<DimenXmlValue>() {
            @Override
            public DimenXmlValue create(Token token) {
                return new DimenXmlValue(token);
            }
        });
        put("@id", new Resolver<IdXmlValue>() {
            @Override
            public IdXmlValue create(Token token) {
                return new IdXmlValue(token);
            }
        });
        put("@integer", new Resolver<IntegerXmlValue>() {
            @Override
            public IntegerXmlValue create(Token token) {
                return new IntegerXmlValue(token);
            }
        });
        // TODO integer and typed arrays
    }};

    public static XmlValue getXmlValue(Token token) throws ParseException {
        if(xmlValueMap.containsKey(token.getScript())) {
            return xmlValueMap.get(token.getScript()).create(token);
        }
        throw new ParseException(token, String.format("Invalid xml value/key '%s'",token.getScript()));
    }

    public interface Resolver<T extends XmlValue> {
        T create(Token token);
    }

    public static class StringXmlValue extends XmlValue {

        StringXmlValue(Token token) {
            super(token);
        }

        @Override
        public void accept(IVisitor visitor) {
            visitor.visit(this);
        }
    }

    public static class BoolXmlValue extends XmlValue {

        BoolXmlValue(Token token) {
            super(token);
        }

        @Override
        public void accept(IVisitor visitor) {
            visitor.visit(this);
        }
    }

    public static class DimenXmlValue extends XmlValue {

        DimenXmlValue(Token token) {
            super(token);
        }

        @Override
        public void accept(IVisitor visitor) {
            visitor.visit(this);
        }
    }

    public static class IdXmlValue extends XmlValue {

        IdXmlValue(Token token) {
            super(token);
        }

        @Override
        public void accept(IVisitor visitor) {
            visitor.visit(this);
        }
    }

    public static class ColorXmlValue extends XmlValue {

        ColorXmlValue(Token token) {
            super(token);
        }

        @Override
        public void accept(IVisitor visitor) {
            visitor.visit(this);
        }
    }

    public static class IntegerXmlValue extends XmlValue {

        IntegerXmlValue(Token token) {
            super(token);
        }

        @Override
        public void accept(IVisitor visitor) {
            visitor.visit(this);
        }
    }

}
