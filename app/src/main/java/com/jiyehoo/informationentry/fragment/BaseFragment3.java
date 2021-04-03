package com.jiyehoo.informationentry.fragment;

import android.content.ContentUris;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.jiyehoo.informationentry.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import static android.app.Activity.RESULT_OK;

/**
 * @author JiyeHoo
 * @date 21-1-5 上午10:36
 */
public class BaseFragment3 extends Fragment {
    private Uri imageUri;
    public static final int TAKE_PHOTO = 1;
    public static final int CHOOSE_PHOTO = 2;
    private ImageView mIvTakePhoto1;
    private ImageView mIvTakePhoto2;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.base_fragment_layout_3, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //绑定view
        mIvTakePhoto1 = view.findViewById(R.id.iv_take_1);
        mIvTakePhoto2 = view.findViewById(R.id.iv_take_2);

        mIvTakePhoto1.setOnClickListener(v -> {

        });

        mIvTakePhoto2.setOnClickListener(v -> {

        });
    }

}
