package androidapp.smartshopper.smartshopper;

/**
 * Created by Ben on 10/22/2016.
 */
public class SearchOptions {
    private String stores;
    private String price;
    private String price_min;
    private String price_max;
    private String num;

    public SearchOptions(String stores, String price, String price_min, String price_max, String num){
        this.stores = stores;
        this.price = price;
        this.price_min = price_min;
        this.price_max = price_max;
        this.num = num;
    }

    public String getStores() {
        return stores;
    }

    public String getPrice() {
        return price;
    }

    public String getPriceMin() {
        return price_min;
    }

    public String getPriceMax() {
        return price_max;
    }

    public String getNum() {
        return num;
    }
}
