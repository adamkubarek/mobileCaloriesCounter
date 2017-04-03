package pl.akubarek.fitcare.model;

import android.database.Cursor;

import pl.akubarek.fitcare.util.Constants;

/**
 * Created by BloodyFire on 02.04.2017.
 */

public class Transaction {
    long id;
    String name;
    long destinationDate;


    public Transaction () {
    }

    public Transaction(String name, long destinationDate) {
        this.name = name;
        this.destinationDate = destinationDate;
    }

    public static Transaction getTransactionFromCursor (Cursor cursor) {
        long id = cursor.getLong(cursor.getColumnIndex(Constants.COLUMN_ID));
        String name = cursor.getString(cursor.getColumnIndex(Constants.COLUMN_NAME));
        long destinationDate = cursor.getLong(cursor.getColumnIndex(Constants.COLUMN_DATE_CREATED));

        Transaction transaction = new Transaction();
        transaction.setId(id);
        transaction.setName(name);
        transaction.setDestinationDate(destinationDate);

        return transaction;
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

    public long getDestinationDate() {
        return destinationDate;
    }

    public void setDestinationDate(long destinationDate) {
        this.destinationDate = destinationDate;
    }
}
