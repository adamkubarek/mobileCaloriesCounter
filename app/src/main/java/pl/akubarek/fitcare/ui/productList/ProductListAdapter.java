package pl.akubarek.fitcare.ui.productList;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import pl.akubarek.fitcare.R;
import pl.akubarek.fitcare.model.Product;
import pl.akubarek.fitcare.util.Formatter;

/**
 * Created by BloodyFire on 28.03.2017.
 */

public class ProductListAdapter extends ArrayAdapter<Product> {


    List <Product> products;

    public ProductListAdapter(Context context, List<Product> products) {
        super(context, R.layout.product_list_row, products);
        this.products = products;
    }

    private static class ViewHolder {
        TextView name;
        TextView category;
        TextView valueProtein;
        TextView valueCarbs;
        TextView valueFat;
        TextView calories;

//        TextView templateProtein;
//        TextView templateCarbs;
//        TextView templateFat;
    }

    @Override
    public int getCount() {
        return products.size();
    }


    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Product product = getItem(position);

        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.product_list_row, parent, false);
            viewHolder.name = (TextView) convertView.findViewById(R.id.product_name);
            viewHolder.category = (TextView) convertView.findViewById(R.id.product_category);
            viewHolder.valueProtein = (TextView) convertView.findViewById(R.id.value_protein);
            viewHolder.valueCarbs = (TextView) convertView.findViewById(R.id.value_carbs);
            viewHolder.valueFat = (TextView) convertView.findViewById(R.id.value_fat);
            viewHolder.calories = (TextView) convertView.findViewById(R.id.value_calories);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.name.setText(product.getName());
        viewHolder.category.setText(product.getCategory());
        viewHolder.valueProtein.setText(Formatter.formatMicros(product.getProtein()));
        viewHolder.valueCarbs.setText(Formatter.formatMicros(product.getCarbs()));
        viewHolder.valueFat.setText(Formatter.formatMicros(product.getFat()));
        viewHolder.calories.setText(Formatter.formatCalories(product.getCalories()));

        return convertView;
    }
}
