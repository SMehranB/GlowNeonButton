package com.smb.glowbutton

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.content.res.TypedArray
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.animation.DecelerateInterpolator
import androidx.annotation.ColorInt
import androidx.annotation.FontRes
import androidx.annotation.StyleRes
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.ColorUtils

class GlowButton @JvmOverloads constructor(context: Context, attributeSet: AttributeSet? = null): View(context, attributeSet) {

    private val horizontalTextMargin: Float = dpToPixel(24)
    private val verticalTextMargin: Float = dpToPixel(16)

    private val maskPath = Path()

    private var touchX : Float = 0f
    private var touchY : Float = 0f
    private var clicked: Boolean = false

    private var rippleAnimatorSet: AnimatorSet? = null
    private var enableDisableAnimatorSet: AnimatorSet? = null

    //BACKGROUND PROPERTIES
    private val backgroundPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var backgroundRectF = RectF()
    private var backgroundPadding: Float = dpToPixel(16)
    private var glowRadius: Float = dpToPixel(16)
    var glowAnimationDuration: Long = 500L
    var cornerRadius: Float = 0f
        set(value) {
            field = value
            invalidate()
        }

    @ColorInt
    var backColor: Int = Color.GREEN
        set(value) {
            field = value
            invalidate()
        }

    @ColorInt
    var glowColor: Int = 0
        set(value) {
            field = value
            invalidate()
        }

    //RIPPLE EFFECT PROPERTIES
    private val ripplePaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private lateinit var rippleGradient: RadialGradient
    private var mRippleRadius: Float = 0f
    private var mRippleAlpha: Int = 0
    var rippleAnimationDuration: Long = 1500
    @ColorInt
    var rippleColor: Int = 0
    var rippleEnabled: Boolean = true

    //TEXT PROPERTIES
    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.SUBPIXEL_TEXT_FLAG)
    private var mTextAlpha: Int = 255
    private var mTextColorOriginal: Int = 0
    private var mTextX: Float = 0f
    private var mTextY: Float = 0f
    private var mTextSize: Float = 0f
    private var mTextColor: Int = 0

    var text: String = "GLOW BUTTON"
        set(value) {
            field = value
            requestLayout()
        }

    @StyleRes
    var textStyle: Int = Typeface.NORMAL
        set(value) {
            field = value
            requestLayout()
        }

    @FontRes
    var textFont: Int = 0
        set(value) {
            field = value
            requestLayout()
        }


    init {

        val attr : TypedArray = context.theme.obtainStyledAttributes(attributeSet, R.styleable.GlowButton, 0, 0)

        with(attr){

            //retrieving background attributes
            cornerRadius = getDimension(R.styleable.GlowButton_gb_cornerRadius, dpToPixel(100))
            backColor = getInteger(R.styleable.GlowButton_gb_backgroundColor, Color.GREEN)
            glowColor = getInteger(R.styleable.GlowButton_gb_glowColor, backColor)
            glowAnimationDuration = getInteger(R.styleable.GlowButton_gb_glowAnimationDuration, 500).toLong()

            //retrieving ripple effect attributes
            rippleColor = getInteger(R.styleable.GlowButton_gb_rippleColor, ColorUtils.blendARGB(backColor, Color.BLACK, 0.5f))
            rippleAnimationDuration = getInteger(R.styleable.GlowButton_gb_rippleAnimationDuration, 1500).toLong()
            rippleEnabled = getBoolean(R.styleable.GlowButton_gb_rippleEnabled, true)

            //retrieving text attributes
            textStyle = getInt(R.styleable.GlowButton_android_textStyle, Typeface.NORMAL)
            mTextSize = getDimension(R.styleable.GlowButton_android_textSize, resources.getDimension(R.dimen.text_size))
            mTextColor = getInteger(R.styleable.GlowButton_android_textColor, Color.BLACK)
            textFont = getResourceId(R.styleable.GlowButton_android_fontFamily, 0)

            mTextColorOriginal = mTextColor

            text = getString(R.styleable.GlowButton_android_text) ?: "GLOW BUTTON"

            recycle()
        }

    }

    override fun onDraw(canvas: Canvas?) {

        //Drawing background and glow
        backgroundRectF.set(backgroundPadding, backgroundPadding, width.minus(backgroundPadding), height.minus(backgroundPadding))

        with(backgroundPaint) {
            color = backColor
            setShadowLayer(glowRadius.times(1.3f), 0f, 0f, glowColor)
        }

        canvas?.drawRoundRect(backgroundRectF, cornerRadius, cornerRadius, backgroundPaint)

        //Drawing text
        with(textPaint) {
            typeface = Typeface.create(getTypeFace(), textStyle)
            color = mTextColor
            alpha = mTextAlpha
            isLinearText = false
            textAlign = Paint.Align.CENTER
            textSize = mTextSize
        }

        canvas?.drawText(text, mTextX, mTextY, textPaint)

        //Drawing ripple effect
        if(clicked && isEnabled && rippleEnabled){
            drawRipples(canvas)
        }
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {

        val metrics = textPaint.fontMetrics
        mTextX = width.div(2).toFloat() + paddingStart - paddingEnd
        mTextY = height.div(2) - metrics.descent.plus(metrics.ascent).div(2) + paddingTop - paddingBottom

        super.onLayout(changed, left, top, right, bottom)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {

        val minDimensions = getMinDimensions()

        val desiredWidth = minDimensions.width + paddingStart + paddingEnd
        val desiredHeight = minDimensions.height + paddingTop + paddingBottom

        setMeasuredDimension(getFinalDimension(desiredWidth, widthMeasureSpec),
            getFinalDimension(desiredHeight, heightMeasureSpec))
    }

    private fun getMinDimensions(): MinimumDimensions {

        val textBound = Rect()
        with(textPaint) {
            textSize = mTextSize
            typeface = getTypeFace()
            getTextBounds(text, 0, text.length, textBound)
        }

        val width = textBound.width() + backgroundPadding.times(2) +
                horizontalTextMargin.times(2) + paddingStart + paddingEnd
        val height = textBound.height() + backgroundPadding.times(2) +
                verticalTextMargin.times(2) + paddingTop + paddingBottom

        return MinimumDimensions(width.toInt(), height.toInt())
    }

    private fun getFinalDimension(desiredDimen: Int, measureSpec: Int): Int {

        val mode = MeasureSpec.getMode(measureSpec)
        val size = MeasureSpec.getSize(measureSpec)

        return when(mode){
            MeasureSpec.EXACTLY -> {
                size
            }
            MeasureSpec.AT_MOST -> {
                desiredDimen.coerceAtMost(size)
            }
            else -> {
                desiredDimen
            }
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {

        if(event?.action == MotionEvent.ACTION_DOWN){
            touchX = event.x
            touchY = event.y

            performClick()

            return true
        }
        return false
    }

    override fun performClick(): Boolean {

        if(isEnabled) {
            animateRipple()
        }

        return super.performClick()
    }

    fun disable(){

        isEnabled = false

        mTextAlpha = 180
        mTextColor = Color.GRAY
        glowRadius = 0f

        invalidate()
    }

    fun enable(){
        isEnabled = true

        mTextAlpha = 255
        mTextColor = mTextColorOriginal
        glowRadius = backgroundPadding

        invalidate()
    }

    fun disableWithAnimation(){

        isEnabled = false

        enableDisableAnimatorSet?.cancel()

        val textAlpha = ValueAnimator.ofInt(255, 180)
        textAlpha.addUpdateListener {
            mTextAlpha = it.animatedValue as Int
        }

        val textColor = ObjectAnimator.ofArgb(mTextColorOriginal, Color.GRAY)
        textColor.addUpdateListener {
            mTextColor = it.animatedValue as Int
        }

        val glow = ValueAnimator.ofFloat(glowRadius, 0f)
        glow.addUpdateListener {
            glowRadius = it.animatedValue as Float
            postInvalidateOnAnimation()
        }

        enableDisableAnimatorSet = AnimatorSet()
        with(enableDisableAnimatorSet!!){
            duration = glowAnimationDuration
            playTogether(glow, textColor, textAlpha)
            start()
        }
    }

    fun enableWithAnimation(){
        isEnabled = true

        enableDisableAnimatorSet?.cancel()

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
            glowRadius = it.animatedValue as Float
            postInvalidateOnAnimation()
        }
        enableDisableAnimatorSet = AnimatorSet()
        with(enableDisableAnimatorSet!!){
            duration = glowAnimationDuration
            playTogether(glow, textColor, textAlpha)
            start()
        }
    }

    fun setTextSize(textSize: Float){
        mTextSize = textSize
        requestLayout()
    }

    @JvmName("setTextColor_gb")
    fun setTextColor(@ColorInt color: Int){
        mTextColor = color
        mTextColorOriginal = color
        invalidate()
    }

    private fun animateRipple() {

        rippleAnimatorSet?.cancel()

        clicked = true

        val animDuration = rippleAnimationDuration

        val ripple = ValueAnimator.ofFloat(1f, 300f)
        with(ripple){
            addUpdateListener {
                mRippleRadius = it.animatedValue as Float
                rippleGradient = RadialGradient(touchX, touchY, mRippleRadius, rippleColor, Color.TRANSPARENT, Shader.TileMode.MIRROR)
                postInvalidateOnAnimation()
            }
        }

        val rippleAlpha = ValueAnimator.ofInt(70, 0)
        rippleAlpha.addUpdateListener {
            mRippleAlpha = it.animatedValue as Int
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

    private fun drawRipples(canvas: Canvas?){
        maskPath.addRoundRect(backgroundRectF, cornerRadius, cornerRadius, Path.Direction.CW)
        canvas?.clipPath(maskPath)

        with(ripplePaint) {
            alpha = mRippleAlpha
            shader = rippleGradient
        }
        canvas?.drawCircle(touchX, touchY, mRippleRadius.times(3), ripplePaint)
    }

    private fun dpToPixel(dp: Int): Float {
        return dp.times(resources.displayMetrics.density)
    }

    private fun getTypeFace(): Typeface {
        var tf = Typeface.DEFAULT
        if(textFont != 0){
            tf = ResourcesCompat.getFont(context, textFont)
        }

        return tf
    }

    private data class MinimumDimensions(val width: Int, val height: Int)
}