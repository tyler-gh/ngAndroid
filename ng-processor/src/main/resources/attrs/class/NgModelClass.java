private final class $attrClassName implements#if(!$isBoolType) android.text.TextWatcher, #else android.widget.CompoundButton.OnCheckedChangeListener, #end com.ngandroid.lib.ng.ModelObserver<$type> {

    private final $viewType view_;
    private boolean fromSelf = false;

    private $attrClassName($viewType view_) {
        this.view_ = view_;
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
            ${scope.name}.${attrSource.getSetterSource("b")};
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
            ${scope.name}.${attrSource.getSetterSource("ngOptions_.getValueFormatter().getValue(${type}.class, editable.toString(), ngOptions_.getValueFormatter().format(${scope.name}.${attrSource.getterSource}))")};
        }
        fromSelf = false;
    }
    #end
}