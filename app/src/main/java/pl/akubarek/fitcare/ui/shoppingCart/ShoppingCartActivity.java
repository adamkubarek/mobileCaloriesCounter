package pl.akubarek.fitcare.ui.shoppingCart;

import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import pl.akubarek.fitcare.R;
import pl.akubarek.fitcare.data.DatabaseHelper;
import pl.akubarek.fitcare.model.Product;
import pl.akubarek.fitcare.util.Constants;
import pl.akubarek.fitcare.util.Formatter;

public class ShoppingCartActivity extends AppCompatActivity implements ShoppingCartContract, AdapterView.OnItemLongClickListener{

    private TextView allCalories;
    private TextView allProtein;
    private TextView allCarbs;
    private TextView allFat;

    private Button btnConfirmTransaction;
    private Button btnClearCart;

    private Context context = ShoppingCartActivity.this;

    private ListView shoppingListView;
    private static List <Product> shoppingProducts;

    private DatabaseHelper databaseHelper;
    private SQLiteDatabase db;

    private static final String TAG = ShoppingCartActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_cart);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        databaseHelper = new DatabaseHelper(context);
        db = databaseHelper.getWritableDatabase();

        shoppingListView = (ListView) findViewById(R.id.shopping_cart_listview);
        Log.d(TAG, "onCreate: ");
        shoppingProducts = getAllProductsFromCart();

        ShoppingCartAdapter adapter = new ShoppingCartAdapter(context, shoppingProducts);
        shoppingListView.setAdapter(adapter);
        shoppingListView.setOnItemLongClickListener(this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        allCalories = (TextView) findViewById(R.id.shopping_value_calories);
        allProtein = (TextView) findViewById(R.id.shopping_value_protein);
        allCarbs = (TextView) findViewById(R.id.shopping_value_carbs);
        allFat = (TextView) findViewById(R.id.shopping_value_fat);

        btnConfirmTransaction = (Button) findViewById(R.id.btn_make_tansaction);
        btnClearCart = (Button) findViewById(R.id.btn_clear_shopping_cart);

        btnClearCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearShoppingCart();
            }
        });



        calculateItemsInList();


    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: ");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop: ");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: ");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: ");
    }

    private void calculateItemsInList() {
        int calories = 0;
        double protein = 0;
        double carbs = 0;
        double fat = 0;
        for (Product product : shoppingProducts) {
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


    @Override
    public List<Product> getAllProductsFromCart() {
        List <Product> products = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + Constants.TEMP_CART_TABLE;

        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                products.add(Product.getProductFromCursor(cursor));
                cursor.moveToNext();
            }
        }
        return products;
    }

    @Override
    public void deleteProduct(Product product, int itemPosition) {
        if (db != null) {
            int result = db.delete(Constants.TEMP_CART_TABLE, Constants.COLUMN_ID + " = "+ product.getId(), null);
            if (result > 0) {
                shoppingProducts.remove(itemPosition);
                ShoppingCartAdapter adapter = new ShoppingCartAdapter(context, shoppingProducts);
                shoppingListView.setAdapter(adapter);
                calculateItemsInList();
                Toast.makeText(context, "Usunięto produkt z listy", Toast.LENGTH_SHORT).show();

            } else {
                Toast.makeText(context, "Nie udało się usunąć produktu", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void clearShoppingCart() {
        if (db != null) {
            int result = db.delete(Constants.TEMP_CART_TABLE, "1", null);
            if (result > 0) {
                shoppingProducts.clear();
                ShoppingCartAdapter adapter = new ShoppingCartAdapter(context, shoppingProducts);
                shoppingListView.setAdapter(adapter);
                calculateItemsInList();
            }
        }
    }

    @Override
    public long addTransaction() {
        return 0;
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int position, long l) {
        final Product product = shoppingProducts.get(position);

        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setCancelable(true);
        alert.setTitle("Usuwanie");
        alert.setMessage("Na pewno chcesz usunąć produkt z listy?");
        alert.setPositiveButton("Usuń", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                deleteProduct(product, position);
            }
        });
        alert.setNegativeButton("Anuluj", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        alert.show();

        return true;
    }
}
