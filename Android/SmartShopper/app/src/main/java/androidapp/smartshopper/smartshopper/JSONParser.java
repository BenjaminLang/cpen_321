package androidapp.smartshopper.smartshopper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by JohnS on 2016-10-23.
 */

public class JSONParser {
    private String ITEMS_TAG = "items";
    private String COLUMN_TAG = "col_name_1";
    private String NAME_TAG = "name";
    private String PRICE_TAG = "price";
    private String STORE_TAG = "store";
    private String IMG_TAG = "image";
    private String URL_TAG = "url";
    private String DATA_TAG = "data";

    private String LIST_TAG = "list";
    private String RECOMMEND_TAG = "rec_list";
    private String QUANTITY_TAG = "quantity";

    public JSONParser() {}

    /*
    USE FOR SEARCH RESULTS
    Takes JSON string and parse into a list of Product objects.
     */
    public List<Product> parseProductList(String json) {
        if(json.equals("")) {
            return new ArrayList<Product>();
        }

        if(json != null) {
            List<Product> parseList = new ArrayList<Product>();

            try{
                JSONObject jsonObj = new JSONObject(json);
                JSONArray items = jsonObj.getJSONArray(ITEMS_TAG);

                for(int i = 0; i < items.length(); i++) {
                    JSONObject currItem = items.getJSONObject(i);
                    JSONObject currData = currItem.getJSONObject(DATA_TAG);

                    String name = currData.getString(NAME_TAG);
                    String price = currData.getString(PRICE_TAG);
                    String store = currData.getString(STORE_TAG);
                    String img = currData.getString(IMG_TAG);
                    String url = currData.getString(URL_TAG);

                    Product currProduct = new Product(name, price, store, img, url, null);
                    parseList.add(currProduct);
                }
            }catch (JSONException e) {
                e.printStackTrace();
            }

            return parseList;
        }
        else {
            return null;
        }
    }

    /*
    USE FOR PRODUCT RECOMMENDATION
    Takes JSON string and parse into a list of Product objects.
     */
    public List<Product> parseRecommend(String json) {
        if(json.equals("")) {
            return new ArrayList<Product>();
        }

        if(json != null) {
            List<Product> parseList = new ArrayList<Product>();

            try{
                JSONObject jsonObj = new JSONObject(json);
                JSONArray items = jsonObj.getJSONArray(RECOMMEND_TAG);

                for(int i = 0; i < items.length(); i++) {
                    JSONObject currItem = items.getJSONObject(i);
                    JSONObject currData = currItem.getJSONObject(DATA_TAG);

                    String name = currData.getString(NAME_TAG);
                    String price = currData.getString(PRICE_TAG);
                    String store = currData.getString(STORE_TAG);
                    String img = currData.getString(IMG_TAG);
                    String url = currData.getString(URL_TAG);

                    Product currProduct = new Product(name, price, store, img, url, null);
                    parseList.add(currProduct);
                }
            }catch (JSONException e) {
                e.printStackTrace();
            }

            return parseList;
        }
        else {
            return null;
        }
    }

    /*
    USE FOR PARSING SHOPPING LIST
    Takes JSON string and parse into a list of Product objects.
     */
    public List<Product> parseCart(String json) {
        if(json.equals("")) {
            return new ArrayList<Product>();
        }

        if (json != null) {
            List<Product> parseList = new ArrayList<Product>();

            try {
                JSONObject jsonObj = new JSONObject(json);
                JSONArray items = jsonObj.getJSONArray(LIST_TAG);

                for (int i = 0; i < items.length(); i++) {
                    JSONObject currItem = items.getJSONObject(i);

                    String name = currItem.getString(NAME_TAG);
                    String price = currItem.getString(PRICE_TAG);
                    String store = currItem.getString(STORE_TAG);
                    String img = currItem.getString(IMG_TAG);
                    String url = "";
                    String quantity = currItem.getString(QUANTITY_TAG);

                    Product currProduct = new Product(name, price, store, img, url, quantity);
                    parseList.add(currProduct);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return parseList;
        } else {
            return null;
        }
    }

    /*
    Takes JSON string and parse into a list of Strings representing the lists that a user has saved
     */
    public List<String> parseListNames(String json) {
        if(json.equals("")) {
            return new ArrayList<String>();
        }

        if(json != null) {
            List<String> shopLists = new ArrayList<String>();

            try{
                JSONObject jsonObj = new JSONObject(json);
                JSONArray listArray = jsonObj.getJSONArray("list_names");

                for(int i = 0; i < listArray.length(); i++) {
                    shopLists.add(listArray.getString(i));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return shopLists;
        }
        else {
            return null;
        }
    }
}
