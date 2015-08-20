public final class $attrClassName implements com.ngandroid.lib.ng.ModelObserver {

    private final $viewType view_;

    private $attrClassName($viewType view_) {
        this.view_ = view_;
        try {
            this.invoke(null);
        } catch(NullPointerException ignored){
            android.util.Log.w("NgAndroid", "Unable to get initial value for view '${view.id}' because of null pointer");
        }
    }

    @Override public void invoke(Object disabled) {
        if($attrSource.getGetterSource("${scope.javaName}$$NgScope.this.scope.", "${packageName}.")){
            view_.requestFocus();
        } else {
            view_.clearFocus();
        }
    }
}
