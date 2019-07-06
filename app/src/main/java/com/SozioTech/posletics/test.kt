import android.os.AsyncTask
import com.SozioTech.posletics.HttpHandler
import com.SozioTech.posletics.JsonDataModelPos
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.net.MalformedURLException
public class xcvxcV{
private inner class JsonParser : AsyncTask<Void, Void, JSONArray>() {
    override fun onPostExecute(result: JSONArray?) {
        super.onPostExecute(result)
        var itemcount: Int = result?.length()!!
        var lat: Double = 12.192839
        var lng: Double = 12.123123
        var jsonArray = JSONArray()

        for (i in 0 until (itemcount)) {
            var jsonDataModelPos = JsonDataModelPos()
            var jsonObject: JSONObject = result.get(i) as JSONObject
            jsonDataModelPos.id = jsonObject.get("id") as Int
            jsonDataModelPos.user_id = jsonObject.get("user_id") as Int
            jsonDataModelPos.upvotes = jsonObject.get("upvotes") as Int
            jsonDataModelPos.lat = jsonObject.get("lat") as String
            jsonDataModelPos.lng = jsonObject.get("lng") as String
            jsonDataModelPos.hashtags = jsonObject.get("hashtags") as JSONArray
            lat = jsonDataModelPos.lat.toDouble();
            lng = jsonDataModelPos.lng.toDouble();
            var name: String = "";
            if (jsonDataModelPos.hashtags != null && jsonDataModelPos.hashtags.length() > 0) {
                name = (jsonDataModelPos.hashtags.get(0) as JSONObject).get("name") as String
            }

        }


    }


    internal var dataprop = ""
    internal var dataModelPoslist: List<JsonDataModelPos>? = null

    fun FetchDataPos(urladdress: String): List<JsonDataModelPos>? {

        return null
    }

    override fun onPreExecute() {
        super.onPreExecute()
    }
    internal lateinit var dataproperties: String;
    internal lateinit var ListOfPosModel: ArrayList<JsonDataModelPos>

    override fun doInBackground(vararg voids: Void): JSONArray? {
        try {
            var handler = HttpHandler();
            var url = "https://posletics.herokuapp.com/api/pos"
            dataprop = handler.makeServiceCall(url);
            dataproperties = dataprop;
            var jsonArray = JSONArray(dataproperties);
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