package pl.akubarek.fitcare.ui.shoppingCart;

import android.content.Context;
import android.widget.ArrayAdapter;

import java.util.List;

import pl.akubarek.fitcare.R;
import pl.akubarek.fitcare.model.Product;

/**
 * Created by BloodyFire on 29.03.2017.
 */

public class ShoppingCartAdapter extends ArrayAdapter<Product> {

    List<Product> products;

    public ShoppingCartAdapter(Context context, List<Product> products) {
        super(context, R.layout.shopping_cart_list_row, products);
        this.products = products;
    }
}
