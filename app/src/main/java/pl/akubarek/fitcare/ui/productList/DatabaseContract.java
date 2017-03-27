package pl.akubarek.fitcare.ui.productList;

import java.util.List;

import pl.akubarek.fitcare.model.Product;

/**
 * Created by BloodyFire on 26.03.2017.
 */

public interface DatabaseContract {

    void addProduct(Product product);
    Product findNewestProductInTable();
    void deleteProduct (Product product, int itemPosition);
    List<Product> getAllProducts();
}
