$attrClassName $attrClassName = new $attrClassName($viewName);
if($viewName instanceof android.widget.CompoundButton) {
((android.widget.CompoundButton)$viewName).setOnCheckedChangeListener($attrClassName);
} else if($viewName instanceof android.widget.TextView) {
((android.widget.TextView)$viewName).addTextChangedListener($attrClassName);
}
${scope.name}.${attrSource.getObserverSource($attrClassName)};
