package com.example.historialclinico.MenuPaciente.Configuracion;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Patterns;
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
import com.example.historialclinico.R;
import com.example.historialclinico.Registro.RegistroPaciente;
import com.example.historialclinico.Usuario;
import com.google.android.material.textfield.TextInputEditText;

import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class ActualizarCredenciales extends AppCompatActivity {

    Usuario usuario;
    SweetAlertDialog cargando;

    ImageView ivBack;
    EditText etNuevoCorreo,etConfCorreo,etNuevaContra;
    TextInputEditText etContraCorreo,etContraAnterior,etConfNuevaContra;
    Button bActualizarCorreo,bActualizarContra;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actualizar_credenciales);

        ivBack=findViewById(R.id.ivBack);
        etNuevoCorreo=findViewById(R.id.etNuevoCorreo);
        etConfCorreo=findViewById(R.id.etConfCorreo);
        etContraCorreo=findViewById(R.id.etContraCorreo);
        etContraAnterior=findViewById(R.id.etContraAnterior);
        etNuevaContra=findViewById(R.id.etNuevaContra);
        etConfNuevaContra=findViewById(R.id.etConfNuevaContra);
        bActualizarCorreo=findViewById(R.id.bActualizarCorreo);
        bActualizarContra=findViewById(R.id.bActualizarContra);

        usuario =(Usuario)getApplicationContext();

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        bActualizarCorreo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              if(validarDatosCorreo().equals("OK")){
                  cargando = new SweetAlertDialog(ActualizarCredenciales.this, SweetAlertDialog.PROGRESS_TYPE);
                  cargando.setCancelable(false);
                  cargando.getProgressHelper().setBarColor(Color.parseColor("#FE8629"));
                  cargando.show();
                  actualizarCorreo("paciente");
              }
            }
        });
        bActualizarContra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validarDatosPassword().equals("OK")){
                    cargando = new SweetAlertDialog(ActualizarCredenciales.this, SweetAlertDialog.PROGRESS_TYPE);
                    cargando.setCancelable(false);
                    cargando.getProgressHelper().setBarColor(Color.parseColor("#FE8629"));
                    cargando.show();
                    actualizarPassword("paciente");
                }
            }
        });
    }
    private boolean validarArroba(){
        String emailInput= etNuevoCorreo.getText().toString();
        return !emailInput.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(emailInput).matches();
    }

    private String validarDatosCorreo(){
        String validacion="OK";
        if (!validarArroba()){
            etNuevoCorreo.setError("El correo electrónico debe tener una @ (dominio)");
            validacion="Error";
        }else if (etNuevoCorreo.getText().toString().isEmpty()){
            etNuevoCorreo.setError("Ingrese un nuevo correo electrónico");
            validacion="Error";
        }else if (etConfCorreo.getText().toString().isEmpty()){
            etNuevoCorreo.setError("Ingrese un nuevo correo electrónico");
            validacion="Error";
        }else if (!etNuevoCorreo.getText().toString().equals(etConfCorreo.getText().toString())){
            Toast.makeText(getApplicationContext(),"Los correos deben coincidir", Toast.LENGTH_SHORT).show();
            validacion="Error";
        }else if (!etContraCorreo.getText().toString().equals(usuario.getPassword())){
            Toast.makeText(getApplicationContext(),"Contraseña incorrecta", Toast.LENGTH_SHORT).show();
            validacion="Error";
        }
        return validacion;
    }

    private String validarDatosPassword(){
        String validacion="OK";
        if (etNuevaContra.getText().toString().isEmpty()){
            etNuevaContra.setError("Ingrese una nueva contraseña");
            validacion="Error";
        }else if (etConfNuevaContra.getText().toString().isEmpty()){
            etConfNuevaContra.setError("Ingrese una nueva contraseña");
            validacion="Error";
        }else if (!etNuevaContra.getText().toString().equals(etConfNuevaContra.getText().toString())){
            Toast.makeText(getApplicationContext(),"Las contraseñas deben coincidir", Toast.LENGTH_SHORT).show();
            validacion="Error";
        }else if (!etContraAnterior.getText().toString().equals(usuario.getPassword())){
            Toast.makeText(getApplicationContext(),"Contraseña incorrecta", Toast.LENGTH_SHORT).show();
            validacion="Error";
        }
        return validacion;
    }

    private void actualizarPassword(String tipo){
        StringRequest stringRequest=new StringRequest(Request.Method.POST, "https://192.168.1.77/login/actualizarPassword.php",new Response.Listener<String>(){
            @Override
            public void onResponse(String response) {
                if (!response.isEmpty()) {
                    cargando.dismissWithAnimation();
                    new SweetAlertDialog(ActualizarCredenciales.this, SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("Correcto!")
                            .setContentText("Contraseña actualizada")
                            .setConfirmText("Ok")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.dismissWithAnimation();
                                }
                            })
                            .show();
                } else {
                    cargando.dismissWithAnimation();
                    new SweetAlertDialog(ActualizarCredenciales.this, SweetAlertDialog.ERROR_TYPE)
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
        }, error -> Toast.makeText(ActualizarCredenciales.this, error.toString(),Toast.LENGTH_SHORT).show()){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parametros=new HashMap<String, String>();
                if (tipo.equals("paciente")){
                    parametros.put("dato", etNuevaContra.getText().toString());
                    parametros.put("correo",usuario.getCorreo());
                    parametros.put("tipo", tipo);
                }else{
                    parametros.put("dato", etNuevaContra.getText().toString());
                    parametros.put("correo",usuario.getCorreo());
                    parametros.put("tipo", tipo);
                }
                return parametros;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void actualizarCorreo(String tipo){
        StringRequest stringRequest=new StringRequest(Request.Method.POST, "https://192.168.1.77/login/actualizarCorreo.php",new Response.Listener<String>(){
            @Override
            public void onResponse(String response) {
                if (!response.isEmpty()) {
                    cargando.dismissWithAnimation();
                    new SweetAlertDialog(ActualizarCredenciales.this, SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("Correcto!")
                            .setContentText("Correo electrónico actualizado")
                            .setConfirmText("Ok")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.dismissWithAnimation();
                                }
                            })
                            .show();
                } else {
                    cargando.dismissWithAnimation();
                    new SweetAlertDialog(ActualizarCredenciales.this, SweetAlertDialog.ERROR_TYPE)
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
        }, error -> Toast.makeText(ActualizarCredenciales.this, error.toString(),Toast.LENGTH_SHORT).show()){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parametros=new HashMap<String, String>();
                if (tipo.equals("paciente")){
                    parametros.put("dato", etNuevoCorreo.getText().toString());
                    parametros.put("idUsuario",usuario.getIdPaciente());
                    parametros.put("tipo", tipo);
                }else{
                    parametros.put("dato", etNuevoCorreo.getText().toString());
                    parametros.put("idUsuario",usuario.getIdMedico());
                    parametros.put("tipo", tipo);
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