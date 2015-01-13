# ngAndroid

ngAndroid brings some of the angular directives to android xml attributes.

Currently supported angular directives
```
ngModel
```

--------

<h3>ngModel</h3>

```java
// create model

public interface Input {
    public String getInput();
    public void setInput(String input);
    public String getTest();
    public void setTest(String input);
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


--------

ngAndroid is still in extreme alpha stages. pre v 0.1
