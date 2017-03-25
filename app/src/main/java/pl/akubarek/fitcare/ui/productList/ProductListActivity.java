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


public class ProductListActivity extends AppCompatActivity {

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
        TextView textName = (TextView) dialog.findViewById(R.id.textName);
        TextView textCategory = (TextView) dialog.findViewById(R.id.textCategory);
        TextView textCalories = (TextView) dialog.findViewById(R.id.textCalories);
        TextView textProtein = (TextView) dialog.findViewById(R.id.textProtein);
        TextView textCarbs = (TextView) dialog.findViewById(R.id.textCarbs);
        TextView textFat = (TextView) dialog.findViewById(R.id.textFat);
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


                    Toast.makeText(context, name + "\n" + category + "\n" + product.getWeight() + "\n" + calories + "\n" + protein + "\n" + carbs + "\n" + fat, Toast.LENGTH_LONG).show();
                    dialog.dismiss();
                } else {
                    Toast.makeText(context, "Uzupełnij odpowiednio wszystkie pola", Toast.LENGTH_SHORT).show();
                }
            }
        });
        dialog.show();
    }
}
