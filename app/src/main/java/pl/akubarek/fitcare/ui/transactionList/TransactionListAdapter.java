package pl.akubarek.fitcare.ui.transactionList;

import android.content.Context;
import android.widget.ArrayAdapter;

import java.util.List;

import pl.akubarek.fitcare.R;
import pl.akubarek.fitcare.model.Transaction;

/**
 * Created by BloodyFire on 02.04.2017.
 */

public class TransactionListAdapter extends ArrayAdapter<Transaction> {

    private List<Transaction> transactions;

    public TransactionListAdapter(Context context, List<Transaction> transactions) {
        super(context, R.layout.transaction_list_row, transactions);
        this.transactions = transactions;
    }
}
