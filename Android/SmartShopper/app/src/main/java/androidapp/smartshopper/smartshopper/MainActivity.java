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
import android.widget.ListView;
import android.widget.Toast;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("CheckedOut");
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShoppingListFragment cartFrag = new ShoppingListFragment();

                FragmentManager fragMan = getSupportFragmentManager();
                fragMan.beginTransaction()
                        .replace(R.id.result_frame, cartFrag)
                        .addToBackStack("cart_list")
                        .setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right, android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                        .commit();

                toolbar.setTitle("Shopping List");
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        final Menu myMenu = menu;

        getMenuInflater().inflate(R.menu.toolbar_menu, menu);

        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));

        searchView.setOnQueryTextListener(
                new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String s) {
                        final SharedPreferences sharedPref = MainActivity.this.getPreferences(Context.MODE_PRIVATE);
                        String sortDefault = "min";
                        int numDefault = -1;
                        String sortOpt = sharedPref.getString(getString(R.string.sort_opt), sortDefault);
                        String numOpt = Integer.toString(sharedPref.getInt(getString(R.string.num_item), numDefault));

                        String[] dummyArray = {" ", " "};
                        SearchOptions dummyOptions = new SearchOptions(" ", " ", " ", " ", " ", dummyArray, sortOpt, numOpt);

                        RequestBuilder rb = new RequestBuilder();
                        String dbRequest = rb.buildReadReq(s, dummyOptions, " ");

                        try {
                            String jsonResponse = new SendRequest().execute(dbRequest).get();

                            Bundle bundle = new Bundle();
                            bundle.putString("json_response", jsonResponse);
                            ResultFragment newResFrag = new ResultFragment();
                            newResFrag.setArguments(bundle);

                            FragmentManager fragMan = getSupportFragmentManager();
                            fragMan.beginTransaction()
                                    .replace(R.id.result_frame, newResFrag)
                                    .addToBackStack("search_result")
                                    .setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right, android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                                    .commit();

                            toolbar.setTitle(s);
                            (myMenu.findItem(R.id.search)).collapseActionView();
                        } catch(Exception e){
                            e.printStackTrace();
                        }

                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String s) {
                        //Do the auto-correct suggestions here
                        return false;
                    }
                }
        );

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.login:
                SharedPreferences sharedPref = MainActivity.this.getPreferences(Context.MODE_PRIVATE);
                boolean defaultVal = false;
                boolean loggedIn = sharedPref.getBoolean(getString(R.string.login_stat), defaultVal);

                if(!loggedIn) {
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
        if(fragMan.getBackStackEntryCount() > 0)
            fragMan.popBackStack();
        else
            super.onBackPressed();
    }

    private class SendRequest extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... request) {
            SmartShopClient client = new SmartShopClient(MainActivity.this);
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
