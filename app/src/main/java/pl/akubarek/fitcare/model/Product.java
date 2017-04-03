package pl.akubarek.fitcare.model;

import android.database.Cursor;

import pl.akubarek.fitcare.util.Constants;

/**
 * Created by BloodyFire on 25.03.2017.
 */

public class Product {
    private long id;
    private String name;
    private String category;
    private int weight;
    private int calories;
    private double protein;
    private double carbs;
    private double fat;
    private long transactionId;

    public static Product getDetailProductFromCursor(Cursor cursor) {
        long id = cursor.getLong(cursor.getColumnIndex(Constants.COLUMN_ID));
        String name = cursor.getString(cursor.getColumnIndex(Constants.COLUMN_NAME));
        String category = cursor.getString(cursor.getColumnIndex(Constants.COLUMN_CATEGORY));
        int weight = cursor.getInt(cursor.getColumnIndex(Constants.COLUMN_WEIGHT));
        double protein = cursor.getDouble(cursor.getColumnIndex(Constants.COLUMN_PROTEIN));
        double carbs = cursor.getDouble(cursor.getColumnIndex(Constants.COLUMN_CARBS));
        double fat = cursor.getDouble(cursor.getColumnIndex(Constants.COLUMN_FAT));
        int calories = cursor.getInt(cursor.getColumnIndex(Constants.COLUMN_CALORIES));
        long transactionId = cursor.getLong(cursor.getColumnIndex(Constants.COLUMN_MEAL_ID));

        Product product = new Product();
        product.setId(id);
        product.setName(name);
        product.setCategory(category);
        product.setWeight(weight);
        product.setProtein(protein);
        product.setCarbs(carbs);
        product.setFat(fat);
        product.setCalories(calories);
        product.setTransactionId(transactionId);

        return product;
    }

    public static Product getProductFromCursor(Cursor cursor) {
        long id = cursor.getLong(cursor.getColumnIndex(Constants.COLUMN_ID));
        String name = cursor.getString(cursor.getColumnIndex(Constants.COLUMN_NAME));
        String category = cursor.getString(cursor.getColumnIndex(Constants.COLUMN_CATEGORY));
        int weight = cursor.getInt(cursor.getColumnIndex(Constants.COLUMN_WEIGHT));
        double protein = cursor.getDouble(cursor.getColumnIndex(Constants.COLUMN_PROTEIN));
        double carbs = cursor.getDouble(cursor.getColumnIndex(Constants.COLUMN_CARBS));
        double fat = cursor.getDouble(cursor.getColumnIndex(Constants.COLUMN_FAT));
        int calories = cursor.getInt(cursor.getColumnIndex(Constants.COLUMN_CALORIES));

        Product product = new Product();
        product.setId(id);
        product.setName(name);
        product.setCategory(category);
        product.setWeight(weight);
        product.setProtein(protein);
        product.setCarbs(carbs);
        product.setFat(fat);
        product.setCalories(calories);

        return product;
    }

    public Product () {
    }

    public Product(String name, String category, int calories, double protein, double carbs, double fat) {
        this.name = name;
        this.category = category;
        this.weight = 100; // default 100 grams
        this.calories = calories;
        this.protein = protein;
        this.carbs = carbs;
        this.fat = fat;
    }

    public void updateWeight (int newWeight) {
        setCalories((newWeight*getCalories())/this.weight);
        setCarbs((newWeight*getCarbs())/this.weight);
        setFat((newWeight*getFat())/this.weight);
        setProtein((newWeight*getProtein())/this.weight);
        setWeight(newWeight);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getCalories() {
        return calories;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

    public double getProtein() {
        return protein;
    }

    public void setProtein(double protein) {
        this.protein = protein;
    }

    public double getCarbs() {
        return carbs;
    }

    public void setCarbs(double carbs) {
        this.carbs = carbs;
    }

    public double getFat() {
        return fat;
    }

    public void setFat(double fat) {
        this.fat = fat;
    }

    public long getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(long transactionId) {
        this.transactionId = transactionId;
    }
}
