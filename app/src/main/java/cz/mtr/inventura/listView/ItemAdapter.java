package cz.mtr.inventura.listView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import cz.mtr.inventura.R;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemHolder> {
    private ArrayList<Item> items = new ArrayList<>();


    @NonNull
    @Override
    public ItemAdapter.ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new ItemHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemAdapter.ItemHolder holder, int position) {
        Item currentItem = items.get(position);
        holder.mEanTextView.setText("EAN: " + currentItem.getEan());
        holder.mLocationTextView.setText("Lokace: " + currentItem.getLocation());
        holder.mAmountTextView.setText(currentItem.getAmount() + "ks");
    }

    public void setItems(ArrayList<Item> items) {
        this.items = items;
        notifyDataSetChanged();

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class ItemHolder extends RecyclerView.ViewHolder {
        private TextView mEanTextView;
        private TextView mAmountTextView;
        private TextView mLocationTextView;

        public ItemHolder(@NonNull View itemView) {
            super(itemView);
            mEanTextView = itemView.findViewById(R.id.itemEanTextView);
            mAmountTextView = itemView.findViewById(R.id.itemAmountTextview);
            mLocationTextView = itemView.findViewById(R.id.locationTextView);
        }
    }
}
