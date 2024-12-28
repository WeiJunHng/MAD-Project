package com.example.madproject;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GetSupport#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GetSupport extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public GetSupport() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GetSupport.
     */
    // TODO: Rename and change types and number of parameters
    public static GetSupport newInstance(String param1, String param2) {
        GetSupport fragment = new GetSupport();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_get_support, container, false);

        // RecyclerView setup
        RecyclerView recyclerView = view.findViewById(R.id.RecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // FAQ list data
        List<FAQ> faqList = new ArrayList<>();
        faqList.add(new FAQ(getString(R.string.faq_1_question), getString(R.string.faq_1_answer)));
        faqList.add(new FAQ(getString(R.string.faq_2_question), getString(R.string.faq_2_answer)));
        faqList.add(new FAQ(getString(R.string.faq_3_question), getString(R.string.faq_3_answer)));
        faqList.add(new FAQ(getString(R.string.faq_4_question), getString(R.string.faq_4_answer)));
        faqList.add(new FAQ(getString(R.string.faq_5_question), getString(R.string.faq_5_answer)));
        faqList.add(new FAQ(getString(R.string.faq_6_question), getString(R.string.faq_6_answer)));
        faqList.add(new FAQ(getString(R.string.faq_7_question), getString(R.string.faq_7_answer)));
        faqList.add(new FAQ(getString(R.string.faq_8_question), getString(R.string.faq_8_answer)));
        faqList.add(new FAQ(getString(R.string.faq_9_question), getString(R.string.faq_9_answer)));
        faqList.add(new FAQ(getString(R.string.faq_10_question), getString(R.string.faq_10_answer)));

        // Adapter setup
        FAQAdapter adapter = new FAQAdapter(faqList);
        recyclerView.setAdapter(adapter);

        return view;
    }

}