package com.example.historialclinico.MenuMedico.Pacientes;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.historialclinico.MenuPaciente.Medicos.AdaptadorMedicos;
import com.example.historialclinico.MenuPaciente.Medicos.Medico;
import com.example.historialclinico.MenuPaciente.Medicos.MedicoDetalle;
import com.example.historialclinico.R;
import com.example.historialclinico.Usuario;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class MisPacientes extends Fragment implements androidx.appcompat.widget.SearchView.OnQueryTextListener, Response.Listener<JSONObject>,Response.ErrorListener{

    public MisPacientes() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    ArrayList<Paciente> listaMedicos;
    RecyclerView recycler;
    RequestQueue request;
    Usuario usuario;

    JsonObjectRequest jsonObjectRequest;
    androidx.appcompat.widget.SearchView svSearch;
    AdaptadorPacientes adaptador;
    SwipeRefreshLayout refreshLayout;

    SweetAlertDialog cargando;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_mis_pacientes, container, false);
        listaMedicos=new ArrayList<>();
        recycler=(RecyclerView)view.findViewById(R.id.recyclerIdBuscarMedicos);
        svSearch= (androidx.appcompat.widget.SearchView) view.findViewById(R.id.svBuscarMedico);
        recycler.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext(), LinearLayoutManager.VERTICAL,false));
        recycler.setHasFixedSize(true);
        request= Volley.newRequestQueue(getContext());
        refreshLayout=(SwipeRefreshLayout)view.findViewById(R.id.rlBuscarMedicos);
        usuario =(Usuario)getActivity().getApplicationContext();
        initListener();
        cargarWebService();
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                listaMedicos.clear();
                cargarWebService();
                refreshLayout.setRefreshing(false);
            }
        });
        return view;
    }

    private void cargarWebService() {
        String idMedico=usuario.getIdMedico();
        String URL="https://192.168.1.77/login/misPacientes.php?idMedico="+idMedico;
        jsonObjectRequest=new JsonObjectRequest(Request.Method.GET,URL,null,this,this);
        request.add(jsonObjectRequest);
    }

    @Override
    public void onResponse(JSONObject response) {
        Paciente medicosClase=null;
        JSONArray json=response.optJSONArray("paciente");
        try {
            assert json != null;
            for (int i = 0; i<json.length(); i++){
                medicosClase=new Paciente();
                JSONObject jsonObject=null;
                jsonObject=json.getJSONObject(i);
                medicosClase.setIdPacientes(jsonObject.optString("idPaciente"));
                if (jsonObject.optString("segNombre").matches(".*[a-z].*")){
                    medicosClase.setNombrePacientes(jsonObject.optString("nombre")+" "+jsonObject.optString("segNombre")+" "+jsonObject.optString("apellidoPat")+" "+jsonObject.optString("apellidoMat"));
                }else {
                    medicosClase.setNombrePacientes(jsonObject.optString("nombre") + " " + jsonObject.optString("apellidoPat") + " " + jsonObject.optString("apellidoMat"));
                }
                medicosClase.setCorreoPacientes(jsonObject.optString("correo"));
                medicosClase.setFechaNacimientoPacientes(jsonObject.optString("fechaNacimiento"));
                medicosClase.setSexoPacientes(jsonObject.optString("sexo"));
                medicosClase.setImagenPacientes(StringToBitMap(jsonObject.optString("imagen")));
                listaMedicos.add(medicosClase);
            }
            adaptador=new AdaptadorPacientes(listaMedicos);
            adaptador.setOnItemClickListener(new AdaptadorPacientes.OnItemClickListener() {
                @Override
                public void OnItemClick(int position) {
                    Intent intent=new Intent(getActivity().getApplicationContext(), PacienteDetalle.class);
                    intent.putExtra("misMedicos","true");
                    intent.putExtra("id",listaMedicos.get(position).getIdPacientes());
                    intent.putExtra("nombre",listaMedicos.get(position).getNombrePacientes());
                    intent.putExtra("fechaNacimiento",listaMedicos.get(position).getFechaNacimientoPacientes());
                    intent.putExtra("correo",listaMedicos.get(position).getCorreoPacientes());
                    intent.putExtra("sexo",listaMedicos.get(position).getSexoPacientes());
                    if (listaMedicos.get(position).getImagenPacientes()!=null)
                    {
                        intent.putExtra("imagen", BitMapToString(listaMedicos.get(position).getImagenPacientes()));
                    }else{
                        intent.putExtra("imagen","No hay");
                    }
                    startActivity(intent);
                }

                @Override
                public void OnDeleteClick(int position) {
                    new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("¿Adios a "+listaMedicos.get(position).getNombrePacientes()+"?")
                            .setContentText("¿Desea eliminar este contacto?")
                            .setConfirmText("Si")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.dismissWithAnimation();
                                    cargando = new SweetAlertDialog(getContext(), SweetAlertDialog.PROGRESS_TYPE);
                                    cargando.setCancelable(false);
                                    cargando.getProgressHelper().setBarColor(Color.parseColor("#47BDB5"));
                                    cargando.show();
                                    eliminarSolicitud(listaMedicos.get(position).getIdPacientes());
                                }
                            })
                            .setCancelButton("No", new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.dismissWithAnimation();
                                }
                            })
                            .show();
                }
            });
            recycler.setAdapter(adaptador);
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getContext(),"No se puede conectar con el servidor: "+e.toString(),Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onErrorResponse(VolleyError error) {
        adaptador=new AdaptadorPacientes(listaMedicos);
        recycler.setAdapter(adaptador);
    }
    private void eliminarSolicitud(String idPaciente){
        StringRequest stringRequest=new StringRequest(Request.Method.POST, "https://192.168.1.77/login/eliminarSolicitud.php",new Response.Listener<String>(){
            @Override
            public void onResponse(String response) {
                cargando.dismissWithAnimation();
                listaMedicos.clear();
                cargarWebService();
                new SweetAlertDialog(getContext())
                        .setTitleText("Contacto eliminado!")
                        .show();
            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                cargando.dismissWithAnimation();
                Toast.makeText(getContext(), error.toString(),Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parametros=new HashMap<String, String>();
                parametros.put("idPaciente",idPaciente);
                parametros.put("idMedico", usuario.getIdMedico());
                return parametros;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }

    public Bitmap StringToBitMap(String image){
        try{
            byte [] encodeByte= Base64.decode(image,Base64.DEFAULT);

            InputStream inputStream  = new ByteArrayInputStream(encodeByte);
            Bitmap bitmap  = BitmapFactory.decodeStream(inputStream);
            return bitmap;
        }catch(Exception e){
            e.getMessage();
            return null;
        }
    }
    public String BitMapToString(Bitmap bitmap){
        ByteArrayOutputStream baos=new  ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
        byte [] b=baos.toByteArray();
        String temp=Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }
    private void initListener(){
        svSearch.setOnQueryTextListener(this);
    }
    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        adaptador.filter(newText);
        return false;
    }

}