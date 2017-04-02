package pl.akubarek.fitcare.ui.transactionList;

import java.util.List;

import pl.akubarek.fitcare.model.Product;

/**
 * Created by BloodyFire on 02.04.2017.
 */

public interface TransactionListDetailContract {
    List <Product> getAllProductsFromTransaction (long transactionId);
}
