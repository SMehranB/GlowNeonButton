package com.smb.glowbuttonsample

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.smb.glowbutton.GlowButton
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    var enabled = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val existingGlowButtons = arrayListOf<GlowButton>(btnSampleOne, btnSampleTwo, btnSampleThree,btnSampleFour)

        btnAddNewGlowButton.setOnClickListener {

            val myGlowButton = GlowButton(this)
            val params = ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT)
            params.setMargins(16, 8, 16, 0)
            myGlowButton.apply {
                layoutParams = params
                setCornerRadius(5)
                glowAnimationDuration = 500
                rippleAnimationDuration = 1500
                backColor = Color.WHITE
                glowColor = Color.WHITE
                rippleColor = Color.GRAY
                setTextSize(16)
                setTextColor(Color.BLACK)
                text = "Am I Not Cool?!"
                disabledTextColor = Color.DKGRAY
                setDrawableLeft(R.drawable.baseline_face_24)
                drawableTint = Color.BLACK
                setDrawablePadding(16)
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