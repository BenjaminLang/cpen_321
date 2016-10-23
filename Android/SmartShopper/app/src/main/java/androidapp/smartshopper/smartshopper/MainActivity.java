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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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

                        SearchOptions emptyOpt = new SearchOptions("", "");
                        SearchOptions[] dummy1 = {emptyOpt};
                        String[] dummy2 = {""};

                        RequestBuilder rb = new RequestBuilder();
                        String dbRequest = rb.buildReadReq(searchTerm, dummy1, dummy2);

                        new SendSearchRequest().execute(dbRequest);

                        //Toast.makeText(MainActivity.this, s, Toast.LENGTH_LONG).show();
                        //Toast.makeText(MainActivity.this, dbRequest, Toast.LENGTH_LONG).show();

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
            Toast.makeText(MainActivity.this, request, Toast.LENGTH_LONG).show();
            return;
        }
    }
}
