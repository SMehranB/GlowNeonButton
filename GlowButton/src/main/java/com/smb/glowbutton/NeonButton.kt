package com.smb.glowbutton

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.TypedArray
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.annotation.ColorInt
import androidx.annotation.FontRes
import androidx.annotation.StyleRes
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.toBitmap

class NeonButton : View {
    constructor(context: Context): super(context){
        initializeAttributes(context, null)
    }
    constructor(context: Context, attributeSet: AttributeSet): super(context, attributeSet){
        initializeAttributes(context, attributeSet)
    }


    private var textXOffSet: Float = 0f
    private val horizontalTextMargin: Float = dpToPixel(24)
    private val verticalTextMargin: Float = dpToPixel(16)

    private lateinit var backgroundGradient: LinearGradient
    private lateinit var strokeGradient: LinearGradient
    @ColorInt
    var gradientStart: Int = 0
    set(value) {
        field = value
        invalidate()
    }
    @ColorInt
    var gradientFinish: Int = 0
    set(value) {
        field = value
        invalidate()
    }

    private val backgroundPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    private val strokePaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val strokeRectF: RectF = RectF()
    private var strokePadding = dpToPixel(16)
    private var cornerRadius: Float = 0f
    @ColorInt
    var backColor: Int = Color.GREEN
        set(value) {
            field = value
            invalidate()
        }


    //DRAWABLE PROPERTIES
    private val drawablePaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private lateinit var drawableLeftBitmap: Bitmap
    private lateinit var drawableRightBitmap: Bitmap
    private var drawablePadding: Float = dpToPixel(8)
    private var drawableStartX: Float = 0f
    private var drawableEndX: Float = 0f
    private var drawableY: Float = 0f
    private var drawableDimension = dpToPixel(25).toInt()
    private var drawableStart: Int = 0
    private var drawableEnd: Int = 0
    @ColorInt
    var drawableTint: Int = 0
        set(value) {
            field = value
            invalidate()
        }

    //TEXT PROPERTIES
    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.SUBPIXEL_TEXT_FLAG)
    private var mTextAlpha: Int = 255
    private var mTextColorOriginal: Int = 0
    private var mTextX: Float = 0f
    private var mTextY: Float = 0f
    private var mTextSize: Float = 0f
    private var mTextColorCurrent: Int = 0
    var disabledTextColor = Color.GRAY

    var text: String = "NEON BUTTON"
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



    override fun onDraw(canvas: Canvas?) {

        setLayerType(LAYER_TYPE_SOFTWARE, textPaint)

        strokeRectF.set(strokePadding, strokePadding, width.minus(strokePadding), height.minus(strokePadding))

        with(backgroundPaint){
            style = Paint.Style.FILL
            shader = backgroundGradient
        }

        canvas?.drawRoundRect(strokeRectF, dpToPixel(50), dpToPixel(50), backgroundPaint)

        with(strokePaint) {
            style = Paint.Style.STROKE
            strokeWidth = 10f
            color = Color.YELLOW
            shader = strokeGradient
            setShadowLayer(dpToPixel(16), 0f, 0f, Color.YELLOW)
        }

        canvas?.drawRoundRect(strokeRectF, dpToPixel(50), dpToPixel(50), strokePaint)

        if(drawableEnd != 0) {
//            drawablePaint.setShadowLayer(dpToPixel(10), 0f, 0f, gradientFinish)
//            drawablePaint.shader = strokeGradient
            canvas?.drawBitmap(drawableRightBitmap, drawableEndX, drawableY, drawablePaint)
        }

        if(drawableStart != 0) {
//            drawablePaint.setShadowLayer(dpToPixel(10), 0f, 0f, gradientStart)
//            drawablePaint.shader = strokeGradient
            canvas?.drawBitmap(drawableLeftBitmap, drawableStartX, drawableY, drawablePaint)
        }

        //Drawing text
        with(textPaint) {
            setShadowLayer(dpToPixel(5), 0f, 0f, mTextColorCurrent)
            typeface = Typeface.create(getTypeFace(), textStyle)
            color = mTextColorCurrent
            alpha = mTextAlpha
            isLinearText = false
            textAlign = Paint.Align.CENTER
            textSize = mTextSize
            shader = strokeGradient
        }

        canvas?.drawText(text, mTextX, mTextY, textPaint)

    }

    @SuppressLint("DrawAllocation")
    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {

        backgroundGradient = LinearGradient(
            0f, height.times(2).toFloat(), dpToPixel(8),
            height.minus(dpToPixel(60)), gradientStart, Color.TRANSPARENT, Shader.TileMode.CLAMP
        )
        strokeGradient = LinearGradient(0f, 0f, width.toFloat(), 0f, gradientStart, gradientFinish, Shader.TileMode.CLAMP)


        if(drawableStart != 0) {
            val drawable = ContextCompat.getDrawable(context, drawableStart)!!
            if (drawableTint != 0) {
                drawable.setTint(drawableTint)
            }
            drawableLeftBitmap = drawable.toBitmap(drawableDimension, drawableDimension, Bitmap.Config.ARGB_8888)
            drawableStartX = paddingStart + strokePadding + drawablePadding
        }

        if(drawableEnd != 0) {
            val drawable = ContextCompat.getDrawable(context, drawableEnd)!!
            if (drawableTint != 0) {
                drawable.setTint(drawableTint)
            }
            drawableRightBitmap = drawable.toBitmap(drawableDimension, drawableDimension, Bitmap.Config.ARGB_8888)
            drawableEndX = width - paddingEnd - strokePadding - drawablePadding - drawableDimension
        }

        drawableY = height.div(2f).minus(drawableDimension.div(2))

        val metrics = textPaint.fontMetrics
        mTextX = width.div(2).toFloat() + paddingStart - paddingEnd + textXOffSet
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

        val width = textBound.width() + strokePadding.times(2) +
                horizontalTextMargin.times(2) + paddingStart + paddingEnd + getDrawableMeasurements() + textXOffSet
        val height = textBound.height() + strokePadding.times(2) +
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

    private fun getDrawableMeasurements(): Float {

        var width = 0f
        textXOffSet = 0f

        if(drawableStart != 0 ){
            width = width.plus(drawableDimension).plus(drawablePadding)
            textXOffSet = textXOffSet.plus(drawablePadding)
        }

        if(drawableEnd != 0){
            width = width.plus(drawableDimension).plus(drawablePadding)
            textXOffSet = textXOffSet.minus(drawablePadding)
        }

        return width
    }

    private fun initializeAttributes(context: Context, attributeSet: AttributeSet?){

        val attr : TypedArray = context.theme.obtainStyledAttributes(attributeSet, R.styleable.NeonButton, 0, 0)

        with(attr){

            //retrieving background attributes
            cornerRadius = getDimension(R.styleable.NeonButton_nb_cornerRadius, dpToPixel(100))
            gradientStart = getInteger(R.styleable.NeonButton_nb_gradientStart, Color.YELLOW)
            gradientFinish = getInteger(R.styleable.NeonButton_nb_gradientFinish, Color.RED)

            //retrieving drawable attributes
            drawableStart = getResourceId(R.styleable.NeonButton_nb_drawableStart, 0)
            drawableEnd = getResourceId(R.styleable.NeonButton_nb_drawableEnd, 0)
            drawablePadding = getDimension(R.styleable.NeonButton_nb_drawablePadding, dpToPixel(8))
            drawableTint = getInteger(R.styleable.NeonButton_nb_drawableTint, 0)

            //retrieving text attributes
            textStyle = getInt(R.styleable.NeonButton_nb_textStyle, Typeface.NORMAL)
            mTextSize = getDimension(R.styleable.NeonButton_nb_textSize, resources.getDimension(R.dimen.text_size))
            mTextColorCurrent = getInteger(R.styleable.NeonButton_nb_textColor, Color.YELLOW)
            textFont = getResourceId(R.styleable.NeonButton_nb_fontFamily, 0)
            disabledTextColor = getInteger(R.styleable.NeonButton_nb_disabledTextColor, Color.LTGRAY)
            mTextColorOriginal = mTextColorCurrent

            text = getString(R.styleable.NeonButton_nb_text) ?: "NEON BUTTON"

            recycle()
        }
    }

/*
    fun disable(){

        isEnabled = false

        mTextAlpha = 180
        mTextColorCurrent = disabledTextColor
        glowRadius = 0f

        invalidate()
    }

    fun enable(){
        isEnabled = true

        mTextAlpha = 255
        mTextColorCurrent = mTextColorOriginal
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

        val textColor = ObjectAnimator.ofArgb(mTextColorOriginal, disabledTextColor)
        textColor.addUpdateListener {
            mTextColorCurrent = it.animatedValue as Int
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

        val textColor = ObjectAnimator.ofArgb(disabledTextColor, mTextColorOriginal)
        textColor.addUpdateListener {
            mTextColorCurrent = it.animatedValue as Int
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
*/

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