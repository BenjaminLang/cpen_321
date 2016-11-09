package androidapp.smartshopper.smartshopper;

/**
 * Created by JohnS on 2016-10-23.
 */

public class Product {
    private String name;
    private String price;
    private String store;
    private String imgURL;
    private String quantity;

    public Product(String name, String price, String store, String imgURL, String quantity) {
        this.name = name;
        this.price = price;
        this.store = store;
        this.imgURL = imgURL;
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
        return imgURL;
    }

    public String getQuantity() { return quantity; }
}
