package androidapp.smartshopper.smartshopper;

/**
 * Created by Ben on 10/22/2016.
 */
public class SearchOptions {
    private String optionType;
    private String option;
    private String locationX;
    private String locationY;
    private String radius;
    private String[] stores;
    private String price;

    public SearchOptions(String optionType, String option, String locationX,
                         String locationY, String radius, String[] stores, String price){
        this.option = option;
        this.optionType = optionType;
        this.locationX = locationX;
        this.locationY = locationY;
        this.radius = radius;
        this.stores = stores;
        this.price = price;
    }

    public String getOption() {
        return option;
    }

    public String getOptionType() {
        return optionType;
    }

    public String getLocationX() {
        return locationX;
    }

    public String getLocationY() {
        return locationY;
    }

    public String getRadius() {
        return radius;
    }

    public String[] getStores() {
        return stores;
    }

    public String getPrice() {
        return price;
    }
}
