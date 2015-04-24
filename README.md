![Android + Angular](/../pictures/images/ngandroid.png?raw=true "Android + Angular")

# ngAndroid

[![Join the chat at https://gitter.im/davityle/ngAndroid](https://badges.gitter.im/Join%20Chat.svg)](https://gitter.im/davityle/ngAndroid?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)

[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-ngAndroid-brightgreen.svg?style=flat)](http://android-arsenal.com/details/1/1724)

ngAndroid brings some of the angular directives to android xml attributes. 

The current version is based on reflection. This library will soon use compile time annotations with no reflection. The API will change slightly when this is completed. Including allowing any class to be used, along with interfaces, to declare a model. You can try out the reflection version using.

`compile 'com.github.davityle:ngandroid:0.0.4'`

Feedback and contributions are encouraged

Also there are known issues with adding custom attributes to Android views while using the AppCompat libraries such as FragmentActivity. Please report any other issues and we'll work to figure them out

Currently supported angular directives.

[NgModel](#ngmodel)<br>
[NgClick](#ngclick)<br>
[NgLongClick](#nglongclick)<br>
[NgChange](#ngchange)<br>
[NgDisabled](#ngdisabled)<br>
[NgInvisible](#nginvisible)<br>
[NgGone](#nggone)<br>
[NgBlur](#ngblur)<br>
[NgFocus](#ngfocus)<br>
<br>
[Common Gotchas](#a-couple-of-gotchas)<br>

Directives that are on the road map
```
ngRepeat
ngSrc
ngJsonSrc
```
--------
![NgAndroid Demonstration](/../pictures/images/screencast.gif?raw=true "ngAndroid at work")
--------

<b>All examples are using this model</b>
```java
// create model
public interface Input {
    public String getInput();
    public void setInput(String input);
    public int getInteger();
    public void setInteger(int input);
    public boolean getDisabled();
    public void setDisabled(boolean disabled);
    public boolean getGone();
    public void setGone(boolean disabled);
    public boolean getInvisible();
    public void setInvisible(boolean disabled);
}
```

##ngModel

```xml
<!-- add xml attributes -->

<EditText
    android:id="@+id/editText"
    ngAndroid:ngModel="input.input"
    ... />
    
<TextView
    android:id="@+id/textView"
    ngAndroid:ngModel="input.input"
    .../>
```
```java
// create a field with your model (no need to instantiate it)
private Input input;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Use NgAndroid to inflate your view 
        NgAndroid.setContentView(this, R.layout.activity_demo);

        input.setInput("Hello world");
        
        ....
    }

```

With those lines of code, your view is now bound to your data model and vice versa.

--------

##ngClick

```xml
<Button
    android:id="@+id/stringClickEvent"
    ngAndroid:ngClick="stringClickEvent()"
    .../>
```
```java
private void stringClickEvent(){
    stringClickEvent.setText(input.getInput());
}
```

--------

##ngLongClick
```xml
<Button
    android:id="@+id/multiplyButton"
    ngAndroid:ngClick="multiply(input.integer,2)"
    ngAndroid:ngLongClick="multiply(3,input.integer)"
    .../>

```
```java
private void multiply(int num1, int num2){
    Toast.makeText(this, String.valueOf(num1 * num2), Toast.LENGTH_SHORT).show();
}
```
--------

##ngChange
```xml
<EditText
    android:id="@+id/ngChangeEditText"
    ngAndroid:ngChange="onChange()"
    .../>
```
```java
private void onChange(){
    Toast.makeText(this, "Text Changed", Toast.LENGTH_SHORT).show();
}
```

--------

##ngDisabled
```xml
<CheckBox
    android:id="@+id/ngdisabledcheckbox"
    ngAndroid:ngModel="input.disabled"
    ... />

<Button
    android:id="@+id/ineedthisid"
    ngAndroid:ngDisabled="input.disabled"
    .../>
```
![NgDisabled Demonstration](/../pictures/images/ngdisable.gif?raw=true "ngdisabled demonstration")
--------

##ngInvisible
```xml
<Button
    android:id="@+id/nginvisible"
    ngAndroid:ngInvisible="input.invisible"
    .../>

<CheckBox
    android:id="@+id/ngvisiblecb"
    ngAndroid:ngModel="input.invisible"
    .../>
```
![NgInvisible Demonstration](/../pictures/images/nginvisible.gif?raw=true "nginvisible demonstration")
--------

##ngGone
```xml
<Button
    android:id="@+id/nggone"
    ngAndroid:ngGone="input.gone"
    .../>

<CheckBox
    android:id="@+id/nggonedb"
    ngAndroid:ngModel="input.gone"
    ... />
```
![NgGone Demonstration](/../pictures/images/nggone.gif?raw=true "nggone demonstration")
--------
##Other Functionality

####Build a model from Json
```java
ngAndroid.modelFromJson(json, TestJsonModel.class)
```
####Build a model without a view
```java
ngAndroid.buildModel(TestSubModel.class);
```
###Pre-Build a scope
```java
ngAndroid.buildScope(TestScope.class);
```

--------

##A couple of gotchas:

Each view that has an ngangular attribute must also have an id

Your model must be declared using an iterface.
```java
public interface Model{
    public void setField(String field);
    public String getField();
}
```
You would then reference it in your xml attribute as `{name of model in scope}.field`

Your scope is the parent or container of your models and methods and can be as broad as the Application or contained in a single view or even a single Scope class. To declare the above model in a scope you would use do something like this.

```java
public class Scope{
    private Model model;
    private void onClickMethod(String modelField){
    }
}
```

With that scope, you could reference the model and the method like this: `model.field` `onClickMethod(model.field)`

Your scope is usually just your activity. As it is in the examples above. If you use a seperate scope class than you must use `NgAndroid.setContentView(Object scope, Activity activity, int resourceId)` instead of `NgAndroid.setContentView(Activity activity, int resourceId)` which is shown in the examples

The models in your scope will automatically be built if they are referenced in your xml file.


--------

"[AngularJS logo.svg](https://github.com/angular/angular.js/tree/master/images/logo)" by [AngularJS](https://angularjs.org/) is licensed under <a rel="nofollow" class="external text" href="http://creativecommons.org/licenses/by-sa/3.0/">Creative Commons Attribution-ShareAlike 3.0 Unported License</a>


