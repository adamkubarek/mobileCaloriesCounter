package pl.akubarek.fitcare.ui.transactionList;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import pl.akubarek.fitcare.R;
import pl.akubarek.fitcare.model.Transaction;
import pl.akubarek.fitcare.util.Formatter;

/**
 * Created by BloodyFire on 02.04.2017.
 */

public class TransactionListAdapter extends ArrayAdapter<Transaction> {

    private List<Transaction> transactions;

    public TransactionListAdapter(Context context, List<Transaction> transactions) {
        super(context, R.layout.transaction_list_row, transactions);
        this.transactions = transactions;
    }

    private static class ViewHolder {
        TextView name;
        TextView date;
    }

    @Override
    public int getCount() {
        return transactions.size();
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Transaction transaction = getItem(position);

        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.transaction_list_row, parent, false);

            viewHolder.name = (TextView) convertView.findViewById(R.id.transation_name_text);
            viewHolder.date = (TextView) convertView.findViewById(R.id.transation_date_text);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.name.setText(transaction.getName());
        viewHolder.date.setText(Formatter.formatDate(transaction.getDestinationDate()));

        return convertView;
    }
}
