package androidapp.smartshopper.smartshopper;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class ResultFragment extends ListFragment {
    private final String ARG_KEY = "json_response";

    //private ListView listView;
    private String jsonResp;
    private List<Product> result;
    private Context context;

    public ResultFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        this.context = getActivity();

        Bundle bundle = this.getArguments();
        if(bundle != null) {
            jsonResp = bundle.getString(ARG_KEY);
        }

        if(jsonResp == null) {
            Toast.makeText(getActivity(), "The Item You Searched For Doesn't Exist :(", Toast.LENGTH_LONG).show();
        }

        if(jsonResp.equals("Connection Not Established")) {
            Toast.makeText(getActivity(), jsonResp, Toast.LENGTH_LONG).show();
        }

        JSONParser parser = new JSONParser();
        this.result = parser.parseJSON(jsonResp);

        if(result.isEmpty()) {
            Toast.makeText(getActivity(), "The Item You Searched For Doesn't Exist :(", Toast.LENGTH_LONG).show();
        }

        ProductAdapter adapter = new ProductAdapter(this.context, R.layout.search_result, this.result);
        this.setListAdapter(adapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_result, container, false);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id){
        //ProductAdapter adapter = (ProductAdapter) l.getAdapter();
        Product selected = this.result.get(position);
        String productJSON = selected.toJSON();

        Bundle bundle = new Bundle();
        bundle.putString("product_json", productJSON);
        DetailFragment newDetailFrag = new DetailFragment();
        newDetailFrag.setArguments(bundle);

        FragmentManager fragMan = getFragmentManager();
        fragMan.beginTransaction()
                .replace(R.id.result_frame, newDetailFrag)
                .addToBackStack("product_detail")
                .setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right, android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                .commit();
    }
}
