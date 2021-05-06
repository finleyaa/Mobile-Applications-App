package com.findab.mealrecommender

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_meal_list.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.File
import java.lang.Exception
import java.nio.charset.Charset

class MealList : AppCompatActivity() {

    var mealListArray: JSONArray? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_meal_list)

        btnGenerator.setOnClickListener {
            val intent = Intent(this, Generator::class.java)
            startActivity(intent)
        }

        btnClearMealList.setOnClickListener {
            try {
                var file = File(filesDir,"mealList.json")
                file.delete()
                tvMealListItems.text = ""
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        mealListArray = loadMealList()

        if (mealListArray != null) {
            var mealListString = ""
            for (i in 0 until mealListArray!!.length()) {
                var obj = mealListArray!!.getJSONObject(i)
                mealListString += obj.getString("title") + "\n\n"
            }
            tvMealListItems.text = mealListString
        }
    }

    private fun loadMealList() : JSONArray? {
        var json: String? = null
        try {
            val inputStream =
                applicationContext.openFileInput("mealList.json")
            var byteArray = ByteArray(inputStream.available())
            inputStream.read(byteArray)
            inputStream.close()
            json = String(byteArray, Charset.forName("UTF-8"))
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }

        if (json == null) return null

        try {
            var obj = JSONObject(json)
            return obj.getJSONArray("meals")
        } catch (e: JSONException) {
            e.printStackTrace()
            return null
        }
    }

    override fun onBackPressed() {
        return
    }
}