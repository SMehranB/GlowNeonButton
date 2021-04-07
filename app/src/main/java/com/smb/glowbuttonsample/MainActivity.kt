package com.smb.glowbuttonsample

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.util.TypedValue
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.smb.glowbutton.GlowButton
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    var enabled = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val existingGlowButtons = arrayListOf<GlowButton>(btnSampleOne, btnSampleTwo, btnSampleThree)

        btnAddNewGlowButton.setOnClickListener {

            val myGlowButton = GlowButton(this)
            myGlowButton.apply {
                layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT)
                glowAnimationDuration = 500
                backColor = Color.MAGENTA
                glowColor = Color.YELLOW
                rippleColor = Color.WHITE
                setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,16f, resources.displayMetrics))
                setTextColor(Color.WHITE)
                text = "Am I Not Cool?!"
                textStyle = Typeface.BOLD_ITALIC
            }

            viewHolder.addView(myGlowButton)
            existingGlowButtons.add(myGlowButton)
        }

        btnEnableDisableAnimated.setOnClickListener {
            if(enabled){
                existingGlowButtons.forEach {
                    it.disableWithAnimation()
                }
            }else{
                existingGlowButtons.forEach {
                    it.enableWithAnimation()
                }
            }
            enabled = !enabled
        }

        btnEnableDisable.setOnClickListener {
            if(enabled){
                existingGlowButtons.forEach {
                    it.disable()
                }
            }else{
                existingGlowButtons.forEach {
                    it.enable()
                }
            }
            enabled = !enabled
        }
    }
}