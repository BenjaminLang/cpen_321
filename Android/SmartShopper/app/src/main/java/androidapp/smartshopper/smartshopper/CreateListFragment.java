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

        //inflate save list dialog UI
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_create_list, null);
        builder.setTitle("Create New Shopping List");

        //get Java reference to list name field
        final EditText newListName = (EditText) view.findViewById(R.id.new_list_name);

        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                //get the name
                String listName = newListName.getText().toString();

                //get the items stored in the default offline list
                SharedPrefSingle sharedPref = SharedPrefSingle.getInstance(getActivity());
                String listJSON =  sharedPref.getString("default_list", "");

                //create empty JSON array string if the list is empty
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
                //create JSON array from cart string otherwise
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

                //get the current emailed logged into and build request with list name and actual list
                String email = sharedPref.getString(SharedPrefSingle.prefKey.CURR_EMAIL, "");
                request = new RequestBuilder().buildAddListReq(email, listName, listJSON);
                System.out.println(request);

                //put the saved list into a key with the name just created
                //and empty out default offline list
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
        //execute the request created above
        SendRequest reqSender = new SendRequest(getActivity());
        try {
            String resp = reqSender.execute(request).get();
            JSONObject respJSON = new JSONObject(resp);
            String status = respJSON.getString("status");

            if(status.equals("success"))
                Toast.makeText(getActivity(), "List Saved, Please Restart Lists for Updates!", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(getActivity(), "List Hasn't Been Saved!", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getActivity(), "Exception!", Toast.LENGTH_SHORT).show();
            return;
        }
    }
}
