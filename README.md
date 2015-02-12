# ngAndroid

ngAndroid brings some of the angular directives to android xml attributes.

Currently supported angular directives.

[NgModel](#ngmodel)<br>
[NgClick](#ngclick)<br>
[NgLongClick](#nglongclick)<br>
[NgChange](#ngchange)<br>
[NgDisabled](#ngdisabled)<br>
[NgInvisible](#nginvisible)<br>
[NgGone](#nggone)<br>

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
![Alt text](/../pictures/images/screencast.gif?raw=true "ngAndroid at work")
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

##ngClick

```xml
<Button
    android:id="@+id/stringClickEvent"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:layout_below="@+id/editText"
    android:layout_marginTop="20dp"
    ngAndroid:ngClick="stringClickEvent()"
    android:text="stringClickEvent()"/>
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
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:layout_below="@+id/really2"
    android:layout_marginTop="20dp"
    ngAndroid:ngClick="multiply(input.integer,2)"
    ngAndroid:ngLongClick="multiply(3,input.integer)"
    android:text="multiply(input.integer,2) onClick \n multiply(3,input.integer) onLongClick"/>

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
![NgDisabled Demonstration](/../pictures/images/ngdisable.gif?raw=true "ngdisabled demonstration")
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
![NgInvisible Demonstration](/../pictures/images/nginvisible.gif?raw=true "nginvisible demonstration")
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
![NgGone Demonstration](/../pictures/images/nggone.gif?raw=true "nggone demonstration")
