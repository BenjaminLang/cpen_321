package androidapp.smartshopper.smartshopper;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ShoppingListFragment extends Fragment {
    private Context context;
    private ProductAdapter adapter;
    private List<Product> cartItems;
    private double totalPrice;


    public ShoppingListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        this.context = getActivity();

        try {
            String fileName = getResources().getString(R.string.cart_file_name);
            FileInputStream inputStream = this.context.openFileInput(fileName);

            StringBuilder builder = new StringBuilder();
            int ch;
            while ((ch = inputStream.read()) != -1) {
                builder.append((char) ch);
            }
            inputStream.close();
            String cartString = builder.toString();

            JSONObject cartJSON = new JSONObject(cartString);

            cartItems = new JSONParser().parseCart(cartString);
            totalPrice = cartJSON.getDouble("total_price");
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_shopping_list, container, false);

        final TextView total = (TextView) view.findViewById(R.id.cart_summary);
        total.setText("Total: " + Double.toString(round(totalPrice, 2)));


        if(cartItems != null) {
            adapter = new ProductAdapter(this.context, R.layout.search_result, this.cartItems);
            ListView list = (ListView) view.findViewById(R.id.cart_list);
            list.setAdapter(adapter);

            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Product selected = cartItems.get(position);

                    try {
                        String fileName = getResources().getString(R.string.cart_file_name);
                        FileInputStream inputStream = context.openFileInput(fileName);

                        StringBuilder builder = new StringBuilder();
                        int ch;
                        while ((ch = inputStream.read()) != -1) {
                            builder.append((char) ch);
                        }
                        inputStream.close();
                        String cartString = builder.toString();

                        JSONObject cartJSON = new JSONObject(cartString);
                        JSONArray cartArray = cartJSON.getJSONArray("cart_list");

                        for(int i = 0; i < cartArray.length(); i++) {
                            JSONObject currObj = cartArray.getJSONObject(i);
                            if(currObj.getString("image").equals(selected.getImg())) {
                                double newTotal = cartJSON.getDouble("total_price");
                                int quantity = currObj.getInt("quantity");
                                double price = currObj.getDouble("price");
                                newTotal -= (quantity * price);
                                cartJSON.put("total_price", Double.toString(round(newTotal, 2)));

                                cartArray.remove(i);

                                String newCartString = cartJSON.toString(2);
                                List<Product> updatedList = new JSONParser().parseCart(newCartString);

                                byte[] buffer = newCartString.getBytes();
                                //StandardCharsets.US_ASCII

                                FileOutputStream outputStream = context.openFileOutput(fileName, Context.MODE_PRIVATE);
                                outputStream.write(buffer);
                                outputStream.close();

                                adapter.updateProductList(updatedList);
                                total.setText("Total: " + Double.toString(round(newTotal,2)));
                                break;
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        return view;
    }

    private static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}
