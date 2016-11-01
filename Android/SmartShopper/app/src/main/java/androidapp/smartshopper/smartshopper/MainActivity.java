package androidapp.smartshopper.smartshopper;

import android.app.SearchManager;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        listView = (ListView) findViewById(R.id.search_result_list);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Shopping List Here", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
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
                        List<String> searchTerm = new ArrayList<String>();
                        searchTerm.add(s);
                        String[] searchArray = searchTerm.toArray(new String[0]);

                        SearchOptions emptyOpt = new SearchOptions("", "");
                        SearchOptions[] dummy = {emptyOpt};

                        RequestBuilder rb = new RequestBuilder();
                        //String dbRequest = rb.buildReadReq(searchArray, dummy);

                        String jsonResponse;
                        try {
                            jsonResponse = new SendSearchRequest().execute(s).get();
                            Toast.makeText(MainActivity.this, jsonResponse, Toast.LENGTH_LONG).show();

                            JsonParser parser = new JsonParser();
                            //List<Product> result = parser.parseJSON(jsonResponse);

                            List<Product> result = new ArrayList<Product>(Arrays.asList(
                                    new Product("apple", "1.99", "somewhere", "asdf"),
                                    new Product("orange", "2.99", "somewhere", "asdf"),
                                    new Product("lemons", "3.99", "somewhere", "asdf"),
                                    new Product("chocolate milk", "4.99", "somewhere", "asdf"),
                                    new Product("strawberries", "2.50", "somewhere", "asdf")
                            ));

                            ProductAdapter adapter = new ProductAdapter(MainActivity.this, R.layout.search_result, (ArrayList)result);
                            listView.setAdapter(adapter);
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

    private class SendSearchRequest extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... request) {
            //SmartShopClient client = new SmartShopClient();
            //client.sendRequest(request[0]);
            return request[0];
        }

        @Override
        protected void onPostExecute(String request) {
            super.onPostExecute(request);
        }
    }
}
