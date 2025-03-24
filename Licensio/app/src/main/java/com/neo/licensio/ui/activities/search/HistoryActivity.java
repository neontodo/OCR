package com.neo.licensio.ui.activities.search;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.neo.licensio.R;
import com.neo.licensio.data.models.LoggedUser;
import com.neo.licensio.data.models.Medicine;
import com.neo.licensio.network.ApiMedicineService;
import com.neo.licensio.network.ApiService;
import com.neo.licensio.network.JwtToken;
import com.neo.licensio.ui.adapters.HistoryAdapter;
import com.neo.licensio.ui.adapters.ItemClickListener;

import java.util.Collections;
import java.util.List;

public class HistoryActivity extends AppCompatActivity implements ApiService.HistoryByUserId, ItemClickListener {

    private RecyclerView historyRecyclerView;
    private List<Medicine> historyMedicineList;
    private HistoryAdapter historyAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        historyRecyclerView = findViewById(R.id.historyRecyclerView);
        historyRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        ApiService apiService = new ApiService();
        apiService.setClient(JwtToken.getJwtToken());

        apiService.makeGetHistoryMedicineByUserId(LoggedUser.getCurrentUser().getUserId(), this);
    }

    @Override
    public void onSuccess(List<Medicine> historyMedicine) {
        this.historyMedicineList = historyMedicine;
        if(!historyMedicineList.isEmpty()){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Collections.reverse(historyMedicineList);
                    historyAdapter = new HistoryAdapter(historyMedicineList, HistoryActivity.this);
                    historyRecyclerView.setAdapter(historyAdapter);
                }
            });
        }
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(this, SearchResultActivity.class);

        intent.putExtra("Medicine", historyMedicineList.get(position));

        startActivity(intent);
    }
}