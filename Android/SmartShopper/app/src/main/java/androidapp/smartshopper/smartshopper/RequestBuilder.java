package androidapp.smartshopper.smartshopper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Ben on 10/22/2016.
 */
public class RequestBuilder {
    public RequestBuilder(){}

    public String buildReadReq(String item, String email, SearchOptions options, String userId){
        JSONObject request = new JSONObject();
        try {
            //insert message type
            request.put("message_type", "read");

            //user empty for now
            request.put("email", email);

            //insert options
            JSONObject opt = new JSONObject();
            JSONArray stores = new JSONArray();
            for(String currStore : options.getStores())
                stores.put(currStore);
            opt.put("stores", stores);
            opt.put("price", options.getPrice());
            opt.put("num", options.getNum());
            opt.put("range_min", options.getPriceMin());
            opt.put("range_max", options.getPriceMax());

            request.put("options", opt);

            //insert item names
            //String[] collections = item.split(" ");
            JSONArray searchItems = new JSONArray();
            searchItems.put(item);
            request.put("items", searchItems);
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

    public String buildAccountCreatReq(String id, String pw) {
        JSONObject request = new JSONObject();
        try{
            request.put("message_type", "acc_create");
            request.put("name", "kooner");
            request.put("email", id);
            request.put("password", pw);

            return request.toString(2);
        } catch(JSONException e){
            return "cannot generate message";
        }
    }

    public String buildLoginReq(String id, String pw) {
        JSONObject request = new JSONObject();
        try{
            request.put("message_type", "acc_login");
            request.put("email", id);
            request.put("name", "kooner");
            request.put("password", pw);

            return request.toString(2);
        } catch(JSONException e){
            return "cannot generate message";
        }
    }

    public String buildLogoutReq(String id) {
        JSONObject request = new JSONObject();
        try{
            request.put("message_type", "acc_logout");
            request.put("email", id);

            return request.toString(2);
        } catch(JSONException e){
            return "cannot generate message";
        }
    }

    public String buildChangePass(String id, String oldPass, String newPass) {
        JSONObject request = new JSONObject();
        try{
            request.put("message_type", "acc_update");
            request.put("email", id);
            request.put("old_password", oldPass);
            request.put("password", newPass);

            return request.toString(2);
        } catch(JSONException e){
            return "cannot generate message";
        }
    }

    public String buildAddListReq(String email, String listName, String cartItems){
        JSONObject request = new JSONObject();
        try{
            request.put("message_type", "add_list");
            request.put("email", email);
            request.put("list_name", listName);
            JSONObject cartJSON = new JSONObject(cartItems);
            JSONArray cartArray = cartJSON.getJSONArray("cart_list");
            request.put("list", cartArray);

            return request.toString(2);
        } catch(JSONException e){
            return "cannot generate message";
        }
    }

    public String buildDeleteListReq(String email, String listName) {
        JSONObject request = new JSONObject();
        try{
            request.put("message_type", "delete_list");
            request.put("email", email);
            request.put("list_name", listName);

            return request.toString(2);
        } catch(JSONException e){
            return "cannot generate message";
        }
    }

    public String buildGetListNamesJSON(String email) {
        JSONObject request = new JSONObject();
        try{
            request.put("message_type", "get_list_names");
            request.put("email", email);

            return request.toString(2);
        } catch(JSONException e){
            return "cannot generate message";
        }
    }

    public String buildGetListJSON(String email, String list) {
        JSONObject request = new JSONObject();
        try{
            request.put("message_type", "get_list");
            request.put("email", email);
            request.put("list_name", list);

            return request.toString(2);
        } catch(JSONException e){
            return "cannot generate message";
        }
    }

    private String[] getSuperString(String[] items){
        //String[] collections = Collections.getInstance().getCollection();
        //this is hard
        return null;
    }
}
