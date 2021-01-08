package com.example.historialclinico.MenuPaciente.Configuracion;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.historialclinico.R;

import java.util.ArrayList;
import java.util.List;

public class ConfiguracionFragment extends Fragment implements View.OnClickListener {

    public ConfiguracionFragment() {
        // Required empty public constructor
    }
    LinearLayout llDatosPersonales,llImagenDePerfil,llCredenciales,llEliminarCuenta,llAcercaDe;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_configuracion, container, false);

        llDatosPersonales = view.findViewById(R.id.llDatosPersonales);
        llImagenDePerfil= view.findViewById(R.id.llImagenDePerfil);
        llCredenciales= view.findViewById(R.id.llCambiarSeguridad);
        llEliminarCuenta= view.findViewById(R.id.llEliminarCuenta);
        llAcercaDe= view.findViewById(R.id.llAcercaDe);

        llDatosPersonales.setOnClickListener(this);
        llImagenDePerfil.setOnClickListener(this);
        llCredenciales.setOnClickListener(this);
        llEliminarCuenta.setOnClickListener(this);
        llAcercaDe.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.llAcercaDe:
                intent = new Intent(getActivity(), AcercaDe.class);
                startActivity(intent);
                break;
            case R.id.llDatosPersonales:
                intent = new Intent(getActivity(), ActualizarDatosPersonales.class);
                startActivity(intent);
                break;
            case R.id.llCambiarSeguridad:
                intent = new Intent(getActivity(), ActualizarCredenciales.class);
                startActivity(intent);
                break;
            case R.id.llImagenDePerfil:
                intent = new Intent(getActivity(), ActualizarImagen.class);
                startActivity(intent);
                break;
            case R.id.llEliminarCuenta:
                intent = new Intent(getActivity(), EliminarCuenta.class);
                startActivity(intent);
                break;
        }
    }
}