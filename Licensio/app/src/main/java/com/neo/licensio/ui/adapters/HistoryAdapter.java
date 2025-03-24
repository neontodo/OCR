package com.neo.licensio.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.neo.licensio.R;
import com.neo.licensio.data.models.Medicine;

import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder> {

    private List<Medicine> medicineList;
    private final ItemClickListener itemClickListener;

    public HistoryAdapter(List<Medicine> medicineList, ItemClickListener itemClickListener) {
        this.medicineList = medicineList;
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public HistoryAdapter.HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, parent, false);
        return new HistoryViewHolder(view, itemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryAdapter.HistoryViewHolder holder, int position) {
        Medicine item = medicineList.get(position);
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return this.medicineList.size();
    }


    public static class HistoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // View references go here
        private TextView itemMedicineName;
        private TextView itemMedicineCategory;
        private ItemClickListener itemClickListener;
        public HistoryViewHolder(@NonNull View itemView, ItemClickListener itemClickListener) {
            super(itemView);
            itemMedicineName = itemView.findViewById(R.id.itemMedicineName);
            itemMedicineCategory = itemView.findViewById(R.id.itemMedicineCategory);
            this.itemClickListener = itemClickListener;
            itemView.setOnClickListener(this);
        }

        public void bind(Medicine item) {
            itemMedicineName.setText(item.getName());
            itemMedicineCategory.setText(item.getTherapeuticClass());
        }

        @Override
        public void onClick(View v) {
            if(itemClickListener != null) {
                int position = getAdapterPosition();
                if (itemClickListener != null) {
                    itemClickListener.onItemClick(position);
                }
            }
        }
    }

}
