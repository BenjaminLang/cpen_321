package androidapp.smartshopper.smartshopper;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class ResultFragment extends ListFragment {
    private final String ARG_KEY = "json_response";

    //private ListView listView;
    private String jsonResp;

    public ResultFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        Bundle bundle = this.getArguments();
        if(bundle != null) {
            jsonResp = bundle.getString(ARG_KEY);
        }

        JsonParser parser = new JsonParser();
        List<Product> result = parser.parseJSON(jsonResp);
        ProductAdapter adapter = new ProductAdapter(getActivity(), R.layout.search_result, result);;
        this.setListAdapter(adapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_result, container, false);
    }

}
