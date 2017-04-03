package pl.akubarek.fitcare.ui.additional;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import pl.akubarek.fitcare.R;
import pl.akubarek.fitcare.data.DatabaseHelper;
import pl.akubarek.fitcare.model.Measurement;
import pl.akubarek.fitcare.util.Constants;

public class WeightJournalActivity extends AppCompatActivity implements WeightJournalContract,
        AdapterView.OnItemLongClickListener{

    private Context context = WeightJournalActivity.this;

    private TextView emptyText;
    private ListView journalListView;

    private WeightJournalAdapter adapter;
    private List<Measurement> weightMeasurements;

    private DatabaseHelper helper;
    private SQLiteDatabase db;

    private static final String TAG = WeightJournalActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weight_journal);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayAddMeasurementDialog();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        emptyText = (TextView) findViewById(R.id.weightJournalEmptyText);
        journalListView = (ListView) findViewById(R.id.weightJournalListView);
        journalListView.setOnItemLongClickListener(this);
        helper = new DatabaseHelper(context);
        db = helper.getWritableDatabase();


    }

    private void displayAddMeasurementDialog() {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.weight_journal_dialog);

        // find dialog fields
        final EditText editWeight = (EditText) dialog.findViewById(R.id.editWeightJournal);

        Button buttonAdd = (Button) dialog.findViewById(R.id.weightJournal_ok_btn);
        Button buttonCancel = (Button) dialog.findViewById(R.id.weightJournal_cancel_btn);

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!editWeight.getText().toString().isEmpty()) {
                    double weight = Double.valueOf(editWeight.getText().toString());
                    long date = System.currentTimeMillis();

                    Measurement measurement = new Measurement(weight, date);
                    addMeasurement(measurement);
                    dialog.dismiss();
                    updateList();
                    setEmptyText();

                } else {
                    Toast.makeText(context, "Uzupełnij odpowiednio pole", Toast.LENGTH_SHORT).show();
                }
            }
        });
        dialog.show();

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: ");
        updateList();
        setEmptyText();

    }

    private void setEmptyText() {
        if (weightMeasurements.size() < 1) {
            emptyText.setVisibility(View.VISIBLE);
        } else {
            emptyText.setVisibility(View.GONE);
        }
    }

    private void updateList() {
        weightMeasurements = getAllMeasurements();
        adapter = new WeightJournalAdapter(context, weightMeasurements);
        journalListView.setAdapter(adapter);
    }

    @Override
    public void addMeasurement(Measurement measurement) {
        ContentValues values = new ContentValues();
        values.put(Constants.COLUMN_WEIGHT, measurement.getWeight());
        values.put(Constants.COLUMN_DATE_CREATED, measurement.getDate());

        try {
            db.insertOrThrow(Constants.WEIGHT_JOURNAL_TABLE, null, values);
        } catch (SQLException e) {
            Toast.makeText(context, "Nie udało się zapisać pomiaru", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void deleteMeasurement(Measurement measurement, int itemPosition) {
        if (db!= null) {
            int result = db.delete(Constants.WEIGHT_JOURNAL_TABLE, Constants.COLUMN_ID + " = " + measurement.getId(), null);
            if (result > 0) {
                weightMeasurements.remove(itemPosition);
                updateList();
                setEmptyText();
                Toast.makeText(context, "Usunięto pomiar", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Nie udało się usunąć pomiaru", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public List<Measurement> getAllMeasurements() {
        List <Measurement> measurements = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + Constants.WEIGHT_JOURNAL_TABLE;

        Cursor cursor = db.rawQuery(selectQuery, null);
        if(cursor.moveToFirst()) {
            while(!cursor.isAfterLast()) {
                measurements.add(Measurement.getMeasurementFromCursor(cursor));
                cursor.moveToNext();
            }
        }
        return measurements;
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int position, long l) {
        final Measurement measurement = weightMeasurements.get(position);

        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setCancelable(true);
        alert.setTitle("Usuwanie");
        alert.setMessage("Na pewno chcesz usunąć pomiar?");
        alert.setPositiveButton("Usuń", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                deleteMeasurement(measurement, position);
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
}
