package androidapp.smartshopper.smartshopper;

/**
 * Created by JohnS on 2016-10-23.
 */

public class Product {
    private String name;
    private String price;
    private String store;
    private String imgURL;

    public Product(String name, String price, String store, String imgURL) {
        this.name = name;
        this.price = price;
        this.store = store;
        this.imgURL = imgURL;
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
}
