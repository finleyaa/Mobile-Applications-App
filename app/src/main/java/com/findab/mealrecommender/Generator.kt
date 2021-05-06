package com.findab.mealrecommender

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_generator.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.nio.charset.Charset

class Generator : AppCompatActivity() {

    private var mealsArray : JSONArray? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_generator)

        mealsArray = loadMealsJSON()

        btnGenerate.setOnClickListener {
            selectMeal()
        }

        btnMealList.setOnClickListener {
            val intent = Intent(this, MealList::class.java)
            startActivity(intent)
        }
    }

    private fun loadMealsJSON() : JSONArray? {
        var json: String? = null
        try {
            val inputStream = assets.open("meals.json")
            val size = inputStream.available()
            var byteArray = ByteArray(size)
            inputStream.read(byteArray)
            inputStream.close()
            json = String(byteArray, Charset.forName("UTF-8"))
        } catch (ex: IOException) {
            ex.printStackTrace()
            return null
        }

        if (json == null) return null

        try {
            var obj = JSONObject(json)
            return obj.getJSONArray("meals")
        } catch (ex: JSONException) {
            ex.printStackTrace()
            return null
        }
    }

    private fun selectMeal() {
        val selectedCalories = etCalories.text.toString().toIntOrNull()

        var suitableMeals: MutableList<JSONObject> = mutableListOf()

        if (selectedCalories != null && mealsArray != null) {
            for (i in 0 until mealsArray!!.length()) {
                val currentMeal = mealsArray!!.getJSONObject(i)
                if (currentMeal.getInt("calories") <= selectedCalories) {
                    suitableMeals.add(currentMeal)
                }
            }
        }

        if (suitableMeals.size > 0) {
            var chosenMeal = suitableMeals.random()
            val mealFragment = Meal.newInstance(chosenMeal.getString("title"), chosenMeal.getInt("calories"))
            supportFragmentManager.beginTransaction()
                .replace(R.id.flMeal, mealFragment)
                .commit()
        }

    }

    override fun onBackPressed() {
        return
    }
}