package pl.akubarek.fitcare.ui.transactionList;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
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

    private TextView emptyListText;

    private static String TAG = TransactionListActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        databaseHelper = new DatabaseHelper(context);
        db = databaseHelper.getWritableDatabase();

        transactionList = (ListView) findViewById(R.id.transaction_list);
        transactionList.setOnItemClickListener(this);
        transactionList.setOnItemLongClickListener(this);

        emptyListText = (TextView) findViewById(R.id.transaction_empty_text);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: ");
        db = databaseHelper.getWritableDatabase();
        transactions = getAllTransactions();
        adapter = new TransactionListAdapter(context, transactions);
        transactionList.setAdapter(adapter);
        if (transactions.size() < 1) {
            emptyListText.setVisibility(View.VISIBLE);
        } else {
            emptyListText.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        db.close();
    }

    @Override
    public void deleteTransaction(Transaction transaction, int position) {
        if (db != null) {
            int result = db.delete(Constants.MEALS_TABLE, Constants.COLUMN_ID + " = " + transaction.getId(), null);
            if (result > 0) {
                transactions.remove(position);
                adapter = new TransactionListAdapter(context, transactions);
                transactionList.setAdapter(adapter);
                deleteTransactionProducts(transaction);
                Toast.makeText(context, "Usunięto jadłospis z listy", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Nie udało się usunąć jadłospisu", Toast.LENGTH_SHORT).show();
            }
        }
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
        if (db!= null) {
            db.delete(Constants.SHOPPING_TABLE, Constants.COLUMN_MEAL_ID + " = "+ transaction.getId(), null);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        Intent intent = new Intent(context, TransactionListDetailActivity.class);
        intent.putExtra(Constants.TRANSACTION_ID, transactions.get(position).getId());
        intent.putExtra(Constants.TRANSACTION_NAME, transactions.get(position).getName());
        intent.putExtra(Constants.TRANSACTION_DATE, transactions.get(position).getDestinationDate());
        startActivity(intent);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int position, long l) {
        final Transaction transaction = transactions.get(position);

        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setCancelable(true);
        alert.setTitle("Usuwanie");
        alert.setMessage("Na pewno chcesz usunąć jadłospis?");
        alert.setNegativeButton("Anuluj", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        alert.setPositiveButton("Usuń", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                deleteTransaction(transaction, position);
                if (transactions.size() < 1) {
                    emptyListText.setVisibility(View.VISIBLE);
                }
            }
        });
        alert.show();
        return true;
    }
}
