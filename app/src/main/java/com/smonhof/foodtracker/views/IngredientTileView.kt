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
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.smonhof.foodtracker.R
import com.smonhof.foodtracker.data.Ingredient
import com.smonhof.foodtracker.data.IngredientAmount
import com.smonhof.foodtracker.fragments.arguments.SelectIngredientAmountFragmentArguments

class IngredientTileView : View {

    private val _label : String
    private val _ingredient : Ingredient

    private val backgroundPaint = Paint(ANTI_ALIAS_FLAG).apply {
        color = resources.getColor(R.color.blue)
        style = Paint.Style.FILL
    }
    private val textPaint = Paint(ANTI_ALIAS_FLAG).apply{
        color = resources.getColor(R.color.black)
        textSize = 42f
    }

    constructor(context :Context, attrs: AttributeSet) : super(context, attrs){
        _ingredient = Ingredient.empty
        _label = "Invalid"

    }

    constructor(context: Context, ingredient: Ingredient, onClick : (IngredientAmount) -> Unit = {}) : super (context)
    {
        _label = ingredient.name
        _ingredient = ingredient

        setOnClickListener{
            val navController = findNavController()
            val bundle = bundleOf("ContainerIngredient" to SelectIngredientAmountFragmentArguments(ingredient, onClick))
            navController.navigate(R.id.action_IngredientList_to_SelectIngredientAmount, bundle)
        }

        var params = layoutParams
        if(params == null)
        {
            params = ViewGroup.LayoutParams(0,0)
        }
        params.height = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,80f, resources.displayMetrics).toInt()
        params.width = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,80f, resources.displayMetrics).toInt()
        layoutParams = params
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
            canvas.apply {
                drawRect(2f,2f,width.toFloat()-4f,height.toFloat()-4f,backgroundPaint)
                drawText(_label,8f,8f + textPaint.textSize,textPaint)
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return super.onTouchEvent(event)
    }

}