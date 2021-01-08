package com.example.historialclinico.Registro;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.net.ParseException;
import android.os.Build;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.historialclinico.Login;
import com.example.historialclinico.R;
import com.google.android.material.textfield.TextInputEditText;

import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class RegistroPaciente extends AppCompatActivity {

    String sexo;
    String fechaNacimiento,fechaValidar;
    Integer anoNac;
    SweetAlertDialog cargando;

    TextInputEditText etNombre,etSegNombre,etApellidoP,etApellidoM,etCorreo,etPassword,etConfPassword;
    RadioButton rbMasculino, rbFemenino;
    EditText etAnoNac,etMesNac,etDiaNac;
    Button bRegistrarPaciente;
    ImageView ivBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_paciente);

        etNombre=findViewById(R.id.etNombre);
        etSegNombre=findViewById(R.id.etSegNombre);
        etApellidoP=findViewById(R.id.etApellidoP);
        etApellidoM=findViewById(R.id.etApellidoM);
        etCorreo=findViewById(R.id.etCorreo);
        etPassword=findViewById(R.id.etPassword);
        etConfPassword=findViewById(R.id.etConfPassword);
        rbMasculino=findViewById(R.id.rbMasculino);
        rbFemenino=findViewById(R.id.rbFemenino);
        etAnoNac=findViewById(R.id.etAnoNac);
        etMesNac=findViewById(R.id.etMesNac);
        etDiaNac=findViewById(R.id.etDiaNac);
        bRegistrarPaciente=findViewById(R.id.bRegistrarPaciente);
        ivBack=findViewById(R.id.ivBack);

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        bRegistrarPaciente.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                if(validarDatos().equals("OK")){
                    if(validarFecha()){
                        if (etPassword.getText().toString().equals(etConfPassword.getText().toString())){
                            if(validarArroba()){
                                cargando = new SweetAlertDialog(RegistroPaciente.this, SweetAlertDialog.PROGRESS_TYPE);
                                cargando.setCancelable(false);
                                cargando.getProgressHelper().setBarColor(Color.parseColor("#FE8629"));
                                cargando.show();
                                validarEmail();
                            } else{
                                Toast.makeText(getApplicationContext(), "Por favor, ingrese un email valido", Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            Toast.makeText(getApplicationContext(), "Las contraseñas deben coincidir", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(getApplicationContext(),"Ingrese una fecha de nacimiento existente",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private boolean validarFecha(){
        try {
            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat formatoFecha = new SimpleDateFormat("dd-MM-yyyy");
            formatoFecha.setLenient(false);
            formatoFecha.parse(fechaValidar);
        } catch (ParseException | java.text.ParseException e) {
            return false;
        }
        return true;
    }

    private boolean validarArroba(){
        String emailInput= etCorreo.getText().toString();
        return !emailInput.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(emailInput).matches();
    }

    private void validarEmail(){
        StringRequest stringRequest=new StringRequest(Request.Method.POST, "https://192.168.1.77/login/validarEmail.php",new Response.Listener<String>(){
            @Override
            public void onResponse(String response) {
                cargando.dismissWithAnimation();
                if(!response.isEmpty()){
                    Toast.makeText(RegistroPaciente.this,"Su correo electrónico ya se encuentra registrado",Toast.LENGTH_SHORT).show();
                }else{
                    insertarUsuario();
                    //Toast.makeText(RegistroPaciente.this,"Todo correcto",Toast.LENGTH_SHORT).show();
                }
            }
        }, error -> Toast.makeText(RegistroPaciente.this, error.toString(),Toast.LENGTH_SHORT).show()){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                String paciente="paciente";
                Map<String, String> parametros=new HashMap<String, String>();
                parametros.put("usuario",etCorreo.getText().toString());
                parametros.put("tipoUsuario",paciente);
                return parametros;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void insertarUsuario(){
        StringRequest stringRequest=new StringRequest(Request.Method.POST, "https://192.168.1.77/login/insertarPaciente.php",new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Toast.makeText(getApplicationContext(),"Usuario registrado correctamente",Toast.LENGTH_SHORT).show();
                cargando.dismissWithAnimation();
                new SweetAlertDialog(RegistroPaciente.this, SweetAlertDialog.SUCCESS_TYPE)
                        .setTitleText("Correcto!")
                        .setContentText("Su usuario ha sido dado de alta de manera exitosa")
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
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                cargando.dismissWithAnimation();
                Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_SHORT).show();
            }
        }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError{
                String estado="Activo";

                Map<String, String> parametros=new HashMap<String, String>();
                parametros.put("nombre",etNombre.getText().toString());
                parametros.put("segNombre",etSegNombre.getText().toString());
                parametros.put("apellidoPat",etApellidoP.getText().toString());
                parametros.put("apellidoMat",etApellidoM.getText().toString());
                parametros.put("correo",etCorreo.getText().toString());
                parametros.put("contraseña",etPassword.getText().toString());
                parametros.put("estado",estado);
                parametros.put("sexo",sexo);
                parametros.put("fechaNacimiento",fechaNacimiento);

                return parametros;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private String validarDatos(){
        Integer anoActual;

        String validacion="OK";

        fechaNacimiento=etAnoNac.getText().toString()+"-"+etMesNac.getText().toString()+"-"+etDiaNac.getText().toString();
        fechaValidar=etDiaNac.getText().toString()+"-"+etMesNac.getText().toString()+"-"+etAnoNac.getText().toString();
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            Calendar fecha = Calendar.getInstance();
            anoActual=fecha.get(Calendar.YEAR);
        }else{
            anoActual=2020;
        }
        if(etPassword.getText().toString().length()<6){
            //binding.etPassword.setError("La contraseña debe tener al menos 6 caracteres");
            Toast.makeText(getApplicationContext(),"La contraseña debe tener al menos 6 caracteres",Toast.LENGTH_SHORT).show();
            validacion="Error";
        }

        if (rbMasculino.isChecked()){
            sexo="Masculino";
        }else{
            sexo="Femenino";
        }

        if (etNombre.getText().toString().isEmpty()){
            etNombre.setError("Campo necesario");
            validacion="Error";
        }
        if (etApellidoP.getText().toString().isEmpty()){
            etApellidoP.setError("Campo necesario");
            validacion="Error";
        }
        if (etApellidoM.getText().toString().isEmpty()){
            etApellidoM.setError("Campo necesario");
            validacion="Error";
        }
        if (etDiaNac.getText().toString().length()<2 && etDiaNac.getText().toString().length()>0){
            etDiaNac.setError("Formato de día incorrecto (usar DD)");
            validacion="Error";
        }
        if (etMesNac.getText().toString().length()<2 && etMesNac.getText().toString().length()>0){
            etMesNac.setError("Formato de mes incorrecto (usar MM)");
            validacion="Error";
        }
        if (etAnoNac.getText().toString().length()<2 && etAnoNac.getText().toString().length()>0){
            etAnoNac.setError("Formato de año incorrecto (usar AAAA)");
            validacion="Error";
        }
        if (etDiaNac.getText().toString().isEmpty()){
            etDiaNac.setError("Campo necesario");
            validacion="Error";
        }
        if (etMesNac.getText().toString().isEmpty()){
            etMesNac.setError("Campo necesario");
            validacion="Error";
        }
        if (etAnoNac.getText().toString().isEmpty()){
            etAnoNac.setError("Campo necesario");
            validacion="Error";
        }else{
            anoNac=Integer.parseInt(etAnoNac.getText().toString());
            if((anoActual-anoNac)>=100 || (anoActual-anoNac)<8){
                Toast.makeText(getApplicationContext(),"Ingrese una edad valida", Toast.LENGTH_SHORT).show();
                validacion="Error";
            }
        }
        if (etCorreo.getText().toString().isEmpty()){
            etCorreo.setError("Campo necesario");
            validacion="Error";
        }
        if (etPassword.getText().toString().isEmpty()){
            etPassword.setError("Campo necesario");
            validacion="Error";
        }
        if (etConfPassword.getText().toString().isEmpty()){
            etConfPassword.setError("Campo necesario");
            validacion="Error";
        }
        return validacion;
    }

    @Override //====To avoid memory peaks====
    protected void onDestroy() {
        super.onDestroy();
    }
}