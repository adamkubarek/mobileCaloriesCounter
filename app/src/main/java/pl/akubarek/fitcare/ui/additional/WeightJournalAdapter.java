package pl.akubarek.fitcare.ui.additional;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import pl.akubarek.fitcare.R;
import pl.akubarek.fitcare.model.Measurement;
import pl.akubarek.fitcare.util.Formatter;

/**
 * Created by BloodyFire on 03.04.2017.
 */

public class WeightJournalAdapter extends ArrayAdapter<Measurement> {

    private List<Measurement> measurements;

    public WeightJournalAdapter(Context context, List<Measurement> measurements) {
        super(context, R.layout.weight_journal_list_row, measurements);
        this.measurements = measurements;
    }

    private static class ViewHolder {
        TextView measurement;
        TextView date;
    }

    @Override
    public int getCount() {
        return measurements.size();
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Measurement measurement = getItem(position);

        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.weight_journal_list_row, parent, false);

            viewHolder.date = (TextView) convertView.findViewById(R.id.weight_journal_date);
            viewHolder.measurement = (TextView) convertView.findViewById(R.id.weight_journal_measurement);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.measurement.setText(Formatter.formatWeight(measurement.getWeight()));
        viewHolder.date.setText(Formatter.formatDate(measurement.getDate()));

        return convertView;
    }
}
