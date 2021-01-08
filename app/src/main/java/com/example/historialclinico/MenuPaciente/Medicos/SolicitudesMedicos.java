package com.example.historialclinico.MenuPaciente.Medicos;

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
import com.example.historialclinico.Login;
import com.example.historialclinico.MedicoActivity;
import com.example.historialclinico.PacienteActivity;
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
import java.util.Objects;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class SolicitudesMedicos extends Fragment implements androidx.appcompat.widget.SearchView.OnQueryTextListener, Response.Listener<JSONObject>,Response.ErrorListener{

    public SolicitudesMedicos() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    ArrayList<Medico> listaMedicos;
    RecyclerView recycler;
    RequestQueue request;
    Usuario usuario;
    JsonObjectRequest jsonObjectRequest;
    androidx.appcompat.widget.SearchView svSearch;
    AdaptadorMedicos adaptador;
    SweetAlertDialog cargando;
    SwipeRefreshLayout refreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_solicitudes, container, false);
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
        String URL="https://192.168.1.77/login/solicitudesMedicos.php?idPaciente="+idPaciente;
        jsonObjectRequest=new JsonObjectRequest(Request.Method.GET,URL,null,this,this);
        request.add(jsonObjectRequest);
    }

    @Override
    public void onResponse(JSONObject response) {
            Medico medicosClase=null;
            JSONArray json=response.optJSONArray("medico");
            try {
                assert json != null;
                for (int i = 0; i<json.length(); i++){
                    medicosClase=new Medico();
                    JSONObject jsonObject=null;
                    jsonObject=json.getJSONObject(i);
                    medicosClase.setIdMedico(jsonObject.optString("idMedico"));
                    if (jsonObject.optString("segNombre").matches(".*[a-z].*")){
                        medicosClase.setNombreMedico(jsonObject.optString("nombre")+" "+jsonObject.optString("segNombre")+" "+jsonObject.optString("apellidoPat")+" "+jsonObject.optString("apellidoMat"));
                    }else {
                        medicosClase.setNombreMedico(jsonObject.optString("nombre") + " " + jsonObject.optString("apellidoPat") + " " + jsonObject.optString("apellidoMat"));
                    }
                    medicosClase.setCorreoMedico(jsonObject.optString("correo"));
                    medicosClase.setEspecialidadMedico(jsonObject.optString("especialidad"));
                    medicosClase.setUbicacionMedico(jsonObject.optString("ubicacion"));
                    medicosClase.setImagenMedico(StringToBitMap(jsonObject.optString("imagen")));
                    listaMedicos.add(medicosClase);
                }
                adaptador=new AdaptadorMedicos(listaMedicos);
                adaptador.setOnItemClickListener(new AdaptadorMedicos.OnItemClickListener() {
                    @Override
                    public void OnItemClick(int position) {
                        new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                                .setTitleText("El médico aun no acepta tu solicitud")
                                .setContentText("¿Cancelar solicitud de contacto?")
                                .setConfirmText("Si")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        sDialog.dismissWithAnimation();
                                        cargando = new SweetAlertDialog(getContext(), SweetAlertDialog.PROGRESS_TYPE);
                                        cargando.setCancelable(false);
                                        cargando.getProgressHelper().setBarColor(Color.parseColor("#FE8629"));
                                        cargando.show();
                                        eliminarSolicitud(listaMedicos.get(position).getIdMedico());
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
        adaptador=new AdaptadorMedicos(listaMedicos);
        recycler.setAdapter(adaptador);
        //Toast.makeText(getContext(),"No hay médicos para mostrar",Toast.LENGTH_SHORT).show();
    }
    private void eliminarSolicitud(String idMedico){
        StringRequest stringRequest=new StringRequest(Request.Method.POST, "https://192.168.1.77/login/eliminarSolicitud.php",new Response.Listener<String>(){
            @Override
            public void onResponse(String response) {
                        cargando.dismissWithAnimation();
                        listaMedicos.clear();
                        cargarWebService();
                        new SweetAlertDialog(getContext())
                                    .setTitleText("Solicitud eliminada!")
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
                parametros.put("idPaciente",usuario.getIdPaciente());
                parametros.put("idMedico", idMedico);
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