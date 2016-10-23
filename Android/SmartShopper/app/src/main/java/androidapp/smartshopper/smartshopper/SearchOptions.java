package androidapp.smartshopper.smartshopper;

/**
 * Created by Ben on 10/22/2016.
 */
public class SearchOptions {
    String optionType;
    String option;

    public SearchOptions(String optionType, String option){
        this.option = option;
        this.optionType = optionType;
    }

    public String getOption() {
        return option;
    }

    public String getOptionType() {
        return optionType;
    }

}
