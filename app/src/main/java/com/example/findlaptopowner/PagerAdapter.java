package com.example.findlaptopowner;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class PagerAdapter extends FragmentStatePagerAdapter {

    private int num;

    public PagerAdapter(FragmentManager fm, int num)
    {
        super(fm, num);
        this.num = num;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        if (position == 1) {
            return new EncodeQR();
        }

        return new DecodeQR();
    }


    @Override
    public int getCount() {
        return num;
    }
}
