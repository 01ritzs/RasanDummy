package com.du.de.rasandummy.home;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.du.de.rasandummy.db.Category;

import java.util.List;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    private final List<Category> list;

    public ViewPagerAdapter(FragmentManager supportFragmentManager, List<Category> categories) {
        super(supportFragmentManager);
        this.list = categories;
    }

    @NonNull
    @Override
    public ProductFragment getItem(int position) {
        ProductFragment fragment = ProductFragment.getInstance();
        fragment.setProducts(list.get(position).getProducts());
        return fragment;
    }

    @Override
    public int getCount() {
        return (list != null && list.size() > 0) ? list.size() : 0;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return list.get(position).getTitle();
    }
}
