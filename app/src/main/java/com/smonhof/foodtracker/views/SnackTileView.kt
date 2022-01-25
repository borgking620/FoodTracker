package com.smonhof.foodtracker.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Paint.ANTI_ALIAS_FLAG
import android.util.AttributeSet
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import com.smonhof.foodtracker.R
import com.smonhof.foodtracker.data.*

class SnackTileView : View {

    private val label : String
    private val snack : Snack
    private val onClickAction : (Snack) -> Unit

    private val backgroundPaint = Paint(ANTI_ALIAS_FLAG).apply {
        color = resources.getColor(R.color.green)
        style = Paint.Style.FILL
    }
    private val textPaint = Paint(ANTI_ALIAS_FLAG).apply{
        color = resources.getColor(R.color.black)
        textSize = 42f
    }

    constructor(context :Context, attrs: AttributeSet) : super(context, attrs){
        snack = ValidSnack("", IngredientSnack("",NutritionalValues.empty))
        label = "Invalid"
        onClickAction = {}

        constructCommon()
    }

    constructor(context: Context, snack: Snack, onClick: (Snack) -> Unit = {}) : super (context)
    {
        label = snack.displayName
        this.snack = snack
        onClickAction = onClick
        var params = layoutParams
        if(params == null)
        {
            params = ViewGroup.LayoutParams(0,0)
        }
        params.height = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,80f, resources.displayMetrics).toInt()
        params.width = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,80f, resources.displayMetrics).toInt()
        layoutParams = params

        constructCommon()
    }

    private fun constructCommon()
    {
        setOnClickListener(::onClick)

    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.apply {
            drawRect(2f,2f,width.toFloat()-4f,height.toFloat()-4f,backgroundPaint)
            drawText(label,8f,8f + textPaint.textSize,textPaint)
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return super.onTouchEvent(event)
    }

    fun onClick (view :View)
    {
        onClickAction(snack)
    }

}