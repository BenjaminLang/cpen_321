package androidapp.smartshopper.smartshopper;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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

        //get the current selected list, email, and login status
        currList = sharedPref.getString(SharedPrefSingle.prefKey.CURR_LIST, "default_list");
        email = sharedPref.getString(SharedPrefSingle.prefKey.CURR_EMAIL, "");
        loggedIn = sharedPref.getBoolean(SharedPrefSingle.prefKey.LOGIN_STAT, false);

        if(loggedIn) {
            try {
                //build get list request
                String getListsReq = new RequestBuilder().buildGetListNamesJSON(email);
                String getAllListResp = new SendRequest(getActivity()).execute(getListsReq).get();

                JSONObject respJSON = new JSONObject(getAllListResp);
                String stat = respJSON.getString("status");

                //if the all list names retrieval is successful parse it into a string array
                if(stat.equals("success")) {
                    List<String> listNames = new JSONParser().parseListNames(getAllListResp);
                    listNames.add("Add New List...");
                    listNameOpts = listNames.toArray(new String[0]);
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
        View view = inflater.inflate(R.layout.fragment_shopping_list, container, false);

        //set the adapter of the list view wtih an emtpy array list
        adapter = new ProductAdapter(this.context, R.layout.search_result, new ArrayList<Product>());
        ListView list = (ListView) view.findViewById(R.id.cart_list);
        list.setAdapter(adapter);

        final TextView total = (TextView) view.findViewById(R.id.cart_summary);

        //Initialize the list dropdown with string array containing list names
        final Spinner listNameSpin = (Spinner) view.findViewById(R.id.all_usr_list);
        ArrayAdapter<String> listNamesAdpt = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, listNameOpts);
        listNameSpin.setAdapter(listNamesAdpt);
        //set the position of the dropdown to be on the current selected list
        if(currList.equals("default_list"))
            listNameSpin.setSelection(listNamesAdpt.getPosition("Add New List..."));
        else
            listNameSpin.setSelection(listNamesAdpt.getPosition(currList));

        //initialize multi functional button and set as "save" or "update" depending on list selected
        final Button modList = (Button) view.findViewById(R.id.new_list_button);
        if(currList.equals("default_list"))
            modList.setText("Save List");
        else
            modList.setText("Update List");

        listNameSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                //get the selected list
                String listSelected = listNameOpts[position];
                //if selected "Add New List..." option, access default offline list
                if(listSelected.equals("Add New List...")) {
                    modList.setText("Save List");

                    //set currently selected list
                    currList = "default_list";
                    sharedPref.put(SharedPrefSingle.prefKey.CURR_LIST, currList);

                    //get the list and display it
                    String cartString = sharedPref.getString(currList, "");
                    try {
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

                    //set currently selected list
                    currList = listSelected;
                    sharedPref.put(SharedPrefSingle.prefKey.CURR_LIST, listSelected);

                    if(sharedPref.contains(listSelected)) {
                        //if the list already exists within local storage, load it locally and display it
                        String cartString = sharedPref.getString(listSelected, "");
                        try {
                            List<Product> updatedList = new JSONParser().parseCart(cartString);

                            adapter.updateProductList(updatedList);
                            total.setText("Total: " + Double.toString(adapter.getTotalPrc()));
                            cartItems = updatedList;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    else {
                        //if list doesn't exist in local storage, retrieve it from the server and display it
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
                if(currList.equals("default_list")) {
                    //if the list selected is the default offline list, launch the create new list dialog
                    FragmentManager fm = getFragmentManager();
                    CreateListFragment newListDialog = new CreateListFragment();
                    newListDialog.show(fm, "fragment_new_list");
                }
                else {
                    //if list is a list stored online, update it in the background

                    //get list string
                    String listJSON =  sharedPref.getString(currList, "");
                    String listJSONFinal = "";

                    if(listJSON.equals("")) {
                        //if list is empty create empty JSON array
                        try {
                            JSONObject newListJSON = new JSONObject();
                            JSONArray emptyArray = new JSONArray();
                            newListJSON.put("list", emptyArray);

                            listJSONFinal = newListJSON.toString();
                        } catch (Exception e) {
                            //put toast
                        }
                    }
                    else {
                        //turn list into JSON object
                        try {
                            JSONObject currListJSON = new JSONObject(listJSON);
                            JSONArray listArray = currListJSON.getJSONArray("list");

                            JSONObject newListJSON = new JSONObject();
                            newListJSON.put("list", listArray);

                            listJSONFinal = newListJSON.toString();
                        } catch (Exception e) {
                            //put toast
                        }
                    }

                    //create update list request
                    String updateReq = new RequestBuilder().buildAddListReq(email, currList, listJSONFinal);
                    System.out.println(updateReq);

                    try {
                        //send request and retrieve response
                        String updateResp = new SendRequest(getActivity()).execute(updateReq).get();
                        JSONObject updateRespJSON = new JSONObject(updateResp);

                        //get response status and display toast message corresponding to it
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
            if(currList.isEmpty() || currList == null || currList.equals("")) {
                //default to default offline list if there is no current list selection
                currList = "default_list";
            }

            if(sharedPref.contains(currList)) {
                //if the list already exists within local storage, load it locally and display it
                String cartString = sharedPref.getString(currList, "");
                try {
                    List<Product> updatedList = new JSONParser().parseCart(cartString);

                    cartItems = updatedList;
                    adapter.updateProductList(updatedList);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else {
                //if list doesn't exist in local storage, retrieve it from the server and display it
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
                        //find item clicked on and delete the item from the list
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
            //if the user hasn't logged in, disable multiple list feature
            listNameSpin.setVisibility(View.INVISIBLE);
            modList.setEnabled(false);

            //load default offline list and display it
            try {
                String defaultVal = "";
                final String cartString = sharedPref.getString("default_list", defaultVal);

                cartItems = new JSONParser().parseCart(cartString);
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (cartItems != null) {
                //find item clicked on and delete the item from the list
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

        Button getRoute = (Button) view.findViewById(R.id.get_route);

        getRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), RoutingActivity.class);
                //get list of stores user needs to go to
                String storeList = adapter.getStores();
                Bundle bundle = new Bundle();
                bundle.putString("stores", storeList);
                //start routing activity
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        return view;
    }
}
