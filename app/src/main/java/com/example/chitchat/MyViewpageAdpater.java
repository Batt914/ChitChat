package com.example.chitchat;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class MyViewpageAdpater extends FragmentStateAdapter {
    public MyViewpageAdpater(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    /**
     * @param position
     * @return
     */
    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new Recently_Played_Fragment();
            case 1:
                return new PlayList_Fragment();
            default:
                return new Recently_Played_Fragment();
        }
    }

    /**
     * @return
     */
    @Override
    public int getItemCount() {
        return 2;
    }
}
