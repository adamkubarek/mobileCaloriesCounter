package pl.akubarek.fitcare.ui.transactionList;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.List;

import pl.akubarek.fitcare.R;
import pl.akubarek.fitcare.model.Transaction;

public class TransactionListActivity extends AppCompatActivity implements TransactionListContract,
        AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    private Context context = TransactionListActivity.this;

    private List<Transaction> transactions;
    private ListView transactionList;
    private TransactionListAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        transactionList = (ListView) findViewById(R.id.transaction_list);
        transactions = getAllTransactions();
    }


    @Override
    public void deleteTransaction(Transaction transaction, int position) {

    }

    @Override
    public List<Transaction> getAllTransactions() {
        return null;
    }

    @Override
    public void deleteTransactionProducts(Transaction transaction) {

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
        return false;
    }
}
