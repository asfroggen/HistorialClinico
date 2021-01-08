package com.example.historialclinico.MenuMedico.Consultas;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.historialclinico.MenuMedico.Pacientes.MisPacientes;
import com.example.historialclinico.MenuMedico.Pacientes.PacientesFragment;
import com.example.historialclinico.MenuMedico.Pacientes.SolicitudesPacientes;
import com.example.historialclinico.R;
import com.google.android.material.tabs.TabLayout;

public class ConsultasFragment extends Fragment {

    public ConsultasFragment() {
        // Required empty public constructor
    }

    private ViewPager _pages;
    private TabLayout _tabs;
    private PagerAdapter _adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_consultas, container, false);

        _tabs=view.findViewById(R.id.tlPacientes);
        _pages=view.findViewById(R.id.vpTabsPacientes);

        _tabs.addTab(_tabs.newTab().setText("Mis consultas"));
        _tabs.addTab(_tabs.newTab().setText("Crear consultas"));

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
    public class PagerAdapter extends FragmentPagerAdapter {

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
                case 0: fragment=new MisConsultas(); break;
                case 1: fragment=new CrearConsulta(); break;
            }
            return fragment;
        }

        @Override
        public int getCount() {
            return _numerodetabs;
        }
    }
}