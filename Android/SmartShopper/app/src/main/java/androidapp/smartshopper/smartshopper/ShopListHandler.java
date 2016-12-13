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

    public ShopListHandler(Activity context, String listName) {
        this.context = context;
        this.listName = listName;
    }

    public boolean addToList(Product toAdd, int numToAdd) {
        try{
            JSONObject jsonAppend = new JSONObject();
            jsonAppend.put("name", toAdd.getName());
            jsonAppend.put("price", toAdd.getPrice());
            jsonAppend.put("store", toAdd.getStore());
            jsonAppend.put("image", toAdd.getImg());
            jsonAppend.put("url", toAdd.getURL());

            SharedPrefSingle sharedPref = SharedPrefSingle.getInstance(context);
            String defaultVal = "";
            final String cartString = sharedPref.getString(listName, defaultVal);

            if(cartString != "") {
                boolean alreadyAdded = false;
                List<Product> currCart = new JSONParser().parseCart(cartString);

                for(int i = 0; i < currCart.size(); i++) {
                    Product currProduct = currCart.get(i);
                    if(currProduct.getImg().equals(toAdd.getImg()))
                        alreadyAdded = true;
                }

                JSONObject cartJSON = new JSONObject(cartString);
                JSONArray cartArray = cartJSON.getJSONArray("list");
                if(!alreadyAdded) {
                    jsonAppend.put("quantity", Integer.toString(numToAdd));
                    cartArray.put(jsonAppend);

                    /*
                    double totalPrice = cartJSON.getDouble("total_price");
                    totalPrice += numToAdd * Double.parseDouble(toAdd.getPrice());
                    cartJSON.put("total_price", Double.toString(totalPrice));*/

                    String newCartString = cartJSON.toString(2);
                    sharedPref.put(listName, newCartString);
                }
                else {
                    for(int i = 0; i < cartArray.length(); i++) {
                        JSONObject currItem = cartArray.getJSONObject(i);
                        if(currItem.getString("image").equals(toAdd.getImg())) {
                            int quantity = currItem.getInt("quantity");
                            quantity += numToAdd;
                            currItem.put("quantity", Integer.toString(quantity));

                            /*
                            double totalPrice = cartJSON.getDouble("total_price");
                            totalPrice += numToAdd * Double.parseDouble(toAdd.getPrice());
                            cartJSON.put("total_price", Double.toString(totalPrice));*/

                            String newCartString = cartJSON.toString(2);
                            sharedPref.put(listName, newCartString);

                            break;
                        }
                    }
                }
            }
            else {
                JSONObject jsonObj = new JSONObject();
                JSONArray jsonArray = new JSONArray();
                jsonAppend.put("quantity", Integer.toString(numToAdd));
                jsonArray.put(jsonAppend);

                jsonObj.put("list", jsonArray);

                /*
                double price = Double.parseDouble(toAdd.getPrice());
                price *= numToAdd;
                jsonObj.put("total_price", Double.toString(price));*/

                String newCartString = jsonObj.toString();
                sharedPref.put(listName, newCartString);
            }

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Product> deleteFromList(Product product) {
        try {
            SharedPrefSingle sharedPref = SharedPrefSingle.getInstance(context);
            String defaultVal = "";
            final String cartString = sharedPref.getString(listName, defaultVal);

            JSONObject cartJSON = new JSONObject(cartString);
            JSONArray cartArray = cartJSON.getJSONArray("list");

            List<Product> updatedList = new ArrayList<Product>();
            for(int i = 0; i < cartArray.length(); i++) {
                JSONObject currObj = cartArray.getJSONObject(i);
                if(currObj.getString("image").equals(product.getImg())) {
                    /*
                    double newTotal = cartJSON.getDouble("total_price");
                    int quantity = currObj.getInt("quantity");
                    double price = currObj.getDouble("price");
                    newTotal -= (quantity * price);
                    cartJSON.put("total_price", Double.toString(round(newTotal, 2)));*/

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

    /*
    public double getListTotal() {
        try {
            SharedPreferences sharedPref = context.getPreferences(Context.MODE_PRIVATE);
            String defaultVal = "";
            final String cartString = sharedPref.getString(listName, defaultVal);

            JSONObject cartJSON = new JSONObject(cartString);
            return cartJSON.getDouble("total_price");
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }*/

    private static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}
