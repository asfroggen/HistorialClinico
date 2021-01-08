package com.example.historialclinico;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.historialclinico.MenuPaciente.Configuracion.ActualizarCredenciales;
import com.example.historialclinico.Registro.ContraOlvidada;
import com.example.historialclinico.Registro.RegistroMedico;
import com.example.historialclinico.Registro.RegistroPaciente;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.iid.FirebaseInstanceId;

import org.w3c.dom.Text;

import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class Login extends AppCompatActivity {

    SweetAlertDialog cargando;

    RadioButton rbPaciente,rbMedico;
    Button bIniciar, bOlvidarContra, bRegistrarse;
    TextInputEditText etCorreoLogin,etPasswordLogin;
    ConstraintLayout principal;
    String token= FirebaseInstanceId.getInstance().getToken();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        rbPaciente=findViewById(R.id.rbPaciente);
        rbMedico=findViewById(R.id.rbMedico);

        bIniciar=findViewById(R.id.bIniciar);
        bOlvidarContra=findViewById(R.id.bOlvidarContra);
        bRegistrarse=findViewById(R.id.bRegistrarse);

        etCorreoLogin=findViewById(R.id.etCorreoLogin);
        etPasswordLogin=findViewById(R.id.etPasswordLogin);

        principal=findViewById(R.id.principal);
        System.out.println("token; "+token);

        handleSSLHandshake();

        bIniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (rbPaciente.isChecked()){
                    cargando = new SweetAlertDialog(Login.this, SweetAlertDialog.PROGRESS_TYPE);
                    cargando.setCancelable(false);
                    cargando.getProgressHelper().setBarColor(Color.parseColor("#FE8629"));
                    cargando.show();
                    validarUsuario("https://192.168.1.77/login/validarCredenciales.php","paciente");
                }
                if (rbMedico.isChecked()){
                    cargando = new SweetAlertDialog(Login.this, SweetAlertDialog.PROGRESS_TYPE);
                    cargando.setCancelable(false);
                    cargando.getProgressHelper().setBarColor(Color.parseColor("#47BDB5"));
                    cargando.show();
                    validarUsuario("https://192.168.1.77/login/validarCredenciales.php","medico");
                }
            }
        });
        bRegistrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (rbPaciente.isChecked()){
                    Intent is=new Intent(Login.this, RegistroPaciente.class);
                    startActivity(is);
                }
                if (rbMedico.isChecked()){
                    Intent is=new Intent(Login.this, RegistroMedico.class);
                    startActivity(is);
                }
            }
        });
        bOlvidarContra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String paciente="paciente",medico="medico";
                if (rbPaciente.isChecked()){
                    Intent is=new Intent(Login.this, ContraOlvidada.class);
                    is.putExtra("tipoUsuario", paciente);
                    startActivity(is);
                }
                if (rbMedico.isChecked()){
                    Intent is=new Intent(Login.this,ContraOlvidada.class);
                    is.putExtra("tipoUsuario", medico);
                    startActivity(is);
                }
            }
        });

    }
    private void validarUsuario(String URL, String tipoUsuario){
        StringRequest stringRequest=new StringRequest(Request.Method.POST, URL,new Response.Listener<String>(){
            @Override
            public void onResponse(String response) {
                if(!response.isEmpty()){
                    if(tipoUsuario.equals("paciente")){
                        Intent intent=new Intent(getApplicationContext(), PacienteActivity.class);
                        intent.putExtra("correoUsuario",etCorreoLogin.getText().toString());
                        intent.putExtra("passwordUsuario",etPasswordLogin.getText().toString());
                        cargando.dismissWithAnimation();
                        startActivity(intent);
                    }else{
                        Intent intent=new Intent(getApplicationContext(), MedicoActivity.class);
                        intent.putExtra("correoUsuario",etCorreoLogin.getText().toString());
                        intent.putExtra("passwordUsuario",etPasswordLogin.getText().toString());
                        cargando.dismissWithAnimation();
                        startActivity(intent);
                    }
                }else{
                    cargando.dismissWithAnimation();
                    new SweetAlertDialog(Login.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Ups!")
                            .setContentText("Usuario o contraseña incorrectos")
                            .setConfirmText("Ok")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.dismiss();
                                }
                            }).show();
                }
            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                cargando.dismissWithAnimation();
                Toast.makeText(Login.this, error.toString(),Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parametros=new HashMap<String, String>();
                parametros.put("usuario",etCorreoLogin.getText().toString());
                parametros.put("password",etPasswordLogin.getText().toString());
                parametros.put("tipoUsuario",tipoUsuario);

                return parametros;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    @SuppressLint({"ResourceAsColor", "UseCompatLoadingForDrawables"})
    public void onRadioButtonClicked(View view){
        boolean isSelected=((RadioButton)view).isChecked();
        switch (view.getId()){
            case R.id.rbPaciente:
                if(isSelected){
                    Drawable email_icon = getResources().getDrawable(R.drawable.custom_email_icon_paciente);
                    Drawable lock_icon = getResources().getDrawable(R.drawable.custom_lock_icon_paciente);
                    rbPaciente.setTextColor(getResources().getColor(R.color.colorFondoClaro));
                    rbMedico.setTextColor(getResources().getColor(R.color.colorGrayFuerte));
                    bIniciar.setBackgroundResource(R.drawable.button_rounded_orange);
                    bRegistrarse.setTextColor(getResources().getColor(R.color.colorOrangeFuerte));
                    principal.setBackgroundResource(R.mipmap.fondo_alto_naranja);
                    etCorreoLogin.setBackgroundResource(R.drawable.custom_edit_text);
                    etPasswordLogin.setBackgroundResource(R.drawable.custom_edit_text_medico);
                    etCorreoLogin.setCompoundDrawablesRelativeWithIntrinsicBounds(email_icon,null,null,null);
                    etPasswordLogin.setCompoundDrawablesRelativeWithIntrinsicBounds(lock_icon,null,null,null);
                }
                break;
            case R.id.rbMedico:
                if(isSelected){
                    Drawable email_icon = getResources().getDrawable(R.drawable.custom_email_icon_medico);
                    //email_icon.setBounds( 0, 0, 60, 60 );
                    Drawable lock_icon = getResources().getDrawable(R.drawable.custom_lock_icon_medico);
                    //lock_icon.setBounds( 0, 0, 60, 60 );
                    rbPaciente.setTextColor(getResources().getColor(R.color.colorGrayFuerte));
                    rbMedico.setTextColor(getResources().getColor(R.color.colorFondoClaro));
                    bIniciar.setBackgroundResource(R.drawable.button_rounded_cyan);
                    bRegistrarse.setTextColor(getResources().getColor(R.color.colorCyanFuerte));
                    principal.setBackgroundResource(R.mipmap.fondo_alto_cyan);
                    etCorreoLogin.setBackgroundResource(R.drawable.custom_edit_text);
                    etPasswordLogin.setBackgroundResource(R.drawable.custom_edit_text_medico);
                    etCorreoLogin.setCompoundDrawablesRelativeWithIntrinsicBounds(email_icon,null,null,null);
                    etPasswordLogin.setCompoundDrawablesRelativeWithIntrinsicBounds(lock_icon,null,null,null);
                }
                break;
        }
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode==event.KEYCODE_BACK){
            AlertDialog.Builder builder= new AlertDialog.Builder(this);
            builder.setMessage("¿Salir de Historial Clínico?")
                    .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent=new Intent(Intent.ACTION_MAIN);
                            intent.addCategory(Intent.CATEGORY_HOME);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                    })
                    .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            builder.show();
        }
        return super.onKeyDown(keyCode, event);
    }
    @SuppressLint("TrulyRandom")
    public static void handleSSLHandshake() {
        try {
            TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }

                @Override
                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                }

                @Override
                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                }
            }};

            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String arg0, SSLSession arg1) {
                    return true;
                }
            });
        } catch (Exception ignored) {
        }
    }

    @Override //====To avoid memory peaks====
    protected void onDestroy() {
        super.onDestroy();
    }

}