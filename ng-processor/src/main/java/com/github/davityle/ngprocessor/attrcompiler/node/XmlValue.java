package com.github.davityle.ngprocessor.attrcompiler.node;

import com.github.davityle.ngprocessor.attrcompiler.parse.ParseException;
import com.github.davityle.ngprocessor.attrcompiler.parse.Token;

import java.util.HashMap;
import java.util.Map;

public abstract class XmlValue extends Expression {
    private final Token require;

    protected XmlValue(Token token, Token require) {
        super(token);
        this.require = require;
    }

    private static Map<String, Resolver<?>> xmlValueMap = new HashMap<String, Resolver<?>>(){{
        put("@string", new Resolver<StringXmlValue>() {
            @Override
            public StringXmlValue create(Token token, Token require) {
                return new StringXmlValue(token, require);
            }
        });
        put("@bool", new Resolver<BoolXmlValue>() {
            @Override
            public BoolXmlValue create(Token token, Token require) {
                return new BoolXmlValue(token, require);
            }
        });
        put("@color", new Resolver<ColorXmlValue>() {
            @Override
            public ColorXmlValue create(Token token, Token require) {
                return new ColorXmlValue(token, require);
            }
        });
        put("@dimen", new Resolver<DimenXmlValue>() {
            @Override
            public DimenXmlValue create(Token token, Token require) {
                return new DimenXmlValue(token, require);
            }
        });
        put("@id", new Resolver<IdXmlValue>() {
            @Override
            public IdXmlValue create(Token token, Token require) {
                return new IdXmlValue(token, require);
            }
        });
        put("@integer", new Resolver<IntegerXmlValue>() {
            @Override
            public IntegerXmlValue create(Token token, Token require) {
                return new IntegerXmlValue(token, require);
            }
        });
        // TODO integer and typed arrays
    }};

    public static XmlValue getXmlValue(Token token, Token require) throws ParseException {
        if(xmlValueMap.containsKey(token.getScript())) {
            return xmlValueMap.get(token.getScript()).create(token, require);
        }
        throw new ParseException(token, String.format("Invalid xml value/key '%s'",token.getScript()));
    }

    public String getSource(String prependage) {
        return getXmlValueSource(prependage) + require.getScript().substring(1);
    }

    protected abstract String getXmlValueSource(String prependage);

    public interface Resolver<T extends XmlValue> {
        T create(Token token, Token require);
    }

    public static class StringXmlValue extends XmlValue {

        StringXmlValue(Token token, Token require) {
            super(token, require);
        }

        @Override
        public void accept(IVisitor visitor) {
            visitor.visit(this);
        }

        @Override
        public String getXmlValueSource(String prependage) {
            return prependage + "R.string.";
        }
    }

    public static class BoolXmlValue extends XmlValue {

        BoolXmlValue(Token token, Token require) {
            super(token, require);
        }

        @Override
        public void accept(IVisitor visitor) {
            visitor.visit(this);
        }

        @Override
        public String getXmlValueSource(String prependage) {
            return prependage + "R.bool.";
        }
    }

    public static class DimenXmlValue extends XmlValue {

        DimenXmlValue(Token token, Token require) {
            super(token, require);
        }

        @Override
        public void accept(IVisitor visitor) {
            visitor.visit(this);
        }

        @Override
        public String getXmlValueSource(String prependage) {
            return prependage + "R.dimen.";
        }
    }

    public static class IdXmlValue extends XmlValue {

        IdXmlValue(Token token, Token require) {
            super(token, require);
        }

        @Override
        public void accept(IVisitor visitor) {
            visitor.visit(this);
        }

        @Override
        public String getXmlValueSource(String prependage) {
            return prependage + "R.id.";
        }
    }

    public static class ColorXmlValue extends XmlValue {

        ColorXmlValue(Token token, Token require) {
            super(token, require);
        }

        @Override
        public void accept(IVisitor visitor) {
            visitor.visit(this);
        }

        @Override
        public String getXmlValueSource(String prependage) {
            return prependage + "R.color.";
        }
    }

    public static class IntegerXmlValue extends XmlValue {

        IntegerXmlValue(Token token, Token require) {
            super(token, require);
        }

        @Override
        public void accept(IVisitor visitor) {
            visitor.visit(this);
        }

        @Override
        public String getXmlValueSource(String prependage) {
            return prependage + "R.integer.";
        }
    }

}
