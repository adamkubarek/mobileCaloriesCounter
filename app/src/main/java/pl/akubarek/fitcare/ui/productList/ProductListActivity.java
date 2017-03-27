package pl.akubarek.fitcare.ui.productList;

import android.app.Dialog;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import pl.akubarek.fitcare.R;
import pl.akubarek.fitcare.model.Product;


public class ProductListActivity extends AppCompatActivity implements DatabaseContract{

    private Context context = ProductListActivity.this;

    private ListView productListView;

    private static List <Product> products;

    private Cursor cursor;
    private SQLiteDatabase db;

    private final static String TAG = ProductListActivity.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        productListView = (ListView) findViewById(R.id.product_list_view);
        products = new ArrayList<>();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayAddProductDialog();
                refreshStaticList();
            }
        });
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
                    addProductToDatabase();
                    // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

                    Toast.makeText(context, name + "\n" + category + "\n" + product.getWeight() + "\n" + calories + "\n" + protein + "\n" + carbs + "\n" + fat, Toast.LENGTH_LONG).show();
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
        // 2. Wyciagnac go z bazy
        // 3. Wrzucic do arrayListy z bazodanowym ID
        // 4. Po wykonaniu w którymś momencie zadbać o refresh adaptera (onResume?)

    }

    @Override
    public Product findNewestProductInTable() {
        // Realizacja 2 pktu z addProduct, ewentualnie statyczna metoda w modelu produktu
        // SELECT produkt z najwiekszym id (dzieki autoincrement)
        return null;
    }

    @Override
    public void deleteProduct(Product product, int itemPosition) {
        // wykonac w onLongItemClick
        // 1. Wyciagnac z obiektu id i usunąc ten rekord z bazy
        // 2. itemPosition to id obiektu w liscie ... również usunąć
        // 3. Po wykonaniu zadbać o refresh adaptera
    }

    @Override
    public List<Product> getAllProducts() {
        // wywolywac ta funkcje w onCreate vs onStart ?
        return null;
    }
}
