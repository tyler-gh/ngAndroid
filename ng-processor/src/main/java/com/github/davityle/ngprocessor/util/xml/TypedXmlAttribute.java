package com.github.davityle.ngprocessor.util.xml;

import com.github.davityle.ngprocessor.attrcompiler.sources.BinaryOperatorSource;
import com.github.davityle.ngprocessor.attrcompiler.sources.KnotSource;
import com.github.davityle.ngprocessor.attrcompiler.sources.MethodSource;
import com.github.davityle.ngprocessor.attrcompiler.sources.ModelSource;
import com.github.davityle.ngprocessor.attrcompiler.sources.Source;
import com.github.davityle.ngprocessor.attrcompiler.sources.TernarySource;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by tyler on 4/22/15.
 */
public class TypedXmlAttribute extends XmlAttribute {

    private final Map<ModelSource, ModelSource> typedModels;
    private final Map<MethodSource, MethodSource> typedMethods;

    public TypedXmlAttribute(XmlAttribute attribute, Map<ModelSource, ModelSource> typedModels, Map<MethodSource, MethodSource> typedMethods) {
        super(attribute.getName(), attribute.getValue());
        this.typedModels = typedModels;
        this.typedMethods = typedMethods;
        setSource(setTypedModelsAndMethods(attribute.getSource()));
    }

    private Source setTypedModelsAndMethods(Source source){
        if(source instanceof ModelSource){
            return typedModels.get(source);
        }else if(source instanceof MethodSource){
            MethodSource typed = typedMethods.get(source);
            ArrayList<Source> parameters = new ArrayList<Source>();
            for(Source s : typed.getParameters()){
                parameters.add(setTypedModelsAndMethods(s));
            }
            typed.setParameters(parameters);
            return typed;
        }else if(source instanceof BinaryOperatorSource){
            BinaryOperatorSource copy = ((BinaryOperatorSource) source).copy();
            copy.setLeftSide(setTypedModelsAndMethods(copy.getLeftSide()));
            copy.setRightSide(setTypedModelsAndMethods(copy.getRightSide()));
            return copy;
        }else if(source instanceof TernarySource){
            TernarySource copy = ((TernarySource) source).copy();
            copy.setBooleanSource(setTypedModelsAndMethods(copy.getBooleanSource()));
            copy.setValFalse(setTypedModelsAndMethods(copy.getValFalse()));
            copy.setValTrue(setTypedModelsAndMethods(copy.getValTrue()));
            return copy;
        }else if(source instanceof KnotSource){
            KnotSource copy = ((KnotSource) source).copy();
            copy.setBooleanSource(setTypedModelsAndMethods(copy.getBooleanSource()));
            return copy;
        }
        return source.copy();
    }

}
