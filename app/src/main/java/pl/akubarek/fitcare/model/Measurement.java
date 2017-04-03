package pl.akubarek.fitcare.model;

import android.database.Cursor;

import pl.akubarek.fitcare.util.Constants;

/**
 * Created by BloodyFire on 03.04.2017.
 */

public class Measurement {
    private long id;
    private double weight;
    private long date;




    public static Measurement getMeasurementFromCursor (Cursor cursor) {
        long id = cursor.getLong(cursor.getColumnIndex(Constants.COLUMN_ID));
        double weight = cursor.getDouble(cursor.getColumnIndex(Constants.COLUMN_WEIGHT));
        long date = cursor.getLong(cursor.getColumnIndex(Constants.COLUMN_DATE_CREATED));

        Measurement measurement = new Measurement();
        measurement.setId(id);
        measurement.setWeight(weight);
        measurement.setDate(date);

        return measurement;
    }

    public Measurement () {

    }

    public Measurement(double weight, long date) {
        this.weight = weight;
        this.date = date;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }
}
