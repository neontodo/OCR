package com.neo.licensio.network;

import androidx.annotation.NonNull;
import com.neo.licensio.data.models.Medicine;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ApiMedicineService {

    private OkHttpClient client;

    public ApiMedicineService() {
        client = new OkHttpClient.Builder()
                .addInterceptor(new JwtInterceptor(JwtToken.getJwtToken()))
                .build();
    }

    public void makeGetMedicineByName(String name, MedicineByNameCallback callback){
        String url = "http://localhost:8000/api/medicine/filter/" + name;
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
                //make a toast of failure
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()){
                    String myResponse = response.body().string();
                    try{
                        // Assuming the response is in JSON format
                        JSONArray jsonArray = new JSONArray(myResponse);

                        List<Medicine> medicineList = new ArrayList<>();

                        for(int i=0; i<jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);

                            // Extract the user data from the JSON response
                            Long medicineId = jsonObject.getLong("id");
                            String name = jsonObject.getString("name");

                            String substitute0 = jsonObject.getString("substitute0");
                            String substitute1 = jsonObject.getString("substitute1");
                            String substitute2 = jsonObject.getString("substitute2");
                            String substitute3 = jsonObject.getString("substitute3");
                            String substitute4 = jsonObject.getString("substitute4");

                            String sideEffect1 = jsonObject.getString("sideEffect1");
                            String sideEffect2 = jsonObject.getString("sideEffect2");
                            String sideEffect3 = jsonObject.getString("sideEffect3");
                            String sideEffect4 = jsonObject.getString("sideEffect4");
                            String sideEffect5 = jsonObject.getString("sideEffect5");

                            String use0 = jsonObject.getString("use0");
                            String use1 = jsonObject.getString("use1");
                            String use2 = jsonObject.getString("use2");
                            String use3 = jsonObject.getString("use3");

                            String chemicalClass = jsonObject.getString("chemical_class");
                            String habitForming = jsonObject.getString("habit_forming");
                            String therapeuticClass = jsonObject.getString("therapeutic_class");
                            String actionClass = jsonObject.getString("action_class");


                            //create the medicine object
                            Medicine medicine = new Medicine();
                            medicine.setId(medicineId);
                            medicine.setName(name);

                            medicine.setSubstitute0(substitute0);
                            medicine.setSubstitute1(substitute1);
                            medicine.setSubstitute2(substitute2);
                            medicine.setSubstitute3(substitute3);
                            medicine.setSubstitute4(substitute4);

                            medicine.setSideEffect1(sideEffect1);
                            medicine.setSideEffect2(sideEffect2);
                            medicine.setSideEffect3(sideEffect3);
                            medicine.setSideEffect4(sideEffect4);
                            medicine.setSideEffect5(sideEffect5);

                            medicine.setUse0(use0);
                            medicine.setUse1(use1);
                            medicine.setUse2(use2);
                            medicine.setUse3(use3);

                            medicine.setChemicalClass(chemicalClass);
                            medicine.setHabitForming(habitForming);
                            medicine.setTherapeuticClass(therapeuticClass);
                            medicine.setActionClass(actionClass);

                            medicineList.add(medicine);
                        }
                        callback.onSuccess(medicineList);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
    }

    public interface MedicineByNameCallback {
        void onSuccess(List<Medicine> medicine);
        void onError(String errorMessage);
        void onFailure(IOException e);
    }

}
