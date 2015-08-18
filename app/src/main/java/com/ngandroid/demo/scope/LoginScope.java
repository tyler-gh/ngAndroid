package com.ngandroid.demo.scope;

import com.ngandroid.demo.model.User;
import com.ngandroid.lib.annotations.NgModel;
import com.ngandroid.lib.annotations.NgScope;

@NgScope(name="Login")
public class LoginScope {

    @NgModel
    User user;

}
