package com.neo.licensio.ui.adapters;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.neo.licensio.R;
import com.neo.licensio.data.models.Medicine;
import com.neo.licensio.network.ApiMedicineService;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CollectionGridAdapter extends BaseAdapter{
    private List<String> imagePaths;
    private Handler handler;

    public CollectionGridAdapter(Handler handler) {
        this.imagePaths = new ArrayList<>();
        this.handler = handler;
    }

    public void addImage(String path) {
        imagePaths.add(path);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if(!imagePaths.isEmpty()) {
            return imagePaths.size();
        } else {
            return 0;
        }
    }

    @Override
    public Object getItem(int position) {
        if(!imagePaths.isEmpty()){
            return imagePaths.get(position);
        } else {
            return null;
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView prescriptionImage;
        TextView medicineName;
        TextView medicineCategory;

        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.collections_gridview_item, parent, false);
        }

        prescriptionImage = convertView.findViewById(R.id.prescriptionImageView);
        medicineName = convertView.findViewById(R.id.prescriptionNameTextView);
        medicineCategory = convertView.findViewById(R.id.prescriptionCategoryTextView);

        String imagePath = (String) getItem(position);

        if (imagePath != null) {
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            prescriptionImage.setImageBitmap(bitmap);
            File image = new File(imagePath);
            String fileName = image.getName();
            fileName = fileName.substring(0, fileName.lastIndexOf("."));
            fileName = fileName.replace("_", " ");
            ApiMedicineService apiMedicineService = new ApiMedicineService();
            apiMedicineService.makeGetMedicineByName(fileName, new ApiMedicineService.MedicineByNameCallback() {
                @Override
                public void onSuccess(List<Medicine> medicine) {
                    /*Medicine med = medicine.get(0);
                    Log.d("Tag", med.getName());
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            medicineName.setText(med.getName());
                            medicineCategory.setText(med.getTherapeuticClass());
                        }
                    });*/
                    Medicine med = medicine.get(0);
                    Log.d("Tag", med.getName());

                    TextViewsHolder holder = new TextViewsHolder();
                    holder.medicineName = medicineName;
                    holder.medicineCategory = medicineCategory;

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            setTextViews(holder, med.getName(), med.getTherapeuticClass());
                        }
                    });
                }

                @Override
                public void onError(String errorMessage) {

                }

                @Override
                public void onFailure(IOException e) {

                }
            });
        }
        return convertView;
    }

    private void setTextViews(TextViewsHolder holder, String medicineName, String medicineCategory) {
        holder.medicineName.setText(medicineName);
        holder.medicineCategory.setText(medicineCategory);
    }

    private static class TextViewsHolder {
        TextView medicineName;
        TextView medicineCategory;
    }
}
