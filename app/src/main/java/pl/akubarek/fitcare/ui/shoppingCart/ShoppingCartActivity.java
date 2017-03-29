package pl.akubarek.fitcare.ui.shoppingCart;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;

import pl.akubarek.fitcare.R;
import pl.akubarek.fitcare.data.DatabaseHelper;
import pl.akubarek.fitcare.model.Product;
import pl.akubarek.fitcare.ui.productList.ProductListActivity;
import pl.akubarek.fitcare.util.Constants;

public class ShoppingCartActivity extends AppCompatActivity {

    private Context context = ShoppingCartActivity.this;

    private ListView shoppingListView;
    private static ArrayList<Product> shoppingProducts;

    private DatabaseHelper databaseHelper;
    private SQLiteDatabase db;

    private static final String TAG = ShoppingCartActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if (savedInstanceState != null && shoppingProducts.size() < 1) {
            shoppingProducts = savedInstanceState.getParcelableArrayList(Constants.PREVIOUS_ITEMS);
        } else {
            shoppingProducts = new ArrayList<>();
        }
        setContentView(R.layout.activity_shopping_cart);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Log.d(TAG, "onCreate: ");
        shoppingListView = (ListView) findViewById(R.id.shopping_cart_listview);
        ShoppingCartAdapter adapter = new ShoppingCartAdapter(context, shoppingProducts);
        shoppingListView.setAdapter(adapter);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ProductListActivity.class);
                startActivity(intent);
            }
        });
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
        Product product = getIntent().getParcelableExtra(Constants.PARCELABLE_PRODUCT);
        shoppingProducts.add(product);
        ShoppingCartAdapter adapter = new ShoppingCartAdapter(context, shoppingProducts);
        shoppingListView.setAdapter(adapter);

        ///!!!!!!!!!!!
        System.out.println(product.getId());
        System.out.println(product.getName());
        System.out.println(product.getCategory());
        System.out.println(product.getCalories());
        System.out.println(product.getWeight());
        System.out.println(product.getCarbs());
        System.out.println(product.getProtein());
        System.out.println(product.getFat());
        //!!!!!!!!!!!!!!!!!
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: ");
    }

    public void clearShoppingCart() {

    }
}
