${scope.javaName}$$NgScope.${javaName}.${attrClassName} $attrClassName = ${scope.name}.${javaName}.get${attrClassName}($viewName);
#if($isBoolType)
${viewName}.setOnCheckedChangeListener(${attrClassName});
#else
${viewName}.addTextChangedListener(${attrClassName});
#end
$attrSource.getObserverSource($attrClassName, "${scope.name}.");
