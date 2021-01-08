package com.example.historialclinico.MenuPaciente.Medicos;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.historialclinico.Login;
import com.example.historialclinico.R;
import com.example.historialclinico.Usuario;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
import de.hdodenhof.circleimageview.CircleImageView;

public class MedicoDetalle extends AppCompatActivity {

    CircleImageView tvMedicoImagen;
    TextView tvMedicoNombre,tvMedicoEspecialidad,tvMedicoCorreo,tvMedicoUbicacion;
    Button bAgregarMedico;
    ImageView bBack;
    String nombre,especialidad,correo,ubicacion;
    String imagen,idMedico;
    SweetAlertDialog cargando;
    Usuario usuario;
    String misMedicos="false";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medico_detalle);


        usuario =(Usuario)getApplicationContext();

        Bundle bundle=this.getIntent().getExtras();
        assert bundle != null;

        misMedicos=bundle.getString("misMedicos");
        idMedico=bundle.getString("id");
        nombre=bundle.getString("nombre");
        especialidad=bundle.getString("especialidad");
        correo=bundle.getString("correo");
        ubicacion=bundle.getString("ubicacion");
        imagen=bundle.getString("imagen");

        tvMedicoNombre=(TextView)findViewById(R.id.tvMedicoNombre);
        tvMedicoEspecialidad=(TextView)findViewById(R.id.tvMedicoEspecialidad);
        tvMedicoCorreo=(TextView)findViewById(R.id.tvMedicoCorreo);
        tvMedicoUbicacion=(TextView)findViewById(R.id.tvMedicoUbicacion);

        tvMedicoImagen=(CircleImageView)findViewById(R.id.ivMedicoPerfil);
        bAgregarMedico=(Button)findViewById(R.id.bAgregarMedico);
        bBack=(ImageView) findViewById(R.id.ivBack);

        InicializarMedico();


        bAgregarMedico.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(misMedicos.equals("true")){
                    new SweetAlertDialog(MedicoDetalle.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("¿Eliminar médico?")
                            .setContentText("¿Está seguro que desea eliminar este contacto?")
                            .setConfirmText("Si")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.dismissWithAnimation();
                                    cargando = new SweetAlertDialog(MedicoDetalle.this, SweetAlertDialog.PROGRESS_TYPE);
                                    cargando.setCancelable(false);
                                    cargando.getProgressHelper().setBarColor(Color.parseColor("#FE8629"));
                                    cargando.show();
                                    eliminarSolicitud(idMedico);
                                }
                            })
                            .setCancelButton("No", new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.dismissWithAnimation();
                                }
                            })
                            .show();
                }else {
                    cargando = new SweetAlertDialog(MedicoDetalle.this, SweetAlertDialog.PROGRESS_TYPE);
                    cargando.setCancelable(false);
                    cargando.getProgressHelper().setBarColor(Color.parseColor("#FE8629"));
                    cargando.show();
                    mandarSolicitud();
                }
            }
        });
        bBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }


    private void eliminarSolicitud(String idMedico){
        StringRequest stringRequest=new StringRequest(Request.Method.POST, "https://192.168.1.77/login/eliminarSolicitud.php",new Response.Listener<String>(){
            @Override
            public void onResponse(String response) {
                cargando.dismissWithAnimation();
                new SweetAlertDialog(MedicoDetalle.this, SweetAlertDialog.SUCCESS_TYPE)
                        .setTitleText("Adios!")
                        .setContentText("Contacto eliminado correctamente")
                        .setConfirmText("Ok")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.dismiss();
                                finish();
                            }
                        }).show();
            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                cargando.dismissWithAnimation();
                Toast.makeText(MedicoDetalle.this, error.toString(),Toast.LENGTH_SHORT).show();
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
        RequestQueue requestQueue= Volley.newRequestQueue(MedicoDetalle.this);
        requestQueue.add(stringRequest);
    }
    private void validarSolicitud(){
        StringRequest stringRequest=new StringRequest(Request.Method.POST, "https://192.168.1.77/login/validarSolicitud.php",new Response.Listener<String>(){
            @Override
            public void onResponse(String response) {
                if(!response.isEmpty()){
                    String solicitud="Solicitud enviada";
                    bAgregarMedico.setEnabled(false);
                    bAgregarMedico.setBackgroundResource(R.drawable.button_rounded_gradient_gray);
                    bAgregarMedico.setText(solicitud);
                }else{
                    validarContacto();
                }
            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MedicoDetalle.this, error.toString(),Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parametros=new HashMap<String, String>();
                parametros.put("idMedico",idMedico);
                parametros.put("idPaciente",usuario.getIdPaciente());
                return parametros;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void validarContacto(){
        StringRequest stringRequest=new StringRequest(Request.Method.POST, "https://192.168.1.77/login/validarContacto.php",new Response.Listener<String>(){
            @Override
            public void onResponse(String response) {
                if(!response.isEmpty()){
                    String solicitud="Agregado";
                    bAgregarMedico.setEnabled(false);
                    bAgregarMedico.setBackgroundResource(R.drawable.button_rounded_gradient_gray);
                    bAgregarMedico.setText(solicitud);
                }
            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MedicoDetalle.this, error.toString(),Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parametros=new HashMap<String, String>();
                parametros.put("idMedico",idMedico);
                parametros.put("idPaciente",usuario.getIdPaciente());
                return parametros;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void mandarSolicitud(){
        StringRequest stringRequest=new StringRequest(Request.Method.POST, "https://192.168.1.77/login/mandarSolicitud.php",new Response.Listener<String>(){
            @Override
            public void onResponse(String response) {
                    cargando.dismissWithAnimation();
                    new SweetAlertDialog(MedicoDetalle.this, SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("Enviado!")
                            .setContentText("Se ha enviado tu solicitud correctamente")
                            .setConfirmText("Ok")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.dismiss();
                                    finish();
                                }
                            }).show();
            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MedicoDetalle.this, error.toString(),Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parametros=new HashMap<String, String>();
                parametros.put("idMedico",idMedico);
                parametros.put("idPaciente",usuario.getIdPaciente());
                return parametros;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
    private void InicializarMedico(){
        String especialidadTexto="Especialidad: "+especialidad;
        String contacto="Contacto: "+correo;
        String domicilio="Ubicacion: "+ubicacion;
        tvMedicoNombre.setText(nombre);
        tvMedicoEspecialidad.setText(especialidadTexto);
        tvMedicoCorreo.setText(contacto);
        tvMedicoUbicacion.setText(domicilio);
        if (!imagen.equals("No hay")){
            tvMedicoImagen.setImageBitmap(StringToBitMap(imagen));
        }else{
            tvMedicoImagen.setImageResource(R.mipmap.user_icon_dark);
        }
        if(misMedicos.equals("true")){
            String eliminar="Eliminar médico";
            bAgregarMedico.setText(eliminar);
            bAgregarMedico.setBackgroundResource(R.drawable.button_rounded_gradient_red);
        }else{
            validarSolicitud();
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
}