package androidapp.smartshopper.smartshopper;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;

/**
 * Created by Ben on 10/22/2016.
 */
public class Collections {
    private static Collections instance;
    private boolean hasUpdatedCollections = false;
    private boolean collectionsCached = false;
    private ArrayList<String> collectionArray;
    File collectionList = new File("collectionList.txt");

    private Collections() {
    }

    public static synchronized Collections getInstance() {
        if (instance == null) {
            instance = new Collections();
        }
        return instance;
    }

    public void checkForCollection() {
        if (collectionList.exists()) {
            try {
                BufferedReader reader = new BufferedReader(new FileReader(collectionList));
                if (reader.readLine() == "updated")
                    hasUpdatedCollections = true;
                else
                    hasUpdatedCollections = false;
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String[] getCollection() {
        if (!collectionsCached && hasUpdatedCollections) {
            cacheCollection();
        } else if (!hasUpdatedCollections){
            RequestBuilder reqBuilder = new RequestBuilder();
            SmartShopClient client = new SmartShopClient();
            String response = client.sendRequest(reqBuilder.buildCollectionReq());
            ResponseHandler handler = new ResponseHandler();
            collectionArray = handler.HandleUpdateCollection(response);
            storeCollection();
        }
        return collectionArray.toArray(new String[collectionArray.size()]);
    }

    private void cacheCollection(){
        try {
            BufferedReader reader = new BufferedReader(new FileReader(collectionList));
            reader.readLine();
            String str;
            collectionArray.clear();
            while ((str = reader.readLine()) != null) {
                collectionArray.add(str);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void storeCollection(){
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(collectionList));
            writer.write("updated");
            writer.flush();
            for (String string : collectionArray)
                writer.write(string + "\n");
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}