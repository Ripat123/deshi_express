package com.sbitbd.deshi_express.ui.settings;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;
import com.sbitbd.deshi_express.R;
import com.sbitbd.deshi_express.ui.settings.settings_pragment.company_info;
import com.sbitbd.deshi_express.ui.settings.settings_pragment.other_account;
import com.sbitbd.deshi_express.ui.settings.settings_pragment.owner_info;
import com.sbitbd.deshi_express.ui.view_pager.viewPager;


public class settings extends Fragment {

    private View root;
    private TabLayout tabLayout;
    private ViewPager viewpager;
    private company_info company_info;
    private other_account other_account;
    private owner_info owner_info;
    private bank_info bank_info;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_settings, container, false);
        initView();
        return root;
    }

    private void initView(){
        try {
            tabLayout = root.findViewById(R.id.tab_id);
            viewpager = root.findViewById(R.id.view_pager);

            company_info = new company_info();
            other_account = new other_account();
            owner_info = new owner_info();
            bank_info = new bank_info();

            tabLayout.setupWithViewPager(viewpager);
            viewPager viewPager = new viewPager(getChildFragmentManager(),0);
            viewPager.addFragment(company_info,"Company Information");
            viewPager.addFragment(owner_info,"Owner Information");
            viewPager.addFragment(bank_info,"Bank Information");
            viewPager.addFragment(other_account,"Other Account");
            viewpager.setAdapter(viewPager);
        }catch (Exception e){
        }
    }
}