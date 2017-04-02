package pl.akubarek.fitcare.ui.transactionList;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import pl.akubarek.fitcare.R;
import pl.akubarek.fitcare.data.DatabaseHelper;
import pl.akubarek.fitcare.model.Transaction;
import pl.akubarek.fitcare.util.Constants;

public class TransactionListActivity extends AppCompatActivity implements TransactionListContract,
        AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    private Context context = TransactionListActivity.this;

    private List <Transaction> transactions;
    private ListView transactionList;
    private TransactionListAdapter adapter;
    private DatabaseHelper databaseHelper;
    private SQLiteDatabase db;

    private static String TAG = TransactionListActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        databaseHelper = new DatabaseHelper(context);
        db = databaseHelper.getWritableDatabase();

        transactionList = (ListView) findViewById(R.id.transaction_list);
        transactionList.setOnItemClickListener(this);
        transactionList.setOnItemLongClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: ");
        transactions = getAllTransactions();
        adapter = new TransactionListAdapter(context, transactions);
        transactionList.setAdapter(adapter);
    }

    @Override
    public void deleteTransaction(Transaction transaction, int position) {

    }

    @Override
    public List<Transaction> getAllTransactions() {
        List <Transaction> transactions = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + Constants.MEALS_TABLE;

        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                transactions.add(Transaction.getTransactionFromCursor(cursor));
                cursor.moveToNext();
            }
        }
        return transactions;
    }

    @Override
    public void deleteTransactionProducts(Transaction transaction) {

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        Toast.makeText(context, String.valueOf(transactions.get(position).getId()), Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(context, TransactionListDetailActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {
        return false;
    }
}
