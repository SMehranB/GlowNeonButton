# GlowButton

## A cool glowing button with animated properties 
 
## Features!

•	Background Color, Glow Color, Glow Animation Duration

•	Corner Radius

•	Cool Ripple Effect, Ripple Color, Ripple Animation duration, Ripple Effect Enabled

•	Text Font, Text Style, Text Color

•	Animated Enable/Disable, Enable/Disable


## Screen recording
 
 <img src="./screen_recording.gif" height="720">
 
# Install
 
## Gradle
```
dependencies {
        implementation '-----------------------------'
}
```
## Maven
```
<dependency>
    <???????????????????>
</dependency>
 ```
# Use
 
## XML

GlowButton with custom params:
```
<com.smb.glowbutton.GlowButton
    android:id="@+id/btnSampleOne"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:layout_marginHorizontal="16dp"
    android:layout_marginTop="8dp"
    android:text="Sample one"
    android:textColor="@color/black"
    android:textSize="24dp"
    app:gb_rippleColor="@color/purple_500"
    app:gb_rippleAnimationDuration="1500"
    app:gb_glowAnimationDuration="500"
    app:gb_rippleEnabled="true"
    app:gb_backgroundColor="#FFE600"
    app:gb_cornerRadius="10dp"
    app:gb_glowColor="#FFE600" />
 ```
## Kotlin
```
val myGlowButton = GlowButton(this)
myGlowButton.apply {
    layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT)
    glowAnimationDuration = 500
    backColor = Color.MAGENTA
    glowColor = Color.YELLOW
    rippleColor = Color.WHITE
    setTextSize(16)
    setTextColor(Color.WHITE)
    text = "Am I Not Cool?!"
    textStyle = Typeface.BOLD_ITALIC
}

viewHolder.addView(myGlowButton)
```

