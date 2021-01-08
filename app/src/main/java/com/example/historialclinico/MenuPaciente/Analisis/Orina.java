package com.example.historialclinico.MenuPaciente.Analisis;

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
import com.example.historialclinico.MenuPaciente.Analisis.Agregar.AgregarOrina;
import com.example.historialclinico.MenuPaciente.Analisis.Agregar.AgregarOrinaFoto;
import com.example.historialclinico.MenuPaciente.Analisis.Editar.EditarBiometria;
import com.example.historialclinico.MenuPaciente.Analisis.Editar.EditarOrina;
import com.example.historialclinico.MenuPaciente.Analisis.Ver.VerBiometria;
import com.example.historialclinico.MenuPaciente.Analisis.Ver.VerOrina;
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

public class Orina extends Fragment implements androidx.appcompat.widget.SearchView.OnQueryTextListener, Response.Listener<JSONObject>,Response.ErrorListener{

    public Orina() {
        // Required empty public constructor
    }

    ArrayList<Analisis> listaAnalisis;
    RecyclerView recycler;
    RequestQueue request;

    JsonObjectRequest jsonObjectRequest;
    androidx.appcompat.widget.SearchView svSearch;
    AdaptadorAnalisis adaptador;
    SwipeRefreshLayout refreshLayout;
    FloatingActionButton bAgregar,bAgregarAMano,bAgregarAPDF;
    Usuario usuario;
    SweetAlertDialog cargando;

    Animation rotateOpen,rotateClose,fromBottom,toBottom;

    Boolean clickeado;

    String idPaciente;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_orina, container, false);

        listaAnalisis=new ArrayList<>();
        recycler=(RecyclerView)view.findViewById(R.id.recyclerIdBuscarAnalisis);
        svSearch= (androidx.appcompat.widget.SearchView) view.findViewById(R.id.svBuscarAnalisis);

        bAgregarAMano=view.findViewById(R.id.bAgregarAMano);
        bAgregarAPDF=view.findViewById(R.id.bAgregarAPDF);

        recycler.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext(), LinearLayoutManager.VERTICAL,false));
        recycler.setHasFixedSize(true);
        request= Volley.newRequestQueue(getContext());
        refreshLayout=(SwipeRefreshLayout)view.findViewById(R.id.rlBuscarAnalisis);

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


        bAgregarAMano.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), AgregarOrina.class);
                intent.putExtra("tipo","agregar");
                startActivity(intent);
            }
        });

        bAgregarAPDF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), AgregarOrinaFoto.class);
                startActivity(intent);
                //Toast.makeText(getContext(),"A PDF",Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    private void cargarWebService() {
        String tipo="orina";
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
                medicosClase.setTipoAnalisis("orina");
                medicosClase.setCreatininaAnalisis(jsonObject.optString("creatinina"));
                medicosClase.setAlbuminaAnalisis(jsonObject.optString("albumina"));
                medicosClase.setAcidoAnalisis(jsonObject.optString("acido"));

                listaAnalisis.add(medicosClase);
            }
            adaptador=new AdaptadorAnalisis(listaAnalisis);
            adaptador.setOnItemClickListener(new AdaptadorAnalisis.OnItemClickListener() {
                @Override
                public void onItemClick(int position) {
                    Intent intent = new Intent(getContext(), VerOrina.class);
                    intent.putExtra("idAnalisis",listaAnalisis.get(position).getIdAnalisis());
                    intent.putExtra("nombre",listaAnalisis.get(position).getNombreAnalisis());
                    intent.putExtra("fecha",listaAnalisis.get(position).getFechaAnalisis());
                    intent.putExtra("creatinina",listaAnalisis.get(position).getCreatininaAnalisis());
                    intent.putExtra("albumina",listaAnalisis.get(position).getAlbuminaAnalisis());
                    intent.putExtra("acido",listaAnalisis.get(position).getAcidoAnalisis());
                    startActivity(intent);
                }

                @Override
                public void onDeleteClick(int position) {
                    new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("¿Eliminar análisis?")
                            .setContentText("¿Está seguro que desea eliminar este análisis?")
                            .setConfirmText("Si")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.dismissWithAnimation();
                                    cargando = new SweetAlertDialog(getContext(), SweetAlertDialog.PROGRESS_TYPE);
                                    cargando.setCancelable(false);
                                    cargando.getProgressHelper().setBarColor(Color.parseColor("#FE8629"));
                                    cargando.show();
                                    eliminarAnalisis(listaAnalisis.get(position).getIdAnalisis());
                                }
                            })
                            .setCancelButton("No", new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.dismissWithAnimation();
                                }
                            })
                            .show();
                    //Toast.makeText(getContext(),"Eliminar: "+listaAnalisis.get(position).getNombreAnalisis(),Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onEditClick(int position) {
                    Intent intent = new Intent(getContext(), EditarOrina.class);
                    intent.putExtra("idAnalisis",listaAnalisis.get(position).getIdAnalisis());
                    intent.putExtra("nombre",listaAnalisis.get(position).getNombreAnalisis());
                    intent.putExtra("fecha",listaAnalisis.get(position).getFechaAnalisis());
                    intent.putExtra("creatinina",listaAnalisis.get(position).getCreatininaAnalisis());
                    intent.putExtra("albumina",listaAnalisis.get(position).getAlbuminaAnalisis());
                    intent.putExtra("acido",listaAnalisis.get(position).getAcidoAnalisis());
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
        adaptador=new AdaptadorAnalisis(listaAnalisis);
        recycler.setAdapter(adaptador);
        //Toast.makeText(getContext(),"No hay analisis para mostrar",Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        usuario =(Usuario)getActivity().getApplicationContext();
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
    private void eliminarAnalisis(String idAnalisis){
        StringRequest stringRequest=new StringRequest(Request.Method.POST, "https://192.168.1.77/login/eliminarAnalisis.php",new Response.Listener<String>(){
            @Override
            public void onResponse(String response) {
                cargando.dismissWithAnimation();
                new SweetAlertDialog(getContext(), SweetAlertDialog.SUCCESS_TYPE)
                        .setTitleText("Eliminado!")
                        .setContentText("Análisis Clínico correctamente")
                        .setConfirmText("Ok")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.dismiss();
                                listaAnalisis.clear();
                                cargarWebService();
                            }
                        }).show();
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
                parametros.put("idAnalisis",idAnalisis);
                parametros.put("tipo", "orina");
                return parametros;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
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