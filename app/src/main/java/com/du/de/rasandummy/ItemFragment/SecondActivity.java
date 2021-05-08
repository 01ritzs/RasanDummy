package com.du.de.rasandummy.ItemFragment;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.du.de.rasandummy.R;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class SecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second_activity);

        ViewPager viewPager = findViewById(R.id.vp);
        List<Fragment> listFragment = new ArrayList<>();
        listFragment.add(new ChildFragment("First"));
        listFragment.add(new ChildFragment("Second"));
        listFragment.add(new ChildFragment("Third"));
        listFragment.add(new ChildFragment("Fourth"));
        listFragment.add(new ChildFragment("Fifth"));
        listFragment.add(new ChildFragment("Sixth"));
        listFragment.add(new ChildFragment("Seventh"));
        listFragment.add(new ChildFragment("Eighth"));
        viewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager(), listFragment));

        TabLayout tabLayout = findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);
    }
}
