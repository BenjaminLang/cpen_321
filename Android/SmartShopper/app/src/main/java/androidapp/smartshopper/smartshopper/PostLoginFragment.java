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
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;
import org.w3c.dom.Text;


/**
 * A simple {@link Fragment} subclass.
 */
public class PostLoginFragment extends Fragment {


    public PostLoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_post_login, container, false);

        //get current user's name
        final SharedPrefSingle sharedPref = SharedPrefSingle.getInstance(getActivity());
        String defaultVal = "";
        final String currId = sharedPref.getString(SharedPrefSingle.prefKey.CURR_NAME, defaultVal);

        //set user greeting
        TextView userGreeting = (TextView) view.findViewById(R.id.curr_user);
        userGreeting.setText("Welcome, " + currId);

        //get Java reference to UI elements
        Button logout = (Button) view.findViewById(R.id.logout_button);
        final EditText currPw = (EditText) view.findViewById(R.id.curr_pw);
        final EditText newPw = (EditText) view.findViewById(R.id.new_pw);
        final EditText confirmPw = (EditText) view.findViewById(R.id.confirm_new_pw);
        Button changePw = (Button) view.findViewById(R.id.change_pw_button);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currId != "") {
                    //if there is currently a user logged in, log out
                    Toast.makeText(getActivity(), "Logged Out", Toast.LENGTH_SHORT).show();
                    sharedPref.put(SharedPrefSingle.prefKey.LOGIN_STAT, false);
                    getActivity().onBackPressed();
                }
                else {
                    Toast.makeText(getActivity(), "User ID Error", Toast.LENGTH_SHORT).show();
                }
            }
        });

        changePw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get text from input fields
                String currPwString = currPw.getText().toString();
                String newPwString = newPw.getText().toString();
                String confirmPwString = confirmPw.getText().toString();

                if(newPwString.equals(confirmPwString)) {
                    //if new password is same as password confimration, build change password request
                    String changePwReq = new RequestBuilder().buildChangePass(currId, currPwString, newPwString);

                    try {
                        //send request and get response
                        String jsonResponse = new SendRequest(getActivity()).execute(changePwReq).get();

                        //get response status
                        JSONObject respJSON = new JSONObject(jsonResponse);
                        String status = respJSON.getString("status");

                        //print toast message corresponding to response status
                        if (status.equals("DNE"))
                            Toast.makeText(getActivity(), "Wrong Username", Toast.LENGTH_SHORT).show();
                        else if (status.equals("failed"))
                            Toast.makeText(getActivity(), "Wrong Current Password", Toast.LENGTH_SHORT).show();
                        else if (status.equals("exception"))
                            Toast.makeText(getActivity(), "Kooner's Fault", Toast.LENGTH_SHORT).show();
                        else {
                            Toast.makeText(getActivity(), "Password Has Been Changed", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(getActivity(), "Something Went Wrong", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
            }
        });

        return view;
    }
}
