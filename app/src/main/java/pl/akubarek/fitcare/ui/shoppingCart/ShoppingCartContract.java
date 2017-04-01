package pl.akubarek.fitcare.ui.shoppingCart;

import java.util.List;

import pl.akubarek.fitcare.model.Product;

/**
 * Created by BloodyFire on 29.03.2017.
 */

public interface ShoppingCartContract {

    List<Product> getAllProductsFromCart();
    void deleteProduct(Product product, int position);
    void clearShoppingCart();
    long addTransaction(); // return tr_id

}
