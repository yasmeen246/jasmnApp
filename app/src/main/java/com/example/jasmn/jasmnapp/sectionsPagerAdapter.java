package com.example.jasmn.jasmnapp;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class sectionsPagerAdapter extends FragmentPagerAdapter {
    public sectionsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
       switch (position){
           case 0:
               return new Request_fragment();
           case 1:
               return new chatFragment();
           case 2:
               return new frindsFragment();
            default:
                return null;
       }

    }

    @Override
    public int getCount() {
        return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:
                return "REQUESTS";
            case 1:
                return "CHAT";
            case 2:
                return "FRIENDS";
            default:
                return null;
        }
    }
}
