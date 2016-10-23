package androidapp.smartshopper.smartshopper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by Ben on 10/22/2016.
 */
public class RequestBuilder {
    public RequestBuilder(){}

    public String buildReadReq(List<String> items, SearchOptions[] options, String[] collections){
        JSONObject request = new JSONObject();
        try {
            request.put("message_type", "read");
            JSONObject opt = new JSONObject();
            for(SearchOptions option : options)
                opt.put(option.getOptionType(), option.getOption());
            request.put("options", opt);
            JSONArray searchCollections = new JSONArray();
            for(String col : collections)
                searchCollections.put(col);
            request.put("collections", searchCollections);
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

}
