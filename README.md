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
	 implementation 'com.github.SMehranB:GlowButton:v1.0.0'
}
```
## Maven
```
<dependency>
	<groupId>com.github.SMehranB</groupId>
	<artifactId>GlowButton</artifactId>
	<version>v1.0.0</version>
</dependency>
 ```
# Use
 
## XML

GlowButton with custom params:
```xml
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

<!--Some info on the custom attributes
    android:textSize="24dp" //Default size is 16dp (because I don't like sp. Don't judge me!)
    app:gb_rippleColor="@color/purple_500" //Default ripple color is a darker shade of the background color (you think you are good at UI design? Change it!)
    app:gb_rippleAnimationDuration="1500"
    app:gb_glowAnimationDuration="500"
    app:gb_rippleEnabled="true" //Default value is true (because who doesn't like ripples?!)
    app:gb_backgroundColor="#FFE600" 
    app:gb_cornerRadius="10dp" // By default, the button has completely round corners 
    app:gb_glowColor="#FFE600" /> //Default value is the same color as the background (because...logic!)
End-->
 ```
## Kotlin
```kotlin
val myGlowButton = GlowButton(this)
myGlowButton.apply {
    layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.WRAP_CONTENT)
    glowAnimationDuration = 500 //Increase at your own risk. Long animations are annoying. but whatever...I can't tell you what to do...
    rippleAnimationDuration = 1500 //Relax! It's milliseconds, not hours!
    backColor = Color.MAGENTA
    glowColor = Color.MAGENTA
    rippleColor = Color.WHITE //I wouldn't change the ripple color if I were you. But go crazy if you have to.
    setTextSize(16) //Enter desired size in dp (or sp, whatever!)
    setTextColor(Color.WHITE)
    text = "Am I Not Cool?!" //There is no `AllCaps` attribute, so do it yourself. Don't be lazy!
    textStyle = Typeface.BOLD_ITALIC
}

viewHolder.addView(myGlowButton)
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