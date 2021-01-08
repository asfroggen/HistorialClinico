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
import android.widget.TextView;
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
import com.example.historialclinico.MenuPaciente.Configuracion.ActualizarCredenciales;
import com.example.historialclinico.R;
import com.example.historialclinico.Usuario;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.Inet4Address;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class BuscarMedicos extends Fragment implements androidx.appcompat.widget.SearchView.OnQueryTextListener, Response.Listener<JSONObject>,Response.ErrorListener{

    public BuscarMedicos() {
        // Required empty public constructor
    }


    ArrayList<Medico> listaMedicos;
    RecyclerView recycler;
    RequestQueue request;

    JsonObjectRequest jsonObjectRequest;
    androidx.appcompat.widget.SearchView svSearch;
    AdaptadorMedicos adaptador;
    SwipeRefreshLayout refreshLayout;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_buscar_medicos, container, false);
        listaMedicos=new ArrayList<>();
        recycler=(RecyclerView)view.findViewById(R.id.recyclerIdBuscarMedicos);
        svSearch= (androidx.appcompat.widget.SearchView) view.findViewById(R.id.svBuscarMedico);
        recycler.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext(), LinearLayoutManager.VERTICAL,false));
        recycler.setHasFixedSize(true);
        request= Volley.newRequestQueue(getContext());
        refreshLayout=(SwipeRefreshLayout)view.findViewById(R.id.rlBuscarMedicos);

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
        String URL="https://192.168.1.77/login/buscarMedicos.php";
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
                    Intent intent=new Intent(getActivity().getApplicationContext(), MedicoDetalle.class);
                    intent.putExtra("misMedicos","false");
                    intent.putExtra("id",listaMedicos.get(position).getIdMedico());
                    intent.putExtra("nombre",listaMedicos.get(position).getNombreMedico());
                    intent.putExtra("especialidad",listaMedicos.get(position).getEspecialidadMedico());
                    intent.putExtra("correo",listaMedicos.get(position).getCorreoMedico());
                    intent.putExtra("ubicacion",listaMedicos.get(position).getUbicacionMedico());
                    if (listaMedicos.get(position).getImagenMedico()!=null)
                    {
                        intent.putExtra("imagen", BitMapToString(listaMedicos.get(position).getImagenMedico()));
                    }else{
                        intent.putExtra("imagen","No hay");
                    }
                    startActivity(intent);
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
        Toast.makeText(getContext(),"No hay médicos para mostrar",Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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