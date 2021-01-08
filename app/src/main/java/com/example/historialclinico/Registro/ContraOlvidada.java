package com.example.historialclinico.Registro;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.icu.text.SimpleDateFormat;
import android.net.ParseException;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
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
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.historialclinico.Login;
import com.example.historialclinico.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ContraOlvidada extends AppCompatActivity {

    ImageView ivBack;
    TextInputEditText etCorreo;
    TextInputLayout ilCorreo;
    EditText etDiaNac,etMesNac,etAnoNac;
    Button bRecuperar;
    TextView tvFecha;

    String fechaNacimiento;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contra_olvidada);

        ivBack=findViewById(R.id.ivBack);
        ilCorreo=findViewById(R.id.ilCorreo);
        etCorreo=findViewById(R.id.etCorreo);
        etDiaNac=findViewById(R.id.etDiaNac);
        etMesNac=findViewById(R.id.etMesNac);
        etAnoNac=findViewById(R.id.etAnoNac);
        bRecuperar=findViewById(R.id.bRecuperar);
        tvFecha=findViewById(R.id.tvFecha);

        Intent is = getIntent();
        String tipoUsuario = is.getStringExtra("tipoUsuario");
        assert tipoUsuario != null;

        if (tipoUsuario.equals("paciente")){
            @SuppressLint("UseCompatLoadingForDrawables")
            Drawable email_icon = getResources().getDrawable(R.drawable.custom_email_icon_paciente);

            ivBack.setImageResource(R.mipmap.back_icon_orange);
            etCorreo.setCompoundDrawablesRelativeWithIntrinsicBounds(email_icon,null,null,null);
            ilCorreo.setHintTextColor(ColorStateList.valueOf(getResources().getColor(R.color.colorOrangeFuerte)));
            tvFecha.setTextColor(getResources().getColor(R.color.colorOrangeFuerte));
            bRecuperar.setBackgroundResource(R.drawable.button_rounded_orange);
        }else if(tipoUsuario.equals("medico")){
            @SuppressLint("UseCompatLoadingForDrawables")
            Drawable email_icon = getResources().getDrawable(R.drawable.custom_email_icon_medico);

            ivBack.setImageResource(R.mipmap.back_icon_cyan);
            etCorreo.setCompoundDrawablesRelativeWithIntrinsicBounds(email_icon,null,null,null);
            ilCorreo.setHintTextColor(ColorStateList.valueOf(getResources().getColor(R.color.colorCyanFuerte)));
            tvFecha.setTextColor(getResources().getColor(R.color.colorCyanFuerte));
            bRecuperar.setBackgroundResource(R.drawable.button_rounded_cyan);
        }
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        bRecuperar.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                fechaNacimiento=etAnoNac.getText().toString()+"-"+etMesNac.getText().toString()+"-"+etDiaNac.getText().toString();
                if (validarDatos().equals("OK")){
                    if(validarFecha()) {
                        buscarPassword(etCorreo.getText().toString(), fechaNacimiento, tipoUsuario);
                    }else{
                        Toast.makeText(getApplicationContext(),"Credenciales incorrectas", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    private boolean validarFecha(){
        try {
            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyy/MM/dd");
            formatoFecha.setLenient(false);
            formatoFecha.parse(fechaNacimiento);
        } catch (ParseException | java.text.ParseException e) {
            return false;
        }
        return true;
    }
    private String validarDatos(){
        String validacion="OK";
        if(etCorreo.getText().toString().isEmpty()){
            etCorreo.setError("Ingrese un correo electronico");
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
        }
        return validacion;
    }
    private void buscarPassword(String correo, String fecha, String usuario){
        StringRequest stringRequest=new StringRequest(Request.Method.POST, "https://192.168.1.77/login/buscarPassword.php",new Response.Listener<String>(){
            @Override
            public void onResponse(String response) {
                if(!response.isEmpty()){
                    if(usuario.equals("paciente")){
                        Intent intent=new Intent(getApplicationContext(), ReiniciarContra.class);
                        intent.putExtra("correo",correo);
                        startActivity(intent);
                    }else {
                        Intent intent=new Intent(getApplicationContext(), ReiniciarContraMedico.class);
                        intent.putExtra("correo",correo);
                        startActivity(intent);
                    }
                }else{
                    Toast.makeText(ContraOlvidada.this,"Credenciales incorrectas",Toast.LENGTH_SHORT).show();
                }
            }
        }, error -> Toast.makeText(ContraOlvidada.this, error.toString(),Toast.LENGTH_SHORT).show()){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parametros=new HashMap<String, String>();
                parametros.put("usuario",correo);
                parametros.put("fechaNacimiento",fecha);
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