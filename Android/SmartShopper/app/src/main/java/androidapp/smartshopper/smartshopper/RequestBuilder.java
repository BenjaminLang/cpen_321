package androidapp.smartshopper.smartshopper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Ben on 10/22/2016.
 */
public class RequestBuilder {
    public RequestBuilder(){}

    /*
    Build a read/search request using the item name, email, and search options
     */
    public String buildReadReq(String item, String email, SearchOptions options){
        JSONObject request = new JSONObject();
        try {
            //insert message type
            request.put("message_type", "read");

            //user empty for now
            request.put("email", email);

            //insert options
            JSONObject opt = new JSONObject();
            JSONArray stores = new JSONArray(options.getStores());
            opt.put("stores", stores);
            opt.put("price", options.getPrice());
            opt.put("num", options.getNum());
            opt.put("range_min", options.getPriceMin());
            opt.put("range_max", options.getPriceMax());

            request.put("options", opt);

            //insert item names
            JSONArray searchItems = new JSONArray();
            searchItems.put(item);
            request.put("items", searchItems);
            return request.toString(2);
        } catch (JSONException e){
            e.printStackTrace();
            return "cannot generate message";
        }
    }

    /*
    Build a build collection request
     */
    public String buildCollectionReq(){
        JSONObject request = new JSONObject();
        try{
            request.put("message_type", "collection_request");
            return request.toString(2);
        } catch(JSONException e){
            return "cannot generate message";
        }
    }

    /*
    Build a create account request using id, email, and password
     */
    public String buildAccountCreateReq(String id, String email, String pw) {
        JSONObject request = new JSONObject();
        try{
            request.put("message_type", "acc_create");
            request.put("name", id);
            request.put("email", email);
            request.put("password", pw);

            return request.toString(2);
        } catch(JSONException e){
            return "cannot generate message";
        }
    }

    /*
    Build an account verification request using email and verification code
     */
    public String buildAccVerifyReq(String email, String verCode) {
        JSONObject request = new JSONObject();
        try{
            request.put("message_type", "acc_verify");
            request.put("email", email);
            request.put("verify_num", verCode);

            return request.toString(2);
        } catch(JSONException e){
            return "cannot generate message";
        }
    }

    /*
    Build a login request using email and password
     */
    public String buildLoginReq(String email, String pw) {
        JSONObject request = new JSONObject();
        try{
            request.put("message_type", "acc_login");
            request.put("email", email);
            request.put("name", "kooner");
            request.put("password", pw);

            return request.toString(2);
        } catch(JSONException e){
            return "cannot generate message";
        }
    }

    /*
    Build a change password request using email, old password, and new password
     */
    public String buildChangePass(String email, String oldPass, String newPass) {
        JSONObject request = new JSONObject();
        try{
            request.put("message_type", "acc_update");
            request.put("email", email);
            request.put("old_password", oldPass);
            request.put("password", newPass);

            return request.toString(2);
        } catch(JSONException e){
            return "cannot generate message";
        }
    }

    /*
    Build a add list request using email, list name, and the list
     */
    public String buildAddListReq(String email, String listName, String cartItems){
        JSONObject request = new JSONObject();
        try{
            request.put("message_type", "add_list");
            request.put("email", email);
            request.put("list_name", listName);
            JSONObject cartJSON = new JSONObject(cartItems);
            JSONArray cartArray = cartJSON.getJSONArray("list");
            request.put("list", cartArray);

            return request.toString(2);
        } catch(JSONException e){
            return "cannot generate message";
        }
    }

    /*
    Build a get list names request using email
     */
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

    /*
    Build a get list request using email and list name
     */
    public String buildGetListReq(String email, String list) {
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

    /*
    Build a get recommendation request using email
     */
    public String buildGetRecommend(String email) {
        JSONObject request = new JSONObject();
        try{
            request.put("message_type", "recommend");
            request.put("email", email);

            return request.toString(2);
        } catch(JSONException e){
            return "cannot generate message";
        }
    }
}
