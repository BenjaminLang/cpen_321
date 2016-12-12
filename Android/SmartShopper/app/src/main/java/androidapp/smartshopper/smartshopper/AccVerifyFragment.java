package androidapp.smartshopper.smartshopper;


import android.content.Context;
import android.content.SharedPreferences;
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
public class AccVerifyFragment extends Fragment {


    public AccVerifyFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_acc_verify, container, false);

        final EditText emailField = (EditText) view.findViewById(R.id.email_to_ver);
        final EditText codeField = (EditText) view.findViewById(R.id.ver_code);
        final Button verify = (Button) view.findViewById(R.id.verify_button);

        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailField.getText().toString();
                String code = codeField.getText().toString();

                String req = new RequestBuilder().buildAccVerifyReq(email, code);

                try {
                    String resp = new SendRequest().execute(req).get();

                    JSONObject respJSON = new JSONObject(resp);
                    String status = respJSON.getString("status");

                    if(status.equals("DNE"))
                        Toast.makeText(getActivity(), "User Doesn't Exist", Toast.LENGTH_SHORT).show();
                    else if(status.equals("failed"))
                        Toast.makeText(getActivity(), "Verification Code Wrong", Toast.LENGTH_SHORT).show();
                    else if(status.equals("exception"))
                        Toast.makeText(getActivity(), "Server Error", Toast.LENGTH_SHORT).show();
                    else {
                        Toast.makeText(getActivity(), "Verification Successful", Toast.LENGTH_SHORT).show();
                        getActivity().onBackPressed();
                    }
                } catch (Exception e) {
                    //put toast
                }
            }
        });

        return view;
    }

    private class SendRequest extends AsyncTask<String, Void, String> {
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
