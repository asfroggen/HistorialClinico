package com.example.historialclinico;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
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
import com.example.historialclinico.Registro.RegistroPaciente;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.iid.FirebaseInstanceId;

import androidx.appcompat.app.AlertDialog;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class PacienteActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;

    TextView tvNombrePaciente,tvCorreoPaciente;
    ImageView ivImagenPaciente;
    String correo,password;
    Usuario usuario;

    String token;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paciente);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        usuario =(Usuario)getApplicationContext();
        Intent is = getIntent();
        correo = is.getStringExtra("correoUsuario");
        password=is.getStringExtra("passwordUsuario");
        usuario.setPassword(password);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        //navigationView.setItemIconTintList(null);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_analisis, R.id.nav_medicos,R.id.nav_clinicas,R.id.nav_confi)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        View headerView = navigationView.getHeaderView(0);
        tvNombrePaciente=(TextView)headerView.findViewById(R.id.tvNombrePaciente);
        tvCorreoPaciente=(TextView)headerView.findViewById(R.id.tvCorreoPaciente);
        ivImagenPaciente=(ImageView)headerView.findViewById(R.id.ivImagenPaciente);

        buscarDatos("https://192.168.1.77/login/buscarDatosPaciente.php?correo="+correo+"");
        token= FirebaseInstanceId.getInstance().getToken();
        agregarToken();
    }
    private void agregarToken(){
        StringRequest stringRequest=new StringRequest(Request.Method.POST, "https://192.168.1.77/login/registrarToken.php",new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Toast.makeText(getApplicationContext(),"Usuario registrado correctamente",Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> parametros=new HashMap<String, String>();
                parametros.put("token",token);
                parametros.put("idUsuario",usuario.getIdPaciente());
                parametros.put("tipo","paciente");
                return parametros;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
    private void buscarDatos(String URL){
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject=null;
                String nombreFinal;
                for (int i = 0; i < response.length(); i++) {
                    try {
                        jsonObject = response.getJSONObject(i);
                        usuario.setIdPaciente(jsonObject.getString("idPaciente"));
                        usuario.setNombre(jsonObject.getString("nombre"));
                        usuario.setSegNombre(jsonObject.getString("segNombre"));
                        usuario.setApellidoPat(jsonObject.getString("apellidoPat"));
                        usuario.setApellidoMat(jsonObject.getString("apellidoMat"));
                        usuario.setCorreo(jsonObject.getString("correo"));
                        usuario.setSexo(jsonObject.getString("sexo"));
                        usuario.setImagen(StringToBitMap(jsonObject.optString("imagen")));
                        if (usuario.getImagen()!=null){
                            ivImagenPaciente.setImageBitmap(usuario.getImagen());
                        }else{
                            ivImagenPaciente.setImageResource(R.mipmap.user);
                        }
                        if (usuario.getSexo().equals("Masculino")){
                             nombreFinal="¡Bienvenido de nuevo, "+ usuario.getNombre()+"!";
                        }else{
                             nombreFinal="¡Bienvenida de nuevo, "+ usuario.getNombre()+"!";
                        }
                        tvNombrePaciente.setText(nombreFinal);
                        tvCorreoPaciente.setText(jsonObject.getString("correo"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue=Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(jsonArrayRequest);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.paciente, menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        if (item.getItemId() == R.id.action_cerrar_sesion) {
            new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("¿Salir de Historial Clínico?")
                    .setContentText("Se cerrará su sesión activa")
                    .setConfirmText("Si")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            sDialog.dismissWithAnimation();
                            Intent intent = new Intent(PacienteActivity.this, Login.class);
                            startActivity(intent);
                        }
                    })
                    .setCancelButton("No", new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            sDialog.dismissWithAnimation();
                        }
                    })
                    .show();
        }
        return super.onOptionsItemSelected(item);
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

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
    public Bitmap StringToBitMap(String image){
        try{
            byte [] encodeByte= Base64.decode(image,Base64.DEFAULT);

            InputStream inputStream  = new ByteArrayInputStream(encodeByte);
            Bitmap bitmap  = BitmapFactory.decodeStream(inputStream);
            return bitmap;
        }catch(Exception e){
            e.getMessage();
            return null;
        }
    }
    public String BitMapToString(Bitmap bitmap){
        ByteArrayOutputStream baos=new  ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
        byte [] b=baos.toByteArray();
        String temp=Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }
}