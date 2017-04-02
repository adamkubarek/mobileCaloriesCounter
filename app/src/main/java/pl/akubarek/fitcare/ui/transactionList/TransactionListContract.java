package pl.akubarek.fitcare.ui.transactionList;

import java.util.List;

import pl.akubarek.fitcare.model.Transaction;

/**
 * Created by BloodyFire on 02.04.2017.
 */

public interface TransactionListContract {
    void deleteTransaction (Transaction transaction, int position);
    List <Transaction> getAllTransactions();
    void deleteTransactionProducts(Transaction transaction);
}
