package com.downloader_app

import android.animation.ValueAnimator
import android.content.Context
import android.content.res.Resources.Theme
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import androidx.core.animation.doOnEnd
import androidx.core.content.res.ResourcesCompat
import androidx.core.content.withStyledAttributes
import kotlinx.android.synthetic.main.content_main.view.*


class LoadingButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    lateinit var bitmap: Bitmap
    lateinit var customCanvas: Canvas
    var customWidth: Int = 0
    var customHeight: Int = 0
    var drawColor: Int = 0
    var durationForAnimation: Long = 0
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        customWidth = resolveSizeAndState(
            (paddingLeft + paddingRight + suggestedMinimumWidth),
            widthMeasureSpec,
            2
        )
        customHeight = resolveSizeAndState(
            MeasureSpec.getSize(
                resolveSizeAndState(
                    (paddingLeft + paddingRight + suggestedMinimumWidth),
                    widthMeasureSpec,
                    2
                )
            ), heightMeasureSpec, 2
        )
        setMeasuredDimension(customWidth, customHeight)
    }

    init {
        context.withStyledAttributes(attrs, R.styleable.LoadingButton) {
            drawColor = getColor(R.styleable.LoadingButton_CirclePaintColor, 2)
            durationForAnimation = getInteger(R.styleable.LoadingButton_DurationTime, 2).toLong()
        }

    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        val paint: Paint? = null
        canvas!!.drawBitmap(bitmap, 15f, 15f, paint)
    }


    fun animatorLoading(binding: View): ValueAnimator? {
        var effectAnimator = ValueAnimator.ofInt(
            binding.loading_button.layoutParams.width, custom_button.customWidth
        )
        effectAnimator.addUpdateListener { animation ->
            binding.loading_button.layoutParams.width = animation.animatedValue as Int
            binding.loading_button.requestLayout()
        }
        effectAnimator.duration = durationForAnimation
        effectAnimator.start()
        return effectAnimator
    }

    override fun onSizeChanged(width: Int, height: Int, oldWidth: Int, oldHeight: Int) {
        super.onSizeChanged(width, height, oldWidth, oldHeight)
        if (::bitmap.isInitialized) bitmap.recycle()
        bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        customCanvas = Canvas(bitmap)
    }

    fun animatorCircle() {
        val valueAnimatorCircle = ValueAnimator.ofFloat(15F, 360F)
        val paint = Paint()
        paint.color = drawColor
        paint.style = Paint.Style.FILL_AND_STROKE
        paint.strokeWidth = 45F
        valueAnimatorCircle.addUpdateListener { animation ->
            customCanvas.drawArc(
                (RectF(120f, 60f, 170f, 100f)),
                10f,
                animation.animatedValue as Float,
                false,
                paint
            )
        }

        valueAnimatorCircle.duration = durationForAnimation
        valueAnimatorCircle.start()
        valueAnimatorCircle.doOnEnd {
            val paint = Paint()
            val theme: Theme? = null
            paint.color = ResourcesCompat.getColor(resources, R.color.colorPrimary, theme)
            paint.style = Paint.Style.FILL_AND_STROKE
            paint.strokeWidth = 50F
            customCanvas.drawArc((RectF(120f, 60f, 170f, 100f)), 10f, 360F, false, paint)
        }
    }


}