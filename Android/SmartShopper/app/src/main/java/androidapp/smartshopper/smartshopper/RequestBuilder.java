package androidapp.smartshopper.smartshopper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Ben on 10/22/2016.
 */
public class RequestBuilder {
    public RequestBuilder(){}

    public String buildReadReq(String item, SearchOptions options, String userId){
        JSONObject request = new JSONObject();
        try {
            //insert message type
            request.put("message_type", "read");

            //user empty for now
            request.put("userID", userId);

            //insert options
            JSONObject opt = new JSONObject();
            opt.put("locationX", options.getLocationX());
            opt.put("locationY", options.getLocationY());
            opt.put("Radius", options.getRadius());
            JSONArray stores = new JSONArray();
            for(String currStore : options.getStores())
                stores.put(currStore);
            opt.put("stores", stores);

            request.put("options", opt);

            //insert item names
            String[] collections = item.split(" ");
            JSONArray searchItems = new JSONArray();
            for(String currColle : collections)
                searchItems.put(currColle);
            request.put("collections", searchItems);
            return request.toString(2);
        } catch (JSONException e){
            e.printStackTrace();
            return "cannot generate message";
        }
    }

    public String buildCollectionReq(){
        JSONObject request = new JSONObject();
        try{
            request.put("message_type", "collection_request");
            return request.toString(2);
        } catch(JSONException e){
            return "cannot generate message";
        }
    }

    private String[] getSuperString(String[] items){
        String[] collections = Collections.getInstance().getCollection();
        //this is hard
        return null;
    }
}
