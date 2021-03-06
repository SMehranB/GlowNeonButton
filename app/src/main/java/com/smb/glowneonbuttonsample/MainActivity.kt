package com.smb.glowneonbuttonsample

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.smb.glowbutton.GlowButton
import com.smb.glowbutton.NeonButton
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    var enabled = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val existingGlowButtons = arrayListOf<GlowButton>(btnSampleOne, btnSampleTwo, btnSampleThree,btnSampleFour)
        val existingNeonButtons = arrayListOf<NeonButton>(btnNeonOne, btnNeonTwo, btnNeonThree)

        btnAddNewGlowButton.setOnClickListener {

            val params = ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.MATCH_PARENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT
            )
            params.setMargins(16, 8, 16, 0)

            val myNeonButton = NeonButton(this)
            myNeonButton.apply {
                layoutParams = params
                setCornerRadius(16)
                enableAnimationDuration = 500L
                text = "What a Button!"
                setTextSize(16)
                disabledStateColor = Color.GRAY
                drawableTint = Color.YELLOW
                setDrawableStart(R.drawable.baseline_face_24)
                setDrawableEnd(R.drawable.baseline_face_24)
                setDrawablePadding(24)
                textStyle = Typeface.ITALIC
                gradientStart = Color.MAGENTA
                gradientEnd = Color.MAGENTA
            }

            val myGlowButton = GlowButton(this)
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
                setDrawableStart(R.drawable.baseline_face_24)
                drawableTint = Color.BLACK
                setDrawablePadding(16)
                textStyle = Typeface.BOLD_ITALIC
            }

            viewHolder.addView(myNeonButton)
            viewHolder.addView(myGlowButton)
            existingNeonButtons.add(myNeonButton)
            existingGlowButtons.add(myGlowButton)
        }

        btnEnableDisableAnimated.setOnClickListener {
            if(enabled){
                existingGlowButtons.forEach {
                    it.disableWithAnimation()
                }
                existingNeonButtons.forEach {
                    it.disableWithAnimation()
                }
            }else{
                existingGlowButtons.forEach {
                    it.enableWithAnimation()
                }
                existingNeonButtons.forEach {
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
                existingNeonButtons.forEach {
                    it.disable()
                }
            }else{
                existingGlowButtons.forEach {
                    it.enable()
                }
                existingNeonButtons.forEach {
                    it.enable()
                }
            }
            enabled = !enabled
        }
    }
}