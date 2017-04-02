package pl.akubarek.fitcare.ui.transactionList;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import pl.akubarek.fitcare.R;
import pl.akubarek.fitcare.data.DatabaseHelper;
import pl.akubarek.fitcare.model.Product;
import pl.akubarek.fitcare.util.Constants;
import pl.akubarek.fitcare.util.Formatter;

public class TransactionListDetailActivity extends AppCompatActivity implements TransactionListDetailContract {

    private static final String TAG = TransactionListDetailActivity.class.getSimpleName();
    Context context = TransactionListDetailActivity.this;
    private DatabaseHelper databaseHelper;
    private SQLiteDatabase db;

    private List<Product> products;

    private ListView productsList;
    private TextView allCalories;
    private TextView allProtein;
    private TextView allCarbs;
    private TextView allFat;

    private TransactionListDetailAdapter adapter;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_list_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        databaseHelper = new DatabaseHelper(context);
        db = databaseHelper.getWritableDatabase();


        productsList = (ListView) findViewById(R.id.tr_detail_list_view);
        allCalories = (TextView) findViewById(R.id.detail_calories_value);
        allProtein = (TextView) findViewById(R.id.detail_protein_value);
        allCarbs = (TextView) findViewById(R.id.detail_carbs_value);
        allFat = (TextView) findViewById(R.id.detail_fat_value);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: ");
        db = databaseHelper.getWritableDatabase();
        long transactionId = getIntent().getLongExtra(Constants.TRANSACTION_ID, 0);
        products = getAllProductsFromTransaction(transactionId);
        adapter = new TransactionListDetailAdapter(context, products);
        productsList.setAdapter(adapter);
        calculateItemsInList();
    }

    @Override
    protected void onStop() {
        super.onStop();
        db.close();
    }

    @Override
    public List<Product> getAllProductsFromTransaction(long transactionId) {
        List <Product> products = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + Constants.SHOPPING_TABLE +
                " WHERE " + Constants.COLUMN_MEAL_ID + " = " + transactionId;
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            while(!cursor.isAfterLast()) {
                products.add(Product.getDetailProductFromCursor(cursor));
                cursor.moveToNext();
            }
        }

        return products;
    }

    private void calculateItemsInList() {
        int calories = 0;
        double protein = 0;
        double carbs = 0;
        double fat = 0;
        for (Product product : products) {
            calories += product.getCalories();
            protein += product.getProtein();
            carbs += product.getCarbs();
            fat += product.getFat();
        }
        allCalories.setText(Formatter.formatCalories(calories));
        allProtein.setText(Formatter.formatMicros(protein));
        allCarbs.setText(Formatter.formatMicros(carbs));
        allFat.setText(Formatter.formatMicros(fat));
    }
}
