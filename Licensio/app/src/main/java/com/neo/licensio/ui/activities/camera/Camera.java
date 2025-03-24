package com.neo.licensio.ui.activities.camera;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ImageWriter;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.googlecode.tesseract.android.BuildConfig;
import com.googlecode.tesseract.android.TessBaseAPI;
import com.neo.licensio.R;
import com.neo.licensio.data.models.Medicine;
import com.neo.licensio.network.ApiMedicineService;
import com.neo.licensio.ui.activities.search.SearchResultActivity;

import org.opencv.android.Utils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class Camera extends AppCompatActivity implements ApiMedicineService.MedicineByNameCallback{


    public static final int CAMERA_PERMISSION_CODE = 101;
    public static final int CAMERA_REQUEST_CODE = 102;
    private ImageView prescriptionImage;
    private Button takePictureButton;
    private Button backButton;

    private ActivityResultLauncher<Intent> cameraLauncher;

    //added
    public static final String TESS_DATA = "/tessdata";
    private static final String TAG = Camera.class.getSimpleName();
    private static final String DATA_PATH = Environment.getExternalStorageDirectory().toString() + "/Tess";
    private TessBaseAPI tessBaseAPI;
    private Uri outputFileDir;
    private String mCurrentPhotoPath;
    private File photoFile;
    private Medicine prescriptionMedicine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        //load elements
        prescriptionImage = findViewById(R.id.prescriptionImageView);
        takePictureButton = findViewById(R.id.takePictureButton);
        backButton = findViewById(R.id.backButtonPrescr);

        final Activity activity = this;

        takePictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPermission();
            }
        });

        cameraLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == RESULT_OK) {
                    // Get the captured image from the intent data
                    Bitmap imageBitmap = BitmapFactory.decodeFile(mCurrentPhotoPath);

                    // Display the captured image in the ImageView
                    prescriptionImage.setImageBitmap(imageBitmap);

                    Log.d("Tag", "ABOUT TO PREPARE THE TESS DATA");
                    prepareTessData();
                    startOCR(outputFileDir);
                }
            }
        });

        //back button to bring back to Homepage Activity
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void askCameraPermissions(){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
        } else {
            //openCamera();
            dispatchTakePictureIntent();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //openCamera();
                dispatchTakePictureIntent();
            }
        }
    }

    private void openCamera(){
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Log.d("Tag", "CREATED THE INTENT" + "Intent: " + cameraIntent.resolveActivity(getPackageManager()));
        cameraLauncher.launch(cameraIntent);
    }

    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (!Environment.isExternalStorageManager()) {
                Log.d("Tag", "REQUESTING NEW PERMISSION");
                // Request permission to manage external storage
                Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                intent.setData(Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, 120);
            } else {
                askCameraPermissions();
            }
        } else {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                Log.d("Tag", "REQUESTING PERMISSIONS");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 120);
            } else {
                Log.d("Tag", "STARTING askCameraPermission()");
                askCameraPermissions();
            }
        }
    }


    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Log.d("Tag", "CREATED THE INTENT" + "Intent: " + takePictureIntent.resolveActivity(getPackageManager()));
        // Ensure that there's a camera activity to handle the intent
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
                this.photoFile = photoFile;
            } catch (IOException ex) {
                // Error occurred while creating the File
                ex.printStackTrace();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        BuildConfig.APPLICATION_ID + ".provider",
                        photoFile);
                Log.d("Tag", photoURI.toString());
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                //startActivityForResult(takePictureIntent, 1024);
                cameraLauncher.launch(takePictureIntent);
            }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "licensio");
        if(!storageDir.exists()){
            storageDir.mkdirs();
        }
        /*File image = File.createTempFile(
                imageFileName,  *//* prefix *//*
                ".jpg",         *//* suffix *//*
                storageDir      *//* directory *//*
        );*/

        File image = new File(storageDir, imageFileName + ".jpg");

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1024) {
            if (resultCode == Activity.RESULT_OK) {
                Log.d("Tag", "ABOUT TO PREPARE THE TESS DATA");
                prepareTessData();
                startOCR(outputFileDir);
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(getApplicationContext(), "Result canceled.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "Activity result failed.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void prepareTessData(){
        try{
            File dir = getExternalFilesDir(TESS_DATA);
            if(!dir.exists()){
                if (!dir.mkdir()) {
                    Toast.makeText(getApplicationContext(), "The folder " + dir.getPath() + "was not created", Toast.LENGTH_SHORT).show();
                }
            }
            String fileList[] = getAssets().list("");
            for(String fileName : fileList){
                String pathToDataFile = dir + "/" + fileName;
                if(!(new File(pathToDataFile)).exists()){
                    InputStream in = getAssets().open(fileName);
                    OutputStream out = new FileOutputStream(pathToDataFile);
                    byte [] buff = new byte[1024];
                    int len ;
                    while(( len = in.read(buff)) > 0){
                        out.write(buff,0,len);
                    }
                    in.close();
                    out.close();
                }
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

    //THE IMPORTANT FUNCTION -------------------------------------------------------------------------------------------
    private void startOCR(Uri imageUri){
        Boolean renamed = false;
        try{
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = false;
            //options.inSampleSize = 4;
            options.inSampleSize = 1;
            Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, options);
            String result = this.getText(bitmap);
            prescriptionImage.setImageBitmap(bitmap);
            Log.d("Tag", "THE SCANNED TEXT IS: " + result);
            String medicine = getMedicineFromText(result);

            //when medicine is found in the taken image
            if(!medicine.equals("Could not find the medicine")){
                Log.d("CHECKER", "DID FIND MEDICINE");
                renameFileToMedicine(medicine);
                ApiMedicineService apiMedicineService = new ApiMedicineService();
                Log.d("CHECKER", "CREATED MEDICINE API SERVICE");
                Log.d("CHECKER", "FOUND MEDICINE: " + medicine);
                apiMedicineService.makeGetMedicineByName(medicine, Camera.this);
            } else {
                //not a valid medicine will result in the deletion of the image (prevent wasting storage)
                File imageFile = new File(mCurrentPhotoPath);
                if(imageFile.exists()){
                    imageFile.delete();
                }
            }
        }catch (Exception e){
            Log.e(TAG, e.getMessage());
        }
    }

    private String getText(Bitmap bitmap){
        try{
            tessBaseAPI = new TessBaseAPI();
        }catch (Exception e){
            Log.e(TAG, e.getMessage());
        }
        tessBaseAPI.setVariable(TessBaseAPI.VAR_CHAR_WHITELIST, "aAbBcCdDeEfFgGhHiIjJkKlLmMnNoOpPqQrRsStTuUvVwWxXyYzZ'");
        tessBaseAPI.setVariable(TessBaseAPI.VAR_CHAR_BLACKLIST, "0123456789,.;'[]`~_+-!@#$%^&*()?><:");
        //tessBaseAPI.setPageSegMode(TessBaseAPI.PageSegMode.PSM_AUTO_OSD);
        tessBaseAPI.setPageSegMode(TessBaseAPI.PageSegMode.PSM_SPARSE_TEXT);
        String dataPath = getExternalFilesDir("/").getPath() + "/";
        tessBaseAPI.init(dataPath, "eng");
        bitmap = applyFilters(bitmap);
        tessBaseAPI.setImage(bitmap);
        String retStr = "No result";
        try{
            retStr = tessBaseAPI.getUTF8Text();
        }catch (Exception e){
            Log.e(TAG, e.getMessage());
        }
        tessBaseAPI.end();
        return retStr;
    }

    public String getMedicineFromText(String result){
        String[] lines = result.split("\n");
        String prescription = "prescr";

        for (int i = 0; i < lines.length - 1; i++) {
            if (lines[i].toLowerCase().contains(prescription) && !lines[i+1].isEmpty()) {
                return lines[i + 1].toLowerCase().replaceAll("[-+.^:,]", "");
            } else if(lines[i].toLowerCase().contains(prescription) && lines[i+1].isEmpty()) {
                return lines[i + 2].toLowerCase().replaceAll("[-+.^:,]", "");
            }
        }

        return "Could not find the medicine";
    }

    //rename image file to contain data regarding the found medicine (name of medicine)
    private void renameFileToMedicine(String prescriptionText){
        File storageDir = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "licensio");
        if(!storageDir.exists()){
            storageDir.mkdirs();
        }
        File newFileName = new File(storageDir, prescriptionText.replace(" ", "_") + ".jpeg");

        try {
            // Load the existing image from photoFile
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(photoFile.getAbsolutePath(), options);

            // Calculate the inSampleSize to reduce image dimensions
            options.inSampleSize = calculateInSampleSize(options, 200, 200);
            options.inJustDecodeBounds = false;

            Bitmap imageBitmap = BitmapFactory.decodeFile(photoFile.getAbsolutePath(), options);

            // Compress and save the image
            float compressionQuality = 0.7f; // Adjust the compression quality here (0.0f to 1.0f)
            FileOutputStream outputStream = new FileOutputStream(newFileName);
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, (int) (compressionQuality * 100), outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Rename the file
        Boolean renamed = photoFile.renameTo(newFileName);
    }

    private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            while ((halfHeight / inSampleSize) >= reqHeight && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }

    //Use OPENCV for image pre-processing
    public static Bitmap applyFilters(Bitmap inputBitmap) {
        Mat matInput = new Mat(inputBitmap.getHeight(), inputBitmap.getWidth(), CvType.CV_8UC1);
        Utils.bitmapToMat(inputBitmap, matInput);

        Mat matOutput = new Mat();

        // Convert the image to grayscale
        Imgproc.cvtColor(matInput, matOutput, Imgproc.COLOR_BGR2GRAY);

        // Apply thresholding
        Imgproc.threshold(matOutput, matOutput, 0, 255, Imgproc.THRESH_BINARY | Imgproc.THRESH_OTSU);

        // Apply Gaussian blur
        Imgproc.GaussianBlur(matOutput, matOutput, new Size(5, 5), 0);

        // Convert the processed Mat back to Bitmap
        Bitmap outputBitmap = Bitmap.createBitmap(matOutput.cols(), matOutput.rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(matOutput, outputBitmap);

        return outputBitmap;
    }


    @Override
    public void onSuccess(List<Medicine> medicine) {
        if(!medicine.isEmpty()) {
            this.prescriptionMedicine = medicine.get(0);
        } else {
            Log.d("CHECKER", "NO MEDICINE IN THE LIST");
        }
        if(Objects.nonNull(prescriptionMedicine)){
            Intent intent = new Intent(Camera.this, SearchResultActivity.class);
            intent.putExtra("Medicine", this.prescriptionMedicine);
            Camera.this.startActivity(intent);
        }
    }

    @Override
    public void onError(String errorMessage) {

    }

    @Override
    public void onFailure(IOException e) {

    }
}