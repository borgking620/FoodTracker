package com.smonhof.foodtracker.data

// KEEP IN SYNC WITH THE SAME TYPE OF THE CONVERTER!
enum class Unit {
    Unknown,
    Grams,
    Kilograms,
    Milliliters,
    Liters,
    Pieces,
    Calorie
}

object UnitHelper{
    fun getUnit(asInt: Int): Unit {
        return when (asInt){
            1 -> Unit.Grams
            2 -> Unit.Kilograms
            3 -> Unit.Milliliters
            4 -> Unit.Liters
            5 -> Unit.Pieces
            6 -> Unit.Calorie
            else -> Unit.Unknown
        }
    }

    fun displayUnit(unit: Unit,amount : Float) : String{
        return when(unit){
            Unit.Grams -> "${amount.format(0)}g"
            Unit.Kilograms -> "${amount}kg"
            Unit.Milliliters ->"${amount.format(0)}mL"
            Unit.Liters -> "${amount}L"
            Unit.Pieces -> "${amount.format(0)}st"
            Unit.Calorie -> "${amount.format(0)}cal"
            else -> amount.toString()
        }
    }
    private fun Float.format(digits: Int) = "%.${digits}f".format(this)
}
