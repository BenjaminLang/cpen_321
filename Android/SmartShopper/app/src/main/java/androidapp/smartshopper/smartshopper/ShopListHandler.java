package androidapp.smartshopper.smartshopper;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by JohnS on 2016-11-24.
 */

public class ShopListHandler {
    private Activity context;
    private String listName;

    /*
    Constructor for shopping list handler with context and list name of the list wanting to modify
     */
    public ShopListHandler(Activity context, String listName) {
        this.context = context;
        this.listName = listName;
    }

    /*
    Add product toAdd to the list, with numToAdd amount of it
     */
    public boolean addToList(Product toAdd, int numToAdd) {
        try{
            JSONObject jsonAppend = new JSONObject(toAdd.toJSON());
            /*
            jsonAppend.put("name", toAdd.getName());
            jsonAppend.put("price", toAdd.getPrice());
            jsonAppend.put("store", toAdd.getStore());
            jsonAppend.put("image", toAdd.getImg());
            jsonAppend.put("url", toAdd.getURL());
            */

            //get the list string associated with the current list name
            SharedPrefSingle sharedPref = SharedPrefSingle.getInstance(context);
            String defaultVal = "";
            final String cartString = sharedPref.getString(listName, defaultVal);

            //if list isn't empty
            if(cartString != "") {
                //parse list JSON string into a list of products
                boolean alreadyAdded = false;
                List<Product> currCart = new JSONParser().parseCart(cartString);

                //check if the product is already in the list
                for(int i = 0; i < currCart.size(); i++) {
                    Product currProduct = currCart.get(i);
                    if(currProduct.getImg().equals(toAdd.getImg()))
                        alreadyAdded = true;
                }

                JSONObject cartJSON = new JSONObject(cartString);
                JSONArray cartArray = cartJSON.getJSONArray("list");
                if(!alreadyAdded) {
                    //if the product isn't in the list simply append quantity and add to the list
                    jsonAppend.put("quantity", Integer.toString(numToAdd));
                    cartArray.put(jsonAppend);

                    String newCartString = cartJSON.toString(2);
                    sharedPref.put(listName, newCartString);
                }
                else {
                    //if the product is in the list, find the product, and change the quantity associated
                    for(int i = 0; i < cartArray.length(); i++) {
                        JSONObject currItem = cartArray.getJSONObject(i);
                        //product is same if the associated image url is the same
                        if(currItem.getString("image").equals(toAdd.getImg())) {
                            int quantity = currItem.getInt("quantity");
                            quantity += numToAdd;
                            currItem.put("quantity", Integer.toString(quantity));

                            String newCartString = cartJSON.toString(2);
                            sharedPref.put(listName, newCartString);

                            break;
                        }
                    }
                }
            }
            //if list is empty
            else {
                //if list is empty simply add the product as the only item in the list
                JSONObject jsonObj = new JSONObject();
                JSONArray jsonArray = new JSONArray();
                jsonAppend.put("quantity", Integer.toString(numToAdd));
                jsonArray.put(jsonAppend);

                jsonObj.put("list", jsonArray);

                String newCartString = jsonObj.toString();
                sharedPref.put(listName, newCartString);
            }

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /*
    Delete a given product from the list
     */
    public List<Product> deleteFromList(Product product) {
        try {
            //get the JSON string of the list
            SharedPrefSingle sharedPref = SharedPrefSingle.getInstance(context);
            String defaultVal = "";
            final String cartString = sharedPref.getString(listName, defaultVal);

            JSONObject cartJSON = new JSONObject(cartString);
            JSONArray cartArray = cartJSON.getJSONArray("list");

            //convert JSON string to list of products
            List<Product> updatedList = new ArrayList<Product>();
            //find the product needed to be deleted
            for(int i = 0; i < cartArray.length(); i++) {
                JSONObject currObj = cartArray.getJSONObject(i);
                //product is same if associated image url is the same
                if(currObj.getString("image").equals(product.getImg())) {
                    //simply remove the item and save the modified list
                    cartArray.remove(i);

                    String newCartString = cartJSON.toString(2);
                    updatedList = new JSONParser().parseCart(newCartString);

                    sharedPref.put(listName, newCartString);
                    break;
                }
            }

            return updatedList;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
