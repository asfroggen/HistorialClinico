package com.example.historialclinico.MenuPaciente.Analisis.Editar;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.net.ParseException;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.example.historialclinico.R;
import com.example.historialclinico.Usuario;

import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class EditarOrina extends AppCompatActivity {

    String nombre, fecha, creatinina, albumina, acido, idAnalisis;
    String encabezado;
    String fechaNacimiento, fechaValidar;
    SweetAlertDialog cargando;
    Usuario usuario;
    Integer anoActual, anoNac;

    ImageView ivBack;
    EditText etNombreAnalisis,etDiaAnalisis,etMesAnalisis,etAnoAnalisis,etLeucocitos,etEritrocitos,etPlaquetas;
    Button bActualizarNombre, bActualizarFecha,bActualizarDatos;
    TextView tvUnidadesLeucocitos,tvUnidadesEritrocitos,tvUnidadesPlaquetas,tvNombre,tvGlucosaAnterior,tvColesterolAnterior,tvHierroAnterior;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_orina);

        ivBack=findViewById(R.id.ivBack);
        etNombreAnalisis=findViewById(R.id.etNombreAnalisis);
        etDiaAnalisis=findViewById(R.id.etDiaAnalisis);
        etMesAnalisis=findViewById(R.id.etMesAnalisis);
        etAnoAnalisis=findViewById(R.id.etAnoAnalisis);
        etLeucocitos=findViewById(R.id.etLeucocitos);
        etEritrocitos=findViewById(R.id.etEritrocitos);
        etPlaquetas=findViewById(R.id.etPlaquetas);
        bActualizarNombre=findViewById(R.id.bActualizarNombre);
        bActualizarFecha=findViewById(R.id.bActualizarFecha);
        bActualizarDatos=findViewById(R.id.bActualizarDatos);
        tvUnidadesLeucocitos=findViewById(R.id.tvUnidadesLeucocitos);
        tvUnidadesEritrocitos=findViewById(R.id.tvUnidadesEritrocitos);
        tvUnidadesPlaquetas=findViewById(R.id.tvUnidadesPlaquetas);
        tvNombre=findViewById(R.id.tvNombre);
        tvGlucosaAnterior=findViewById(R.id.tvGlucosaAnterior);
        tvColesterolAnterior=findViewById(R.id.tvColesterolAnterior);
        tvHierroAnterior=findViewById(R.id.tvHierroAnterior);

        Intent is = getIntent();

        idAnalisis = is.getStringExtra("idAnalisis");
        nombre = is.getStringExtra("nombre");
        fecha = is.getStringExtra("fecha");
        creatinina = is.getStringExtra("creatinina");
        albumina = is.getStringExtra("albumina");
        acido = is.getStringExtra("acido");

        encabezado = "Editar: " + nombre;

        tvNombre.setText(encabezado);
        tvGlucosaAnterior.setText(creatinina);
        tvColesterolAnterior.setText(albumina);
        tvHierroAnterior.setText(acido);

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        bActualizarNombre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validarNombre().equals("OK")) {
                    cargando = new SweetAlertDialog(EditarOrina.this, SweetAlertDialog.PROGRESS_TYPE);
                    cargando.setCancelable(false);
                    cargando.getProgressHelper().setBarColor(Color.parseColor("#FE8629"));
                    cargando.show();
                    Log.d("idAnalisis:", idAnalisis);
                    actualizarAnalisis("orina", "nombre");
                }
            }
        });

        bActualizarFecha.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                if (validarFecha()) {
                    if (validarFechaAnos().equals("OK")) {
                        cargando = new SweetAlertDialog(EditarOrina.this, SweetAlertDialog.PROGRESS_TYPE);
                        cargando.setCancelable(false);
                        cargando.getProgressHelper().setBarColor(Color.parseColor("#FE8629"));
                        cargando.show();
                        actualizarAnalisis("orina", "fecha");
                    }
                }
            }
        });

        bActualizarDatos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validarDatos().equals("OK")) {
                    cargando = new SweetAlertDialog(EditarOrina.this, SweetAlertDialog.PROGRESS_TYPE);
                    cargando.setCancelable(false);
                    cargando.getProgressHelper().setBarColor(Color.parseColor("#FE8629"));
                    cargando.show();
                    actualizarAnalisis("orina", "datos");
                }
            }
        });
    }
    private String validarNombre() {
        String validacion = "OK";
        if (etNombreAnalisis.getText().toString().isEmpty()) {
            etNombreAnalisis.setError("Campo necesario");
            validacion = "Error";
        }
        return validacion;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private boolean validarFecha() {
        fechaNacimiento = etAnoAnalisis.getText().toString() + "-" + etMesAnalisis.getText().toString() + "-" + etDiaAnalisis.getText().toString();
        fechaValidar = etDiaAnalisis.getText().toString() + "-" + etMesAnalisis.getText().toString() + "-" + etAnoAnalisis.getText().toString();

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

    private String validarFechaAnos() {
        String validacion = "OK";
        Integer mesActual = 0;
        Integer mesNac;
        Integer diaNac,diaActual=0;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            Calendar fecha = Calendar.getInstance();
            anoActual = fecha.get(Calendar.YEAR);
            mesActual= fecha.get(Calendar.MONTH)+1;
            diaActual= fecha.get(Calendar.DAY_OF_MONTH);
        } else {
            anoActual = 2020;
            mesActual=12;
        }

        Log.d("mes",mesActual.toString());
        Log.d("dia",diaActual.toString());

        anoNac = Integer.parseInt(etAnoAnalisis.getText().toString());
        if ((anoActual - anoNac) >= 100 || (anoActual - anoNac) <= -1 ) {
            Toast.makeText(getApplicationContext(), "Ingrese una fecha valida", Toast.LENGTH_SHORT).show();
            validacion = "Error";
        }
        mesNac= Integer.parseInt(etMesAnalisis.getText().toString());
        if ((anoActual.equals(anoNac)) && mesNac>mesActual){
            Toast.makeText(getApplicationContext(), "Ingrese una fecha valida", Toast.LENGTH_SHORT).show();
            validacion = "Error";
        }
        diaNac=Integer.parseInt(etDiaAnalisis.getText().toString());
        if ((anoActual.equals(anoNac)) && mesNac.equals(mesActual) && diaNac>diaActual){
            Toast.makeText(getApplicationContext(), "Ingrese una fecha valida", Toast.LENGTH_SHORT).show();
            validacion = "Error";
        }
        return validacion;
    }

    private String validarDatos() {
        String validacion = "OK";

        if (etLeucocitos.getText().toString().isEmpty()) {
            etLeucocitos.setError("Campo necesario");
            validacion = "Error";
        }
        if (etEritrocitos.getText().toString().isEmpty()) {
            etEritrocitos.setError("Campo necesario");
            validacion = "Error";
        }
        if (etPlaquetas.getText().toString().isEmpty()) {
            etPlaquetas.setError("Campo necesario");
            validacion = "Error";
        }

        return validacion;
    }

    private void actualizarAnalisis(String tipo, String parametro) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://192.168.1.77/login/actualizarAnalisis.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Toast.makeText(getApplicationContext(),"Usuario registrado correctamente",Toast.LENGTH_SHORT).show();
                cargando.dismissWithAnimation();
                if (parametro.equals("nombre")) {
                    new SweetAlertDialog(EditarOrina.this, SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("Correcto!")
                            .setContentText("Nombre actualizado")
                            .setConfirmText("Ok")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    finish();
                                }
                            })
                            .show();
                } else if (parametro.equals("fecha")) {
                    new SweetAlertDialog(EditarOrina.this, SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("Correcto!")
                            .setContentText("Fecha actualizada")
                            .setConfirmText("Ok")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    finish();
                                }
                            })
                            .show();
                } else {
                    new SweetAlertDialog(EditarOrina.this, SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("Correcto!")
                            .setContentText("Datos actualizados")
                            .setConfirmText("Ok")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    finish();
                                }
                            })
                            .show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                cargando.dismissWithAnimation();
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parametros = new HashMap<String, String>();
                if (tipo.equals("orina")) {
                    switch (parametro) {
                        case "nombre":
                            parametros.put("tipo", tipo);
                            parametros.put("idAnalisis", idAnalisis);
                            parametros.put("parametro", parametro);
                            parametros.put("dato", etNombreAnalisis.getText().toString());
                            break;
                        case "fecha":
                            parametros.put("tipo", tipo);
                            parametros.put("idAnalisis", idAnalisis);
                            parametros.put("parametro", parametro);
                            parametros.put("dato", fechaNacimiento);
                            break;
                        case "datos":
                            parametros.put("tipo", tipo);
                            parametros.put("idAnalisis", idAnalisis);
                            parametros.put("parametro", parametro);
                            parametros.put("creatinina", etLeucocitos.getText().toString());
                            parametros.put("albumina", etEritrocitos.getText().toString());
                            parametros.put("acido", etPlaquetas.getText().toString());
                            break;
                    }
                }

                return parametros;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}