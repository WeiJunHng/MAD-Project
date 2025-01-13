package com.example.madproject.ui.admin;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.madproject.R;
import com.example.madproject.data.model.Report;
import com.example.madproject.data.repository.DiscussionRepository;
import com.example.madproject.data.repository.ReportRepository;
import com.example.madproject.databinding.FragmentManageContentBinding;
import com.example.madproject.ui.ViewModelFactory;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.List;

public class ManageContentFragment extends Fragment {

    private FragmentManageContentBinding binding;
//    private ReportListAdapter reportListAdapter;
    private DiscussionRepository discussionRepository;
    private ReportRepository reportRepository;
    private AdminViewModel adminViewModel;
//    private List<Report> reportList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        discussionRepository = new DiscussionRepository(requireContext());
        reportRepository = new ReportRepository(requireContext());
        discussionRepository.fetchDiscussions();
        reportRepository.fetchReports();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentManageContentBinding.inflate(inflater, container, false);

        ViewModelFactory factory = new ViewModelFactory(getContext());
        adminViewModel = new ViewModelProvider(requireActivity(),factory).get(AdminViewModel.class);

        View root = binding.getRoot();

//        reportList = reportRepository.getAllReport();
//        reportListAdapter = new ReportListAdapter(getActivity(), getContext(), reportList);
//
//        recyclerViewUserList.setLayoutManager(new LinearLayoutManager(getContext()));
//        recyclerViewUserList.setAdapter(reportListAdapter);

        adminViewModel.getReportMutableLiveData().observe(getViewLifecycleOwner(), report -> {
            if(report != null) {
                FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.AdminFCV, new ManageContentDetailsFragment());
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        TabLayout tabLayout = binding.tabLayout;
        ViewPager2 viewPager = binding.viewPager;

        DiscussionReportPagerAdapter adapter = new DiscussionReportPagerAdapter(requireActivity());
        viewPager.setAdapter(adapter);

        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            switch (position) {
                case 0:
                    tab.setText("Reported");
                    tab.setContentDescription("View reported discussions");
                    break;
                case 1:
                    tab.setText("Removed");
                    tab.setContentDescription("View removed discussions");
                    break;
            }
        }).attach();

        return root;
    }
}