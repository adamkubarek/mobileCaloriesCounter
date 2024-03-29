package pl.akubarek.fitcare.util;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by BloodyFire on 28.03.2017.
 */

public class Formatter {
    public static String formatMicros (double number) {
        DecimalFormat df = new DecimalFormat("#.00");
        if (number >= 1) {
            return df.format(number)+"g";
        } else {
            return "0"+df.format(number)+"g";
        }
    }

    public static String formatCalories (int number) {
        String result = Integer.toString(number) + " kcal";

        return result;
    }

    public static String formatCaloriesWithoutKcal (int number) {
        String result = Integer.toString(number);

        return result;
    }

    public static String formatMicrosWithoutG (double number) {
        DecimalFormat df = new DecimalFormat("#.00");
        if (number >= 1) {
            return df.format(number)+"";
        } else {
            return "0"+df.format(number);
        }
    }

    public static String formatQuantity (int number) {
        String result = Integer.toString(number) + "g";

        return result;
    }

    public static String formatDate(long date) {
        String displayDate = new SimpleDateFormat("dd MMMM", new Locale("pl", "DEU")).format(new Date(date));
        return displayDate;
    }

    public static String formatPercent (double number) {
        String result = Formatter.formatMicrosWithoutG(number)+"%";
        return result;
    }

    public static String formatWeight (double weight) {
        DecimalFormat df = new DecimalFormat("#.00");
        if (weight >= 1) {
            return df.format(weight)+" kg";
        } else {
            return "0"+df.format(weight)+" kg";
        }
    }
}
