package androidapp.smartshopper.smartshopper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JohnS on 2016-10-23.
 */

public class JsonParser {
    private String COLLECTIONS_TAG = "collections";
    private String COLUMN_TAG = "col_name_1";
    private String NAME_TAG = "name";
    private String PRICE_TAG = "price";
    private String STORE_TAG = "store";
    private String URL_TAG = "image link";

    public JsonParser() {}

    public List<Product> ParseJSON(String json) {
        if(json != null) {
            List<Product> parseList = new ArrayList<Product>();

            try{
                JSONObject jsonObj = new JSONObject(json);
                JSONArray collections = jsonObj.getJSONArray(COLLECTIONS_TAG);

                for(int i = 0; i < collections.length(); i++) {
                    JSONArray items = collections.getJSONArray(i);

                    for(int j = 0; j < items.length(); j++) {

                    }
                }
            }catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else {
            return null;
        }
    }
}
