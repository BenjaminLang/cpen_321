package androidapp.smartshopper.smartshopper;

import org.json.JSONObject;

/**
 * Created by JohnS on 2016-10-23.
 */

public class Product {
    private String name;
    private String price;
    private String store;
    private String img;
    private String quantity;
    private String url;

    /*
    Constructor to initialize all product information
     */
    public Product(String name, String price, String store, String img, String url, String quantity) {
        this.name = name;
        this.price = price;
        this.store = store;
        this.img = img;
        this.url = url;
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public String getPrice() {
        return price;
    }

    public String getStore() {
        return store;
    }

    public String getImg() {
        return img;
    }

    public String getURL() {
        return url;
    }

    public String getQuantity() { return quantity; }

    /*
    Serialize product into a JSON string
     */
    public String toJSON() {
        try {
            JSONObject jsonString = new JSONObject();
            jsonString.put("name", name);
            jsonString.put("price", price);
            jsonString.put("store", store);
            jsonString.put("image", img);
            jsonString.put("url", url);

            if(quantity != null)
                jsonString.put("quantity", quantity);

            return jsonString.toString(2);
        } catch (Exception e) {
            return null;
        }
    }
}
