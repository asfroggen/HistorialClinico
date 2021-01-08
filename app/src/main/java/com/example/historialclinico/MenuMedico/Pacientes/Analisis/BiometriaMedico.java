package com.example.historialclinico.MenuMedico.Pacientes.Analisis;

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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import com.example.historialclinico.MenuPaciente.Analisis.Agregar.AgregarBiometria;
import com.example.historialclinico.MenuPaciente.Analisis.Analisis;
import com.example.historialclinico.MenuPaciente.Analisis.Editar.EditarBiometria;
import com.example.historialclinico.MenuPaciente.Analisis.Ver.VerBiometria;
import com.example.historialclinico.R;
import com.example.historialclinico.Usuario;
import com.getbase.floatingactionbutton.FloatingActionButton;

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


public class BiometriaMedico extends Fragment implements androidx.appcompat.widget.SearchView.OnQueryTextListener, Response.Listener<JSONObject>,Response.ErrorListener{

    public BiometriaMedico() {
        // Required empty public constructor
    }

    ArrayList<Analisis> listaAnalisis;
    RecyclerView recycler;
    RequestQueue request;

    JsonObjectRequest jsonObjectRequest;
    androidx.appcompat.widget.SearchView svSearch;
    AdaptadorAnalisisMedico adaptador;
    SwipeRefreshLayout refreshLayout;

    Usuario usuario;
    SweetAlertDialog cargando;

    String idPaciente;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_biometria_medico, container, false);

        listaAnalisis=new ArrayList<>();
        recycler=(RecyclerView)view.findViewById(R.id.recyclerIdBuscarAnalisis);
        svSearch= (androidx.appcompat.widget.SearchView) view.findViewById(R.id.svBuscarAnalisis);


        recycler.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext(), LinearLayoutManager.VERTICAL,false));
        recycler.setHasFixedSize(true);
        request= Volley.newRequestQueue(getContext());
        refreshLayout=(SwipeRefreshLayout)view.findViewById(R.id.rlBuscarAnalisis);
        usuario =(Usuario)getActivity().getApplicationContext();

        initListener();
        cargarWebService();
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                listaAnalisis.clear();
                cargarWebService();
                refreshLayout.setRefreshing(false);
            }
        });

        return view;
    }

    private void cargarWebService() {

        String tipo="biometria";
        String URL="https://192.168.1.77/login/buscarAnalisis.php?idPaciente="+idPaciente+"&tipo="+tipo;
        jsonObjectRequest=new JsonObjectRequest(Request.Method.GET,URL,null,this,this);
        request.add(jsonObjectRequest);
    }

    @Override
    public void onResponse(JSONObject response) {
        Analisis medicosClase=null;
        JSONArray json=response.optJSONArray("medico");
        try {
            assert json != null;
            for (int i = 0; i<json.length(); i++){
                medicosClase=new Analisis();
                JSONObject jsonObject=null;
                jsonObject=json.getJSONObject(i);
                medicosClase.setIdAnalisis(jsonObject.optString("idAnalisis"));
                medicosClase.setNombreAnalisis(jsonObject.optString("nombre"));
                medicosClase.setFechaAnalisis(jsonObject.optString("fecha"));
                medicosClase.setTipoAnalisis("biometria");
                medicosClase.setLeucocitosAnalisis(jsonObject.optString("leucocitos"));
                medicosClase.setEritrocitosAnalisis(jsonObject.optString("eritrocitos"));
                medicosClase.setPlaquetasAnalisis(jsonObject.optString("plaquetas"));

                listaAnalisis.add(medicosClase);
            }
            adaptador=new AdaptadorAnalisisMedico(listaAnalisis);
            adaptador.setOnItemClickListener(new AdaptadorAnalisisMedico.OnItemClickListener() {
                @Override
                public void onItemClick(int position) {
                    Intent intent = new Intent(getContext(), VerBiometria.class);
                    intent.putExtra("idAnalisis",listaAnalisis.get(position).getIdAnalisis());
                    intent.putExtra("nombre",listaAnalisis.get(position).getNombreAnalisis());
                    intent.putExtra("fecha",listaAnalisis.get(position).getFechaAnalisis());
                    intent.putExtra("leucocitos",listaAnalisis.get(position).getLeucocitosAnalisis());
                    intent.putExtra("eritrocitos",listaAnalisis.get(position).getEritrocitosAnalisis());
                    intent.putExtra("plaquetas",listaAnalisis.get(position).getPlaquetasAnalisis());
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
        adaptador=new AdaptadorAnalisisMedico(listaAnalisis);
        recycler.setAdapter(adaptador);
        //Toast.makeText(getContext(),"No hay analisis para mostrar",Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null){
            idPaciente=getArguments().getString("idPaciente");
        }else{
            idPaciente=usuario.getIdPaciente();
        }

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