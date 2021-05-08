package com.du.de.rasandummy.ItemFragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.du.de.rasandummy.R;

public class ChildFragment extends Fragment {

    private final String message;

    public ChildFragment(String message) {
        this.message = message;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle saveInstanceState) {
        super.onCreateView(inflater,container,saveInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_1_layout, container, false);
        TextView tvFragment = rootView.findViewById(R.id.tvFragment);
        tvFragment.setText(message);
        return rootView;
    }
}
