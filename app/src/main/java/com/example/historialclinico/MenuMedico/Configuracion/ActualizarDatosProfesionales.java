package com.example.historialclinico.MenuMedico.Configuracion;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.OneShotPreDrawListener;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.historialclinico.Login;
import com.example.historialclinico.MenuPaciente.Configuracion.ActualizarDatosPersonales;
import com.example.historialclinico.R;
import com.example.historialclinico.Usuario;

import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class ActualizarDatosProfesionales extends AppCompatActivity {

    Usuario usuario;
    SweetAlertDialog cargando;

    ImageView ivBack;
    EditText etUbicacion,etEspecialidad;
    Button bActualizarUbicacion,bActualizarEspecialidad;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actualizar_datos_profesionales);

        usuario =(Usuario)getApplicationContext();

        ivBack=findViewById(R.id.ivBack);
        etUbicacion=findViewById(R.id.etUbicacion);
        etEspecialidad=findViewById(R.id.etEspecialidad);
        bActualizarUbicacion=findViewById(R.id.bActualizarUbicacion);
        bActualizarEspecialidad=findViewById(R.id.bActualizarEspecialidad);

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        bActualizarEspecialidad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!etEspecialidad.getText().toString().isEmpty()){
                    cargando = new SweetAlertDialog(ActualizarDatosProfesionales.this, SweetAlertDialog.PROGRESS_TYPE);
                    cargando.setCancelable(false);
                    cargando.getProgressHelper().setBarColor(Color.parseColor("#47BDB5"));
                    cargando.show();
                    actualizarDatosProfesionales("especialidad");
                }
            }
        });
        bActualizarUbicacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!etUbicacion.getText().toString().isEmpty()){
                    cargando = new SweetAlertDialog(ActualizarDatosProfesionales.this, SweetAlertDialog.PROGRESS_TYPE);
                    cargando.setCancelable(false);
                    cargando.getProgressHelper().setBarColor(Color.parseColor("#47BDB5"));
                    cargando.show();
                    actualizarDatosProfesionales("ubicacion");
                }
            }
        });
    }
    private void actualizarDatosProfesionales(String tipo){
        StringRequest stringRequest=new StringRequest(Request.Method.POST, "https://192.168.1.77/login/actualizarDatosProfesionales.php",new Response.Listener<String>(){
            @Override
            public void onResponse(String response) {
                if(!response.isEmpty()){
                    switch (tipo) {
                        case "especialidad":
                            cargando.dismissWithAnimation();
                            new SweetAlertDialog(ActualizarDatosProfesionales.this, SweetAlertDialog.SUCCESS_TYPE)
                                    .setTitleText("Correcto!")
                                    .setContentText("Especialidad actualizada")
                                    .setConfirmText("Ok")
                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sDialog) {
                                            sDialog.dismissWithAnimation();
                                        }
                                    })
                                    .show();
                            break;
                        case "ubicacion":
                            cargando.dismissWithAnimation();
                            new SweetAlertDialog(ActualizarDatosProfesionales.this, SweetAlertDialog.SUCCESS_TYPE)
                                    .setTitleText("Correcto!")
                                    .setContentText("Ubicación actualizada")
                                    .setConfirmText("Ok")
                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sDialog) {
                                            sDialog.dismissWithAnimation();
                                        }
                                    })
                                    .show();
                            break;
                    }
                }else{
                    cargando.dismissWithAnimation();
                    new SweetAlertDialog(ActualizarDatosProfesionales.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Error")
                            .setContentText("Algo salió mal")
                            .setConfirmText("Ok")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.dismissWithAnimation();
                                }
                            })
                            .show();
                }
            }
        }, error -> Toast.makeText(ActualizarDatosProfesionales.this, error.toString(),Toast.LENGTH_SHORT).show()){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parametros=new HashMap<String, String>();
                switch (tipo) {
                    case "especialidad":
                        parametros.put("dato", etEspecialidad.getText().toString());
                        parametros.put("correo",usuario.getCorreo());
                        parametros.put("tipo", tipo);
                        break;
                    case "ubicacion":
                        parametros.put("dato", etUbicacion.getText().toString());
                        parametros.put("correo",usuario.getCorreo());
                        parametros.put("tipo", tipo);
                        break;
                }

                return parametros;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    @Override //====To avoid memory peaks====
    protected void onDestroy() {
        super.onDestroy();
    }
}