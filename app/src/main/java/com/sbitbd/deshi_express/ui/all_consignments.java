package com.sbitbd.deshi_express.ui;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;
import com.sbitbd.deshi_express.R;
import com.sbitbd.deshi_express.ui.consignments_fragment.All;
import com.sbitbd.deshi_express.ui.consignments_fragment.approval_pending;
import com.sbitbd.deshi_express.ui.consignments_fragment.cancelled;
import com.sbitbd.deshi_express.ui.consignments_fragment.delivered;
import com.sbitbd.deshi_express.ui.consignments_fragment.in_review;
import com.sbitbd.deshi_express.ui.consignments_fragment.pending;
import com.sbitbd.deshi_express.ui.view_pager.viewPager;

public class all_consignments extends Fragment {

    private AllConsignmentsViewModel mViewModel;
    private View root;
    private TabLayout tabLayout;
    private ViewPager viewpager;
    private All all;
    private pending pending;
    private approval_pending approval_pending;
    private cancelled cancelled;
    private delivered delivered;
    private in_review in_review;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(this).get(AllConsignmentsViewModel.class);
        root=inflater.inflate(R.layout.all_consignments_fragment, container, false);
        initView();

        return root;
    }

    private void initView(){
        try {
            tabLayout = root.findViewById(R.id.tab_id);
            viewpager = root.findViewById(R.id.view_pager);
            all = new All();
            pending = new pending();
            approval_pending = new approval_pending();
            cancelled = new cancelled();
            delivered = new delivered();
            in_review = new in_review();

            tabLayout.setupWithViewPager(viewpager);
            viewPager viewPager = new viewPager(getChildFragmentManager(),0);
            viewPager.addFragment(all,"All");
            viewPager.addFragment(pending,"Pending");
            viewPager.addFragment(approval_pending,"Approval-Pending");
            viewPager.addFragment(cancelled,"Cancelled");
            viewPager.addFragment(delivered,"Delivered");
            viewPager.addFragment(in_review,"In-Review");
            viewpager.setAdapter(viewPager);
        }catch (Exception e){
        }
    }

}