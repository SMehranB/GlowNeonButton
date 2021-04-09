# GlowButton

## A cool glowing button with animated properties 
 
# What's new
 In version v1.1.0
 •  You can set drawableStart and drawableEnd
 •  You can adjust drawablePadding
 •  You can set drawableTing
 •  You can set disabledTextColor
 •  More Glow!
 
## Features!

 •	 Background Color, Glow Color, Glow Animation Duration
 •	 Corner Radius
 •	 Cool Ripple Effect, Ripple Color, Ripple Animation duration, Ripple Effect Enabled
 •	 Text Font, Text Style, Text Color, disabledTextColor
 •   Drawable Start and End, Drawable Tint, Drawable Padding
 •	 Animated Enable/Disable, Enable/Disable

## Screen recording
 
 <img src="./screen_recording.gif" height="720">
 
# Install
 
## Gradle
```
dependencies {
	 implementation 'com.github.SMehranB:GlowButton:v1.1.0'
}
```
## Maven
```
<dependency>
	<groupId>com.github.SMehranB</groupId>
	<artifactId>GlowButton</artifactId>
	<version>v1.1.0</version>
</dependency>
 ```
# Use
 
## XML

GlowButton with custom params:
```xml
<com.smb.glowbutton.GlowButton
    android:id="@+id/myGlowButton"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:layout_marginHorizontal="16dp"
    android:layout_marginTop="8dp"
    android:drawableStart="@drawable/outline_thumb_up_24"
    android:drawableEnd="@drawable/outline_thumb_down_off_alt_20"
    android:drawablePadding="24dp"
    android:drawableTint="@color/black"
    android:text="I am a Glow Button"
    android:textColor="@color/black"
    android:textSize="16dp"
    app:disabledTextColor="#808080"
    app:gb_backgroundColor="#FFE600"
    app:gb_glowAnimationDuration="500"
    app:gb_glowColor="#FFE600"
    app:gb_cornerRadius="15dp"
    app:gb_rippleAnimationDuration="1500"
    app:gb_rippleColor="@color/purple_500"
    app:gb_rippleEnabled="true" />

<!--Some info on the custom attributes
    android:textSize="24dp" //Default size is 16dp (because I don't like sp. Don't judge me!)
    app:gb_rippleColor="@color/purple_500" //Default ripple color is a darker shade of the background color
    app:gb_rippleEnabled="true" //Default value is true (because who doesn't like ripples?!)
    app:gb_cornerRadius="10dp" //By default, the button has completely round corners 
    app:gb_glowColor="#FFE600" /> //Default value is the same color as the background (because...logic!)
    android:drawablePadding="24dp" //Default value is 8dp
End-->
 ```

## Kotlin
```kotlin
val myGlowButton = GlowButton(this)
val params = ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT,
    ConstraintLayout.LayoutParams.WRAP_CONTENT)
params.setMargins(16, 8, 16, 0)
myGlowButton.apply {
    layoutParams = params
    setCornerRadius(5)
    glowAnimationDuration = 500 //Increase at your own risk. Long animations are annoying. but whatever...I can't tell you what to do...
    rippleAnimationDuration = 1500 //Relax! It's milliseconds, not hours!
    backColor = Color.WHITE
    glowColor = Color.WHITE
    rippleColor = Color.GRAY //I wouldn't change the ripple color if I were you. But go crazy if you have to.
    setTextSize(16) //Enter desired size in dp (or sp, whatever!)
    setTextColor(Color.BLACK)
    disabledTextColor = Color.DKGRAY
    setDrawableLeft(R.drawable.baseline_face_24)
    drawableTint = Color.BLACK
    setDrawablePadding(16)
    text = "Am I Not Cool?!" //There is no `AllCaps` attribute, so do it yourself. Don't be lazy!
    textStyle = Typeface.BOLD_ITALIC
}

viewHolder.addView(myGlowButton)
```

## Functions
```kotlin
myGlowButton.enableWithAnimation()
myGlowButton.disableWithAnimation()
myGlowButton.enable()
myGlowButton.disable()
```

## 📄 License
```text
MIT License

Copyright (c) 2021 Seyed Mehran Behbahani

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```