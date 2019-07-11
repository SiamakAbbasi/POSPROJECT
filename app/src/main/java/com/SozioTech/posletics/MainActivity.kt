package com.SozioTech.posletics

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.os.AsyncTask
import android.view.View
import android.widget.EditText
import android.widget.Toast
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.net.MalformedURLException
import java.util.ArrayList


class MainActivity : AppCompatActivity() {
    internal lateinit var dataproperties: String
    internal lateinit var ListOfUserModel: ArrayList<JsonDataModelUser>
    var userId = 0;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // Get Data From Json
        var jsonParserClass = JsonParser()
        ListOfUserModel = ArrayList<JsonDataModelUser>()
        jsonParserClass.execute()
        //Get Data From Json
    }

    fun onButtonClick(v: View) {
        val myIntent = Intent(baseContext, MapActivity::class.java)
        val txtusername: EditText = findViewById(R.id.txtUsername)
        for (user in ListOfUserModel) {
            if (user.name.trim().toUpperCase() == txtusername.text.toString().trim().toUpperCase()) {
                userId = user.id
                break
            }
        }
        if (userId > 0) {
            myIntent.putExtra(Constants.USERID, userId )
            startActivity(myIntent)

        } else {
            Toast.makeText(applicationContext, "User not found!",Toast.LENGTH_SHORT).show()
        }
    }


    private inner class JsonParser : AsyncTask<Void, Void, JSONArray>() {
        override fun onPostExecute(result: JSONArray?) {
            super.onPostExecute(result)
            var itemcount: Int = result?.length()!!
            var jsonArray = JSONArray()

            for (i in 0 until (itemcount)) {
                var jsonDataModelUser = JsonDataModelUser()
                var jsonObject: JSONObject = result.get(i) as JSONObject
                jsonDataModelUser.id = jsonObject.get("id") as Int
                jsonDataModelUser.name = jsonObject.get("name") as String
                jsonDataModelUser.email = jsonObject.get("email") as String
                ListOfUserModel.add(jsonDataModelUser)
            }

        }

        override fun doInBackground(vararg voids: Void): JSONArray? {
            try {
                var handler = HttpHandler();
                var url = "https://posletics.herokuapp.com/api/users"
                dataproperties = handler.makeServiceCall(url)
                var jsonArray = JSONArray(dataproperties)
                return jsonArray

            } catch (e: MalformedURLException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            } catch (e: JSONException) {
                e.printStackTrace();
            }
            return null
        }

        // return JSON String
    }
}
