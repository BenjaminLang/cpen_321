package androidapp.smartshopper.smartshopper;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;


/**
 * A simple {@link Fragment} subclass.
 */
public class CreateListFragment extends DialogFragment {
    private String request = "";

    public CreateListFragment() {
        // Required empty public constructor
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_create_list, null);
        builder.setTitle("Create New Shopping List");

        final EditText newListName = (EditText) view.findViewById(R.id.new_list_name);

        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                String listName = newListName.getText().toString();

                SharedPrefSingle sharedPref = SharedPrefSingle.getInstance(getActivity());
                String listJSON =  sharedPref.getString("default_list", "");

                if(listJSON.equals("")) {
                    try {
                        JSONObject newListJSON = new JSONObject();
                        JSONArray emptyArray = new JSONArray();
                        newListJSON.put("list", emptyArray);

                        listJSON = newListJSON.toString(2);
                    } catch (Exception e) {
                        //put toast
                    }
                }
                else {
                    try {
                        JSONObject currListJSON = new JSONObject(listJSON);
                        JSONArray listArray = currListJSON.getJSONArray("list");

                        JSONObject newListJSON = new JSONObject();
                        newListJSON.put("list", listArray);

                        listJSON = newListJSON.toString(2);
                    } catch (Exception e) {
                        //put toast
                    }
                }

                String email = sharedPref.getString(getString(R.string.curr_user), "");
                request = new RequestBuilder().buildAddListReq(email, listName, listJSON);

                sharedPref.put(listName, listJSON);
                sharedPref.put("default_list", "");
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

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        SendRequest reqSender = new SendRequest(getActivity());
        reqSender.execute(request);
    }
}
