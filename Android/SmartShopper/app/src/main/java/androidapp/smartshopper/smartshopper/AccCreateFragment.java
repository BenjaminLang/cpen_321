package androidapp.smartshopper.smartshopper;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
public class AccCreateFragment extends Fragment {
    private Context context;
    private EditText idField;
    private EditText passField;
    private EditText passConfirmField;
    private Button create;

    private String newId;
    private String newPw;
    private String pwConf;

    public AccCreateFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_acc_create, container, false);

        idField = (EditText) view.findViewById(R.id.new_id);
        passField = (EditText) view.findViewById(R.id.new_pw);
        passConfirmField = (EditText) view.findViewById(R.id.new_pw_confirm);
        create = (Button) view.findViewById(R.id.create_button);

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newId = idField.getText().toString();
                newPw = passField.getText().toString();
                pwConf = passConfirmField.getText().toString();

                if(newPw.equals(pwConf)) {
                    String accCreateReq = new RequestBuilder().buildAccountCreatReq(newId, newPw);
                    Toast.makeText(getActivity(), accCreateReq, Toast.LENGTH_SHORT).show();
                    getActivity().onBackPressed();
                }
                else {
                    Toast.makeText(getActivity(), "Password Confirmation is Wrong", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

}
