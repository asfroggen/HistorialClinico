package com.example.historialclinico.MenuMedico.Configuracion;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
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
import com.example.historialclinico.MedicoActivity;
import com.example.historialclinico.MenuPaciente.Configuracion.ActualizarDatosPersonales;
import com.example.historialclinico.MenuPaciente.Configuracion.EliminarCuenta;
import com.example.historialclinico.R;
import com.example.historialclinico.Usuario;
import com.google.android.material.textfield.TextInputEditText;

import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class EliminarCuentaMedico extends AppCompatActivity {

    Usuario usuario;
    SweetAlertDialog cargando;

    TextInputEditText etPassword,etConfPassword;
    ImageView ivBack;
    Button bEliminarCuenta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eliminar_cuenta_medico);

        usuario =(Usuario)getApplicationContext();

        etPassword=findViewById(R.id.etPassword);
        etConfPassword=findViewById(R.id.etConfPassword);
        ivBack=findViewById(R.id.ivBack);
        bEliminarCuenta=findViewById(R.id.bEliminarCuenta);


        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        bEliminarCuenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validarDatos().equals("OK")){
                    preguntar();
                }else{
                    Toast.makeText(getApplicationContext(),"Debe ingresar las credenciales con las que inicia sesión",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void preguntar(){
        AlertDialog.Builder builder= new AlertDialog.Builder(EliminarCuentaMedico.this);
        builder.setTitle("¿Ya te vas?");
        builder.setMessage("¿Estas seguro que quieres eliminar tu cuenta?")
                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        cargando = new SweetAlertDialog(EliminarCuentaMedico.this, SweetAlertDialog.PROGRESS_TYPE);
                        cargando.setCancelable(false);
                        cargando.getProgressHelper().setBarColor(Color.parseColor("#47BDB5"));
                        cargando.show();
                        eliminarCuenta("medico");
                    }
                }).setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        }).show();
    }
    private String validarDatos(){
        String validacion="OK";
        if(!etPassword.getText().toString().equals(usuario.getPassword())){
            validacion="Error";
        }else if(!etConfPassword.getText().toString().equals(usuario.getPassword())){
            validacion="Error";
        }

        return validacion;
    }

    private void eliminarCuenta(String tipo){
        StringRequest stringRequest=new StringRequest(Request.Method.POST, "https://192.168.1.77/login/eliminarCuenta.php",new Response.Listener<String>(){
            @Override
            public void onResponse(String response) {
                if (!response.isEmpty()) {
                    cargando.dismissWithAnimation();
                    new SweetAlertDialog(EliminarCuentaMedico.this, SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("Adios!")
                            .setContentText("Su cuenta ha sido eliminada")
                            .setConfirmText("Ok")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    Intent intent=new Intent(getApplicationContext(), Login.class);
                                    startActivity(intent);
                                }
                            })
                            .show();
                } else {
                    cargando.dismissWithAnimation();
                    new SweetAlertDialog(EliminarCuentaMedico.this, SweetAlertDialog.ERROR_TYPE)
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
        }, error -> Toast.makeText(EliminarCuentaMedico.this, error.toString(),Toast.LENGTH_SHORT).show()){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parametros=new HashMap<String, String>();

                parametros.put("correo",usuario.getCorreo());
                parametros.put("tipo", tipo);

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