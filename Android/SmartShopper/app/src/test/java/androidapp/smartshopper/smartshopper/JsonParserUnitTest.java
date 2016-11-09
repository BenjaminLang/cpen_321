package androidapp.smartshopper.smartshopper;

import android.widget.ArrayAdapter;

import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by JohnS on 2016-11-01.
 */

public class JsonParserUnitTest {
    @Before
    public void setUp(){

    }

    @Test
    public void testParse() {
        try{
            InputStream in = new FileInputStream("C:\\Users\\JohnS\\Desktop\\cpen_321\\Android\\SmartShopper\\app\\src\\test\\testFiles\\testJson.txt");
            BufferedReader buf = new BufferedReader(new InputStreamReader(in));

            String line = buf.readLine();
            StringBuilder sb = new StringBuilder();

            while(line != null){
                sb.append(line).append("\n");
                line = buf.readLine();
            }

            String testJson = sb.toString();
            JsonParser testParser = new JsonParser();
            ArrayList<Product> result = (ArrayList<Product>) testParser.parseJSON(testJson);

            /*
            List<Product> expected = new ArrayList<Product>(Arrays.asList(
                    new Product("Class KS9500 Curved 4k SUHD TV", "19999.99", "Samsung", "dummy.com"),
                    new Product("Class KS9500 Curved 4K SUHD TV", "6999.99", "Best Buy", "dummy.com")
            ));*/

            // assertEquals(result.size(), expected.size());

            //for(int i = 0; i < result)
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
