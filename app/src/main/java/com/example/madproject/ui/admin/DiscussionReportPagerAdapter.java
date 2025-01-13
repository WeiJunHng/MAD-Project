package com.example.madproject.ui.admin;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class DiscussionReportPagerAdapter extends FragmentStateAdapter {
    public DiscussionReportPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new ManageContentReportedFragment();
            case 1:
                return new ManageContentReportedFragment();
//            case 2:
//                return new AllNotificationsFragment();
            default:
                return new ManageContentReportedFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
