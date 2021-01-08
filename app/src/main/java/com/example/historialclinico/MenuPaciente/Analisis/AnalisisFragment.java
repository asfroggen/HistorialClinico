package com.example.historialclinico.MenuPaciente.Analisis;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.historialclinico.R;
import com.google.android.material.tabs.TabLayout;

public class AnalisisFragment extends Fragment {

    public AnalisisFragment(){

    }

    private ViewPager _pages;
    private TabLayout _tabs;
    private PagerAdapter _adapter;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_analisis, container, false);

        _tabs=view.findViewById(R.id.tlAnalisis);
        _pages=view.findViewById(R.id.vpTabsAnalisis);

        _tabs.addTab(_tabs.newTab().setText("Químicas sanguíneas"));
        _tabs.addTab(_tabs.newTab().setText("Biometrías hemáticas"));
        _tabs.addTab(_tabs.newTab().setText("Examenes de orina"));

        _adapter=new PagerAdapter(this.getChildFragmentManager(),_tabs.getTabCount());
        _pages.setAdapter(_adapter);
        _pages.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(_tabs));
        _tabs.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                _pages.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        return view;
    }
    public class PagerAdapter extends FragmentPagerAdapter{

        int _numerodetabs;
        public PagerAdapter(@NonNull FragmentManager fm, int numerodetabs) {
            super(fm);
            this._numerodetabs=numerodetabs;
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            Fragment fragment=null;
            switch (position){
                case 0: fragment=new Quimica(); break;
                case 1: fragment=new Biometria(); break;
                case 2: fragment=new Orina(); break;
            }
            return fragment;
        }

        @Override
        public int getCount() {
            return _numerodetabs;
        }
    }
}