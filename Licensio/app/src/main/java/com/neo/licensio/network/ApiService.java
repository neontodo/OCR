package com.neo.licensio.network;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.nfc.Tag;
import android.util.Log;

import androidx.annotation.NonNull;

import com.neo.licensio.data.models.Medicine;
import com.neo.licensio.data.models.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ApiService {
    private OkHttpClient client;

    public ApiService() {
        client = new OkHttpClient();
    }

    public void setClient(String jwt){
        this.client = new OkHttpClient.Builder()
                .addInterceptor(new JwtInterceptor(jwt))
                .build();
    }

    public void makeGetAuthentication(String username, String password, UserCallBack callBack){
        // Create the request body
        RequestBody requestBody = RequestBody.create(MediaType
                .parse("application/json"), "{\"username\":\"" + username + "\", \"password\":\"" + password + "\"}");

        //url to access authentication
        //String url = "http://192.168.1.131:8000/authenticate";
        String url = "http://localhost:8000/authenticate";
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
                //make a toast of failure
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String jwtToken = "";
                if (response.isSuccessful()){
                    //receive jwt token
                    String myResponse = response.body().string();
                    try {
                        JSONObject jsonObject = new JSONObject(myResponse);
                        jwtToken = jsonObject.getString("jwttoken");
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                    Boolean authenticated = true;
                    setClient(jwtToken);
                    JwtToken.setJwtToken(jwtToken);
                    callBack.onSuccess(authenticated, jwtToken);
                }
            }
        });
    }

    public void makeRegisterAccount(String fullName, String username, String emailAddress, String password, RegisterCallback callBack){
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"),
                "{\"fullName\":\"" + fullName + "\"," +
                        " \"username\":\"" + username + "\"," +
                        " \"emailAddress\":\"" + emailAddress + "\"," +
                        " \"password\":\"" + password + "\"}");
        String url = "http://localhost:8000/api/users/register-user";
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
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
                    Log.d(TAG, myResponse);
                    callBack.onSuccess(true);
                }
            }
        });
    }

    public void makeGetUserByUsername(String username, UserByUsernameCallback callback){
        //String url = "http://192.168.1.131:8000/api/users/find-user-by-username/" + username;
        String url = "http://localhost:8000/api/users/find-user-by-username/" + username;
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
                        JSONObject jsonObject = new JSONObject(myResponse);

                        // Extract the user data from the JSON response
                        Long userId = jsonObject.getLong("userId");
                        String fullName = jsonObject.getString("fullName");
                        String username = jsonObject.getString("username");
                        String email = jsonObject.getString("emailAddress");
                        String password = jsonObject.getString("password");
                        Boolean verified = jsonObject.getBoolean("verified");

                        // Create a new User object
                        User user = new User();
                        user.setUserId(userId);
                        user.setFullName(fullName);
                        user.setUsername(username);
                        user.setEmailAddress(email);
                        user.setPassword(password);
                        user.setVerified(verified);

                        callback.onSuccess(user);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
    }

    public void makeGetUserByEmail(String email, UserByEmailCallback callback){
        String url = "http://localhost:8000/api/users/find-user-by-email/" + email;
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
                    Long userId;
                    try{
                        // Assuming the response is in JSON format
                        JSONObject jsonObject = new JSONObject(myResponse);
                        if(!jsonObject.getString("emailAddress").isEmpty()){
                            userId = jsonObject.getLong("userId");
                        } else {
                            userId = null;
                        }
                        callback.onSuccess(userId);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
    }

    public void makeFullUpdateAccount(User user, UpdateCallback callBack){
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"),
                "{\"fullName\":\"" + user.getFullName() + "\"," +
                        " \"username\":\"" + user.getUsername() + "\"," +
                        " \"emailAddress\":\"" + user.getEmailAddress() + "\"," +
                        " \"password\":\"" + user.getPassword() + "\"}");
        String url = "http://localhost:8000/api/users/update-user/" + user.getUserId();
        Request request = new Request.Builder()
                .url(url)
                .put(requestBody)
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
                    Log.d(TAG, myResponse);
                    callBack.onSuccess(true);
                }
            }
        });
    }

    public void makePartialUpdateAccount(Long userId, Map<String, String> changes, UpdateCallback callBack){
        // Build the request body
        FormBody.Builder formBuilder = new FormBody.Builder();
        for (Map.Entry<String, String> entry : changes.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            formBuilder.add(key, value);
        }
        RequestBody requestBody = formBuilder.build();

        String url = "http://localhost:8000/api/users/user-patch/" + userId;
        Request request = new Request.Builder()
                .url(url)
                .patch(requestBody)
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
                    Log.d(TAG, myResponse);
                    callBack.onSuccess(true);
                }
            }
        });
    }

    public void makeDeleteUser(Long userId, DeleteCallback callback){
        String url = "http://localhost:8000/api/users/delete-user/" + userId;
        Request request = new Request.Builder()
                .url(url)
                .delete()
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
                        callback.onSuccess(true);
                }
            }
        });
    }

    public void makeGetHistoryMedicineByUserId(Long userId, HistoryByUserId callback){
        String url = "http://localhost:8000/api/history/find-history-by-userId/" + userId;
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()){
                    String myResponse = response.body().string();
                    try {
                        JSONArray jsonArray = new JSONArray(myResponse);
                        List<Medicine> medicineList = new ArrayList<>();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);

                            // Extract the user data from the JSON response
                            Long medicineId = Long.valueOf(jsonObject.getInt("id"));
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

    public void makeSubmitFeedback(String feedbackText, String feedbackCategory, Long userId, FeedbackCallback callBack){
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"),
                "{\"feedbackText\":\"" + feedbackText + "\"," +
                        " \"feedbackCategory\":\"" + feedbackCategory + "\"}");

        String url = "http://localhost:8000/api/feedback/submit-feedback";
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
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
                    Log.d(TAG, myResponse);
                    callBack.onSuccess();
                }
            }
        });
    }

    public void makeAddHistory(Long userId, Long medicineId,  AddHistoryCallBack callBack){
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"),
                "{\"userId\":\"" + userId + "\"," +
                        " \"medicineId\":\"" + medicineId + "\"}");
        String url = "http://localhost:8000/api/history/add-history";
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
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
                    Log.d(TAG, myResponse);
                    callBack.onSuccess();
                }
            }
        });
    }


    public interface UserCallBack {
        void onSuccess(Boolean authenticated, String jwtToken);
        void onError(String errorMessage);
        void onFailure(IOException e);
    }

    public interface RegisterCallback {
        void onSuccess(Boolean registered);
        void onError(String errorMessage);
        void onFailure(IOException e);
    }

    public interface UpdateCallback {
        void onSuccess(Boolean updated);
        void onError(String errorMessage);
        void onFailure(IOException e);
    }

    public interface UserByUsernameCallback {
        void onSuccess(User user);
        void onError(String errorMessage);
        void onFailure(IOException e);
    }

    public interface UserByEmailCallback {
        void onSuccess(Long userId);
        void onError(String errorMessage);
        void onFailure(IOException e);
    }

    public interface DeleteCallback {
        void onSuccess(Boolean deleted);
        void onError(String errorMessage);
        void onFailure(IOException e);
    }

    public interface HistoryByUserId {
        void onSuccess(List<Medicine> historyMedicine);
    }

    public interface FeedbackCallback{
        void onSuccess();
    }

    public interface AddHistoryCallBack{
        void onSuccess();
    }
}
