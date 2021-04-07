package com.smb.glowbuttonsample

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.util.TypedValue
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.smb.glowbutton.GlowButton
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    var enabled = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnGoToJava.setOnClickListener {
//            startActivity(Intent(this, JavaSample::class.java))
        }

        val gb = GlowButton(this)
        gb.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT)
//        gb.glowAnimationDuration = 1500
//        gb.glowColor = Color.YELLOW
//        gb.rippleColor = Color.CYAN
//        gb.backColor = Color.MAGENTA
//        gb.setTextColor(Color.RED)
        gb.text = "Awesooooome"
        gb.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,16f, resources.displayMetrics))
//        gb.textStyle = Typeface.BOLD_ITALIC
//        gb.textFont = R.font.smile
        gb.cornerRadius =  TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,30f, resources.displayMetrics)

//        viewHolder.addView(gb)


        btnFormat.setOnClickListener {

            with(btnGoToJava){
                text = "New Text for glow"
                glowColor = Color.RED
                rippleColor = Color.GREEN
                backColor = Color.YELLOW
                textStyle = Typeface.NORMAL
                textFont = R.font.smile
                cornerRadius = 10f
                setTextColor(ContextCompat.getColor(this@MainActivity, R.color.purple_200))
                setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,30f, resources.displayMetrics))
                glowAnimationDuration = 2000
                rippleAnimationDuration = 5000
            }
        }

        btnEnableDisableAnimated.setOnClickListener {
            if(enabled){
                glowButton.disableWithAnimation()
                btnGoToJava.disableWithAnimation()
                gb.disableWithAnimation()
            }else{
                glowButton.enableWithAnimation()
                btnGoToJava.enableWithAnimation()
                gb.enableWithAnimation()
            }
            enabled = !enabled
        }

        btnEnableDisable.setOnClickListener {
            if(enabled){
                glowButton.disable()
                gb.disable()

            }else{
                glowButton.enable()
                gb.enable()
            }
            enabled = !enabled
        }

        glowButton.setOnClickListener {
//            Toast.makeText(this, "Glow Button Clicked!", Toast.LENGTH_SHORT).show()
        }
    }
}