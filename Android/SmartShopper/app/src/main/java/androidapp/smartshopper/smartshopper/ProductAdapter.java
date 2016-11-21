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

import java.util.List;

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

    static class ProductHolder{
        TextView name;
        TextView price;
        TextView store;
        TextView quantity;
        ImageView img;
    }
}
