package androidapp.smartshopper.smartshopper;


import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;


/**
 * A simple {@link Fragment} subclass.
 */
public class AccCreateFragment extends Fragment {
    private Context context;
    private EditText idField;
    private EditText emailField;
    private EditText passField;
    private EditText passConfirmField;
    private Button create;

    public AccCreateFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //fill view with that defined in xml
        View view = inflater.inflate(R.layout.fragment_acc_create, container, false);

        //associate UI elements defined in xml with Java objects
        idField = (EditText) view.findViewById(R.id.id);
        emailField = (EditText) view.findViewById(R.id.new_email);
        passField = (EditText) view.findViewById(R.id.pw);
        passConfirmField = (EditText) view.findViewById(R.id.new_pw_confirm);
        create = (Button) view.findViewById(R.id.create_button);

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newId = idField.getText().toString();    //get user name
                String newEmail = emailField.getText().toString();      //get email
                String newPw = passField.getText().toString();      //get password
                String pwConf = passConfirmField.getText().toString();      //get password confirmation

                //check if password and password confirmation are the same
                if(newPw.equals(pwConf)) {
                    //build account creation request
                    String accCreateReq = new RequestBuilder().buildAccountCreateReq(newId, newEmail, newPw);
                    try {
                        //send request and get response
                        String jsonResponse = new SendAccCreateRequest().execute(accCreateReq).get();

                        //parse response json and obtain status field
                        JSONObject respJSON = new JSONObject(jsonResponse);
                        String created = respJSON.getString("status");

                        //check if account creation has succeeded and print corresponding message
                        if(created.equals("failed"))
                            Toast.makeText(getActivity(), "Account Already Exists", Toast.LENGTH_SHORT).show();
                        else {
                            Toast.makeText(getActivity(), "Accoutn Has Been Created", Toast.LENGTH_SHORT).show();
                            //close fragment
                            getActivity().onBackPressed();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(getActivity(), "Something Went Wrong", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                else {
                    Toast.makeText(getActivity(), "Password Confirmation is Wrong", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    private class SendAccCreateRequest extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... request) {
            SmartShopClient client = new SmartShopClient(getActivity());
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
