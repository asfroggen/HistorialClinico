package com.example.historialclinico.MenuPaciente.Analisis.Agregar;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.net.ParseException;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer;

import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class AgregarOrinaFoto extends AppCompatActivity {

    String fechaNacimiento,fechaValidar;
    SweetAlertDialog cargando;
    Usuario usuario;
    Integer anoActual,anoNac;
    Bitmap imageBitmap;

    static final int REQUEST_IMAGE_CAPTURE = 1;

    ImageView ivFoto, ivBack;
    EditText etNombreAnalisis,etDiaAnalisis,etMesAnalisis,etAnoAnalisis,etAlbumina,etAcido,etCreatinina;
    Button bTomarFoto, bGuardarAnalisis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_orina_foto);

        usuario =(Usuario)getApplicationContext();

        bTomarFoto=findViewById(R.id.bTomarFoto);
        bGuardarAnalisis=findViewById(R.id.bGuardarAnalisis);

        ivFoto=findViewById(R.id.ivFoto);
        ivBack=findViewById(R.id.ivBack);

        etNombreAnalisis=findViewById(R.id.etNombreAnalisis);
        etDiaAnalisis=findViewById(R.id.etDiaAnalisis);
        etMesAnalisis=findViewById(R.id.etMesAnalisis);
        etAnoAnalisis=findViewById(R.id.etAnoAnalisis);

        etAlbumina=findViewById(R.id.etAlbumina);
        etAcido=findViewById(R.id.etAcido);
        etCreatinina=findViewById(R.id.etCreatinina);

        comprobarPermisos();

        bTomarFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dispatchTakePictureIntent();
            }
        });

        bGuardarAnalisis.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder= new AlertDialog.Builder(AgregarOrinaFoto.this);
                builder.setTitle("¿Listo?");
                builder.setMessage("¿Estás seguro que los datos son correctos?")
                        .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                cargando = new SweetAlertDialog(AgregarOrinaFoto.this, SweetAlertDialog.PROGRESS_TYPE);
                                cargando.setCancelable(false);
                                cargando.getProgressHelper().setBarColor(Color.parseColor("#FE8629"));
                                cargando.show();
                                if(validarDatos().equals("OK")){
                                    if (validarFecha()){
                                        if(validarFechaAnos().equals("OK")){
                                            guardarAnalisis();
                                        }
                                    }else{
                                        Toast.makeText(getApplicationContext(),"Debe ingresar una fecha valida",Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        }).setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).show();
            }
        });

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
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
        if(etAlbumina.getText().toString().isEmpty()){
            etAlbumina.setError("Campo necesario");
            validacion="Error";
        }
        if(etAcido.getText().toString().isEmpty()){
            etAcido.setError("Campo necesario");
            validacion="Error";
        }
        if(etCreatinina.getText().toString().isEmpty()){
            etCreatinina.setError("Campo necesario");
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
    private void guardarAnalisis(){
        StringRequest stringRequest=new StringRequest(Request.Method.POST, "https://192.168.1.77/login/insertarAnalisis.php",new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Toast.makeText(getApplicationContext(),"Usuario registrado correctamente",Toast.LENGTH_SHORT).show();
                cargando.dismissWithAnimation();
                new SweetAlertDialog(AgregarOrinaFoto.this, SweetAlertDialog.SUCCESS_TYPE)
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
                String tipo="orina";

                Map<String, String> parametros=new HashMap<String, String>();
                parametros.put("idPaciente",usuario.getIdPaciente());
                parametros.put("nombre",etNombreAnalisis.getText().toString());
                parametros.put("acido",etAcido.getText().toString());
                parametros.put("creatinina",etCreatinina.getText().toString());
                parametros.put("albumina",etAlbumina.getText().toString());
                parametros.put("tipo",tipo);
                parametros.put("fecha",fechaNacimiento);
                return parametros;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void comprobarPermisos(){
        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.CAMERA}, 101);
        }
    }
    private void detectarTextoDeImagen(){
        FirebaseVisionImage firebaseVisionImage= FirebaseVisionImage.fromBitmap(imageBitmap);
        FirebaseVision firebaseVision= FirebaseVision.getInstance();
        FirebaseVisionTextRecognizer firebaseVisionTextRecognizer=firebaseVision.getOnDeviceTextRecognizer();
        Task<FirebaseVisionText> task=firebaseVisionTextRecognizer.processImage(firebaseVisionImage);
        task.addOnSuccessListener(new OnSuccessListener<FirebaseVisionText>() {
            @Override
            public void onSuccess(FirebaseVisionText firebaseVisionText) {

                String s="addfadsfasdfafdadfafasdafd Creatinina: 0.0000 adadad Albumina: 0.0000 adadad Acido: 0.0000";
                if (firebaseVisionText.getText().length()<20){
                    Toast.makeText(getApplicationContext(),"No se encontró texto válido",Toast.LENGTH_SHORT).show();
                    String leucocitos="Creatinina:";
                    String eritrocitos="Albumina:";
                    String plaquetas="Acido:";

                    int indexLeucocitos=s.indexOf(leucocitos);
                    int indexEritrocitos=s.indexOf(eritrocitos);
                    int indexPlaquetas=s.indexOf(plaquetas);

                    etCreatinina.setText(s.substring(indexLeucocitos+11, indexLeucocitos+17).trim());
                    etAlbumina.setText(s.substring(indexEritrocitos+9, indexEritrocitos+15).trim());
                    etAcido.setText(s.substring(indexPlaquetas+6, indexPlaquetas+12).trim());
                    cargando.dismissWithAnimation();
                }else{
                    s=firebaseVisionText.getText();
                    String leucocitos="Creatinina:";
                    String eritrocitos="Albumina:";
                    String plaquetas="Acido:";

                    int indexLeucocitos=s.indexOf(leucocitos);
                    int indexEritrocitos=s.indexOf(eritrocitos);
                    int indexPlaquetas=s.indexOf(plaquetas);

                    etCreatinina.setText(s.substring(indexLeucocitos+11, indexLeucocitos+17).trim());
                    etAlbumina.setText(s.substring(indexEritrocitos+9, indexEritrocitos+15).trim());
                    etAcido.setText(s.substring(indexPlaquetas+21, indexPlaquetas+27).trim());
                    cargando.dismissWithAnimation();
                }

            }
        });
        task.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), e.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            cargando = new SweetAlertDialog(AgregarOrinaFoto.this, SweetAlertDialog.PROGRESS_TYPE);
            cargando.setCancelable(false);
            cargando.getProgressHelper().setBarColor(Color.parseColor("#FE8629"));
            cargando.show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            imageBitmap = (Bitmap) extras.get("data");
            ivFoto.setImageBitmap(imageBitmap);
            detectarTextoDeImagen();
        }

    }

}