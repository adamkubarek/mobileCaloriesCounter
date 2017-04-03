package pl.akubarek.fitcare.ui.shoppingCart;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import pl.akubarek.fitcare.R;
import pl.akubarek.fitcare.data.DatabaseHelper;
import pl.akubarek.fitcare.model.Product;
import pl.akubarek.fitcare.model.Transaction;
import pl.akubarek.fitcare.ui.additional.MyPreferencesActivity;
import pl.akubarek.fitcare.ui.productList.ProductListActivity;
import pl.akubarek.fitcare.ui.transactionList.TransactionListActivity;
import pl.akubarek.fitcare.util.Constants;
import pl.akubarek.fitcare.util.Formatter;

public class ShoppingCartActivity extends AppCompatActivity implements ShoppingCartContract, AdapterView.OnItemLongClickListener, View.OnClickListener{

    private TextView allCalories;
    private TextView allProtein;
    private TextView allCarbs;
    private TextView allFat;

    private TextView maxCalories;
    private TextView maxProtein;
    private TextView maxCarbs;
    private TextView maxFat;

    private TextView percentCalories;
    private TextView percentProtein;
    private TextView percentCarbs;
    private TextView percentFat;

    private TextView emptyTextForList;

    private Button btnConfirmTransaction;
    private Button btnClearCart;

    private Context context = ShoppingCartActivity.this;

    private ListView shoppingListView;
    private static List <Product> shoppingProducts;

    private DatabaseHelper databaseHelper;
    private SQLiteDatabase db;

    private SharedPreferences sharedPreferences;

    private int globalCalories;

    private static final String TAG = ShoppingCartActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_cart);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        databaseHelper = new DatabaseHelper(context);
        db = databaseHelper.getWritableDatabase();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        shoppingListView = (ListView) findViewById(R.id.tr_detail_list_view);
        Log.d(TAG, "onCreate: ");

        shoppingListView.setOnItemLongClickListener(this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ProductListActivity.class);
                startActivity(intent);
            }
        });

        allCalories = (TextView) findViewById(R.id.shopping_value_calories);
        allProtein = (TextView) findViewById(R.id.shopping_value_protein);
        allCarbs = (TextView) findViewById(R.id.shopping_value_carbs);
        allFat = (TextView) findViewById(R.id.shopping_value_fat);

        emptyTextForList = (TextView) findViewById(R.id.empty_shopping_text);

        maxCalories = (TextView) findViewById(R.id.prefMaxCalories);
        maxProtein = (TextView) findViewById(R.id.prefMaxProtein);
        maxCarbs = (TextView) findViewById(R.id.prefMaxCarbs);
        maxFat = (TextView) findViewById(R.id.prefMaxFat);

        percentCalories = (TextView) findViewById(R.id.prefCaloriesPercent);
        percentProtein = (TextView) findViewById(R.id.prefProteinPercent);
        percentCarbs = (TextView) findViewById(R.id.prefCarbsPercent);
        percentFat = (TextView) findViewById(R.id.prefFatPercent);

        btnConfirmTransaction = (Button) findViewById(R.id.btn_make_tansaction);
        btnClearCart = (Button) findViewById(R.id.btn_clear_shopping_cart);

        btnClearCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearShoppingCart();
            }
        });

        btnConfirmTransaction.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_shopping_cart, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        Intent intent;

        //noinspection SimplifiableIfStatement
        if (id == R.id.shopping_productList) {
            intent = new Intent(context, ProductListActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.action_transactions) {
            intent = new Intent(context, TransactionListActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.action_preferences) {
            intent = new Intent(context, MyPreferencesActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: ");
        db = databaseHelper.getWritableDatabase();
        shoppingProducts = getAllProductsFromCart();
        if (shoppingProducts.size() < 1) {
            emptyTextForList.setVisibility(View.VISIBLE);
        } else {
            emptyTextForList.setVisibility(View.GONE);
        }

        ShoppingCartAdapter adapter = new ShoppingCartAdapter(context, shoppingProducts);
        shoppingListView.setAdapter(adapter);
        calculateItemsInList();
        globalCalories = fullCaloriesCalculation();
        setMicrosAndCaloriesInLayout(globalCalories);
    }

    private void setMicrosAndCaloriesInLayout(int calories) {
        // carbs 45%
        // protein 30%
        // fat 25%
        if (calories > 0) {
            double maximumCarbs = ((double)calories*45)/400;
            double maximumProtein = ((double)calories*30)/400;
            double maximumFat = ((double)calories*25)/900;
            maxCalories.setText("/"+Formatter.formatCalories(calories));
            maxCarbs.setText("/"+Formatter.formatMicros(maximumCarbs));
            maxProtein.setText("/"+Formatter.formatMicros(maximumProtein));
            maxFat.setText("/"+Formatter.formatMicros(maximumFat));

            double calc = Integer.valueOf(allCalories.getText().toString());
            String tCarbs = allCarbs.getText().toString();
            tCarbs = tCarbs.replace(",",".");
            double carbs = Double.valueOf(tCarbs);
            String tProtein = allProtein.getText().toString();
            tProtein = tProtein.replace(",",".");
            double protein = Double.valueOf(tProtein);
            String tFat = allFat.getText().toString();
            tFat = tFat.replace(",",".");
            double fat = Double.valueOf(tFat);


            double percCalories = (calc/calories)*100;
            double percCarbs = (carbs/maximumCarbs)*100;
            double percProtein = (protein/maximumProtein)*100;
            double percFat = (fat/maximumFat)*100;

            percentCalories.setText(Formatter.formatPercent(percCalories));
            percentCarbs.setText(Formatter.formatPercent(percCarbs));
            percentProtein.setText(Formatter.formatPercent(percProtein));
            percentFat.setText(Formatter.formatPercent(percFat));

            if (percCalories > 100) {
                percentCalories.setTextColor(getResources().getColor(R.color.warning));
            } else {
                percentCalories.setTextColor(getResources().getColor(R.color.colorAccent));
            }

            if (percCarbs > 100) {
                percentCarbs.setTextColor(getResources().getColor(R.color.warning));
            } else {
                percentCarbs.setTextColor(getResources().getColor(R.color.colorSecondaryText));
            }

            if (percProtein > 100) {
                percentProtein.setTextColor(getResources().getColor(R.color.warning));
            } else {
                percentProtein.setTextColor(getResources().getColor(R.color.colorSecondaryText));
            }

            if (percFat > 100) {
                percentFat.setTextColor(getResources().getColor(R.color.warning));
            } else {
                percentFat.setTextColor(getResources().getColor(R.color.colorSecondaryText));
            }
        }


    }

    private int fullCaloriesCalculation() {
        boolean isManual = sharedPreferences.getBoolean(Constants.BMR_ALGORITHM, false);
        String smanualCalories = sharedPreferences.getString(Constants.MANUAL_LIMIT, "0");
        String sheight = sharedPreferences.getString(Constants.HEIGHT, "-1");
        String sweight = sharedPreferences.getString(Constants.WEIGHT, "-1");
        String ssex = sharedPreferences.getString(Constants.SEX, "0");
        String sage = sharedPreferences.getString(Constants.AGE, "-1");
        String sactivityLevel = sharedPreferences.getString(Constants.ACTIVITY_LEVEL, "0");
        String sdietGoals = sharedPreferences.getString(Constants.DIET_GOALS, "0");

        int basicCaloriesInDiet;
        int caloriesInDiet = 1;
        if (isManual) {
            if (!smanualCalories.isEmpty()) {
                caloriesInDiet = Integer.valueOf(smanualCalories);
            }
        } else {
            if (!sheight.isEmpty() && !sweight.isEmpty() && !sage.isEmpty()) {
                int sex = Integer.valueOf(ssex);
                int activityLevel = Integer.valueOf(sactivityLevel);
                int dietGoals = Integer.valueOf(sdietGoals);
                int height = Integer.valueOf(sheight);
                double weight = Double.valueOf(sweight);
                int age = Integer.valueOf(sage);

                if (sex == 0) {
                    basicCaloriesInDiet = performAlgorithmForMan(height, weight, age, activityLevel);
                } else {
                    basicCaloriesInDiet = performAlgorithmForWoman(height, weight, age, activityLevel);
                }

                if (dietGoals == 0) {
                    caloriesInDiet = reductionCalories(basicCaloriesInDiet);
                } else if (dietGoals == 1) {
                    caloriesInDiet = basicCaloriesInDiet;
                } else {
                    caloriesInDiet = developCalories(basicCaloriesInDiet);
                }
            }
        }
        return caloriesInDiet;
    }

    private int developCalories(int basicCaloriesInDiet) {
        int calories = (int)((basicCaloriesInDiet*115)/100);
        return calories;
    }


    private int reductionCalories(int basicCaloriesInDiet) {
        int calories = (int)((basicCaloriesInDiet*80)/100);
        return calories;
    }

    private int performAlgorithmForWoman(int height, double weight, int age, int activityLevel) {
        int calories = (int) (655 + (9.6*weight) + (1.8*height) - (4.7*age));

         if (activityLevel == 1) {
             calories = (int)(calories*1.2);
        } else if (activityLevel == 2) {
             calories = (int)(calories*1.5);
        } else if (activityLevel == 3) {
             calories = (int)(calories*1.7);
        }

        return calories;
    }

    private int performAlgorithmForMan(int height, double weight, int age, int activityLevel) {
        int calories = (int) (66 + (13.7*weight) + (5*height) - (6.8*age));

        if (activityLevel == 1) {
            calories = (int)(calories*1.2);
        } else if (activityLevel == 2) {
            calories = (int)(calories*1.5);
        } else if (activityLevel == 3) {
            calories = (int)(calories*1.7);
        }

        return calories;
    }


    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop: ");
        db.close();
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
        allCalories.setText(Formatter.formatCaloriesWithoutKcal(calories));
        allProtein.setText(Formatter.formatMicrosWithoutG(protein));
        allCarbs.setText(Formatter.formatMicrosWithoutG(carbs));
        allFat.setText(Formatter.formatMicrosWithoutG(fat));
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
                setMicrosAndCaloriesInLayout(globalCalories);
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
                emptyTextForList.setVisibility(View.VISIBLE);
                calculateItemsInList();
                setMicrosAndCaloriesInLayout(globalCalories);
            }
        }
    }

    @Override
    public long addTransaction(Transaction transaction) {
        boolean successfulAdded = true;
        ContentValues values = new ContentValues();
        values.put(Constants.COLUMN_NAME, transaction.getName());
        values.put(Constants.COLUMN_DATE_CREATED, transaction.getDestinationDate());

        try {
            db.insertOrThrow(Constants.MEALS_TABLE, null, values);
        } catch (SQLException e) {
            System.out.println(e);
            successfulAdded = false;
        }

        if (successfulAdded) {
            Transaction transactionWithId = findNewestTransactionInTable();
            if (transactionWithId != null) {
                return transactionWithId.getId();
            }
        }
        return 0;
    }

    @Override
    public void addTrProduct(Product product) {
        ContentValues values = new ContentValues();
        values.put(Constants.COLUMN_NAME, product.getName());
        values.put(Constants.COLUMN_CATEGORY, product.getCategory());
        values.put(Constants.COLUMN_WEIGHT, product.getWeight());
        values.put(Constants.COLUMN_CALORIES, product.getCalories());
        values.put(Constants.COLUMN_PROTEIN, product.getProtein());
        values.put(Constants.COLUMN_CARBS, product.getCarbs());
        values.put(Constants.COLUMN_FAT, product.getFat());
        values.put(Constants.COLUMN_MEAL_ID, product.getTransactionId());

        try {
            db.insertOrThrow(Constants.SHOPPING_TABLE, null, values);
        } catch (SQLException e) {

        }
    }

    @Override
    public Transaction findNewestTransactionInTable() {
        Cursor cursor = db.rawQuery("SELECT * FROM " + Constants.MEALS_TABLE +
        " ORDER BY " + Constants.COLUMN_ID + " DESC LIMIT 1", null);
        Transaction transaction;
        if (cursor.moveToFirst()) {
            transaction = Transaction.getTransactionFromCursor(cursor);
        } else {
            transaction = null;
        }
        return transaction;
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
                if (shoppingProducts.size() < 1) {
                    emptyTextForList.setVisibility(View.VISIBLE);
                }
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

    @Override
    public void onClick(View view) {
        if (shoppingProducts.size() > 0) {
            final Dialog dialog = new Dialog(context,android.R.style.Theme_DeviceDefault_Light_NoActionBar_Fullscreen);
            dialog.setContentView(R.layout.set_transaction_dialog);

            // find dialog fields;
            final DatePicker datePicker = (DatePicker) dialog.findViewById(R.id.datePicker);
            final EditText editName = (EditText) dialog.findViewById(R.id.edit_meal_name);

            Button buttonAdd = (Button) dialog.findViewById(R.id.btn_create_transaction);
            Button buttonCancel = (Button) dialog.findViewById(R.id.btn_cancel_transaction);

            buttonCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });

            buttonAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!editName.getText().toString().isEmpty()) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth());
                        TimeZone timeZone1 = TimeZone.getTimeZone("UTC+2");
                        calendar.setTimeZone(timeZone1);
                        long milis = calendar.getTimeInMillis();
                        String name = editName.getText().toString();

                        Transaction newTransaction = new Transaction(name, milis);
                        long trId = addTransaction(newTransaction);
                        if (trId > 0) {
                            for (Product product : shoppingProducts) {
                                product.setTransactionId(trId);
                                addTrProduct(product);
                            }
                        }
                        dialog.dismiss();
                        Toast.makeText(context, "Dodano nowy jadłospis", Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(context, "Wypełnij pole", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            dialog.show();
        } else {
            Toast.makeText(context, "Brak produktów w jadłospisie, uzupełnij listę", Toast.LENGTH_SHORT).show();
        }
    }
}
