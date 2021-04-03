package com.smb.glowbutton

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.content.res.TypedArray
import android.graphics.*
import android.util.AttributeSet
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View
import android.view.animation.DecelerateInterpolator
import androidx.core.graphics.ColorUtils

class GlowButton(context: Context, attrSet: AttributeSet?) : View(context, attrSet) {

    //BACKGROUND PROPERTIES
    private val backgroundPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var backgroundRectF = RectF()
    private var shadowPadding = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16f, resources.displayMetrics)
    var cornerRadius: Float = 0f
    var backColor: Int = Color.CYAN
    var shadowColor: Int = Color.CYAN
    var backgroundPadding = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16f, resources.displayMetrics)

    //RIPPLE EFFECT PROPERTIES
    private val ripplePaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var mRippleRadius = 100f
    private var mRippleAlpha: Int = 0
    private lateinit var rippleGradient : RadialGradient
    var mRippleColor: Int = 0

    //TEXT PROPERTIES
    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.SUBPIXEL_TEXT_FLAG)
    private var mTextAlpha: Int = 255
    private var mTextColorOriginal: Int = 0
    private var mTextX: Float = 0f
    private var mTextY: Float = 0f
    var mText: String = "Glow Button"
    var mTextStyle = 0 //Normal Text (1 = Bold, 2 = Italic)
    var mTextSize: Float
    var mTextColor: Int = Color.BLACK

    private val maskPath = Path()

    private var clickX : Float = width.div(2).toFloat()
    private var clickY : Float = height.div(2).toFloat()
    private var clicked: Boolean = false

    private var rippleAnimatorSet: AnimatorSet? = null


    init {

        val attr : TypedArray = context.theme.obtainStyledAttributes(attrSet, R.styleable.GlowButton,0, 0)

        with(attr){
            cornerRadius = getDimension(R.styleable.GlowButton_gb_cornerRadius, resources.getDimension(R.dimen.cornerRadius))
            backColor = getInteger(R.styleable.GlowButton_gb_backgroundColor, Color.GREEN)

            shadowColor = getInteger(R.styleable.GlowButton_gb_glowColor, Color.GREEN)
            mRippleColor = getInteger(R.styleable.GlowButton_gb_rippleColor, ColorUtils.blendARGB(backColor, Color.BLACK, 0.5f))

            mTextSize = getDimension(R.styleable.GlowButton_android_textSize, resources.getDimension(R.dimen.text_size))
            mTextStyle = getInt(R.styleable.GlowButton_android_textStyle, 0)
            mTextColor = getInteger(R.styleable.GlowButton_android_textColor, Color.BLACK)
            mTextColorOriginal = mTextColor
            val textString = getString(R.styleable.GlowButton_text)

            if (textString != null) {
                mText = textString
            }

            recycle()
        }
    }

    override fun onDraw(canvas: Canvas?) {

        backgroundRectF.set(backgroundPadding + paddingStart, backgroundPadding + paddingStart,
            width.minus(backgroundPadding).minus(paddingEnd), height.minus(backgroundPadding).minus(paddingBottom))
        with(backgroundPaint) {
            color = backColor
            setShadowLayer(shadowPadding.times(1.2f), 0f, 0f, shadowColor)
        }

        canvas?.drawRoundRect(backgroundRectF, cornerRadius, cornerRadius, backgroundPaint)

        with(textPaint) {
            color = mTextColor
            alpha = mTextAlpha
            textAlign = Paint.Align.CENTER
            textSize = mTextSize
            val metrics = this.fontMetrics
            mTextX = width.div(2).toFloat()
            mTextY = height.div(2) - metrics.descent.plus(metrics.ascent).div(2)
            typeface = Typeface.create(Typeface.DEFAULT, mTextStyle)
        }

        canvas?.drawText(mText, mTextX, mTextY, textPaint)

        if(clicked && isEnabled){
            drawRipples(canvas)
        }
    }

    private fun drawRipples(canvas: Canvas?){
        maskPath.addRoundRect(backgroundRectF, cornerRadius, cornerRadius, Path.Direction.CW)
        canvas?.clipPath(maskPath)

        with(ripplePaint) {
            alpha = mRippleAlpha
            shader = rippleGradient
        }
        canvas?.drawCircle(clickX, clickY, mRippleRadius.times(3), ripplePaint)

    }
    override fun onTouchEvent(event: MotionEvent?): Boolean {

        if(event != null && event.action == MotionEvent.ACTION_DOWN){
            clickX = event.x
            clickY = event.y
            performClick()
        }

        return super.onTouchEvent(event)
    }

    override fun performClick(): Boolean {

        animateRipple()

        return super.performClick()
    }

    private fun animateRipple() {

        if(rippleAnimatorSet != null){
            rippleAnimatorSet!!.cancel()
        }

        clicked = true

        val animDuration = 1500L

        val ripple = ValueAnimator.ofFloat(1f, 300f)
        with(ripple){
            addUpdateListener {
                mRippleRadius = it.animatedValue as Float
                rippleGradient = RadialGradient(clickX, clickY, mRippleRadius, mRippleColor, Color.TRANSPARENT, Shader.TileMode.MIRROR)
                postInvalidateOnAnimation()
            }
        }

        val rippleAlpha = ValueAnimator.ofInt(70, 0)
        with(rippleAlpha) {
            addUpdateListener {
                mRippleAlpha = it.animatedValue as Int
            }
        }

        rippleAnimatorSet = AnimatorSet()
        with(rippleAnimatorSet!!) {
            interpolator = DecelerateInterpolator()
            duration = animDuration
            addListener(object : Animator.AnimatorListener{
                override fun onAnimationStart(p0: Animator?) {
                }
                override fun onAnimationEnd(p0: Animator?) {
                    clicked = false
                    invalidate()
                }
                override fun onAnimationCancel(p0: Animator?) {
                }
                override fun onAnimationRepeat(p0: Animator?) {
                }
            })
            playTogether(ripple, rippleAlpha)
            start()
        }
    }

    fun disable(){
        isEnabled = false

        mTextAlpha = 180
        mTextColor = Color.GRAY
        shadowPadding = 0f

        invalidate()
    }

    fun enable(){
        isEnabled = true

        mTextAlpha = 255
        mTextColor = mTextColorOriginal
        shadowPadding = backgroundPadding

        invalidate()
    }

    fun disableWithAnimation(){
        isEnabled = false

        val textAlpha = ValueAnimator.ofInt(255, 180)
        textAlpha.addUpdateListener {
            mTextAlpha = it.animatedValue as Int
        }

        val textColor = ObjectAnimator.ofArgb(mTextColorOriginal, Color.GRAY)
        textColor.addUpdateListener {
            mTextColor = it.animatedValue as Int
        }

        val glow = ValueAnimator.ofFloat(backgroundPadding, 0f)
        glow.addUpdateListener {
            shadowPadding = it.animatedValue as Float
            postInvalidateOnAnimation()
        }

        val animatorSet = AnimatorSet()
        with(animatorSet){
            duration = 500L
            playTogether(glow, textColor, textAlpha)
            start()
        }
    }

    fun enableWithAnimation(){
        isEnabled = true

        val textAlpha = ValueAnimator.ofInt(180, 255)
        textAlpha.addUpdateListener {
            mTextAlpha = it.animatedValue as Int
        }

        val textColor = ObjectAnimator.ofArgb(Color.GRAY, mTextColorOriginal)
        textColor.addUpdateListener {
            mTextColor = it.animatedValue as Int
        }

        val glow = ValueAnimator.ofFloat(0f, backgroundPadding)
        glow.addUpdateListener {
            shadowPadding = it.animatedValue as Float
            postInvalidateOnAnimation()
        }
        val animatorSet = AnimatorSet()
        with(animatorSet){
            duration = 500L
            playTogether(glow, textColor, textAlpha)
            start()
        }
    }
}