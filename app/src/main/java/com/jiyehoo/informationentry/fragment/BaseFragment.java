package com.jiyehoo.informationentry.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.jiyehoo.informationentry.R;

/**
 * @author JiyeHoo
 * @date 21-1-1 下午10:55
 */
public class BaseFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.base_fragment_layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //绑定view
        ImageView startLocation = view.findViewById(R.id.start_location);
        EditText locationText = view.findViewById(R.id.location_text);
        startLocation.setOnClickListener(v -> {
            //todo 定位
            locationText.setText("100,110");
        });

        Button mBtnNext = view.findViewById(R.id.btn_base_1_next);
        mBtnNext.setOnClickListener(v -> {
            //切换
            ViewPager2 viewPager = getActivity().findViewById(R.id.view_pager_2);
            viewPager.setCurrentItem(1);
        });
    }
}
