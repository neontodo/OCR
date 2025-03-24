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

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private List<Medicine> itemList;
    private final ItemClickListener itemClickListener;

    public MyAdapter(List<Medicine> itemList, ItemClickListener itemClickListener) {
        this.itemList = itemList;
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, parent, false);
        return new ViewHolder(view, itemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Medicine item = itemList.get(position);
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView itemMedicineName;
        private TextView itemMedicineCategory;
        private ItemClickListener itemClickListener;

        public ViewHolder(@NonNull View itemView, ItemClickListener itemClickListener) {
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
        public void onClick(View view) {
            if(itemClickListener != null) {
                int position = getAdapterPosition();
                if (itemClickListener != null) {
                    itemClickListener.onItemClick(position);
                }
            }
        }
    }


}
