package com.ngandroid.lib.ng;

import com.ngandroid.lib.utils.Tuple;
import com.ngandroid.lib.utils.TypeUtils;

import java.util.List;
import java.util.Map;

/**
 * Created by davityle on 1/12/15.
 */
public class MethodInvoker {
    // TODO enforce type checking

    private final Map<String, List<ModelMethod>> methodMap;
    private final Map<String, Tuple<Integer,Object>> fieldMap;

    public MethodInvoker(Map<String, List<ModelMethod>> methodMap, Map<String, Tuple<Integer,Object>> fieldMap) {
        this.methodMap = methodMap;
        this.fieldMap = fieldMap;
    }

    public Object invoke(String methodName, Object ... objects) throws Throwable{
        String fieldName = methodName.substring(3).toLowerCase();
        Tuple<Integer, Object> value= fieldMap.get(fieldName);
        if(value == null)
            return null;
        if(methodName.startsWith("get")){
            return value.getSecond();
        }else if(methodName.startsWith("set")){
            Object obj = objects[0];
            if(obj == TypeUtils.EMPTY){
                System.out.println("equals empty");
                obj = TypeUtils.getEmptyValue(value.getFirst());
                System.out.println(obj.getClass().getSimpleName());
            }
            value.setSecond(obj);
            List<ModelMethod> setters = methodMap.get(methodName.toLowerCase());
            for(ModelMethod modelMethod : setters){
                modelMethod.invoke(fieldName, obj);
            }
            return null;
        }else{
            // TODO throw error
            return null;
        }
    }
}
