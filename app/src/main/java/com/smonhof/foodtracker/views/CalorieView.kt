package com.smonhof.foodtracker.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Paint.ANTI_ALIAS_FLAG
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import com.smonhof.foodtracker.R
import com.smonhof.foodtracker.data.NutritionalValues

class CalorieView : View {

    private var _values: NutritionalValues

    private val calorieBackgroundPaint = Paint(ANTI_ALIAS_FLAG).apply{
        color =resources.getColor(R.color.calorieBackground)
        style = Paint.Style.FILL
    }
    private val calorieForegroundPaint = Paint(ANTI_ALIAS_FLAG).apply {
        color = resources.getColor(R.color.calorieForeground)
        style = Paint.Style.FILL
    }

    private val nutritionBackgroundPaint = Paint(ANTI_ALIAS_FLAG).apply {
        color = resources.getColor(R.color.nutritionBackground)
        style = Paint.Style.FILL
    }

    private val nutritionForegroundPaint = Paint(ANTI_ALIAS_FLAG).apply {
        color = resources.getColor(R.color.nutritionForeground)
        style = Paint.Style.FILL
    }
    private val textPaint = Paint(ANTI_ALIAS_FLAG).apply {
        color = resources.getColor(R.color.black)
        textSize = 42f
        textAlign = Paint.Align.CENTER
    }

    constructor(context: Context, attrs: AttributeSet) : super(context,attrs){
        _values = NutritionalValues.empty
    }
    constructor(context: Context, attrs: AttributeSet, values: NutritionalValues) : super(context,attrs){
        _values = values
    }
    fun updateValue(values: NutritionalValues){
        _values = values
        postInvalidate()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if(canvas == null){
            return
        }
        val useWidthReference =height * 4f / 3f > width
        val actualHeight : Float = if (useWidthReference) width* 3f / 4f else height.toFloat()
        val actualWidth : Float = if (useWidthReference) width.toFloat() else height * 4f / 3f
        val leftOffset : Float = (width-actualWidth) /2f

        val recommended = NutritionalValues.recommended

        canvas.apply {
            drawCircleFill(this,
                leftOffset + actualWidth * 0.375f,
                actualHeight * 0.5f,
                actualHeight * 0.45f,
                _values.Calories / recommended.Calories,
                calorieBackgroundPaint,
                calorieForegroundPaint)
            drawCircleFill(this,
                leftOffset + actualWidth * 0.875f,
                actualHeight * 0.1666f,
                actualHeight * 0.4f *.3333f,
                _values.Carbs / recommended.Carbs,
                nutritionBackgroundPaint,
                nutritionForegroundPaint)
            drawCircleFill(this,
                leftOffset + actualWidth * 0.875f,
                actualHeight * 0.5f,
                actualHeight * 0.4f *.3333f,
                _values.Protein / recommended.Protein,
                nutritionBackgroundPaint,
                nutritionForegroundPaint)
            drawCircleFill(this,
                leftOffset + actualWidth * 0.875f,
                actualHeight * 0.8333f,
                actualHeight * 0.4f *.3333f,
                _values.Fat / recommended.Fat,
                nutritionBackgroundPaint,
                nutritionForegroundPaint)
            drawText(_values.Calories.toString()+"/",leftOffset + actualWidth * 0.375f,actualHeight * 0.5f,textPaint)
            drawText(recommended.Calories.toString(),leftOffset + actualWidth * 0.375f,actualHeight * 0.5f + 42f,textPaint)
        }
    }

    private fun drawCircleFill(canvas: Canvas, posX: Float, posY:Float,radius: Float, percentage: Float, background:Paint, foreground: Paint) {
        val cappedPercentage = if(percentage >1f) 1f else percentage
        canvas.drawCircle(posX, posY,radius,background)
        val rect =RectF(posX-radius, posY-radius,posX+radius, posY+radius)
        canvas.drawArc(rect,0f,cappedPercentage * 360,true, foreground)
    }



}