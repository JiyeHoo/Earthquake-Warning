package com.jiyehoo.informationentry.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.jiyehoo.informationentry.R;

/**
 * @author JiyeHoo
 * @date 21-1-1 下午10:55
 */
public class BaseFragment2 extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.base_fragment_layout_2, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //绑定view
        Button mBtnNext = view.findViewById(R.id.btn_base_2_next);
        mBtnNext.setOnClickListener(v -> {
            //提交
        });
    }


}
