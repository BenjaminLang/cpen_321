package androidapp.smartshopper.smartshopper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Ben on 10/22/2016.
 */
public class RequestBuilder {
    public RequestBuilder(){}

    public String buildReadReq(String[] items, SearchOptions[] options){
        String[] queryCollections = getSuperString(items);
        JSONObject request = new JSONObject();
        try {
            //insert message type
            request.put("message_type", "read");

            //insert options
            JSONObject opt = new JSONObject();
            for(SearchOptions option : options)
                opt.put(option.getOptionType(), option.getOption());
            request.put("options", opt);

            //insert item names
            JSONArray searchCollections = new JSONArray();
            for(int i = 0; i < items.length; i++)
                searchCollections.put(new JSONObject().put(items[i], queryCollections[i]));

            return request.toString(2);
        } catch (JSONException e){
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
