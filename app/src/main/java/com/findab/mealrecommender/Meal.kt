package com.findab.mealrecommender

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_meal.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.File
import java.nio.charset.Charset

// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_TITLE = "title"
private const val ARG_CALORIES = "calories"

/**
 * A simple [Fragment] subclass.
 * Use the [Meal.newInstance] factory method to
 * create an instance of this fragment.
 */
class Meal : Fragment() {
    private var title: String? = null
    private var calories: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            title = it.getString(ARG_TITLE)
            calories = it.getInt(ARG_CALORIES)
        }
    }

    private fun addToList() {
        var json: String? = null
        try {
            val inputStream =
                context?.openFileInput("mealList.json")
            var byteArray = ByteArray(inputStream!!.available())
            inputStream?.read(byteArray)
            inputStream?.close()
            json = String(byteArray, Charset.forName("UTF-8"))
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }

        var jsonArray: JSONArray? = null

        if (json == null) {
            jsonArray = JSONArray()
        } else {
            try {
                var obj = JSONObject(json)
                jsonArray = obj.getJSONArray("meals")
            } catch (e: JSONException) {
                e.printStackTrace()
                return
            }
        }

        var jsonObj = JSONObject()
        jsonObj.put("title", title)
        jsonObj.put("calories", calories)

        jsonArray!!.put(jsonObj)

        var mealObj = JSONObject()
        mealObj.put("meals", jsonArray)

        try {
            var outputStream = context?.openFileOutput("mealList.json", Context.MODE_PRIVATE)
            outputStream?.write(mealObj.toString().toByteArray())
            outputStream?.close()
        } catch (e: Exception) {
            e.printStackTrace()
            return
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_meal, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnAddToList.setOnClickListener {
            addToList()
        }

        if (title != null && calories != null) {
            tvTitle.text = title!!
            tvCalories.text = "Calories: " + calories!!.toString()
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(title: String, calories: Int) =
            Meal().apply {
                arguments = Bundle().apply {
                    putString(ARG_TITLE, title)
                    putInt(ARG_CALORIES, calories)
                }
            }
    }
}