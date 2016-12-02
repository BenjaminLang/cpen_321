package androidapp.smartshopper.smartshopper;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;


/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment {
    private Context context;
    private EditText idField;
    private EditText passField;
    private Button login;

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.context = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        idField = (EditText) view.findViewById(R.id.id);
        passField = (EditText) view.findViewById(R.id.pw);
        login = (Button) view.findViewById(R.id.login_button);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = idField.getText().toString();
                String pw = passField.getText().toString();
                String loginReq = new RequestBuilder().buildLoginReq(id, pw);
                //loginReq = new RequestBuilder().modAccReq(id, pw);
                try {
                    String jsonResponse = new SendLoginRequest().execute(loginReq).get();

                    JSONObject respJSON = new JSONObject(jsonResponse);
                    String status = respJSON.getString("status");

                    if(status.equals("DNE"))
                        Toast.makeText(getActivity(), "Wrong Username", Toast.LENGTH_SHORT).show();
                    else if(status.equals("failed"))
                        Toast.makeText(getActivity(), "Wrong Password", Toast.LENGTH_SHORT).show();
                    else if(status.equals("exception"))
                        Toast.makeText(getActivity(), "Kooner's Fault", Toast.LENGTH_SHORT).show();
                    else {
                        Toast.makeText(getActivity(), "Login Successful", Toast.LENGTH_SHORT).show();
                        SharedPreferences.Editor editor = getActivity().getPreferences(Context.MODE_PRIVATE).edit();
                        editor.putString(getString(R.string.curr_user), id);
                        editor.putBoolean(getString(R.string.login_stat), true);
                        editor.commit();
                        getActivity().onBackPressed();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "Something Went Wrong", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });

        TextView accCreate = (TextView) view.findViewById(R.id.createAcc);
        accCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AccCreateFragment createAccFrag = new AccCreateFragment();

                FragmentManager fragMan = getFragmentManager();
                fragMan.beginTransaction()
                        .replace(R.id.result_frame, createAccFrag)
                        .addToBackStack("account_creation")
                        .setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right, android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                        .commit();
            }
        });

        return view;
    }

    private class SendLoginRequest extends AsyncTask<String, Void, String> {
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
