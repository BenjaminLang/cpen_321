package androidapp.smartshopper.smartshopper;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by JohnS on 2016-10-23.
 */

public class ProductAdapter extends ArrayAdapter<Product>{

    private Context context;
    int layoutId;
    private List<Product> products;

    public ProductAdapter(Context context, int resource, List<Product> products){
        super(context, resource, products);

        this.layoutId = resource;
        this.context = context;
        this.products = products;
    }

    @Override
    public int getCount(){
        return products.size();
    }

    @Override
    public Product getItem(int i){
        return products.get(i);
    }

    @Override
    public long getItemId(int i){
        return i;
    }

    public void updateProductList(List<Product> newList) {
        products.clear();
        products.addAll(newList);
        this.notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        View view = convertView;
        ProductHolder holder = null;

        if(view == null){
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            view = inflater.inflate(layoutId, parent, false);

            holder = new ProductHolder();
            holder.name = (TextView) view.findViewById(R.id.name);
            holder.price = (TextView) view.findViewById(R.id.price);
            holder.store = (TextView) view.findViewById(R.id.store);
            holder.quantity = (TextView) view.findViewById(R.id.quantity_to_add);
            holder.img = (ImageView) view.findViewById(R.id.item_img);

            view.setTag(holder);
        }
        else{
            holder = (ProductHolder) view.getTag();
        }

        Product currProduct = products.get(position);
        holder.name.setText(currProduct.getName());
        holder.price.setText(currProduct.getPrice());
        holder.store.setText("From: " + currProduct.getStore());
        Picasso.with(context).load(currProduct.getImg()).into(holder.img);

        if(currProduct.getQuantity() != null)
            holder.quantity.setText("x" + currProduct.getQuantity());

        return view;
    }

    public double getTotalPrc() {
        double total = 0;

        for(Product curr : products) {
            String quantity = curr.getQuantity();
            if(quantity != null)
                total += Integer.parseInt(quantity) * Double.parseDouble(curr.getPrice());
            else
                total += Double.parseDouble(curr.getPrice());
        }

        return round(total, 2);
    }

    public String getStores() {
        JSONArray stores = new JSONArray();

        if(products.isEmpty())
            return stores.toString();

        try {
            Set<String> storeSet = new HashSet<String>();
            for(Product curr : products) {
                storeSet.add(curr.getStore());
            }

            for(String currStore : storeSet) {
                stores.put(currStore);
            }

            return stores.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return stores.toString();
        }
    }

    private static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    static class ProductHolder{
        TextView name;
        TextView price;
        TextView store;
        TextView quantity;
        ImageView img;
    }
}
