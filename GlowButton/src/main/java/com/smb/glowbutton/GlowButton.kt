package com.smb.glowbutton

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.content.res.TypedArray
import android.graphics.*
import android.util.AttributeSet
import android.util.Log.d
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View
import android.view.animation.DecelerateInterpolator
import androidx.core.graphics.ColorUtils
import java.util.*

class GlowButton(context: Context, attrSet: AttributeSet?) : View(context, attrSet) {

    private val EXTRA_PADDING_WIDTH = 100
    private val EXTRA_PADDING_HEIGHT = 25

    //BACKGROUND PROPERTIES
    private val backgroundPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var backgroundRectF = RectF()
    private var backgroundPadding = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16f, resources.displayMetrics)
    private var glowPadding = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16f, resources.displayMetrics)
    var cornerRadius: Float = 0f
    var backColor: Int = Color.GREEN
    var glowColor: Int = Color.GREEN

    //RIPPLE EFFECT PROPERTIES
    private val ripplePaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private lateinit var rippleGradient : RadialGradient
    private var mRippleRadius = 100f
    private var mRippleAlpha: Int = 0
    var rippleColor: Int = 0
    var rippleEnabled = true

    //TEXT PROPERTIES
    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.SUBPIXEL_TEXT_FLAG)
    private var mTextAlpha: Int = 255
    private var mTextColorOriginal: Int = 0
    private var mTextX: Float = 0f
    private var mTextY: Float = 0f
    private var mTextSize: Float = 0f
    var textColor: Int = 0
    var textAllCaps: Boolean = true
    var text: String = "GLOW BUTTON"
    var textStyle = 0 //Normal Text (1 = Bold, 2 = Italic, 3 = bold_italic)

    private val maskPath = Path()

    private var touchX : Float = width.div(2).toFloat()
    private var touchY : Float = height.div(2).toFloat()
    private var clicked: Boolean = false

    private var rippleAnimatorSet: AnimatorSet? = null
    private var enableDisableAnimatorSet: AnimatorSet? = null

    var animationDuration: Long = 500L

    init {

        val attr : TypedArray = context.theme.obtainStyledAttributes(attrSet, R.styleable.GlowButton,0, 0)

        with(attr){
            cornerRadius = getDimension(R.styleable.GlowButton_gb_cornerRadius, resources.getDimension(R.dimen.cornerRadius))
            backColor = getInteger(R.styleable.GlowButton_gb_backgroundColor, Color.GREEN)

            glowColor = getInteger(R.styleable.GlowButton_gb_glowColor, Color.GREEN)

            rippleColor = getInteger(R.styleable.GlowButton_gb_rippleColor, ColorUtils.blendARGB(backColor, Color.BLACK, 0.5f))
            rippleEnabled = getBoolean(R.styleable.GlowButton_gb_rippleEnabled, true)

            textStyle = getInt(R.styleable.GlowButton_android_textStyle, Typeface.NORMAL)
            mTextSize = getDimension(R.styleable.GlowButton_android_textSize, resources.getDimension(R.dimen.text_size))
            textColor = getInteger(R.styleable.GlowButton_android_textColor, Color.BLACK)
            textAllCaps = getBoolean(R.styleable.GlowButton_android_textAllCaps, true)
            mTextColorOriginal = textColor

            val textString = getString(R.styleable.GlowButton_text)
            if (textString != null) {
                text = if(textAllCaps){
                    textString.toUpperCase(Locale.ROOT)
                }else{
                    textString
                }
            }

            recycle()
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {

        val minDimensions = getDesiredDimensions()

        val desiredWidth = minDimensions[0] + paddingLeft + paddingRight
        val desiredHeight = minDimensions[1]  + backgroundPadding.times(2) + paddingTop + paddingBottom

        setMeasuredDimension(getDimension(desiredWidth, widthMeasureSpec), getDimension(desiredHeight.toInt(), heightMeasureSpec))
    }

    private fun getDimension(desiredDimen: Int, measureSpec: Int): Int {

        val mode = MeasureSpec.getMode(measureSpec)
        val size = MeasureSpec.getSize(measureSpec)

        d("MMM", "onMeasure " +  MeasureSpec.toString(measureSpec) + " desired " + desiredDimen);

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

    private fun getDesiredDimensions() : Array<Int> {

        val textBound = Rect()
        textPaint.textSize = mTextSize
        textPaint.getTextBounds(text, 0, text.length, textBound)

        val width = textBound.width() + backgroundPadding.times(2) + EXTRA_PADDING_WIDTH.times(2)
        val height = textBound.height() + backgroundPadding.times(2) + EXTRA_PADDING_HEIGHT.times(2)

        return arrayOf(width.toInt(), height.toInt())
    }

    override fun onDraw(canvas: Canvas?) {

        backgroundRectF.set(backgroundPadding + paddingStart, backgroundPadding + paddingStart,
            width.minus(backgroundPadding).minus(paddingEnd), height.minus(backgroundPadding).minus(paddingBottom))
        with(backgroundPaint) {
            color = backColor
            setShadowLayer(glowPadding.times(1.3f), 0f, 0f, glowColor)
        }

        canvas?.drawRoundRect(backgroundRectF, cornerRadius, cornerRadius, backgroundPaint)

        with(textPaint) {
            color = textColor
            alpha = mTextAlpha
            isLinearText = false
            textAlign = Paint.Align.CENTER
            textSize = mTextSize
            val metrics = this.fontMetrics
            mTextX = width.div(2).toFloat()
            mTextY = height.div(2) - metrics.descent.plus(metrics.ascent).div(2)
            typeface = Typeface.create(Typeface.DEFAULT, textStyle)
        }

        canvas?.drawText(text, mTextX, mTextY, textPaint)

        if(clicked && isEnabled && rippleEnabled){
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
        canvas?.drawCircle(touchX, touchY, mRippleRadius.times(3), ripplePaint)
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

        animateRipple()

        return super.performClick()
    }

    private fun animateRipple() {

        rippleAnimatorSet?.cancel()

        clicked = true

        val animDuration = 1500L

        val ripple = ValueAnimator.ofFloat(1f, 300f)
        with(ripple){
            addUpdateListener {
                mRippleRadius = it.animatedValue as Float
                rippleGradient = RadialGradient(touchX, touchY, mRippleRadius, rippleColor, Color.TRANSPARENT, Shader.TileMode.MIRROR)
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
        mTextColorOriginal = textColor

        isEnabled = false

        mTextAlpha = 180
        textColor = Color.GRAY
        glowPadding = 0f

        invalidate()
    }

    fun enable(){
        isEnabled = true

        mTextAlpha = 255
        textColor = mTextColorOriginal
        glowPadding = backgroundPadding

        invalidate()
    }

    fun disableWithAnimation(){

        mTextColorOriginal = textColor

        isEnabled = false

        enableDisableAnimatorSet?.cancel()

        val textAlpha = ValueAnimator.ofInt(255, 180)
        textAlpha.addUpdateListener {
            mTextAlpha = it.animatedValue as Int
        }

        val textColor = ObjectAnimator.ofArgb(mTextColorOriginal, Color.GRAY)
        textColor.addUpdateListener {
            this.textColor = it.animatedValue as Int
        }

        val glow = ValueAnimator.ofFloat(glowPadding, 0f)
        glow.addUpdateListener {
            glowPadding = it.animatedValue as Float
            postInvalidateOnAnimation()
        }

        enableDisableAnimatorSet = AnimatorSet()
        with(enableDisableAnimatorSet!!){
            duration = animationDuration
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
            this.textColor = it.animatedValue as Int
        }

        val glow = ValueAnimator.ofFloat(0f, backgroundPadding)
        glow.addUpdateListener {
            glowPadding = it.animatedValue as Float
            postInvalidateOnAnimation()
        }
        enableDisableAnimatorSet = AnimatorSet()
        with(enableDisableAnimatorSet!!){
            duration = animationDuration
            playTogether(glow, textColor, textAlpha)
            start()
        }
    }

//    fun setTextColor(@ColorInt color: Int){
//        mTextColor = color
//        mTextColorOriginal = color
//    }

    fun setTextSize(textSize: Float){
        mTextSize = textSize
    }
}