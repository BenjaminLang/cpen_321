package androidapp.smartshopper.smartshopper;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;


/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFilterFragment extends DialogFragment {
    private final String[] sortOpt = new String[] {"Lowest to Highest", "Highest to Lowest"};
    private final String[] sortActual = new String[] {"min", "max"};
    private final String[] numItemOpt = new String[] {"No Restriction", "10", "20", "30", "40", "50", "60", "70", "80", "90", "100"};
    private final int[] numItemActual = new int[] {-1, 10, 20, 30, 40, 50, 60, 70, 80, 90, 100};

    private String sortSelected = "min";
    private int numSelected = -1;

    public SearchFilterFragment() {
        // Required empty public constructor
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_search_filter, null);
        builder.setTitle("Search Filter");

        //Initialize dropdown menu for the sorting options
        Spinner sortSpin = (Spinner) view.findViewById(R.id.sort_spinner);
        ArrayAdapter<String> sortAdpt = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, sortOpt);
        sortAdpt.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sortSpin.setAdapter(sortAdpt);

        //Initialize dropdown menu for the number of results to be displayed
        Spinner numItemSpin = (Spinner) view.findViewById(R.id.num_spinner);
        ArrayAdapter<String> numItemAdpt = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, numItemOpt);
        numItemAdpt.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        numItemSpin.setAdapter(numItemAdpt);

        //get the current positions of the dropdown menus
        final SharedPrefSingle sharedPref = SharedPrefSingle.getInstance(getActivity());
        int sortDefault = 0;
        int numDefault = 0;
        int sortSpinPos = sharedPref.getInt(SharedPrefSingle.prefKey.SORT_SPIN_POS, sortDefault);
        int numSpinPos = sharedPref.getInt(SharedPrefSingle.prefKey.NUM_SPIN_POS, numDefault);

        //set the current position
        sortSpin.setSelection(sortSpinPos);
        numItemSpin.setSelection(numSpinPos);

        sortSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                sortSelected = sortActual[position];
                sharedPref.put(SharedPrefSingle.prefKey.SORT_SPIN_POS, position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                return;
            }
        });

        numItemSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                numSelected = numItemActual[position];
                sharedPref.put(SharedPrefSingle.prefKey.NUM_SPIN_POS, position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                return;
            }
        });

        //Get checkboxes
        final CheckBox walmart = (CheckBox) view.findViewById(R.id.walmart_chk);
        final CheckBox costco = (CheckBox) view.findViewById(R.id.costco_chk);
        final CheckBox superstore = (CheckBox) view.findViewById(R.id.superstore_chk);

        //get current checked status for all checkboxes
        boolean walmart_stat = sharedPref.getBoolean(SharedPrefSingle.prefKey.WALMART_STAT, true);
        final boolean costco_stat = sharedPref.getBoolean(SharedPrefSingle.prefKey.COSTCO_STAT, true);
        boolean superstore_stat = sharedPref.getBoolean(SharedPrefSingle.prefKey.SUPERSTORE_STAT, true);

        //set checked status
        walmart.setChecked(walmart_stat);
        costco.setChecked(costco_stat);
        superstore.setChecked(superstore_stat);

        //Java reference to text fields for max and min price
        final EditText max_prc = (EditText) view.findViewById(R.id.max_prc_field);
        final EditText min_prc = (EditText) view.findViewById(R.id.min_prc_field);

        //get the past selected price
        String curr_max = sharedPref.getString(SharedPrefSingle.prefKey.CURR_MAX, "");
        String curr_min = sharedPref.getString(SharedPrefSingle.prefKey.CURR_MIN, "");

        //set past selected price as current
        max_prc.setText(curr_max);
        min_prc.setText(curr_min);

        builder.setPositiveButton("Apply", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                //get max and min price as text
                String new_max = max_prc.getText().toString();
                String new_min = min_prc.getText().toString();

                //set max and min price string empty status
                boolean max_empty = (new_max.isEmpty() || new_max == null);
                boolean min_empty = (new_min.isEmpty() || new_min == null);

                //put the correct data into the correct field or display message
                //corresponding to the data the user entered
                if(max_empty && min_empty) {
                    sharedPref.put(SharedPrefSingle.prefKey.CURR_MAX, "");
                    sharedPref.put(SharedPrefSingle.prefKey.CURR_MIN, "");
                }
                else if(max_empty && !min_empty) {
                    sharedPref.put(SharedPrefSingle.prefKey.CURR_MAX, "");
                    sharedPref.put(SharedPrefSingle.prefKey.CURR_MIN, new_min);
                }
                else if (!max_empty && min_empty) {
                    sharedPref.put(SharedPrefSingle.prefKey.CURR_MAX, new_max);
                    sharedPref.put(SharedPrefSingle.prefKey.CURR_MIN, "");
                }
                else {
                    if(Double.parseDouble(new_max) < Double.parseDouble(new_min)) {
                        Toast.makeText(getActivity(), "Max and Min not Saved (Max is Smaller Than Min)", Toast.LENGTH_SHORT).show();
                    }
                    else if(Double.parseDouble(new_max) == Double.parseDouble(new_min)) {
                        Toast.makeText(getActivity(), "Max and Min not Saved (Max is the Same as Min)", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        sharedPref.put(SharedPrefSingle.prefKey.CURR_MAX, new_max);
                        sharedPref.put(SharedPrefSingle.prefKey.CURR_MIN, new_min);
                    }
                }

                //create a list of stores checked
                List<String> stores = new ArrayList<String>();
                if(walmart.isChecked())
                    stores.add(walmart.getText().toString());
                if(costco.isChecked())
                    stores.add(costco.getText().toString());
                if(superstore.isChecked())
                    stores.add(superstore.getText().toString());

                //store the checked status of the stores
                sharedPref.put(SharedPrefSingle.prefKey.WALMART_STAT, walmart.isChecked());
                sharedPref.put(SharedPrefSingle.prefKey.COSTCO_STAT, costco.isChecked());
                sharedPref.put(SharedPrefSingle.prefKey.SUPERSTORE_STAT, superstore.isChecked());

                //store the modified options
                sharedPref.put(SharedPrefSingle.prefKey.STORE_OPT, storesToJSON(stores));
                sharedPref.put(SharedPrefSingle.prefKey.SORT_OPT, sortSelected);
                sharedPref.put(SharedPrefSingle.prefKey.NUM_ITEM, numSelected);
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
            }
        });

        builder.setView(view);
        return builder.create();
    }

    /*
    Takes a list of store names and converts it into a JSON array string
     */
    private String storesToJSON (List<String> stores) {
        JSONArray storeJSON = new JSONArray();

        if(stores.isEmpty())
            return storeJSON.toString();

        try {
            for(String curr : stores) {
                storeJSON.put(curr);
            }
            return storeJSON.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return storeJSON.toString();
        }
    }
}
