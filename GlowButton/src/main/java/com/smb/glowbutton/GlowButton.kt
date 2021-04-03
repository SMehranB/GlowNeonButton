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

    private val maskPath = Path()
    private var clickX : Float = width.div(2).toFloat()

    private var clickY : Float = height.div(2).toFloat()
    private var isClicked: Boolean = false

    private val backgroundPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var backgroundRectF = RectF()
    private var mCornerRadius : Float = 0f
    private var mBackgroundColor: Int = Color.GREEN
    var mShadowColor: Int = Color.GREEN
    private val backgroundPadding = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16f, resources.displayMetrics)
    private var shadowPadding = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16f, resources.displayMetrics)
    private val ripplePaint = Paint(Paint.ANTI_ALIAS_FLAG)

    private var mRippleColor: Int = 0
    private var mRippleRadius = 100f
    private var mRippleAlpha: Int = 0
    private lateinit var rippleGradient : RadialGradient

    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.SUBPIXEL_TEXT_FLAG)
    private var mTextAlpha: Int = 255
    private var mText: String = "Glow Button"
    private var mTextStyle = 0 //Normal Text (1 = Bold, 2 = Italic)
    private var mTextSize: Float
    private var mTextColor: Int = Color.BLACK
    private var mTextColorOriginal = mTextColor
    private var mTextX: Float = 0f
    private var mTextY: Float = 0f

    init {

        val attr : TypedArray = context.theme.obtainStyledAttributes(attrSet, R.styleable.GlowButton,0, 0)

        with(attr){
            mCornerRadius = getDimension(R.styleable.GlowButton_gb_cornerRadius, resources.getDimension(R.dimen.cornerRadius))
            mBackgroundColor = getInteger(R.styleable.GlowButton_gb_backgroundColor, Color.GREEN)

            mShadowColor = getInteger(R.styleable.GlowButton_gb_glowColor, Color.GREEN)
            mRippleColor = getInteger(R.styleable.GlowButton_gb_rippleColor, ColorUtils.blendARGB(mBackgroundColor, Color.BLACK, 0.5f))

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

        backgroundRectF.set(backgroundPadding, backgroundPadding, width.minus(backgroundPadding), height.minus(backgroundPadding))
        with(backgroundPaint) {
            color = mBackgroundColor
            setShadowLayer(shadowPadding.times(1.2f), 0f, 0f, mShadowColor)
        }

        canvas?.drawRoundRect(backgroundRectF, mCornerRadius, mCornerRadius, backgroundPaint)

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

        if(isClicked && isEnabled){

            maskPath.addRoundRect(backgroundRectF, mCornerRadius, mCornerRadius, Path.Direction.CW)
            canvas?.clipPath(maskPath)

            with(ripplePaint) {
                alpha = mRippleAlpha
                shader = rippleGradient
            }
            canvas?.drawCircle(clickX, clickY, mRippleRadius.times(3), ripplePaint)
        }
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

        isClicked = true

        val animDuration = 1000L

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

        val animationSet = AnimatorSet()
        with(animationSet) {
            interpolator = DecelerateInterpolator()
            duration = animDuration
            addListener(object : Animator.AnimatorListener{
                override fun onAnimationStart(p0: Animator?) {
                }
                override fun onAnimationEnd(p0: Animator?) {
                    isClicked = false
                    invalidate()
                }
                override fun onAnimationCancel(p0: Animator?) {
                }
                override fun onAnimationRepeat(p0: Animator?) {
                }
            })
            animationSet.play(ripple).with(rippleAlpha)
            animationSet.start()
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