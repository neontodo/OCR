package com.neo.licensio.ui.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.neo.licensio.R;
import com.neo.licensio.data.models.LoggedUser;
import com.neo.licensio.data.models.Medicine;
import com.neo.licensio.data.models.TherapeuticClass;
import com.neo.licensio.network.ApiMedicineService;
import com.neo.licensio.network.ApiService;
import com.neo.licensio.network.JwtToken;
import com.neo.licensio.ui.activities.search.SearchResultActivity;
import com.neo.licensio.ui.adapters.MyAdapter;
import com.neo.licensio.ui.adapters.ItemClickListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment implements SearchView.OnQueryTextListener, ApiMedicineService.MedicineByNameCallback, ApiService.AddHistoryCallBack, ItemClickListener {

    private SearchView searchView;
    private RecyclerView recyclerView;
    private FloatingActionButton filterButton;
    private MyAdapter adapter;
    private List<Medicine> filteredList;

    private Medicine filteredMedicine;
    private List<String> selectedFilters;

    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_search, container, false);

        filterButton = rootView.findViewById(R.id.filterFloatingButton);
        // Initialize RecyclerView
        recyclerView = rootView.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        // Initialize SearchView and set its listener
        searchView = rootView.findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(this);
        searchView.clearFocus();

        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(SearchFragment.this.getActivity());
                LayoutInflater inflater = getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.dialog_filters, null);
                dialogBuilder.setView(dialogView);

                AlertDialog dialog = dialogBuilder.create();
                dialog.show();

                CheckBox habitFormingYes = dialogView.findViewById(R.id.checkBoxHabitYes);
                CheckBox habitFormingNo = dialogView.findViewById(R.id.checkBoxHabitNo);
                Button applyFiltersButton = dialogView.findViewById(R.id.applyFiltersButton);

                applyFiltersButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        LinearLayout checkboxContainer = dialogView.findViewById(R.id.checkbox_container);
                        int childCount = checkboxContainer.getChildCount();
                        selectedFilters = new ArrayList<>();

                        for (int i = 0; i < childCount; i++) {
                            View view = checkboxContainer.getChildAt(i);
                            if (view instanceof LinearLayout) {
                                LinearLayout linearLayout = (LinearLayout) view;
                                int checkBoxCount = linearLayout.getChildCount();

                                for (int j = 0; j < checkBoxCount; j++) {
                                    View checkBoxView = linearLayout.getChildAt(j);
                                    if (checkBoxView instanceof CheckBox) {
                                        CheckBox checkBox = (CheckBox) checkBoxView;
                                        // Access the checkbox and perform your desired operations
                                        String checkBoxText = checkBox.getText().toString();
                                        boolean isChecked = checkBox.isChecked();
                                        // Do something with the checkbox data
                                        if (isChecked) {
                                            // Add the checkbox text to your collection
                                            selectedFilters.add(checkBox.getText().toString());
                                        }
                                    }
                                }
                            }
                        }

                        //THERAPEUTIC CLASS CHECKING FILTERS
                        List<Medicine> filteredMedicine = new ArrayList<>();
                        if(selectedFilters != null) {
                            for (Medicine medicine : filteredList) {
                                //if (selectedFilters.contains(medicine.getTherapeuticClass())) {
                                if (containsIgnoreCase(selectedFilters, medicine)) {
                                    filteredMedicine.add(medicine);
                                }
                            }
                        } else {
                            filteredMedicine.addAll(filteredList);
                        }

                        //HABIT CHECKING FILTERS
                        boolean habitYes = false;
                        boolean habitNo = false;
                        if(habitFormingYes.isChecked()){
                            habitYes = true;
                        } else {
                            habitYes = false;
                        }
                        if(habitFormingNo.isChecked()){
                            habitNo = true;
                        } else {
                            habitNo = false;
                        }
                        if(habitYes || habitNo) {
                            List<Medicine> refilteredMedicine = new ArrayList<>();
                            for (Medicine medicine : filteredMedicine) {
                                if ((habitYes && medicine.getHabitForming().equals("Yes"))
                                        || (habitNo && medicine.getHabitForming().equals("No"))){
                                    refilteredMedicine.add(medicine);
                                }
                            }
                            adapter = new MyAdapter(refilteredMedicine, SearchFragment.this);
                        } else {
                            adapter = new MyAdapter(filteredMedicine, SearchFragment.this);
                        }
                        recyclerView.setAdapter(adapter);
                        Log.d("Tag", "SELECTED FILTERS:" + selectedFilters.size());
                        dialog.hide();
                    }
                });

            }
        });

        return rootView;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if(newText.length()>=3) {
            ApiMedicineService apiMedicineService = new ApiMedicineService();

            //call api
            apiMedicineService.makeGetMedicineByName(newText, this);
        }
        return true;
    }

    @Override
    public void onSuccess(List<Medicine> medicineList) {
        this.filteredList = medicineList;
        if(!filteredList.isEmpty()) {
            Activity activity = getActivity();
            if (activity != null) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(selectedFilters != null) {
                            List<Medicine> filteredMedicine = new ArrayList<>();
                            for (Medicine medicine : filteredList) {
                                //if (selectedFilters.contains(medicine.getTherapeuticClass())) {
                                if (containsIgnoreCase(selectedFilters, medicine)) {
                                    filteredMedicine.add(medicine);
                                }
                            }
                            adapter = new MyAdapter(filteredMedicine, SearchFragment.this);
                        } else {
                            adapter = new MyAdapter(filteredList, SearchFragment.this);
                        }
                        recyclerView.setAdapter(adapter);
                    }
                });
            }
        }
    }

    public static boolean containsIgnoreCase(List<String> selectedFilters, Medicine medicine) {
        for (String item : selectedFilters) {
            if (item.equalsIgnoreCase(medicine.getTherapeuticClass())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onError(String errorMessage) {

    }

    @Override
    public void onFailure(IOException e) {

    }

    @Override
    public void onItemClick(int position) {
        Medicine clickedMedicine = filteredList.get(position);
        ApiService apiService = new ApiService();
        apiService.setClient(JwtToken.getJwtToken());

        apiService.makeAddHistory(LoggedUser.getCurrentUser().getUserId(), clickedMedicine.getId(), this);
        Intent intent = new Intent(getActivity(), SearchResultActivity.class);

        intent.putExtra("Medicine", filteredList.get(position));

        startActivity(intent);
    }

    @Override
    public void onSuccess() {
        Log.d("Tag", "History Has Been Added");
    }

}