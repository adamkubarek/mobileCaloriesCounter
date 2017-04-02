package pl.akubarek.fitcare.ui.transactionList;

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
 * Created by BloodyFire on 02.04.2017.
 */

public class TransactionListDetailAdapter  extends ArrayAdapter<Product> {

    List <Product> products;

    public TransactionListDetailAdapter(Context context, List<Product> products) {
        super(context, R.layout.transaction_list_detail_row, products);
        this.products = products;
    }

    private static class ViewHolder {
        TextView name;
        TextView quantity;
        TextView protein;
        TextView carbs;
        TextView fat;
        TextView calories;
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
            convertView = inflater.inflate(R.layout.transaction_list_detail_row, parent, false);

            viewHolder.name = (TextView) convertView.findViewById(R.id.detail_list_product_name);
            viewHolder.quantity = (TextView) convertView.findViewById(R.id.detail_list_product_quantity);
            viewHolder.protein = (TextView) convertView.findViewById(R.id.detail_list_value_protein);
            viewHolder.carbs = (TextView) convertView.findViewById(R.id.detail_list_value_carbs);
            viewHolder.fat = (TextView) convertView.findViewById(R.id.detail_list_value_fat);
            viewHolder.calories = (TextView) convertView.findViewById(R.id.detail_list_value_calories);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.name.setText(product.getName());
        viewHolder.quantity.setText(Formatter.formatQuantity(product.getWeight()));
        viewHolder.protein.setText(Formatter.formatMicros(product.getProtein()));
        viewHolder.carbs.setText(Formatter.formatMicros(product.getCarbs()));
        viewHolder.fat.setText(Formatter.formatMicros(product.getFat()));
        viewHolder.calories.setText(Formatter.formatCalories(product.getCalories()));

        return convertView;
    }
}
