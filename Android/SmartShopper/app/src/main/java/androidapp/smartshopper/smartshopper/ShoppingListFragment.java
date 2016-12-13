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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
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
public class ShoppingListFragment extends Fragment{
    private Context context;
    private ProductAdapter adapter;
    private List<Product> cartItems = new ArrayList<Product>();
    private double totalPrice;
    private String[] listNameOpts = {};
    private String email;
    private boolean loggedIn;
    private String currList = "";

    private SharedPrefSingle sharedPref;

    public ShoppingListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        this.context = getActivity();
        sharedPref = SharedPrefSingle.getInstance(getActivity());

        currList = sharedPref.getString(SharedPrefSingle.prefKey.CURR_LIST, "default_list");
        email = sharedPref.getString(SharedPrefSingle.prefKey.CURR_EMAIL, "");
        loggedIn = sharedPref.getBoolean(SharedPrefSingle.prefKey.LOGIN_STAT, false);

        if(loggedIn) {

            try {
                String getListsReq = new RequestBuilder().buildGetListNamesJSON(email);
                String getAllListResp = new SendRequest(getActivity()).execute(getListsReq).get();

                JSONObject respJSON = new JSONObject(getAllListResp);
                String stat = respJSON.getString("status");

                //JSONArray listsArray = respJSON.getJSONArray("list_names");
                JSONObject listNamesJSON = new JSONObject();

                if(stat.equals("success")) {
                    List<String> listNames = new JSONParser().parseListNames(getAllListResp);
                    listNames.add("Add New List...");
                    listNameOpts = listNames.toArray(new String[0]);
                    System.out.println(getAllListResp);

                    //editor.putString("list_names", getAllListResp);
                    //editor.commit();
                }
                else {
                    Toast.makeText(getActivity(), "Shopping Lists Cannot be Retrieved", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_shopping_list, container, false);

        adapter = new ProductAdapter(this.context, R.layout.search_result, new ArrayList<Product>());
        ListView list = (ListView) view.findViewById(R.id.cart_list);
        list.setAdapter(adapter);

        final TextView total = (TextView) view.findViewById(R.id.cart_summary);

        final Spinner listNameSpin = (Spinner) view.findViewById(R.id.all_usr_list);
        ArrayAdapter<String> listNamesAdpt = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, listNameOpts);
        listNameSpin.setAdapter(listNamesAdpt);
        if(currList.equals("default_list"))
            listNameSpin.setSelection(listNamesAdpt.getPosition("Add New List..."));
        else
            listNameSpin.setSelection(listNamesAdpt.getPosition(currList));

        final Button modList = (Button) view.findViewById(R.id.new_list_button);
        if(currList.equals("default_list"))
            modList.setText("Save List");
        else
            modList.setText("Update List");

        listNameSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String listSelected = listNameOpts[position];
                if(listSelected.equals("Add New List...")) {
                    modList.setText("Save List");

                    currList = "default_list";
                    sharedPref.put(SharedPrefSingle.prefKey.CURR_LIST, currList);

                    String cartString = sharedPref.getString(currList, "");

                    try {
                        //JSONObject cartJSON = new JSONObject(cartString);

                        List<Product> updatedList = new JSONParser().parseCart(cartString);

                        adapter.updateProductList(updatedList);
                        total.setText("Total: " + Double.toString(adapter.getTotalPrc()));
                        cartItems = updatedList;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                else {
                    modList.setText("Update List");

                    sharedPref.put(SharedPrefSingle.prefKey.CURR_LIST, listSelected);

                    if(sharedPref.contains(listSelected)) {
                        String cartString = sharedPref.getString(listSelected, "");
                        try {
                            JSONObject cartJSON = new JSONObject(cartString);

                            List<Product> updatedList = new JSONParser().parseCart(cartString);

                            adapter.updateProductList(updatedList);
                            total.setText("Total: " + Double.toString(adapter.getTotalPrc()));
                            cartItems = updatedList;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    else {
                        String request = new RequestBuilder().buildGetListReq(email, listSelected);

                        try {
                            String jsonResponse = new SendRequest(getActivity()).execute(request).get();
                            String stat = new JSONObject(jsonResponse).getString("status");

                            if(stat.equals("success")) {
                                List<Product> updatedList = new JSONParser().parseCart(jsonResponse);
                                System.out.println(updatedList);
                                adapter.updateProductList(updatedList);
                                cartItems = updatedList;

                                sharedPref.put(listSelected, jsonResponse);
                            }
                            else {
                                //put toast here
                            }
                        } catch (Exception e) {
                            Toast.makeText(getActivity(), "Something Went Wrong", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                return;
            }
        });

        modList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(modList.getText().toString().equals("Save List")) {
                    FragmentManager fm = getFragmentManager();
                    CreateListFragment newListDialog = new CreateListFragment();
                    newListDialog.show(fm, "fragment_new_list");
                }
                else {
                    String listJSON =  sharedPref.getString("default_list", "");

                    if(listJSON.equals("")) {
                        try {
                            JSONObject newListJSON = new JSONObject();
                            JSONArray emptyArray = new JSONArray();
                            newListJSON.put("list", emptyArray);

                            listJSON = newListJSON.toString(2);
                        } catch (Exception e) {
                            //put toast
                        }
                    }
                    else {
                        try {
                            JSONObject currListJSON = new JSONObject(listJSON);
                            JSONArray listArray = currListJSON.getJSONArray("list");

                            JSONObject newListJSON = new JSONObject();
                            newListJSON.put("list", listArray);

                            listJSON = newListJSON.toString(2);
                        } catch (Exception e) {
                            //put toast
                        }
                    }

                    String updateReq = new RequestBuilder().buildAddListReq(email, currList, listJSON);

                    try {
                        String updateResp = new SendRequest(getActivity()).execute(updateReq).get();
                        JSONObject updateRespJSON = new JSONObject(updateResp);

                        String status = updateRespJSON.getString("status");
                        if(status.equals("success")) {
                            Toast.makeText(getActivity(), "List Updated!", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(getActivity(), "List Update Failed!", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        if(loggedIn) {
            if(currList.equals("")) {
                currList = "default_list";
            }

            if(sharedPref.contains(currList)) {
                String cartString = sharedPref.getString(currList, "");
                try {
                    //JSONObject cartJSON = new JSONObject(cartString);

                    List<Product> updatedList = new JSONParser().parseCart(cartString);
                    //totalPrice = cartJSON.getDouble("total_price");

                    cartItems = updatedList;
                    adapter.updateProductList(updatedList);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else {
                String request = new RequestBuilder().buildGetListReq(email, currList);

                try {
                    String jsonResponse = new SendRequest(getActivity()).execute(request).get();
                    String stat = new JSONObject(jsonResponse).getString("status");

                    if(stat.equals("success")) {
                        List<Product> updatedList = new JSONParser().parseCart(jsonResponse);
                        cartItems = updatedList;
                        adapter.updateProductList(updatedList);

                        sharedPref.put(currList, jsonResponse);
                    }
                    else {
                        //put toast here
                    }
                } catch (Exception e) {
                    Toast.makeText(getActivity(), "Something Went Wrong", Toast.LENGTH_SHORT).show();
                }
            }

            if (cartItems != null) {
                adapter.updateProductList(cartItems);

                list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Product selected = cartItems.get(position);

                        ShopListHandler listHandler = new ShopListHandler(getActivity(), currList);
                        List<Product> updatedList = listHandler.deleteFromList(selected);

                        adapter.updateProductList(updatedList);
                        cartItems = updatedList;
                        total.setText("Total: " + Double.toString(adapter.getTotalPrc()));
                    }
                });
            }
        }
        else {
            listNameSpin.setVisibility(View.INVISIBLE);
            modList.setEnabled(false);

            try {
                String defaultVal = "";
                final String cartString = sharedPref.getString("default_list", defaultVal);
                //JSONObject cartJSON = new JSONObject(cartString);

                cartItems = new JSONParser().parseCart(cartString);
                //totalPrice = cartJSON.getDouble("total_price");
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (cartItems != null) {
                adapter.updateProductList(cartItems);

                list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Product selected = cartItems.get(position);

                        ShopListHandler listHandler = new ShopListHandler(getActivity(), "default_list");
                        List<Product> updatedList = listHandler.deleteFromList(selected);

                        adapter.updateProductList(updatedList);
                        cartItems = updatedList;
                        total.setText("Total: " + Double.toString(adapter.getTotalPrc()));
                    }
                });
            }
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
