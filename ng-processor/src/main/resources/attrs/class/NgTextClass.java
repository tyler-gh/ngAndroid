public final class $attrClassName implements com.ngandroid.lib.ng.ModelObserver<$type> {

    private final $viewType view_;

    private $attrClassName($viewType view_) {
        this.view_ = view_;
        try {
            this.invoke($attrSource.getGetterSource("${scope.javaName}$$NgScope.this.scope."));
        } catch(NullPointerException ignored){
            android.util.Log.w("NgAndroid", "Unable to get initial value for view '${view.id}' because of null pointer");
        }
    }

    @Override public void invoke($type arg) {
        view_.setText(ngOptions_.getValueFormatter().format(arg, view_.getText().toString()));
    }
}
