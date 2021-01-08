package com.example.historialclinico.MenuPaciente.Home;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.historialclinico.MenuMedico.Consultas.AdaptadorConsultas;
import com.example.historialclinico.MenuMedico.Consultas.Consulta;
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

public class HomeFragment extends Fragment implements androidx.appcompat.widget.SearchView.OnQueryTextListener, Response.Listener<JSONObject>,Response.ErrorListener{

    public HomeFragment(){

    }
    ArrayList<Consulta> listaMedicos;
    RecyclerView recycler;
    RequestQueue request;
    Usuario usuario;

    JsonObjectRequest jsonObjectRequest;
    androidx.appcompat.widget.SearchView svSearch;
    AdaptadorConsultas adaptador;
    SwipeRefreshLayout refreshLayout;

    SweetAlertDialog cargando;
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
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
        String idPaciente=usuario.getIdPaciente();
        String URL="https://192.168.1.77/login/misConsultasPaciente.php?idConsulta="+idPaciente;
        jsonObjectRequest=new JsonObjectRequest(Request.Method.GET,URL,null,this,this);
        request.add(jsonObjectRequest);
    }
    @Override
    public void onResponse(JSONObject response) {
        Consulta medicosClase=null;
        JSONArray json=response.optJSONArray("paciente");
        try {
            assert json != null;
            for (int i = 0; i<json.length(); i++){
                medicosClase=new Consulta();
                JSONObject jsonObject=null;
                jsonObject=json.getJSONObject(i);
                medicosClase.setTokenPaciente(jsonObject.optString("token"));
                medicosClase.setNombreConsulta(jsonObject.optString("nombreConsulta"));
                medicosClase.setUbicacionConsulta(jsonObject.optString("ubicacion"));
                medicosClase.setIdPacienteConsulta(jsonObject.optString("idPaciente"));
                medicosClase.setIdConsulta(jsonObject.optString("idConsulta"));
                if (jsonObject.optString("segNombre").matches(".*[a-z].*")){
                    medicosClase.setNombrePaciente(jsonObject.optString("nombre")+" "+jsonObject.optString("segNombre")+" "+jsonObject.optString("apellidoPat")+" "+jsonObject.optString("apellidoMat"));
                }else {
                    medicosClase.setNombrePaciente(jsonObject.optString("nombre") + " " + jsonObject.optString("apellidoPat") + " " + jsonObject.optString("apellidoMat"));
                }
                medicosClase.setCorreoConsulta(jsonObject.optString("correo"));
                medicosClase.setFechaNacimientoConsulta(jsonObject.optString("fechaNacimiento"));
                medicosClase.setSexoConsulta(jsonObject.optString("sexo"));
                medicosClase.setImagenConsulta(StringToBitMap(jsonObject.optString("imagen")));
                listaMedicos.add(medicosClase);
            }
            adaptador=new AdaptadorConsultas(listaMedicos);
            adaptador.setOnItemClickListener(new AdaptadorConsultas.OnItemClickListener() {
                @Override
                public void OnItemClick(int position) {
                    new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("¿La agendamos?")
                            .setContentText("¿Desea agendar esta consulta en el calendario?")
                            .setConfirmText("Si")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.dismissWithAnimation();
                                    Intent intent = new Intent(Intent.ACTION_INSERT);
                                    intent.setData(CalendarContract.Events.CONTENT_URI);
                                    intent.putExtra(CalendarContract.Events.TITLE,listaMedicos.get(position).getNombreConsulta());
                                    intent.putExtra(CalendarContract.Events.EVENT_LOCATION,listaMedicos.get(position).getUbicacionConsulta());
                                    intent.putExtra(CalendarContract.Events.DESCRIPTION,"Consulta médica");
                                    intent.putExtra(CalendarContract.Events.ALL_DAY,"false");
                                    intent.putExtra(Intent.EXTRA_EMAIL,listaMedicos.get(position).getCorreoConsulta());
                                    startActivity(intent);
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

                @Override
                public void OnDeleteClick(int position) {
                    new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("¿Adios a "+listaMedicos.get(position).getNombreConsulta()+"?")
                            .setContentText("¿Desea cancelar esta consulta?")
                            .setConfirmText("Si")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.dismissWithAnimation();
                                    cargando = new SweetAlertDialog(getContext(), SweetAlertDialog.PROGRESS_TYPE);
                                    cargando.setCancelable(false);
                                    cargando.getProgressHelper().setBarColor(Color.parseColor("#47BDB5"));
                                    cargando.show();
                                    mandarNotificacion(listaMedicos.get(position).getTokenPaciente());
                                    eliminarSolicitud(listaMedicos.get(position).getIdConsulta());
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
        adaptador=new AdaptadorConsultas(listaMedicos);
        recycler.setAdapter(adaptador);
    }

    private void mandarNotificacion(String tokenPaciente){
        StringRequest stringRequest=new StringRequest(Request.Method.POST, "https://192.168.1.77/login/enviarNotificacion.php",new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Toast.makeText(getApplicationContext(),"Usuario registrado correctamente",Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> parametros=new HashMap<String, String>();
                parametros.put("titulo","Consulta cancelada");
                parametros.put("mensaje",usuario.getNombre()+" ha cancelado una consulta");
                parametros.put("token",tokenPaciente);

                return parametros;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }

    private void eliminarSolicitud(String idConsulta){
        StringRequest stringRequest=new StringRequest(Request.Method.POST, "https://192.168.1.77/login/eliminarConsulta.php",new Response.Listener<String>(){
            @Override
            public void onResponse(String response) {
                cargando.dismissWithAnimation();
                listaMedicos.clear();
                cargarWebService();
                new SweetAlertDialog(getContext())
                        .setTitleText("Consulta cancelada!")
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
                parametros.put("idConsulta",idConsulta);
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