package com.example.historialclinico.MenuMedico.Configuracion;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.historialclinico.MenuPaciente.Configuracion.AcercaDe;
import com.example.historialclinico.MenuPaciente.Configuracion.ActualizarCredenciales;
import com.example.historialclinico.MenuPaciente.Configuracion.ActualizarDatosPersonales;
import com.example.historialclinico.MenuPaciente.Configuracion.ActualizarImagen;
import com.example.historialclinico.MenuPaciente.Configuracion.EliminarCuenta;
import com.example.historialclinico.R;


public class ConfiguracionFragmentMedico extends Fragment implements View.OnClickListener {

    public ConfiguracionFragmentMedico() {
        // Required empty public constructor
    }

    LinearLayout llDatosPersonales,llInfoProfesional,llImagenDePerfil,llCredenciales,llEliminarCuenta,llAcercaDe;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_configuracion_medico, container, false);

        llDatosPersonales = view.findViewById(R.id.llDatosPersonales);
        llInfoProfesional = view.findViewById(R.id.llInformacionProfesional);
        llImagenDePerfil= view.findViewById(R.id.llImagenDePerfil);
        llCredenciales= view.findViewById(R.id.llCambiarSeguridad);
        llEliminarCuenta= view.findViewById(R.id.llEliminarCuenta);
        llAcercaDe= view.findViewById(R.id.llAcercaDe);

        llDatosPersonales.setOnClickListener(this);
        llInfoProfesional.setOnClickListener(this);
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
                intent = new Intent(getActivity(), AcercaDeMedico.class);
                startActivity(intent);
                break;
            case R.id.llInformacionProfesional:
                intent = new Intent(getActivity(), ActualizarDatosProfesionales.class);
                startActivity(intent);
                break;
            case R.id.llDatosPersonales:
                intent = new Intent(getActivity(), ActualizarDatosPersonalesMedico.class);
                startActivity(intent);
                break;
            case R.id.llCambiarSeguridad:
                intent = new Intent(getActivity(), ActualizarCredencialesMedico.class);
                startActivity(intent);
                break;
            case R.id.llImagenDePerfil:
                intent = new Intent(getActivity(), ActualizarImagenMedico.class);
                startActivity(intent);
                break;
            case R.id.llEliminarCuenta:
                intent = new Intent(getActivity(), EliminarCuentaMedico.class);
                startActivity(intent);
                break;
        }
    }
}