package com.neo.licensio.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.neo.licensio.R;
import com.neo.licensio.data.models.Medicine;
import com.neo.licensio.data.models.ResultFields;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResultAdapter extends RecyclerView.Adapter<ResultAdapter.ResultViewHolder>{

    private Medicine medicine;
    private Map<String, String> medicineFields;
    private List<Map.Entry<String, String>> entryList;
    private List<ResultFields> resultFieldsList;
    private final ItemClickListener itemClickListener;

    public ResultAdapter(Medicine medicine, ItemClickListener itemClickListener) {
            this.medicine = medicine;
            this.itemClickListener = itemClickListener;
            this.medicineFields = new HashMap<>();
            this.resultFieldsList = new ArrayList<>();
            if(!medicine.getName().isEmpty()){
                medicineFields.put("Name:", medicine.getName());
                this.setResultField("Name:", medicine.getName());
            }

            if(!medicine.getSubstitute0().isEmpty()){
                medicineFields.put("Substitute1:", medicine.getSubstitute0());
                this.setResultField("Substitute1:", medicine.getSubstitute0());
            }
            if(!medicine.getSubstitute1().isEmpty()){
                medicineFields.put("Substitute2:", medicine.getSubstitute1());
                this.setResultField("Substitute2:", medicine.getSubstitute1());
            }
            if(!medicine.getSubstitute2().isEmpty()){
                medicineFields.put("Substitute3:", medicine.getSubstitute2());
                this.setResultField("Substitute3:", medicine.getSubstitute2());
            }
            if(!medicine.getSubstitute3().isEmpty()){
                medicineFields.put("Substitute4:", medicine.getSubstitute3());
                this.setResultField("Substitute4:", medicine.getSubstitute3());
            }
            if(!medicine.getSubstitute4().isEmpty()){
                medicineFields.put("Substitute5:", medicine.getSubstitute4());
                this.setResultField("Substitute5:", medicine.getSubstitute4());
            }

            if(!medicine.getSideEffect1().isEmpty()){
                medicineFields.put("Side Effect1:", medicine.getSideEffect1());
                this.setResultField("Side Effect1:", medicine.getSideEffect1());
            }
            if(!medicine.getSideEffect2().isEmpty()){
                medicineFields.put("Side Effect2:", medicine.getSideEffect2());
                this.setResultField("Side Effect2:", medicine.getSideEffect2());
            }
            if(!medicine.getSideEffect3().isEmpty()){
                medicineFields.put("Side Effect3:", medicine.getSideEffect3());
                this.setResultField("Side Effect3:", medicine.getSideEffect3());
            }
            if(!medicine.getSideEffect4().isEmpty()){
                medicineFields.put("Side Effect4:", medicine.getSideEffect4());
                this.setResultField("Side Effect4:", medicine.getSideEffect4());
            }
            if(!medicine.getSideEffect5().isEmpty()){
                medicineFields.put("Side Effect5:", medicine.getSideEffect5());
                this.setResultField("Side Effect5:", medicine.getSideEffect5());
            }

            if(!medicine.getUse0().isEmpty()){
                medicineFields.put("Use1:", medicine.getUse0());
                this.setResultField("Use1:", medicine.getUse0());
            }
            if(!medicine.getUse1().isEmpty()){
                medicineFields.put("Use2:", medicine.getUse1());
                this.setResultField("Use2:", medicine.getUse1());
            }
            if(!medicine.getUse2().isEmpty()){
                medicineFields.put("Use3:", medicine.getUse2());
                this.setResultField("Use3:", medicine.getUse2());
            }
            if(!medicine.getUse3().isEmpty()){
                medicineFields.put("Use4:", medicine.getUse3());
                this.setResultField("Use4:", medicine.getUse3());
            }

            if(!medicine.getHabitForming().isEmpty()){
                medicineFields.put("Habit Forming:", medicine.getHabitForming());
                this.setResultField("Habit Forming:", medicine.getHabitForming());
            }
            if(!medicine.getActionClass().isEmpty()){
                medicineFields.put("Action Class:", medicine.getActionClass());
                this.setResultField("Action Class:", medicine.getActionClass());
            }
            if(!medicine.getChemicalClass().isEmpty()){
                medicineFields.put("Chemical Class:", medicine.getChemicalClass());
                this.setResultField("Chemical Class:", medicine.getChemicalClass());
            }
            if(!medicine.getTherapeuticClass().isEmpty()){
                medicineFields.put("TherapeuticClass:", medicine.getTherapeuticClass());
                this.setResultField("Therapeutic Class:", medicine.getTherapeuticClass());
            }
            this.entryList = new ArrayList<>(medicineFields.entrySet());
    }

    private void setResultField(String field, String fieldValue){
        ResultFields resultFields = new ResultFields(field, fieldValue);
        this.resultFieldsList.add(resultFields);
    }

    @NonNull
    @Override
    public ResultAdapter.ResultViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout_search_result, parent, false);
            return new ResultViewHolder(view, itemClickListener);
            }

    @Override
    public void onBindViewHolder(@NonNull ResultAdapter.ResultViewHolder holder, int position) {
            ResultFields resultFields = this.resultFieldsList.get(position);
            holder.bind(resultFields.getField(), resultFields.getFieldValue());
            }

    @Override
    public int getItemCount() {
            return this.medicineFields.size();
            }


    public static class ResultViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // View references go here
        private TextView itemMedicineField;
        private TextView itemMedicineValue;
        private ItemClickListener itemClickListener;
        public ResultViewHolder(@NonNull View itemView, ItemClickListener itemClickListener) {
            super(itemView);
            itemMedicineField = itemView.findViewById(R.id.itemMedicineField);
            itemMedicineValue = itemView.findViewById(R.id.itemMedicineValue);
            this.itemClickListener = itemClickListener;
            itemView.setOnClickListener(this);
        }

        public void bind(String field, String value) {
            itemMedicineField.setText(field);
            itemMedicineValue.setText(value);
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
