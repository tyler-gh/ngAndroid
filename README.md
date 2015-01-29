# ngAndroid

ngAndroid brings some of the angular directives to android xml attributes.

Currently supported angular directives
```
ngModel
ngClick
```
Directives that are on the road map
```
ngDblclick
ngBlur
ngChange
ngDisabled
ngFocus
ngInvisible
ngGone
ngIf
ngRepeat
ngSrc
ngJsonSrc
ngSubmit
ngForm
```

--------

<h3>ngModel</h3>

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

<h3>ngClick</h3>

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
ngAndroid is still in extreme alpha stages. pre v 0.1
