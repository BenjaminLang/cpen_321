package androidapp.smartshopper.smartshopper;

import android.content.Context;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

/**
 * Created by Ben on 2016-10-22.
 */
public class SmartShopClient {
    private int port = 6969;
    private String addr = "ryangroup.westus.cloudapp.azure.com";
    //private String addr = "13.88.11.52/ryangroup.westus.cloudapp.azure.com";
    private String server_hostname = "checkedout";
    private Socket connection;
    private BufferedWriter outputStream;
    private BufferedReader inputStream;
    private boolean connected;

    private char[] keyPass = "ryangroup".toCharArray();

    public SmartShopClient(Context context) {
        try {
            CertificateFactory cf = CertificateFactory.getInstance("X.509");

            InputStream certIn =  context.getResources().openRawResource(R.raw.ca);
            Certificate ca;
            try {
                ca = cf.generateCertificate(certIn);
            } finally {
                certIn.close();
            }

            String keyStoreType = KeyStore.getDefaultType();
            KeyStore ks = KeyStore.getInstance(keyStoreType);
            ks.load(null, null);
            ks.setCertificateEntry("ca", ca);

            String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
            TrustManagerFactory trustMan = TrustManagerFactory.getInstance(tmfAlgorithm);
            trustMan.init(ks);

            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustMan.getTrustManagers(), null);

            SSLSocketFactory sslSocFact = sslContext.getSocketFactory();
            Socket client_socket = new Socket(addr, port);
            SSLSocket sslSocket = (SSLSocket) sslSocFact.createSocket(client_socket, server_hostname, port, false);

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
            //System.out.println(response);
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
