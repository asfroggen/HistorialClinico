package com.example.historialclinico.Registro;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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
import com.google.android.material.textfield.TextInputEditText;

import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class ReiniciarContra extends AppCompatActivity {


    String correo;
    SweetAlertDialog cargando;

    ImageView ivBack;
    TextInputEditText etNuevaContra,etConfNuevaContra;
    Button bActualizarPass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reiniciar_contra);

        ivBack=findViewById(R.id.ivBack);
        etNuevaContra=findViewById(R.id.etNuevaContra);
        etConfNuevaContra=findViewById(R.id.etConfNuevaContra);
        bActualizarPass=findViewById(R.id.bActualizarPass);

        Intent intent = getIntent();
        correo = intent.getStringExtra("correo");

        bActualizarPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validarDatos().equals("OK")){
                    cargando = new SweetAlertDialog(ReiniciarContra.this, SweetAlertDialog.PROGRESS_TYPE);
                    cargando.setCancelable(false);
                    cargando.getProgressHelper().setBarColor(Color.parseColor("#FE8629"));
                    cargando.show();
                    actualizarPassword();
                }
            }
        });
    }
    private String validarDatos(){
        String validacion="OK";
        if(etNuevaContra.getText().toString().length()<6){
            //binding.etNuevaContra.setError("La contraseña debe tener al menos 6 caracteres");
            Toast.makeText(getApplicationContext(),"La contraseña debe tener al menos 6 caracteres",Toast.LENGTH_SHORT).show();
            validacion="Error";
        }
        if(!etNuevaContra.getText().toString().equals(etConfNuevaContra.getText().toString())){
            etNuevaContra.setError("Las contraseñas deben coincidir");
            validacion="Error";
        }
        return validacion;
    }

    private void actualizarPassword(){
        StringRequest stringRequest=new StringRequest(Request.Method.POST, "https://192.168.1.77/login/reiniciarPassword.php",new Response.Listener<String>(){
            @Override
            public void onResponse(String response) {
                        //Toast.makeText(getApplicationContext(),"Contraseña reestablecida",Toast.LENGTH_SHORT).show();
                cargando.dismissWithAnimation();
                new SweetAlertDialog(ReiniciarContra.this, SweetAlertDialog.SUCCESS_TYPE)
                        .setTitleText("Correcto!")
                        .setContentText("Contraseña reestablecida")
                        .setConfirmText("Ok")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                Intent intent=new Intent(getApplicationContext(), Login.class);
                                startActivity(intent);
                            }
                        })
                        .show();
            }
        }, error -> Toast.makeText(ReiniciarContra.this, error.toString(),Toast.LENGTH_SHORT).show()){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                String usuario="paciente";
                Map<String, String> parametros=new HashMap<String, String>();
                parametros.put("usuario",correo);
                parametros.put("password",etNuevaContra.getText().toString());
                parametros.put("tipoUsuario",usuario);

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