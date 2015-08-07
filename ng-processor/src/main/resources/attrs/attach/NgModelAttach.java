$attrClassName $attrClassName = new $attrClassName($viewName);
#if($isBoolType)
${viewName}.setOnCheckedChangeListener(${attrClassName});
#else
${viewName}.addTextChangedListener(${attrClassName});
#end
${scope.name}.${attrSource.getObserverSource($attrClassName)};
