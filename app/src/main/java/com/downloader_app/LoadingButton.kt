package com.downloader_app

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.view.animation.DecelerateInterpolator
import androidx.core.animation.doOnEnd
import androidx.core.content.res.ResourcesCompat
import kotlinx.android.synthetic.main.content_main.view.*


class LoadingButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private lateinit var bitmap: Bitmap
    private lateinit var customCanvas: Canvas
    private var customWidth = 0
    private var customHeight = 0
    private val drawColor = ResourcesCompat.getColor(resources, R.color.colorPrimaryDark, null)

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas!!.drawBitmap(bitmap, 10f, 10f, null)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val minWidth: Int = paddingLeft + paddingRight + suggestedMinimumWidth
        val measuredWidth: Int = resolveSizeAndState(minWidth, widthMeasureSpec, 1)
        val measuredHeight: Int = resolveSizeAndState(
            MeasureSpec.getSize(measuredWidth), heightMeasureSpec, 1
        )
        customWidth = measuredWidth
        customHeight = measuredHeight
        setMeasuredDimension(measuredWidth, measuredHeight)
    }

    override fun onSizeChanged(width: Int, height: Int, oldWidth: Int, oldHeight: Int) {
        super.onSizeChanged(width, height, oldWidth, oldHeight)
        if (::bitmap.isInitialized) bitmap.recycle()
        bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        customCanvas = Canvas(bitmap)
    }

    fun animateLoadingLayout(binding: View): ValueAnimator? {
        val animator = ValueAnimator.ofInt(
            binding.loading_button.layoutParams.width, custom_button.customWidth
        )
        animator.interpolator = DecelerateInterpolator()
        animator.addUpdateListener { animation ->
            binding.loading_button.layoutParams.width = animation.animatedValue as Int
            binding.loading_button.requestLayout()
        }
        animator.duration = 2200
        animator.start()
        animator.doOnEnd {
            binding.loading_button.layoutParams.width = 0
            binding.loading_button.invalidate()
        }
        return animator
    }

    fun moveImageInCircle() {
        val animator = ValueAnimator.ofFloat(10F, 370F)
        val rectangle = RectF(120f, 60f, 170f, 100f)
        val paint = Paint()
        paint.isAntiAlias = true
        paint.color = drawColor
        paint.style = Paint.Style.FILL_AND_STROKE
        paint.strokeWidth = 45F
        animator.interpolator = DecelerateInterpolator()
        animator.addUpdateListener { animation ->
            customCanvas.drawArc(rectangle, 10f, animation.animatedValue as Float, false, paint)
        }

        animator.duration = 2200
        animator.start()
        animator.doOnEnd {
            reverseMoveImageInCircle()
        }
    }

    private fun reverseMoveImageInCircle() {

        val rectangle = RectF(120f, 60f, 170f, 100f)
        val paint = Paint()
        paint.isAntiAlias = true
        paint.color = ResourcesCompat.getColor(resources, R.color.colorPrimary, null)
        paint.style = Paint.Style.FILL_AND_STROKE
        paint.strokeWidth = 45F
        customCanvas.drawArc(rectangle, 10f, 360F, false, paint)

    }

}