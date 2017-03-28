package pl.akubarek.fitcare.ui.productList;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import pl.akubarek.fitcare.R;
import pl.akubarek.fitcare.data.DatabaseHelper;
import pl.akubarek.fitcare.model.Product;
import pl.akubarek.fitcare.util.Constants;


public class ProductListActivity extends AppCompatActivity implements DatabaseContract, AdapterView.OnItemLongClickListener, AdapterView.OnItemClickListener{
    private Context context = ProductListActivity.this;

    private ListView productListView;
    private List <Product> products;

    private DatabaseHelper databaseHelper;
    private SQLiteDatabase db;


    private Cursor cursor;

    private final static String TAG = ProductListActivity.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        databaseHelper = new DatabaseHelper(context);
        db = databaseHelper.getWritableDatabase();

        productListView = (ListView) findViewById(R.id.product_list_view);
        products = getAllProducts();

//        for (Product product : products) {
//            System.out.println(product.getName());
//            System.out.println(product.getCalories());
//        }

        ProductListAdapter productAdapter = new ProductListAdapter(context, products);
        productListView.setAdapter(productAdapter);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayAddProductDialog();

            }
        });

        productListView.setOnItemLongClickListener(this);
        productListView.setOnItemClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        products = getAllProducts();
        Log.d(TAG, "onStart: ");

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_product_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void displayAddProductDialog () {
        final Dialog dialog = new Dialog(context);

        dialog.setContentView(R.layout.add_product_dialog);

        // find dialog fields;
        final EditText editName = (EditText) dialog.findViewById(R.id.editName);
        final EditText editCategory = (EditText) dialog.findViewById(R.id.editCategory);
        final EditText editCalories = (EditText) dialog.findViewById(R.id.editCalories);
        final EditText editProtein = (EditText) dialog.findViewById(R.id.editProtein);
        final EditText editCarbs = (EditText) dialog.findViewById(R.id.editCarbs);
        final EditText editFat = (EditText) dialog.findViewById(R.id.editFat);

        Button buttonAdd = (Button) dialog.findViewById(R.id.button_add);
        Button buttonCancel = (Button) dialog.findViewById(R.id.button_cancel);


        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!editName.getText().toString().isEmpty() && !editCategory.getText().toString().isEmpty() && !editCalories.getText().toString().isEmpty() &&
                        !editProtein.getText().toString().isEmpty() && !editCarbs.getText().toString().isEmpty() && !editFat.getText().toString().isEmpty()) {
                    String name = editName.getText().toString();
                    String category = editCategory.getText().toString();
                    int calories = Integer.valueOf(editCalories.getText().toString());
                    double protein = Double.valueOf(editProtein.getText().toString());
                    double carbs = Double.valueOf(editCarbs.getText().toString());
                    double fat = Double.valueOf(editFat.getText().toString());

                    // !!!!!!!!!!!!!!!!  DODAWANIE OBIEKTU DO BAZY
                    Product product = new Product(name, category, calories, protein, carbs, fat);
                    addProduct(product);
                    // somehow refresh adapter here or in onResume Method
                    // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

                    // aktualizacja
                    ProductListAdapter productAdapter = new ProductListAdapter(context, products);
                    productListView.setAdapter(productAdapter);

                    //Toast.makeText(context, name + "\n" + category + "\n" + product.getWeight() + "\n" + calories + "\n" + protein + "\n" + carbs + "\n" + fat, Toast.LENGTH_LONG).show();
                    dialog.dismiss();
                } else {
                    Toast.makeText(context, "Uzupełnij odpowiednio wszystkie pola", Toast.LENGTH_SHORT).show();
                }
            }
        });
        dialog.show();
    }

    @Override
    public void addProduct(Product product) {
        // poskladany product z dialogu
        // 1. Insertnac go do bazy
        boolean successfulAdded = true;
        ContentValues values = new ContentValues();
        values.put(Constants.COLUMN_NAME, product.getName());
        values.put(Constants.COLUMN_CATEGORY, product.getCategory());
        values.put(Constants.COLUMN_WEIGHT, product.getWeight()); // 100
        values.put(Constants.COLUMN_CALORIES, product.getCalories());
        values.put(Constants.COLUMN_PROTEIN, product.getProtein());
        values.put(Constants.COLUMN_CARBS, product.getCarbs());
        values.put(Constants.COLUMN_FAT, product.getFat());

        try {
            db.insertOrThrow(Constants.PRODUCT_TABLE, null, values);
        } catch (SQLException e) {
            Toast.makeText(context, "Nie udało się zapisać produktu", Toast.LENGTH_SHORT).show();
            successfulAdded = false;
        }

        if (successfulAdded) {
            Product productWithId = findNewestProductInTable();
            if (productWithId != null) {
                products.add(productWithId);
            } else {
                Toast.makeText(context, "Nie udało dodać się produktu do listy", Toast.LENGTH_SHORT).show();
            }
        }

        // 2. Wyciagnac go z bazy
        // 3. Wrzucic do arrayListy z bazodanowym ID
        // 4. Po wykonaniu w którymś momencie zadbać o refresh adaptera (onResume?)

    }

    @Override
    public Product findNewestProductInTable() {
        // Realizacja 2 pktu z addProduct, ewentualnie statyczna metoda w modelu produktu - wywołana tu z hardCoded selectem
        // SELECT produkt z najwiekszym id (dzieki autoincrement)

        Cursor cursor = db.rawQuery("SELECT * FROM " + Constants.PRODUCT_TABLE +
                " ORDER BY " + Constants.COLUMN_ID + " DESC LIMIT 1", null); // return one newest product

        Product product;
        if (cursor.moveToFirst()) {
            product = Product.getProductFromCursor(cursor);
        } else {
            product = null;
        }
        return product;
    }

    @Override
    public void deleteProduct(Product product, int itemPosition) {
        // wykonac w onLongItemClick
        // 1. Wyciagnac z obiektu id i usunąc ten rekord z bazy
        if (db != null ) {
            int result = db.delete(Constants.PRODUCT_TABLE, Constants.COLUMN_ID + " = " +product.getId(), null);
            if (result > 0) {
                products.remove(itemPosition);
                ProductListAdapter productAdapter = new ProductListAdapter(context, products);
                productListView.setAdapter(productAdapter);
            } else {
                Toast.makeText(context, "Nie udało się usunąć produktu", Toast.LENGTH_SHORT).show();
            }
        }
        // 2. itemPosition to id obiektu w liscie ... również usunąć
        // 3. Po wykonaniu zadbać o refresh adaptera
    }

    @Override
    public List<Product> getAllProducts() {
        // wywolywac ta funkcje w onCreate vs onStart ? - Decyzja - onStart
        List <Product> products = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + Constants.PRODUCT_TABLE;

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
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        Toast.makeText(context,"Item position : " + i , Toast.LENGTH_SHORT).show();
        Toast.makeText(context,"db ID : " + products.get(i).getId() , Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int position, long l) {
        final Product product = products.get(position);


        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setCancelable(true);
        alert.setTitle("Usuwanie");
        alert.setMessage("Na pewno chcesz usunąć ten produkt?");
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
