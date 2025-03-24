package com.neo.licensio.ui.activities.search;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ClipData;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.neo.licensio.R;
import com.neo.licensio.data.models.Medicine;
import com.neo.licensio.ui.adapters.ItemClickListener;
import com.neo.licensio.ui.adapters.ResultAdapter;

public class SearchResultActivity extends AppCompatActivity implements ItemClickListener {

    private Button backButton;
    private TextView medicineName;
    private RecyclerView recyclerView;
    private ResultAdapter resultAdapter;
    private Medicine medicine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);

        medicine = getIntent().getParcelableExtra("Medicine");

        medicineName = findViewById(R.id.medicineNameTextView);
        recyclerView = findViewById(R.id.resultRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        medicineName.setText(medicine.getName());

        resultAdapter = new ResultAdapter(medicine, this);
        recyclerView.setAdapter(resultAdapter);

        backButton = findViewById(R.id.backButton);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onItemClick(int position) {

    }
}