public final class $attrClassName implements com.ngandroid.lib.ng.ModelObserver<$type> {

    private final $viewType view_;

    private $attrClassName($viewType view_) {
        this.view_ = view_;
    }

    @Override public void invoke($type arg) {
        view_.setText(ngOptions_.getValueFormatter().format(arg, view_.getText().toString()));
    }
}
