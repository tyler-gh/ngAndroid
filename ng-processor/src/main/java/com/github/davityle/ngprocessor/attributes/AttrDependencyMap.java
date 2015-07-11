package com.github.davityle.ngprocessor.attributes;

import java.util.HashMap;

/**
 * Created by tyler on 7/4/15.
 */
class AttrDependencyMap extends HashMap<String, AttrDependency> {
    public AttrDependency put(AttrDependency value) {
        return super.put(value.getClassName(), value);
    }
}
