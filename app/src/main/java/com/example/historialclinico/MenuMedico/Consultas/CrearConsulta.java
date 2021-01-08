package com.example.historialclinico.MenuMedico.Consultas;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.net.ParseException;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.historialclinico.Login;
import com.example.historialclinico.MenuMedico.Pacientes.AdaptadorPacientes;
import com.example.historialclinico.MenuMedico.Pacientes.Paciente;
import com.example.historialclinico.MenuMedico.Pacientes.PacienteDetalle;
import com.example.historialclinico.R;
import com.example.historialclinico.Registro.RegistroPaciente;
import com.example.historialclinico.Usuario;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class CrearConsulta extends Fragment implements Response.Listener<JSONObject>,Response.ErrorListener{


    public CrearConsulta() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    ImageView ivImagenConsulta;
    Button bCrearConsulta;
    Spinner spPaciente;
    EditText etDiaConsulta,etMesConsulta,etAnoConsulta,etNombreConsulta;

    Usuario usuario;
    String fechaValidar,fechaConsulta;

    RequestQueue request;
    JsonObjectRequest jsonObjectRequest;

    String[] listaPacientes;
    String [] idPacientes;

    SweetAlertDialog cargando;

    int posicion;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_crear_consulta, container, false);

        ivImagenConsulta=view.findViewById(R.id.ivImagenConsultas);
        bCrearConsulta=view.findViewById(R.id.bCrearConsulta);
        spPaciente=view.findViewById(R.id.spPaciente);
        etDiaConsulta=view.findViewById(R.id.etDiaConsulta);
        etMesConsulta=view.findViewById(R.id.etMesConsulta);
        etAnoConsulta=view.findViewById(R.id.etAnoConsulta);
        etNombreConsulta=view.findViewById(R.id.etNombreConsulta);

        usuario =(Usuario)getActivity().getApplicationContext();
        request= Volley.newRequestQueue(getContext());
        rellenarPacientes();

        //Log.d("lista",listaPacientes[1]);

        if(usuario.getSexo().equals("Masculino")){
            ivImagenConsulta.setImageResource(R.mipmap.medico_hombre_icon);
        }else{
            ivImagenConsulta.setImageResource(R.mipmap.medico_mujer_icon);
        }

        //spPaciente.setAdapter(new ArrayAdapter<>(getActivity(),android.R.layout.simple_spinner_dropdown_item,listaPacientes));

        bCrearConsulta.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                if (validarDatos().equals("OK")){
                    if(validarFechaAnos().equals("OK")){
                        if(validarFecha()){
                            posicion=spPaciente.getSelectedItemPosition();
                            cargando = new SweetAlertDialog(getContext(), SweetAlertDialog.PROGRESS_TYPE);
                            cargando.setCancelable(false);
                            cargando.getProgressHelper().setBarColor(Color.parseColor("#47BDB5"));
                            cargando.show();
                            crearConsulta();
                        }else{
                            Toast.makeText(getContext(),"Ingrese una fecha valida",Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });
        return view;
    }

    private void rellenarPacientes(){
        String idMedico=usuario.getIdMedico();
        String URL="https://192.168.1.77/login/misPacientes.php?idMedico="+idMedico;
        Log.d("URL",idMedico);
        jsonObjectRequest=new JsonObjectRequest(Request.Method.GET,URL,null,this,this);
        request.add(jsonObjectRequest);
    }
    @Override
    public void onErrorResponse(VolleyError error) {
        listaPacientes=new String[1];
        idPacientes=new String[1];

        listaPacientes[0]= "No hay pacientes";
        idPacientes[0]= "0";
    }

    @Override
    public void onResponse(JSONObject response) {
        JSONArray json=response.optJSONArray("paciente");
        try {
            assert json != null;
            listaPacientes=new String[json.length()];
            idPacientes=new String[json.length()];

            for (int i = 0; i<json.length(); i++){
                JSONObject jsonObject=null;
                jsonObject=json.getJSONObject(i);
                listaPacientes[i]=jsonObject.optString("nombre") + " " + jsonObject.optString("apellidoMat");
                idPacientes[i]=jsonObject.optString("idPaciente");
            }
            spPaciente.setAdapter(new ArrayAdapter<>(getActivity().getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,listaPacientes));
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getContext(),"No se puede conectar con el servidor: "+e.toString(),Toast.LENGTH_LONG).show();
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    private boolean validarFecha(){
        fechaConsulta=etAnoConsulta.getText().toString()+"-"+etMesConsulta.getText().toString()+"-"+etDiaConsulta.getText().toString();
        fechaValidar=etDiaConsulta.getText().toString()+"-"+etMesConsulta.getText().toString()+"-"+etAnoConsulta.getText().toString();

        try {
            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat formatoFecha = new SimpleDateFormat("dd-MM-yyyy");
            formatoFecha.setLenient(false);
            formatoFecha.parse(fechaValidar);
        } catch (ParseException | java.text.ParseException e) {
            return false;
        }
        return true;
    }



    private String validarDatos(){
        String validacion="OK";

        if (etNombreConsulta.getText().toString().isEmpty()){
            etNombreConsulta.setError("Campo necesario");
            validacion="Error";
        }
        if (etDiaConsulta.getText().toString().isEmpty()){
            etDiaConsulta.setError("Campo necesario");
            validacion="Error";
        }
        if (etMesConsulta.getText().toString().isEmpty()){
            etMesConsulta.setError("Campo necesario");
            validacion="Error";
        }
        if (etAnoConsulta.getText().toString().isEmpty()){
            etAnoConsulta.setError("Campo necesario");
            validacion="Error";
        }

        return validacion;
    }

    private String validarFechaAnos() {
        String validacion = "OK";
        Integer anoActual;
        Integer anoConsulta;
        Integer mesActual = 0;
        Integer mesConsulta;
        Integer diaConsulta,diaActual=0;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            Calendar fecha = Calendar.getInstance();
            anoActual = fecha.get(Calendar.YEAR);
            mesActual= fecha.get(Calendar.MONTH)+1;
            diaActual= fecha.get(Calendar.DAY_OF_MONTH);
        } else {
            anoActual = 2020;
            mesActual=12;
        }

        Log.d("mes",mesActual.toString());
        Log.d("dia",diaActual.toString());

        anoConsulta = Integer.parseInt(etAnoConsulta.getText().toString());
        if ((anoActual - anoConsulta) > 0 || (anoActual-anoConsulta) < -100) {
            Toast.makeText(getContext(), "Ingrese una fecha valida", Toast.LENGTH_SHORT).show();
            validacion = "Error";
        }
        mesConsulta= Integer.parseInt(etMesConsulta.getText().toString());
        if ((anoActual.equals(anoConsulta)) && mesConsulta<mesActual){
            Toast.makeText(getContext(), "Ingrese una fecha valida", Toast.LENGTH_SHORT).show();
            validacion = "Error";
        }
        diaConsulta=Integer.parseInt(etDiaConsulta.getText().toString());
        if ((anoActual.equals(anoConsulta)) && mesConsulta.equals(mesActual) && diaConsulta<diaActual){
            Toast.makeText(getContext(), "Ingrese una fecha valida", Toast.LENGTH_SHORT).show();
            validacion = "Error";
        }
        return validacion;
    }

    private void crearConsulta(){
        StringRequest stringRequest=new StringRequest(Request.Method.POST, "https://192.168.1.77/login/crearConsulta.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                cargando.dismissWithAnimation();
                new SweetAlertDialog(getContext(), SweetAlertDialog.SUCCESS_TYPE)
                        .setTitleText("Correcto!")
                        .setContentText("Consulta creada")
                        .setConfirmText("Ok")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.dismissWithAnimation();
                                etAnoConsulta.setText("");
                                etDiaConsulta.setText("");
                                etMesConsulta.setText("");
                                etNombreConsulta.setText("");
                            }
                        })
                        .show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                cargando.dismissWithAnimation();
                new SweetAlertDialog(getContext(), SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Oops...")
                        .setContentText("Necesitas pacientes para crear una consulta")
                        .show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parametros=new HashMap<String, String>();

                parametros.put("nombre",etNombreConsulta.getText().toString());
                parametros.put("idPaciente",idPacientes[posicion]);
                parametros.put("idMedico",usuario.getIdMedico());
                parametros.put("fecha",fechaConsulta);

                return parametros;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(getContext().getApplicationContext());
        requestQueue.add(stringRequest);
    }

}