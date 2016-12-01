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
import android.widget.Spinner;

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

        Spinner sortSpin = (Spinner) view.findViewById(R.id.sort_spinner);
        ArrayAdapter<String> sortAdpt = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, sortOpt);
        sortAdpt.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sortSpin.setAdapter(sortAdpt);

        Spinner numItemSpin = (Spinner) view.findViewById(R.id.num_spinner);
        ArrayAdapter<String> numItemAdpt = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, numItemOpt);
        numItemAdpt.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        numItemSpin.setAdapter(numItemAdpt);

        final SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPref.edit();
        int sortDefault = 0;
        int numDefault = 0;
        int sortSpinPos = sharedPref.getInt(getString(R.string.sort_spin_pos), sortDefault);
        int numSpinPos = sharedPref.getInt(getString(R.string.num_spin_pos), numDefault);

        sortSpin.setSelection(sortSpinPos);
        numItemSpin.setSelection(numSpinPos);

        sortSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                sortSelected = sortActual[position];
                editor.putInt(getString(R.string.sort_spin_pos), position);
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
                editor.putInt(getString(R.string.num_spin_pos), position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                return;
            }
        });

        builder.setPositiveButton("Apply", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                editor.putString(getString(R.string.sort_opt), sortSelected);
                editor.putInt(getString(R.string.num_item), numSelected);
                editor.commit();
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

}
