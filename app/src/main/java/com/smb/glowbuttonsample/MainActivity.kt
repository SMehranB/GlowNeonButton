package com.smb.glowbuttonsample

import android.graphics.Color
import android.os.Bundle
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.smb.glowbutton.GlowButton
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    var enabled = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val gb = GlowButton(this, null)
        gb.layoutParams = ViewGroup.LayoutParams(800, 300)
        gb.shadowColor = Color.MAGENTA
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
    }
}