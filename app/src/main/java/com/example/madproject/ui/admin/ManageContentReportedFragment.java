package com.example.madproject.ui.admin;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.madproject.data.model.Report;
import com.example.madproject.data.repository.ReportRepository;
import com.example.madproject.databinding.FragmentManageContentReportedBinding;
import com.example.madproject.ui.ViewModelFactory;

import java.util.List;

public class ManageContentReportedFragment extends Fragment {

    private FragmentManageContentReportedBinding binding;
    private RecyclerView recyclerView;
    private ReportRepository reportRepository;
    private AdminViewModel adminViewModel;
    private ReportListAdapter reportListAdapter;
    private List<Report> reportList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        reportRepository = new ReportRepository(requireContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentManageContentReportedBinding.inflate(inflater, container, false);

        ViewModelFactory factory = new ViewModelFactory(getContext());
        adminViewModel = new ViewModelProvider(requireActivity(),factory).get(AdminViewModel.class);

        View root = binding.getRoot();

        recyclerView = binding.recyclerView;

        reportList = reportRepository.getAllReport();
        reportListAdapter = new ReportListAdapter(getActivity(), getContext(), reportList);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(reportListAdapter);

        return root;
    }
}