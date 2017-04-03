package pl.akubarek.fitcare.util;

/**
 * Created by BloodyFire on 25.03.2017.
 */

public class Constants {
    // product list
    public static final String PRODUCT_TABLE = "products";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_CATEGORY = "category";
    public static final String COLUMN_WEIGHT = "weight";
    public static final String COLUMN_CALORIES = "calories";
    public static final String COLUMN_PROTEIN = "protein";
    public static final String COLUMN_FAT = "fat";
    public static final String COLUMN_CARBS = "carbs";

    // shopping list additional fields
    public static final String SHOPPING_TABLE = "shoppings";
    public static final String COLUMN_MEAL_ID = "mealid";

    // transaction list fields
    public static final String MEALS_TABLE = "meals";
    public final static String COLUMN_DATE_CREATED = "create_date";

    // shopping cart (Parcelable items) in bundle
    public static final String TEMP_CART_TABLE = "tempshop";

    // transaction id for detailActivity
    public static final String TRANSACTION_ID = "transaction_id";
    public static final String TRANSACTION_NAME = "transaction_name";
    public static final String TRANSACTION_DATE = "transaction_date";

    // Preference file keys
    public static final String BMR_ALGORITHM = "bmr_algorithm";
    public static final String MANUAL_LIMIT = "manual_calories_limit";
    public static final String HEIGHT = "edittext_height";
    public static final String WEIGHT = "edittext_weight";
    public static final String SEX = "list_sex";
    public static final String AGE = "edittext_age";
    public static final String ACTIVITY_LEVEL = "list_activity_level";
    public static final String DIET_GOALS = "list_diet_goals";

    // weight journal table
    public static final String WEIGHT_JOURNAL_TABLE = "weightjournal";
}
