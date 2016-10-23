package androidapp.smartshopper.smartshopper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by Ben on 10/22/2016.
 */
public class RequestBuilder {
    public RequestBuilder(){

    }

    public String buildReadReq(List<String> items, SearchOptions[] options, String[] collections){
        JSONObject request = new JSONObject();
        try {
            request.put("message_type", "read");
            JSONObject opt = new JSONObject();
            for(SearchOptions option : options)
                opt.put(option.getOptionType(), option.getOption());
            request.put("options", opt);
            JSONArray
            for(String col : collections)


        } catch (JSONException e){
            e.printStackTrace();
        }


    }
}
