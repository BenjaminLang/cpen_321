package androidapp.smartshopper.smartshopper;

import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class RoutingActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private List<LatLng> storeCoords = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routing);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Bundle bundle = getIntent().getExtras();
        String stores = "";
        if(bundle != null)
            stores = bundle.getString("stores");

        List<String> storeList = new ArrayList<String>();
        try {
            JSONArray storesJSON = new JSONArray(stores);
            for(int i = 0; i < storesJSON.length(); i++) {
                storeList.add(storesJSON.getString(i));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        ListView storeListView = (ListView) findViewById(R.id.store_list);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, storeList);
        storeListView.setAdapter(adapter);
    }
    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        } else {
            requestPermission();
        }

        provideAutocomplete();
    }

    public void onSearch(View view){
        mMap.clear();
        LatLng coords = storeCoords.get(0);
        mMap.addMarker(new MarkerOptions().position(coords).title("Destination"));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(coords, 13.0f));
    }

    private void provideAutocomplete(){
        //enables the google placce autocompletion
        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment) getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                storeCoords.add(place.getLatLng());
            }

            @Override
            public void onError(Status status) {
                //Handle error
            }
        });
    }

    private void requestPermission(){
        if(ActivityCompat.shouldShowRequestPermissionRationale(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)){
            new requestPermissionAsync().execute();
        }
        ActivityCompat.requestPermissions(this, new String[] {android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
    }

    class requestPermissionAsync extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Void[] noparams){
            new AlertDialog.Builder(getApplicationContext())
                    .setTitle("Location Request")
                    .setMessage("Uses current location to route you to local stores")
                    .setNegativeButton(android.R.string.ok,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //do nothing
                                }
                            })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
            return true;
        }
        @Override
        protected void onPostExecute(Boolean result){
            //do nothing
        }
    }
}
