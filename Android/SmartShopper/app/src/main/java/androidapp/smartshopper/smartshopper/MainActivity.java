package androidapp.smartshopper.smartshopper;

import android.app.SearchManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.SharedPreferencesCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private Boolean loggedIn;
    private TextView recommendTitle;
    private SharedPrefSingle sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Initializing toolbar and set the title
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("CheckedOut");
        setSupportActionBar(toolbar);

        //initialize floating button on bottom right
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //initialize shopping list fragment
                ShoppingListFragment cartFrag = new ShoppingListFragment();

                //launch shopping list fragment and adding it to the back stack
                FragmentManager fragMan = getSupportFragmentManager();
                fragMan.beginTransaction()
                        .replace(R.id.result_frame, cartFrag)
                        .addToBackStack("cart_list")
                        .setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right, android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                        .commit();

                //set toolbar title to indicate user in shopping list
                toolbar.setTitle("Shopping List");
            }
        });

        //get current login status
        sharedPref = SharedPrefSingle.getInstance(MainActivity.this);
        boolean defaultVal = false;
        loggedIn = sharedPref.getBoolean(SharedPrefSingle.prefKey.LOGIN_STAT, defaultVal);

        recommendTitle = (TextView) findViewById(R.id.recommend_title);
        getRecommend();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        final Menu myMenu = menu;

        //fill toolbar with menu items such as search, etc.
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);

        //setup search view
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));

        //search view query listener
        searchView.setOnQueryTextListener(
                new SearchView.OnQueryTextListener() {
                    //user confirms the search
                    @Override
                    public boolean onQueryTextSubmit(String s) {
                        //get search options from shared preference
                        String sortDefault = "min";
                        int numDefault = -1;
                        String sortOpt = sharedPref.getString(SharedPrefSingle.prefKey.SORT_OPT, sortDefault);   //get sorting option
                        String numOpt = Integer.toString(sharedPref.getInt(SharedPrefSingle.prefKey.NUM_ITEM, numDefault));  //get number of items to display
                        String prc_max = sharedPref.getString(SharedPrefSingle.prefKey.CURR_MAX, "");
                        String prc_min = sharedPref.getString(SharedPrefSingle.prefKey.CURR_MIN, "");
                        String store_opt = sharedPref.getString(SharedPrefSingle.prefKey.STORE_OPT, new JSONArray().toString());

                        //set search options using options obtained above
                        SearchOptions options = new SearchOptions(store_opt, sortOpt, prc_min, prc_max, numOpt);

                        //build search request
                        RequestBuilder rb = new RequestBuilder();

                        boolean defaultVal = false;
                        loggedIn = sharedPref.getBoolean(SharedPrefSingle.prefKey.LOGIN_STAT, defaultVal);
                        String readRequest = "";
                        if(loggedIn) {
                            final String email = sharedPref.getString(SharedPrefSingle.prefKey.CURR_EMAIL, "");
                            readRequest = rb.buildReadReq(s, email, options);
                        }
                        else {
                            readRequest = rb.buildReadReq(s, "", options);
                        }

                        try {
                            //obtain response from request sent
                            String jsonResponse = new SendRequest(MainActivity.this).execute(readRequest).get();

                            //put response string in bundle and pass on to fragment as argument
                            Bundle bundle = new Bundle();
                            bundle.putString("json_response", jsonResponse);
                            ResultFragment newResFrag = new ResultFragment();
                            newResFrag.setArguments(bundle);

                            //launch search results fragment and push to back stack
                            FragmentManager fragMan = getSupportFragmentManager();
                            fragMan.beginTransaction()
                                    .replace(R.id.result_frame, newResFrag)
                                    .addToBackStack("search_result")
                                    .setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right, android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                                    .commit();

                            //set toolbar title to item that user searched for
                            toolbar.setTitle(s);
                            //close search view
                            (myMenu.findItem(R.id.search)).collapseActionView();
                        } catch(Exception e){
                            e.printStackTrace();
                        }

                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String s) {
                        //Do nothing
                        return false;
                    }
                }
        );

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //Check which menu item in the toolbar has been selected
        switch (item.getItemId()) {
            case R.id.login:
                boolean defaultVal = false;
                loggedIn = sharedPref.getBoolean(SharedPrefSingle.prefKey.LOGIN_STAT, defaultVal);

                if(!loggedIn) {
                    //launch login fragment if not logged in
                    LoginFragment loginFrag = new LoginFragment();

                    FragmentManager fragMan = getSupportFragmentManager();
                    fragMan.beginTransaction()
                            .replace(R.id.result_frame, loginFrag)
                            .addToBackStack("login")
                            .setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right, android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                            .commit();
                    return true;
                }
                else {
                    //launch post login fragment(allows for logout and account modifications) if there is a user logged in
                    PostLoginFragment postLoginFrag = new PostLoginFragment();

                    FragmentManager fragMan = getSupportFragmentManager();
                    fragMan.beginTransaction()
                            .replace(R.id.result_frame, postLoginFrag)
                            .addToBackStack("post_login")
                            .setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right, android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                            .commit();
                    return true;
                }
            case R.id.search_filter:
                //bring up search options dialog
                FragmentManager fm = getSupportFragmentManager();
                SearchFilterFragment editNameDialog = new SearchFilterFragment();
                editNameDialog.show(fm, "fragment_edit_name");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        FragmentManager fragMan = getSupportFragmentManager();
        //check if there is still fragment in backstack
        if(fragMan.getBackStackEntryCount() > 1) {
            fragMan.popBackStack();
        }
        else if(fragMan.getBackStackEntryCount() == 1) {
            getRecommend();
            fragMan.popBackStack();
        }
        else {
            super.onBackPressed();
        }
    }

    /*
    Get user recommendations and display it
     */
    private void getRecommend() {
        //get login status
        boolean defaultVal = false;
        loggedIn = sharedPref.getBoolean(SharedPrefSingle.prefKey.LOGIN_STAT, defaultVal);

        //if user has logged in
        if(loggedIn) {
            //set title and get email
            recommendTitle.setText("Here are Your Recommended Products:");
            String currUser = sharedPref.getString(SharedPrefSingle.prefKey.CURR_EMAIL, "");

            //build get recommendation request
            String request = new RequestBuilder().buildGetRecommend(currUser);

            try {
                //send request and get response
                String response = new SendRequest(MainActivity.this).execute(request).get();
                JSONObject respJSON = new JSONObject(response);
                String status = respJSON.getString("status");

                if(status.equals("success")) {
                    //if response indicates success, parse the response for recommendation list
                    final List<Product> recommend = new JSONParser().parseRecommend(response);

                    //set adapter to be recommendations list and display in list view
                    ProductAdapter adapter = new ProductAdapter(MainActivity.this, R.layout.search_result, recommend);
                    ListView recommendList = (ListView) findViewById(R.id.recommend_list);
                    recommendList.setAdapter(adapter);

                    //launch product detail fragment when a product from the list is selected
                    recommendList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Product selected = recommend.get(position);
                            String productJSON = selected.toJSON();

                            Bundle bundle = new Bundle();
                            bundle.putString("product_json", productJSON);
                            DetailFragment newDetailFrag = new DetailFragment();
                            newDetailFrag.setArguments(bundle);

                            FragmentManager fragMan = getSupportFragmentManager();
                            fragMan.beginTransaction()
                                    .replace(R.id.result_frame, newDetailFrag)
                                    .addToBackStack("product_detail")
                                    .setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right, android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                                    .commit();
                        }
                    });
                }
                else {
                    Toast.makeText(MainActivity.this, "Failed to Retrieve Recommendations", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else {
            recommendTitle.setText("Please Create an Account/Login to Receive Product Recommendations!");
        }
    }
}
