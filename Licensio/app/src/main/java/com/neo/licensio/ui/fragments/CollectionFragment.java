package com.neo.licensio.ui.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.neo.licensio.R;
import com.neo.licensio.ui.adapters.CollectionGridAdapter;

import java.io.File;

public class CollectionFragment extends Fragment {

    private static final String LICENSIO_DIRECTORY_PATH = "/storage/emulated/0/Android/data/com.neo.licensio/files/Pictures/licensio";
    private GridView collectionGridView;

    private ImageView prescriptionImage;
    private TextView prescriptionName;
    private TextView prescriptionCategory;
    private CollectionGridAdapter collectionGridAdapter;

    public CollectionFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_collection, container, false);

        collectionGridView = rootView.findViewById(R.id.collectionGridView);

        Handler mainHandler = new Handler(Looper.getMainLooper());
        collectionGridAdapter = new CollectionGridAdapter(mainHandler);
        collectionGridView.setAdapter(collectionGridAdapter);

        File licensioDirectory = new File(LICENSIO_DIRECTORY_PATH);

        if (licensioDirectory.exists() && licensioDirectory.isDirectory()) {
            File[] files = licensioDirectory.listFiles();

            if (files != null) {
                for (File file : files) {
                    if (file.isFile() && isPictureFile(file.getName())) {
                        collectionGridAdapter.addImage(file.getAbsolutePath());
                    }
                }
            }
        }

        collectionGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                File licensioDirectory = new File(LICENSIO_DIRECTORY_PATH);

                if (licensioDirectory.exists() && licensioDirectory.isDirectory()) {
                    File[] files = licensioDirectory.listFiles();
                    if (files != null){
                        //Uri imageUri = Uri.fromFile(files[position]);
                        Uri imageUri = FileProvider.getUriForFile(view.getContext(), "com.neo.licensio.fileprovider", files[position]);

                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_VIEW);
                        intent.setDataAndType(imageUri, "image/*");
                        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                        startActivity(intent);
                    }
                }
            }
        });
        return rootView;
    }

    private boolean isPictureFile(String filename) {
        return filename.endsWith(".png") || filename.endsWith(".jpeg");
    }
}