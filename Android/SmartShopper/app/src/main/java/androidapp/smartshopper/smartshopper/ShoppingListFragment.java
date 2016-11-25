package androidapp.smartshopper.smartshopper;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
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

    private SharedPreferences sharedPref;

    public ShoppingListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        this.context = getActivity();
        sharedPref = sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);

        try {
            String defaultVal = "";
            final String cartString = sharedPref.getString("cart", defaultVal);

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

        final EditText listNameEntry = (EditText) view.findViewById(R.id.new_list_name);
        final Button saveList = (Button) view.findViewById(R.id.save_list_button);

        saveList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newListName = listNameEntry.getText().toString();
                String defaultUser = "";
                String user = sharedPref.getString(getString(R.string.curr_user), defaultUser);
                String defaultList = "";
                String list = sharedPref.getString(getString(R.string.curr_user), defaultList);
                String request = new RequestBuilder().buildSaveListReq(user, newListName, list);

                try {
                    String jsonResponse = new SendRequest().execute(request).get();
                } catch (Exception e) {
                    Toast.makeText(getActivity(), "Something Went Wrong", Toast.LENGTH_SHORT).show();
                }
            }
        });

        if(cartItems != null) {
            adapter = new ProductAdapter(this.context, R.layout.search_result, this.cartItems);
            ListView list = (ListView) view.findViewById(R.id.cart_list);
            list.setAdapter(adapter);

            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Product selected = cartItems.get(position);

                    ShopListHandler listHandler = new ShopListHandler(getActivity(), "cart");
                    List<Product> updatedList = listHandler.deleteFromList(selected);
                    double newTotal = listHandler.getListTotal();

                    adapter.updateProductList(updatedList);
                    total.setText("Total: " + Double.toString(round(newTotal,2)));
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

    private class SendRequest extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... request) {
            SmartShopClient client = new SmartShopClient();
            if(client.getStatus())
                return client.sendRequest(request[0]);
            else
                return "Connection Not Established";
        }

        @Override
        protected void onPostExecute(String request) {
            super.onPostExecute(request);
        }
    }
}
