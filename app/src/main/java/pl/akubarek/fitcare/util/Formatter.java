package pl.akubarek.fitcare.util;

import java.text.DecimalFormat;

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
        String result = Integer.toString(number) + "kcal";

        return result;
    }
}
