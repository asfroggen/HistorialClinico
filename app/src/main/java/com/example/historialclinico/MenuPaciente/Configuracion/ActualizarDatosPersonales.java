package com.example.historialclinico.MenuPaciente.Configuracion;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.net.ParseException;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.historialclinico.Login;
import com.example.historialclinico.MenuMedico.Configuracion.ActualizarCredencialesMedico;
import com.example.historialclinico.R;
import com.example.historialclinico.Registro.RegistroPaciente;
import com.example.historialclinico.Usuario;

import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class ActualizarDatosPersonales extends AppCompatActivity {


    String fechaNacimiento;
    Usuario usuario;
    SweetAlertDialog cargando;

    EditText etActualizarNombre,etActualizarSegNombre,etActualizarApellidoP,etActualizarApellidoM,etDiaNac,etMesNac,etAnoNac;
    Button bActualizarNombre,bActualizarSegNombre,bActualizarApellidoP,bActualizarApellidoM,bActualizarSexo,bActualizarFechaNacimiento;
    RadioButton rbMasculino,rbFemenino;
    ImageView ivBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actualizar_datos_personales);

        usuario =(Usuario)getApplicationContext();

        etActualizarNombre=findViewById(R.id.etActualizarNombre);
        etActualizarSegNombre=findViewById(R.id.etActualizarSegNombre);
        etActualizarApellidoP=findViewById(R.id.etActualizarApellidoP);
        etActualizarApellidoM=findViewById(R.id.etActualizarApellidoM);
        bActualizarNombre=findViewById(R.id.bActualizarNombre);
        bActualizarSegNombre=findViewById(R.id.bActualizarSegNombre);
        bActualizarApellidoP=findViewById(R.id.bActualizarApellidoP);
        bActualizarApellidoM=findViewById(R.id.bActualizarApellidoM);
        bActualizarSexo=findViewById(R.id.bActualizarSexo);
        bActualizarFechaNacimiento=findViewById(R.id.bActualizarFechaNacimiento);
        rbMasculino=findViewById(R.id.rbMasculino);
        rbFemenino=findViewById(R.id.rbFemenino);
        ivBack=findViewById(R.id.ivBack);
        etDiaNac=findViewById(R.id.etDiaNac);
        etMesNac=findViewById(R.id.etMesNac);
        etAnoNac=findViewById(R.id.etAnoNac);



        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        bActualizarNombre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!etActualizarNombre.getText().toString().isEmpty()){
                    cargando = new SweetAlertDialog(ActualizarDatosPersonales.this, SweetAlertDialog.PROGRESS_TYPE);
                    cargando.setCancelable(false);
                    cargando.getProgressHelper().setBarColor(Color.parseColor("#FE8629"));
                    cargando.show();
                    actualizarDatosPaciente("nombre");
                }else{
                    Toast.makeText(getApplicationContext(),"Ingrese su nombre",Toast.LENGTH_SHORT).show();
                }
            }
        });
        bActualizarSegNombre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!etActualizarSegNombre.getText().toString().isEmpty()){
                    cargando = new SweetAlertDialog(ActualizarDatosPersonales.this, SweetAlertDialog.PROGRESS_TYPE);
                    cargando.setCancelable(false);
                    cargando.getProgressHelper().setBarColor(Color.parseColor("#FE8629"));
                    cargando.show();
                    actualizarDatosPaciente("segNombre");
                }else{
                    Toast.makeText(getApplicationContext(),"Ingrese su nombre",Toast.LENGTH_SHORT).show();
                }
            }
        });
        bActualizarApellidoP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!etActualizarApellidoP.getText().toString().isEmpty()){
                    cargando = new SweetAlertDialog(ActualizarDatosPersonales.this, SweetAlertDialog.PROGRESS_TYPE);
                    cargando.setCancelable(false);
                    cargando.getProgressHelper().setBarColor(Color.parseColor("#FE8629"));
                    cargando.show();
                    actualizarDatosPaciente("apellidoPat");
                }else{
                    Toast.makeText(getApplicationContext(),"Ingrese su apellido paterno",Toast.LENGTH_SHORT).show();
                }
            }
        });
        bActualizarApellidoM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!etActualizarApellidoM.getText().toString().isEmpty()){
                    cargando = new SweetAlertDialog(ActualizarDatosPersonales.this, SweetAlertDialog.PROGRESS_TYPE);
                    cargando.setCancelable(false);
                    cargando.getProgressHelper().setBarColor(Color.parseColor("#FE8629"));
                    cargando.show();
                    actualizarDatosPaciente("apellidoMat");
                }else{
                    Toast.makeText(getApplicationContext(),"Ingrese su apellido materno",Toast.LENGTH_SHORT).show();
                }
            }
        });
        bActualizarSexo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cargando = new SweetAlertDialog(ActualizarDatosPersonales.this, SweetAlertDialog.PROGRESS_TYPE);
                cargando.setCancelable(false);
                cargando.getProgressHelper().setBarColor(Color.parseColor("#FE8629"));
                cargando.show();
                actualizarDatosPaciente("sexo");
            }
        });
        bActualizarFechaNacimiento.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                String fechaValidar;
                fechaNacimiento=etAnoNac.getText().toString()+"-"+etMesNac.getText().toString()+"-"+etDiaNac.getText().toString();
                fechaValidar=etDiaNac.getText().toString()+"-"+etMesNac.getText().toString()+"-"+etAnoNac.getText().toString();
                if (validarFecha(fechaValidar)){
                    if(validarEdad()){
                        cargando = new SweetAlertDialog(ActualizarDatosPersonales.this, SweetAlertDialog.PROGRESS_TYPE);
                        cargando.setCancelable(false);
                        cargando.getProgressHelper().setBarColor(Color.parseColor("#FE8629"));
                        cargando.show();
                        actualizarDatosPaciente("fechaNacimiento");
                    }else{
                        Toast.makeText(getApplicationContext(),"Ingrese una fecha de nacimiento valida",Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(getApplicationContext(),"Ingrese una fecha de nacimiento existente",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean validarEdad(){
        Integer anoNac,anoActual;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            Calendar fecha = Calendar.getInstance();
            anoActual=fecha.get(Calendar.YEAR);
        }else{
            anoActual=2020;
        }
        anoNac=Integer.parseInt(etAnoNac.getText().toString());
        //Toast.makeText(getApplicationContext(),"Ingrese una edad valida", Toast.LENGTH_SHORT).show();
        return (anoActual - anoNac) < 100 && (anoActual - anoNac) >= 8;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private boolean validarFecha(String fechaValidar){
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

    private void actualizarDatosPaciente(String tipo){
        StringRequest stringRequest=new StringRequest(Request.Method.POST, "https://192.168.1.77/login/actualizarDatosPaciente.php",new Response.Listener<String>(){
            @Override
            public void onResponse(String response) {
                if(!response.isEmpty()){
                    switch (tipo) {
                        case "nombre":
                            cargando.dismissWithAnimation();
                            new SweetAlertDialog(ActualizarDatosPersonales.this, SweetAlertDialog.SUCCESS_TYPE)
                                    .setTitleText("Correcto!")
                                    .setContentText("Nombre actualizado")
                                    .setConfirmText("Ok")
                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sDialog) {
                                            sDialog.dismissWithAnimation();
                                        }
                                    })
                                    .show();
                            break;
                        case "segNombre":
                            cargando.dismissWithAnimation();
                            new SweetAlertDialog(ActualizarDatosPersonales.this, SweetAlertDialog.SUCCESS_TYPE)
                                    .setTitleText("Correcto!")
                                    .setContentText("Segundo nombre actualizado")
                                    .setConfirmText("Ok")
                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sDialog) {
                                            sDialog.dismissWithAnimation();
                                        }
                                    })
                                    .show();
                            break;
                        case "apellidoPat":
                            cargando.dismissWithAnimation();
                            new SweetAlertDialog(ActualizarDatosPersonales.this, SweetAlertDialog.SUCCESS_TYPE)
                                    .setTitleText("Correcto!")
                                    .setContentText("Apellido paterno actualizado")
                                    .setConfirmText("Ok")
                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sDialog) {
                                            sDialog.dismissWithAnimation();
                                        }
                                    })
                                    .show();
                            break;
                        case "apellidoMat":
                            cargando.dismissWithAnimation();
                            new SweetAlertDialog(ActualizarDatosPersonales.this, SweetAlertDialog.SUCCESS_TYPE)
                                    .setTitleText("Correcto!")
                                    .setContentText("Apellido materno actualizado")
                                    .setConfirmText("Ok")
                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sDialog) {
                                            sDialog.dismissWithAnimation();
                                        }
                                    })
                                    .show();
                            break;
                        case "sexo":
                            cargando.dismissWithAnimation();
                            new SweetAlertDialog(ActualizarDatosPersonales.this, SweetAlertDialog.SUCCESS_TYPE)
                                    .setTitleText("Correcto!")
                                    .setContentText("Sexo actualizado")
                                    .setConfirmText("Ok")
                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sDialog) {
                                            sDialog.dismissWithAnimation();
                                        }
                                    })
                                    .show();
                            break;
                        case "fechaNacimiento":
                            cargando.dismissWithAnimation();
                            new SweetAlertDialog(ActualizarDatosPersonales.this, SweetAlertDialog.SUCCESS_TYPE)
                                    .setTitleText("Correcto!")
                                    .setContentText("Fecha de nacimiento actualizada")
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
                    new SweetAlertDialog(ActualizarDatosPersonales.this, SweetAlertDialog.ERROR_TYPE)
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
        }, error -> Toast.makeText(ActualizarDatosPersonales.this, error.toString(),Toast.LENGTH_SHORT).show()){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parametros=new HashMap<String, String>();
                switch (tipo) {
                    case "nombre":
                        parametros.put("dato", etActualizarNombre.getText().toString());
                        parametros.put("correo",usuario.getCorreo());
                        parametros.put("tipo", tipo);
                        break;
                    case "segNombre":
                        parametros.put("dato", etActualizarSegNombre.getText().toString());
                        parametros.put("correo",usuario.getCorreo());
                        parametros.put("tipo", tipo);
                        break;
                    case "apellidoPat":
                        parametros.put("dato", etActualizarApellidoP.getText().toString());
                        parametros.put("correo",usuario.getCorreo());
                        parametros.put("tipo", tipo);
                        break;
                    case "apellidoMat":
                        parametros.put("dato", etActualizarApellidoM.getText().toString());
                        parametros.put("correo",usuario.getCorreo());
                        parametros.put("tipo", tipo);
                        break;
                    case "sexo":
                        if (rbMasculino.isChecked()){
                            parametros.put("dato", "Masculino");
                            parametros.put("correo",usuario.getCorreo());
                            parametros.put("tipo", tipo);
                        }else {
                            parametros.put("dato", "Femenino");
                            parametros.put("correo",usuario.getCorreo());
                            parametros.put("tipo", tipo);
                        }
                        break;
                    case "fechaNacimiento":
                        parametros.put("dato", fechaNacimiento);
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