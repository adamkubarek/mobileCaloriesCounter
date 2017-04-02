package pl.akubarek.fitcare.ui.transactionList;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import java.util.List;

import pl.akubarek.fitcare.R;
import pl.akubarek.fitcare.data.DatabaseHelper;
import pl.akubarek.fitcare.model.Product;

public class TransactionListDetailActivity extends AppCompatActivity implements TransactionListDetailContract {

    Context context = TransactionListDetailActivity.this;
    private DatabaseHelper databaseHelper;
    private SQLiteDatabase db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_list_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        databaseHelper = new DatabaseHelper(context);
        db = databaseHelper.getWritableDatabase();

    }

    @Override
    public List<Product> getAllProductsFromTransaction(long transactionId) {
        return null;
    }
}
