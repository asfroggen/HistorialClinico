package com.example.historialclinico.MenuPaciente.Analisis.Agregar;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.net.ParseException;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
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

public class AgregarBiometria extends AppCompatActivity {

    String fechaNacimiento,fechaValidar;
    SweetAlertDialog cargando;
    Usuario usuario;
    Integer anoActual,anoNac;

    ImageView ivBack;
    EditText etNombreAnalisis,etDiaAnalisis,etMesAnalisis,etAnoAnalisis,etLeucocitos,etEritrocitos,etPlaquetas;
    Button bAgregarAnalisis;
    TextView tvUnidadesLeucocitos,tvUnidadesEritrocitos,tvUnidadesPlaquetas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_biometria);

        usuario =(Usuario)getApplicationContext();

        ivBack=findViewById(R.id.ivBack);

        etNombreAnalisis=findViewById(R.id.etNombreAnalisis);
        etDiaAnalisis=findViewById(R.id.etDiaAnalisis);
        etMesAnalisis=findViewById(R.id.etMesAnalisis);
        etAnoAnalisis=findViewById(R.id.etAnoAnalisis);
        etLeucocitos=findViewById(R.id.etLeucocitos);
        etEritrocitos=findViewById(R.id.etEritrocitos);
        etPlaquetas=findViewById(R.id.etPlaquetas);

        bAgregarAnalisis=findViewById(R.id.bAgregarAnalisis);

        tvUnidadesLeucocitos=findViewById(R.id.tvUnidadesLeucocitos);
        tvUnidadesEritrocitos=findViewById(R.id.tvUnidadesEritrocitos);
        tvUnidadesPlaquetas=findViewById(R.id.tvUnidadesPlaquetas);

        tvUnidadesLeucocitos.setText(Html.fromHtml("10<sup>3</sup> μl"));
        tvUnidadesEritrocitos.setText(Html.fromHtml("10<sup>3</sup> μl"));
        tvUnidadesPlaquetas.setText(Html.fromHtml("10<sup>3</sup>"));

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        bAgregarAnalisis.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                if(validarDatos().equals("OK")){
                    if (validarFecha()){
                        if(validarFechaAnos().equals("OK")){
                            cargando = new SweetAlertDialog(AgregarBiometria.this, SweetAlertDialog.PROGRESS_TYPE);
                            cargando.setCancelable(false);
                            cargando.getProgressHelper().setBarColor(Color.parseColor("#FE8629"));
                            cargando.show();
                            insertarAnalisis();
                        }
                    }else{
                        Toast.makeText(getApplicationContext(),"Debe ingresar una fecha valida",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
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
    private String validarDatos(){
        String validacion="OK";

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            Calendar fecha = Calendar.getInstance();
            anoActual=fecha.get(Calendar.YEAR);
        }else{
            anoActual=2020;
        }

        anoNac=Integer.parseInt(etAnoAnalisis.getText().toString());
        if((anoActual-anoNac)>=100 || (anoActual-anoNac)<0){
            Toast.makeText(getApplicationContext(),"Ingrese una fecha valida", Toast.LENGTH_SHORT).show();
            validacion="Error";
        }


        if(etNombreAnalisis.getText().toString().isEmpty()){
            etNombreAnalisis.setError("Campo necesario");
            validacion="Error";
        }
        if(etLeucocitos.getText().toString().isEmpty()){
            etLeucocitos.setError("Campo necesario");
            validacion="Error";
        }
        if(etEritrocitos.getText().toString().isEmpty()){
            etEritrocitos.setError("Campo necesario");
            validacion="Error";
        }
        if(etPlaquetas.getText().toString().isEmpty()){
            etPlaquetas.setError("Campo necesario");
            validacion="Error";
        }
        if(etDiaAnalisis.getText().toString().isEmpty()){
            etDiaAnalisis.setError("Campo necesario");
            validacion="Error";
        }
        if(etMesAnalisis.getText().toString().isEmpty()){
            etMesAnalisis.setError("Campo necesario");
            validacion="Error";
        }
        if(etAnoAnalisis.getText().toString().isEmpty()){
            etAnoAnalisis.setError("Campo necesario");
            validacion="Error";
        }
        return validacion;
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    private boolean validarFecha(){
        fechaNacimiento=etAnoAnalisis.getText().toString()+"-"+etMesAnalisis.getText().toString()+"-"+etDiaAnalisis.getText().toString();
        fechaValidar=etDiaAnalisis.getText().toString()+"-"+etMesAnalisis.getText().toString()+"-"+etAnoAnalisis.getText().toString();

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

    private void insertarAnalisis(){
        StringRequest stringRequest=new StringRequest(Request.Method.POST, "https://192.168.1.77/login/insertarAnalisis.php",new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Toast.makeText(getApplicationContext(),"Usuario registrado correctamente",Toast.LENGTH_SHORT).show();
                cargando.dismissWithAnimation();
                new SweetAlertDialog(AgregarBiometria.this, SweetAlertDialog.SUCCESS_TYPE)
                        .setTitleText("Correcto!")
                        .setContentText("Su análisis clínico fue subido correctamente")
                        .setConfirmText("Ok")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                finish();
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
            protected Map<String, String> getParams() throws AuthFailureError {
                String tipo="biometria";

                Map<String, String> parametros=new HashMap<String, String>();
                parametros.put("idPaciente",usuario.getIdPaciente());
                parametros.put("nombre",etNombreAnalisis.getText().toString());
                parametros.put("eritrocitos",etEritrocitos.getText().toString());
                parametros.put("leucocitos",etLeucocitos.getText().toString());
                parametros.put("plaquetas",etPlaquetas.getText().toString());
                parametros.put("tipo",tipo);
                parametros.put("fecha",fechaNacimiento);
                return parametros;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}