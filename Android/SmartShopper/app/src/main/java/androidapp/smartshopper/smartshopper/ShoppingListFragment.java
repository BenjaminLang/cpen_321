package androidapp.smartshopper.smartshopper;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.FileInputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ShoppingListFragment extends Fragment {
    private Context context;
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

            cartItems = new JsonParser().parseCart(cartString);
            totalPrice = cartJSON.getDouble("total_price");
        } catch(Exception e) {
            e.printStackTrace();
        }
        /*
        Bundle bundle = this.getArguments();
        if(bundle != null) {
            jsonResp = bundle.getString(ARG_KEY);
        }*/

        /*
        if(jsonResp.isEmpty()) {
            Toast.makeText(getActivity(), "The Item You Searched For Doesn't Exist :(", Toast.LENGTH_LONG).show();
        }

        JsonParser parser = new JsonParser();
        this.result = parser.parseJSON(jsonResp);
        ProductAdapter adapter = new ProductAdapter(this.context, R.layout.search_result, this.result);
        this.setListAdapter(adapter);
        */
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_shopping_list, container, false);

        TextView total = (TextView) view.findViewById(R.id.cart_summary);
        total.setText("Total: " + Double.toString(round(totalPrice, 2)));


        if(!cartItems.isEmpty() || cartItems != null) {
            ProductAdapter adapter = new ProductAdapter(this.context, R.layout.search_result, this.cartItems);
            ListView list = (ListView) view.findViewById(R.id.cart_list);
            list.setAdapter(adapter);
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
