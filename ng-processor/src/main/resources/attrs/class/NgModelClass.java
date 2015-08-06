private final class $attrClassName implements android.text.TextWatcher ,android.widget.CompoundButton.OnCheckedChangeListener, com.ngandroid.lib.ng.ModelObserver<$type> {

    private final $viewType view_;

    private $attrClassName($viewType view_) {
        this.view_ = view_;
    }

    @Override public void invoke($type arg) {
        #if($boolType)
            view_.setChecked(arg);
        #else
            view_.setText(arg);
        #end
    }

    @Override public void onCheckedChanged(android.widget.CompoundButton compoundButton, boolean b) {
        #if($boolType)
            ${scope.name}.${attrSource.getterSource};
        #end
    }
    @Override public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
    @Override public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
    @Override public void afterTextChanged(android.text.Editable editable) {
        #if(!$boolType)
            ${scope.name}.${attrSource.getterSource};
        #end
    }
}