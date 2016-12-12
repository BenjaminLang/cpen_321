package androidapp.smartshopper.smartshopper;

import android.content.Context;
import android.os.AsyncTask;

/**
 * Created by JohnS on 2016-12-11.
 */

class SendRequest extends AsyncTask<String, Void, String> {
    private Context context;

    public SendRequest (Context context){
        this.context = context;
    }
    @Override
    protected String doInBackground(String... request) {
        SmartShopClient client = new SmartShopClient(context);
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
