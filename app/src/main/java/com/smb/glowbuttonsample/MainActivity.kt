package com.smb.glowbuttonsample

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.smb.glowbutton.GlowButton
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    var enabled = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val gb = GlowButton(this, null)
//        gb.layoutParams = ViewGroup.LayoutParams(800, 300)
//        gb.animationDuration = 1500
//        gb.glowColor = Color.YELLOW
//        gb.rippleColor = Color.CYAN
        gb.textColor = Color.CYAN
//        gb.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,24f, resources.displayMetrics))
//        gb.backColor = Color.YELLOW
//        gb.textStyle = Typeface.BOLD_ITALIC
//        gb.cornerRadius =  TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,30f, resources.displayMetrics)

        viewHolder.addView(gb)


        btnEnableDisableAnimated.setOnClickListener {
            if(enabled){
                glowButton.disableWithAnimation()
                gb.disableWithAnimation()
            }else{
                glowButton.enableWithAnimation()
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