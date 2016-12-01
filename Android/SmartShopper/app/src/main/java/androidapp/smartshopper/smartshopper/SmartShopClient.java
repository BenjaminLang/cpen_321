package androidapp.smartshopper.smartshopper;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

/**
 * Created by Ben on 2016-10-22.
 */
public class SmartShopClient {
    private int port = 6969;
    private String addr = "ryangroup.westus.cloudapp.azure.com";
    //private String addr = "192.168.0.19";
    private Socket connection;
    private BufferedWriter outputStream;
    private BufferedReader inputStream;
    private boolean connected;

    public SmartShopClient() {
        try {
            SSLSocketFactory sslSocFact = (SSLSocketFactory) SSLSocketFactory.getDefault();
            SSLSocket sslSocket = (SSLSocket) sslSocFact.createSocket(addr, port);

            connection = new Socket(addr, port);
            outputStream = new BufferedWriter(
                    new OutputStreamWriter(sslSocket.getOutputStream()));
            inputStream = new BufferedReader(
                    new InputStreamReader(sslSocket.getInputStream()));
            connected = true;
        } catch (Exception e){
            e.printStackTrace();
            connected = false;
            return;
        }
    }

    public String sendRequest(String request){
        try{
            outputStream.write(request);//
            outputStream.flush();
            String response = inputStream.readLine();
            System.out.println(response);
            return response;
        } catch (IOException e){
            e.printStackTrace();
            return null;
        }
    }

    public boolean getStatus() {
        return connected;
    }
}
