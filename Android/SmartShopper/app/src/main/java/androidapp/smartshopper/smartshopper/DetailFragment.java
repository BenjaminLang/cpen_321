package androidapp.smartshopper.smartshopper;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NotificationCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class DetailFragment extends Fragment {
    private Context context;
    private Product product;

    private String ARG_KEY = "product_json";

    public DetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.context = getActivity();

        String productJSON;
        Bundle bundle = this.getArguments();
        if (bundle != null)
            productJSON = bundle.getString(ARG_KEY);
        else
            productJSON = null;

        if(productJSON != null)
            product = jsonToProduct(productJSON);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_detail, container, false);

        ImageView img = (ImageView) view.findViewById(R.id.detail_img);
        TextView name = (TextView) view.findViewById(R.id.product_name);
        TextView price = (TextView) view.findViewById(R.id.product_price);
        TextView store = (TextView) view.findViewById(R.id.product_store);
        final EditText quantity = (EditText) view.findViewById(R.id.quantity_to_add);
        quantity.setGravity(Gravity.CENTER_HORIZONTAL);
        quantity.setText("1");
        Button addCart = (Button) view.findViewById(R.id.add_to_cart);
        Button toWeb = (Button) view.findViewById(R.id.visit_url);

        addCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                final SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
                boolean loggedIn = sharedPref.getBoolean(getString(R.string.login_stat), false);

                String currListName;
                if(loggedIn) {
                    currListName = "default_list";
                }
                else {
                    currListName = "default_list";
                }

                ShopListHandler listHandler = new ShopListHandler(getActivity(), currListName);
                int numToAdd = Integer.parseInt(quantity.getText().toString());

                if(listHandler.addToList(product, numToAdd))
                    Toast.makeText(getActivity(), "Added to cart!", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(getActivity(), "Adding failed!", Toast.LENGTH_SHORT).show();
            }
        });

        toWeb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Bundle bundle = new Bundle();
                bundle.putString("url", product.getURL());
                ProductWebFragment webFrag = new ProductWebFragment();
                webFrag.setArguments(bundle);

                FragmentManager fragMan = getFragmentManager();
                fragMan.beginTransaction()
                        .replace(R.id.result_frame, webFrag)
                        .addToBackStack("product_website")
                        .setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right, android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                        .commit();
            }
        });

        Picasso.with(context).load(product.getImg()).into(img);
        name.setText(product.getName());
        price.setText("$"+product.getPrice());
        store.setText(product.getStore());

        return view;
    }

    private Product jsonToProduct (String json) {
        try {
            JSONObject obj = new JSONObject(json);
            String name = obj.getString("name");
            String price = obj.getString("price");
            String store = obj.getString("store");
            String img = obj.getString("image");
            String url = obj.getString("url");

            return new Product(name, price, store, img, url, "");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
