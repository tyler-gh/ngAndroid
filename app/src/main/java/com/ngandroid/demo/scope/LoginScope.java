package com.ngandroid.demo.scope;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;

import com.ngandroid.demo.model.User;
import com.ngandroid.demo.ui.pages.main.DemoActivity;
import com.ngandroid.lib.annotations.NgModel;
import com.ngandroid.lib.annotations.NgScope;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

@NgScope(name="Login")
public class LoginScope {

    @NgModel
    User user;

    void onSubmit(final Context context) {
        final ProgressDialog dialog = new ProgressDialog(context);
        dialog.setCancelable(false);
        dialog.setMessage("Logging in");
        dialog.show();
        Observable.timer(4, TimeUnit.SECONDS)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Subscriber<Long>() {
                @Override
                public void onCompleted() {
                    dialog.dismiss();
                    context.startActivity(new Intent(context, DemoActivity.class));
                }

                @Override
                public void onError(Throwable e) {
                }

                @Override
                public void onNext(Long aLong) {
                }
            });
    }

}
