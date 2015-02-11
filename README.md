# ngAndroid

ngAndroid brings some of the angular directives to android xml attributes.

Currently supported angular directives.

[NgModel](#ngmodel)
[NgLongClick](#nglongclick)
[NgClick](#ngclick)
[NgInvisible](#nginvisible)
[NgGone](#nggone)
[NgChange](#ngchange)
[NgDisabled](#ngdisabled)

Directives that are on the road map
```
ngBlur
ngFocus
ngRepeat
ngSrc
ngJsonSrc
ngSubmit
ngForm
```

--------

##ngModel

```java
// create model

public interface Input {
    public String getInput();
    public void setInput(String input);
    ...
}
```
```xml
<!-- add xml attributes -->

<EditText
    android:id="@+id/editText"
    ngAndroid:ngModel="input.input"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content" />
    
<TextView
    android:id="@+id/textView"
    android:layout_alignParentRight="true"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    ngAndroid:ngModel="input.input"/>
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

![Alt text](/../pictures/images/screencast.gif?raw=true "ngAndroid at work")

--------

##ngClick

```xml
<Button
    android:id="@+id/multiplyButton"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:layout_below="@+id/really2"
    android:layout_marginTop="20dp"
    ngAndroid:ngClick="multiply(input.test,2)"
    android:text="Multiply"/>
```
```java
private void multiply(int num1, int num2){
    Toast.makeText(this, String.valueOf(num1 * num2), Toast.LENGTH_SHORT).show();
}
```

--------

##ngLongClick
```xml
<Button
    android:id="@+id/multiplyButton"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:layout_below="@+id/really2"
    android:layout_marginTop="20dp"
    ngAndroid:ngClick="multiply(input.integer,2)"
    ngAndroid:ngLongClick="multiply(3,input.integer)"
    android:text="multiply(input.integer,2) onClick \n multiply(3,input.integer) onLongClick"/>

```

--------

##ngChange
```xml
<EditText
    android:id="@+id/ngChangeEditText"
    ngAndroid:ngChange="onChange()"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:layout_marginTop="10dp"
    android:layout_below="@id/multiplyButton"/>
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
    android:layout_width="wrap_content"
    android:layout_height="wrap_content" />

<Button
    android:id="@+id/ineedthisid"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    ngAndroid:ngDisabled="input.disabled"
    android:text="button"/>
```

--------

##ngInvisible
```xml
<Button
    android:id="@+id/nginvisible"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    ngAndroid:ngInvisible="input.invisible"
    android:text="button"/>

<CheckBox
    android:id="@+id/ngvisiblecb"
    ngAndroid:ngModel="input.invisible"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content" />
```

--------

##ngGone
```xml
<Button
    android:id="@+id/nggone"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    ngAndroid:ngGone="input.gone"
    android:text="button"/>

<CheckBox
    android:id="@+id/nggonedb"
    ngAndroid:ngModel="input.gone"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content" />
```
