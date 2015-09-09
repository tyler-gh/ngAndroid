![Android + Angular](/../pictures/images/ngandroid.png?raw=true "Android + Angular")

#NgAndroid

[![Join the chat at https://gitter.im/davityle/ngAndroid](https://badges.gitter.im/Join%20Chat.svg)](https://gitter.im/davityle/ngAndroid?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)

[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-ngAndroid-brightgreen.svg?style=flat)](http://android-arsenal.com/details/1/1724)

NgAndroid provides two-way data binding and MVC to Android. It accomplishes this using compile time annotation processing and Java source generation which makes the bindings type safe and effecient.

the latest version NgAndroid is in an unstable beta and is subject to API changes. The readme to a more stable version can be found [here](https://github.com/davityle/ngAndroid/blob/master/README-OLD.md)

NgAndroid can be added as a gradle dependency:
```groovy
buildscript {
    repositories {
        jcenter()
    }
    dependencies {
        classpath 'com.neenbedankt.gradle.plugins:android-apt:1.4'
    }
}

allprojects {
    repositories {
        maven {
            url 'http://oss.sonatype.org/content/repositories/snapshots'
        }
    }
}
apply plugin: 'com.neenbedankt.android-apt'

dependencies {
    compile 'com.github.davityle:ngandroid:1.0.10-SNAPSHOT'
    apt 'com.github.davityle:ng-processor:1.0.10-SNAPSHOT'
}
```

#Usage

NgAndoid works by generating a controller class and event binding models using two annotations (subject to change) `@NgModel` and `@NgScope`. You can read more about them below. After you have annotated a class with `@NgScope` and referenced it in your xml file a controller will be generated for that xml file. If I created a scope class `MyScope` and referenced it in an xml layout `login.xml` a controller called `LoginController` would be created for the xml layout. To bind your scope to your layout you would instaniate the controller, passing in the scope, and call `LoginController#attach(android.view.View view)` passing in the view. For example 

```java
View v = inflater.inflate(R.layout.login, container, false);
MyScope scope = new MyScope();
LoginController controller = new LoginController(new NgOptions.Builder().build(), scope)
controller.attach(v);
```

##@NgModel

`@NgModel` marks a field within a scope as a model that can be bound to views. Any field that is marked as a model will be injected into the scope automatically. This is imporant because ngAndroid will create a subclass of the Model that triggers events when it is changed and somthing is listening to those changes.

The type of any field marked with `@NgModel` should have getters and setters declared for each of it's fields.

##@NgScope

`@NgScope` marks a class as a scope. A scope is the base reference for any data binding reference. 

```java
@NgScope(name="Login")
public class LoginScope {

    @NgModel
    User user;

    void onSubmit(/*any paramaters*/) {
        // submission code
    }

}

```
To use a scope you set the ngScope attribute in your xml to the name of the scope. For example if I were to use the above scope I would add

```xml
<RelativeLayout ...
    xmlns:x="http://schemas.android.com/apk/res-auto"
    x:ngScope="Login">
```

to my layout. I could then reference any methods or models declared in the scope in my xml bindings.

```xml
<EditText ...
    x:ngModel="user.username" />

<EditText
    x:ngModel="user.password"/>

<Button ...
    x:ngDisabled="user.username.length() &lt; 6 || user.password.length() &lt; 6"
    x:ngClick="onSubmit($view.context)"/>
```

##Attributes

[NgModel](#ngmodel) Two way data binding<br>
[NgText](#ngmodel) One way data to text view binding<br>
[NgClick](#ngclick) Click event binding<br>
[NgLongClick](#nglongclick) Long click event binding<br>
[NgDisabled](#ngdisabled) Two way boolean data binding to view disabled state<br>
[NgInvisible](#nginvisible) Two way boolean data binding to view invisible state<br>
[NgGone](#nggone) Two way boolean data binding to view gone state<br>
[NgFocus](#ngfocus) Two way boolean data binding to view focus state<br>
<br>
[Common Gotchas](#gotchas)<br>

##Coming Soon
```
ngRepeat
ngChange
deep integration with injection of scopes and models using Dagger 2
```

--------
![NgAndroid Demonstration](/../pictures/images/screencast.gif?raw=true "ngAndroid at work")
--------

<b>All examples are using this model</b>
```java
public class Input {
    private String input;
    private int integer;
    private boolean disabled;
    private boolean gone;
    private boolean invisible;
    private boolean blur;
    private boolean focus;

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }

    public int getInteger() {
        return integer;
    }

    public void setInteger(int integer) {
        this.integer = integer;
    }

    public boolean getDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public boolean getGone() {
        return gone;
    }

    public void setGone(boolean gone) {
        this.gone = gone;
    }

    public boolean getInvisible() {
        return invisible;
    }

    public void setInvisible(boolean invisible) {
        this.invisible = invisible;
    }

    public boolean getBlur() {
        return blur;
    }

    public void setBlur(boolean blur) {
        this.blur = blur;
    }

    public boolean getFocus() {
        return focus;
    }

    public void setFocus(boolean focus) {
        this.focus = focus;
    }
}
```

##ngModel

```xml
<!-- add xml attributes -->

<EditText
    android:id="@+id/edit_text"
    x:ngModel="input.input"
    ... />
    
<TextView
    android:id="@+id/text_view"
    x:ngModel="input.input"
    .../>
```

--------

##ngClick

```xml
<Button
    android:id="@+id/click_event"
    x:ngClick="onClickEvent()"
    .../>
```
```java
    void onClickEvent(){
        // do something
    }
```

--------

##ngLongClick
```xml
<Button
    android:id="@+id/multiplyButton"
    x:ngClick="multiply(input.integer,2)"
    x:ngLongClick="multiply(3,input.integer)"
    .../>

```
```java
    void multiply(int num1, int num2){
        // do something
    }
```

--------

##ngDisabled
```xml
<CheckBox
    android:id="@+id/ngdisabledcheckbox"
    x:ngModel="input.disabled"
    ... />

<Button
    android:id="@+id/an_id_is_required_for_any_ng_android_binding"
    x:ngDisabled="input.disabled"
    .../>
```
![NgDisabled Demonstration](/../pictures/images/ngdisable.gif?raw=true "ngdisabled demonstration")
--------

##ngInvisible
```xml
<Button
    android:id="@+id/nginvisible"
    x:ngInvisible="input.invisible"
    .../>

<CheckBox
    android:id="@+id/ngvisiblecb"
    x:ngModel="input.invisible"
    .../>
```
![NgInvisible Demonstration](/../pictures/images/nginvisible.gif?raw=true "nginvisible demonstration")
--------

##ngGone
```xml
<Button
    android:id="@+id/nggone"
    x:ngGone="input.gone"
    .../>

<CheckBox
    android:id="@+id/nggonedb"
    x:ngModel="input.gone"
    ... />
```
![NgGone Demonstration](/../pictures/images/nggone.gif?raw=true "nggone demonstration")

--------

##Gotchas:

Each view that has an ngangular attribute must also have an id

--------

Feedback and contributions are encouraged

--------

"[AngularJS logo.svg](https://github.com/angular/angular.js/tree/master/images/logo)" by [AngularJS](https://angularjs.org/) is licensed under <a rel="nofollow" class="external text" href="http://creativecommons.org/licenses/by-sa/3.0/">Creative Commons Attribution-ShareAlike 3.0 Unported License</a>


