package androidapp.smartshopper.smartshopper;


import android.content.Context;
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
                int numToAdd = Integer.parseInt(quantity.getText().toString());

                if(addToCart(product, numToAdd))
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

    private boolean addToCart(Product toAdd, int numToAdd) {
        try{
            JSONObject jsonAppend = new JSONObject();
            jsonAppend.put("name", toAdd.getName());
            jsonAppend.put("price", toAdd.getPrice());
            jsonAppend.put("store", toAdd.getStore());
            jsonAppend.put("image", toAdd.getImg());
            jsonAppend.put("url", toAdd.getURL());

            String fileName = getResources().getString(R.string.cart_file_name);
            File file = this.context.getFileStreamPath(fileName);

            if(file.exists()) {
                FileInputStream inputStream = this.context.openFileInput(fileName);

                StringBuilder builder = new StringBuilder();
                int ch;
                while((ch = inputStream.read()) != -1) {
                    builder.append((char)ch);
                }
                inputStream.close();
                String cartString = builder.toString();

                boolean alreadyAdded = false;
                List<Product> currCart = new JSONParser().parseCart(cartString);

                for(int i = 0; i < currCart.size(); i++) {
                    Product currProduct = currCart.get(i);
                    if(currProduct.getImg().equals(toAdd.getImg()))
                        alreadyAdded = true;
                }

                JSONObject cartJSON = new JSONObject(cartString);
                JSONArray cartArray = cartJSON.getJSONArray("cart_list");
                if(!alreadyAdded) {
                    jsonAppend.put("quantity", Integer.toString(numToAdd));
                    cartArray.put(jsonAppend);

                    double totalPrice = cartJSON.getDouble("total_price");
                    totalPrice += numToAdd * Double.parseDouble(toAdd.getPrice());
                    cartJSON.put("total_price", Double.toString(totalPrice));

                    String newCartString = cartJSON.toString(2);
                    byte[] buffer = newCartString.getBytes();
                    //StandardCharsets.US_ASCII

                    FileOutputStream outputStream = this.context.openFileOutput(fileName, Context.MODE_PRIVATE);
                    outputStream.write(buffer);
                    outputStream.close();
                }
                else {
                    for(int i = 0; i < cartArray.length(); i++) {
                        JSONObject currItem = cartArray.getJSONObject(i);
                        if(currItem.getString("image").equals(toAdd.getImg())) {
                            int quantity = currItem.getInt("quantity");
                            quantity += numToAdd;
                            currItem.put("quantity", Integer.toString(quantity));

                            double totalPrice = cartJSON.getDouble("total_price");
                            totalPrice += numToAdd * Double.parseDouble(toAdd.getPrice());
                            cartJSON.put("total_price", Double.toString(totalPrice));

                            String newCartString = cartJSON.toString(2);
                            byte[] buffer = newCartString.getBytes();
                            //StandardCharsets.US_ASCII

                            FileOutputStream outputStream = this.context.openFileOutput(fileName, Context.MODE_PRIVATE);
                            outputStream.write(buffer);
                            outputStream.close();

                            break;
                        }
                    }
                }
            }
            else {
                File newFile = new File(this.context.getFilesDir(), fileName);
                FileOutputStream outputStream = new FileOutputStream(newFile);

                JSONObject jsonObj = new JSONObject();
                JSONArray jsonArray = new JSONArray();
                jsonAppend.put("quantity", Integer.toString(numToAdd));
                jsonArray.put(jsonAppend);

                jsonObj.put("cart_list", jsonArray);

                double price = Double.parseDouble(toAdd.getPrice());
                price *= numToAdd;
                jsonObj.put("total_price", Double.toString(price));

                String newCartString = jsonObj.toString();
                byte[] buffer = newCartString.getBytes();
                outputStream.write(buffer);
                outputStream.close();
            }

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
