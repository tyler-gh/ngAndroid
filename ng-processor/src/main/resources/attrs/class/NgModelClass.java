public final class $attrClassName implements#if(!$isBoolType) android.text.TextWatcher, #else android.widget.CompoundButton.OnCheckedChangeListener, #end com.ngandroid.lib.ng.ModelObserver<$type> {

    private final $viewType view_;
    private boolean fromSelf;

    private $attrClassName($viewType view_) {
        this.view_ = view_;
        try {
            this.invoke($attrSource.getGetterSource("${scope.javaName}$$NgScope.this.scope.", "${packageName}."));
        } catch(NullPointerException ignored){
            android.util.Log.w("NgAndroid", "Unable to get initial value for view '${view.id}' because of null pointer");
        }
    }

    @Override public void invoke($type arg) {
        if(!fromSelf){
            fromSelf = true;
        #if($isBoolType)
            view_.setChecked(arg);
        #else
            view_.setText(ngOptions_.getValueFormatter().format(arg, view_.getText().toString()));
        #end
        }
        fromSelf = false;
    }
    #if($isBoolType)
    @Override public void onCheckedChanged(android.widget.CompoundButton compoundButton, boolean b) {
        if(!fromSelf){
            fromSelf = true;
            ${scope.javaName}$$NgScope.this.scope.${attrSource.getSetterSource("b")};
        }
        fromSelf = false;
    }
    #end
    #if(!$isBoolType)
    @Override public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
    @Override public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
    @Override public void afterTextChanged(android.text.Editable editable) {
        if(!fromSelf){
            fromSelf = true;
            #set( $getterSource = $attrSource.getGetterSource("${scope.javaName}$$NgScope.this.scope.", "${package}."))
            ${scope.javaName}$$NgScope.this.scope.${attrSource.getSetterSource("ngOptions_.getValueFormatter().getValue(${getterSource}, editable.toString())")};
        }
        fromSelf = false;
    }
    #end
}