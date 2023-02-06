package com.udacity

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.animation.ValueAnimator.INFINITE
import android.content.Context
import android.graphics.*
import android.provider.CalendarContract.Colors
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.animation.LinearInterpolator
import androidx.core.content.withStyledAttributes
import kotlin.math.min
import kotlin.properties.Delegates

class LoadingButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var widthSize = 0
    private var heightSize = 0
    var progress = 0
    private val valueAnimator = ValueAnimator.ofInt(0, 1000).apply {
        duration = 1000
        interpolator = LinearInterpolator()
        repeatMode = ObjectAnimator.RESTART
        addUpdateListener { valueAnimator ->
            progress = valueAnimator.animatedValue as Int
            currentSweepAngle = valueAnimator.animatedValue as Int
            invalidate()
        }
        addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator?) {
                super.onAnimationEnd(animation)
                if (buttonState == ButtonState.Loading)
                    start()
            }
        })
    }
    private var radius = 0.0f                   // Radius of the circle.
    private var btnRadius = 0.0f
    private var btnBackground = 0
    private var loaderColor = 0
    private var textColor = 0
    private var arcColor = 0
    private var text = ""

    // position variable which will be used to draw label and indicator circle position
    private val pointPosition: PointF = PointF(0.0f, 0.0f)


    private val paint = Paint().apply {
        style = Paint.Style.FILL
        textAlign = Paint.Align.CENTER
        textSize = 55.0f
    }
    var currentSweepAngle = 0


    private var buttonState: ButtonState by Delegates.observable<ButtonState>(ButtonState.Completed) { p, old, new ->
        when (new) {
            ButtonState.Loading -> {
                text = context.getString(R.string.button_loading)
                valueAnimator.start()
                isClickable = false
            }
            ButtonState.Clicked -> {
                isClickable = false
                text = context.getString(R.string.button_loading)
            }
            ButtonState.Completed -> {
                isClickable = true
                text = context.getString(R.string.button_download)
            }

        }
    }

    init {
        isClickable = true
        context.withStyledAttributes(attrs, R.styleable.LoadingButton) {
            text = context.getString(R.string.button_download)
            loaderColor = getColor(
                R.styleable.LoadingButton_btnBackground,
                context.getColor(R.color.colorPrimaryDark)
            )
            btnRadius = getFloat(R.styleable.LoadingButton_btnRadius, 0f)
            btnBackground = getColor(
                R.styleable.LoadingButton_btnBackground,
                context.getColor(R.color.colorPrimary)
            )
            textColor =
                getColor(R.styleable.LoadingButton_textColor, context.getColor(R.color.white))
            arcColor = context.getColor(R.color.colorAccent)
        }

    }


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        drawMainBtn(canvas)

        when (buttonState) {
            ButtonState.Loading -> {
                drawLoadingArc(canvas)
            }
        }

        drawText(canvas)

    }

    private fun drawText(canvas: Canvas?) {
        paint.color = textColor
        canvas!!.drawText(
            text,
            widthSize.toFloat() / 2,
            (heightSize.toFloat() * 0.6).toFloat(),
            paint
        )
    }

    private fun drawLoadingArc(canvas: Canvas?) {
        val progressRect = progress / 1000f * widthSize
        var loadRec = RectF(
            0f,
            0f, progressRect, heightSize.toFloat()
        )
        paint.color = loaderColor
        canvas!!.drawRoundRect(
            loadRec,
            btnRadius,
            btnRadius,
            paint
        )

        paint.color = arcColor
        val arcAngle = currentSweepAngle - 60f
        val arc = RectF(
            (widthSize * 0.85).toFloat(),
            heightSize * 0.2f,
            widthSize.toFloat() * 0.95f,
            heightSize.toFloat() * 0.8f
        )
        canvas.drawArc(
            arc,
            0f,
            arcAngle,
            true,
            paint
        )
    }

    fun changeBtnState(newButtonState: ButtonState) {
        buttonState = newButtonState
    }

    private fun drawMainBtn(canvas: Canvas?) {
        paint.color = btnBackground
        canvas!!.drawRoundRect(
            RectF(0f, 0f, widthSize.toFloat(), heightSize.toFloat()),
            btnRadius,
            btnRadius,
            paint
        )
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val minw: Int = paddingLeft + paddingRight + suggestedMinimumWidth
        val w: Int = resolveSizeAndState(minw, widthMeasureSpec, 1)
        val h: Int = resolveSizeAndState(
            MeasureSpec.getSize(w),
            heightMeasureSpec,
            0
        )
        widthSize = w
        heightSize = h
        setMeasuredDimension(w, h)
    }


    override fun onSizeChanged(width: Int, height: Int, oldWidth: Int, oldHeight: Int) {
        invalidate()
    }


}