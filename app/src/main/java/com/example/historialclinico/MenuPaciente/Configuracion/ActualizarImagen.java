package com.example.historialclinico.MenuPaciente.Configuracion;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.historialclinico.Login;
import com.example.historialclinico.R;
import com.example.historialclinico.Usuario;
import com.example.historialclinico.PacienteActivity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
import de.hdodenhof.circleimageview.CircleImageView;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class ActualizarImagen extends AppCompatActivity {


    Usuario usuario;
    SweetAlertDialog cargando;
    final int COD_SELECCIONA=10;
    final int COD_FOTO=20;
    int imagenCargada=0;
    String path;

    ImageView ivBack;
    Button bSeleccionarImagen,bActualizarImagen;
    CircleImageView ivImagenConfiguracion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actualizar_imagen);

        usuario =(Usuario)getApplicationContext();

        ivBack=findViewById(R.id.ivBack);
        bSeleccionarImagen=findViewById(R.id.bSeleccionarImagen);
        bActualizarImagen=findViewById(R.id.bActualizarImagen);
        ivImagenConfiguracion=findViewById(R.id.ivImagenConfiguracion);

        if (usuario.getImagen()!=null){
            ivImagenConfiguracion.setImageBitmap(usuario.getImagen());
        }else{
            ivImagenConfiguracion.setImageResource(R.mipmap.user);
        }
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        if (validaPermisos()){
            bSeleccionarImagen.setEnabled(true);
            bActualizarImagen.setEnabled(true);
        }else{
            bSeleccionarImagen.setEnabled(false);
            bActualizarImagen.setEnabled(false);
        }

        bSeleccionarImagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cargarImagen();
                imagenCargada=1;
            }
        });
        bActualizarImagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (imagenCargada==1){
                    cargando = new SweetAlertDialog(ActualizarImagen.this, SweetAlertDialog.PROGRESS_TYPE);
                    cargando.setCancelable(false);
                    cargando.getProgressHelper().setBarColor(Color.parseColor("#FE8629"));
                    cargando.show();
                    actualizarImagen();
                }else{
                    Toast.makeText(getApplicationContext(),"Debe seleccionar una imagen",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void actualizarImagen(){
        StringRequest stringRequest=new StringRequest(Request.Method.POST, "https://192.168.1.77/login/actualizarImagen.php",new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.trim().equalsIgnoreCase("Actualizado")){
                    cargando.dismissWithAnimation();
                    new SweetAlertDialog(ActualizarImagen.this, SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("Correcto!")
                            .setContentText("Imagen de perfil actualizada")
                            .setConfirmText("Ok")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    Intent intent=new Intent(getApplicationContext(), PacienteActivity.class);
                                    intent.putExtra("correoUsuario", usuario.getCorreo());
                                    startActivity(intent);
                                }
                            })
                            .show();
                }else{
                    cargando.dismissWithAnimation();
                    new SweetAlertDialog(ActualizarImagen.this, SweetAlertDialog.ERROR_TYPE)
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
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                cargando.dismissWithAnimation();
                Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_SHORT).show();
            }
        }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                String imagenConvertida=convertirImagenString(usuario.getImagen());
                String tipoUsuario="paciente";
                Map<String, String> parametros=new HashMap<String, String>();
                parametros.put("correo", usuario.getCorreo());
                parametros.put("imagen",imagenConvertida);
                parametros.put("tipoUsuario",tipoUsuario);

                return parametros;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void cargarImagen(){
        final CharSequence[] opciones={"Tomar Foto", "Cargar Imagen", "Cancelar"};
        final AlertDialog.Builder alertOpciones=new AlertDialog.Builder(ActualizarImagen.this);

        alertOpciones.setTitle("Seleccione una Opción");
        alertOpciones.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                if (opciones[i].equals("Tomar Foto")){
                    //Toast.makeText(getApplicationContext(), "Tomar Foto", Toast.LENGTH_SHORT).show();
                    tomarFotografia();
                }else{
                    if (opciones[i].equals("Cargar Imagen")){
                        //Toast.makeText(getContext(), "Cargar Imagen", Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        intent.setType("image/");
                        startActivityForResult(intent.createChooser(intent,"Seleccione la Aplicación"),COD_SELECCIONA);
                    }else {
                        dialog.dismiss();
                    }
                }
            }
        });
        alertOpciones.show();
    }
    private void tomarFotografia() {
        String CARPETA_RAIZ = "misImagenesPrueba/";
        String RUTA_IMAGEN = CARPETA_RAIZ + "misFotos";
        File fileImagen =new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), RUTA_IMAGEN);
        boolean isCreada=fileImagen.exists();
        String nombreImagen = "";

        if (!isCreada){
            isCreada=fileImagen.mkdirs();
        }
        if (isCreada){
            nombreImagen=(System.currentTimeMillis()/1000)+".jpg";
        }

        path=Environment.getExternalStorageDirectory()+File.separator+ RUTA_IMAGEN +File.separator+nombreImagen;
        File imagen = new File(path);

        Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            String authorities=this.getPackageName()+".provider";
            Uri imageUri = FileProvider.getUriForFile(this,authorities,imagen);
            intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
        }else{
            intent.putExtra(MediaStore.EXTRA_OUTPUT,Uri.fromFile(imagen));
        }
        startActivityForResult(intent,COD_FOTO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (resultCode==RESULT_OK){
            switch (requestCode){
                case COD_SELECCIONA:
                    Uri miPath = data.getData();
                    ivImagenConfiguracion.setImageURI(miPath);
                    try {
                        usuario.setImagen(MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), miPath));
                        ivImagenConfiguracion.setImageBitmap(usuario.getImagen());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case COD_FOTO:
                    MediaScannerConnection.scanFile(this, new String[]{path}, null, new MediaScannerConnection.OnScanCompletedListener() {
                        @Override
                        public void onScanCompleted(String s, Uri uri) {
                            Log.i("Ruta de almacenamiento","Path: "+path);
                        }
                    });

                    usuario.setImagen(BitmapFactory.decodeFile(path));
                    //bitmap = BitmapFactory.decodeFile(path);
                    ivImagenConfiguracion.setImageBitmap(usuario.getImagen());

                    break;

            }
            usuario.setImagen(redimensionarImagen(usuario.getImagen(),300,300));
        }
    }

    private String convertirImagenString(Bitmap bitmap){
        ByteArrayOutputStream array=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,array);
        byte[] imagenByte=array.toByteArray();
        String imagenString= Base64.encodeToString(imagenByte,Base64.DEFAULT);

        return imagenString;
    }
    private boolean validaPermisos(){
        if(Build.VERSION.SDK_INT<Build.VERSION_CODES.M){
            return true;
        }
        if ((checkSelfPermission(CAMERA)== PackageManager.PERMISSION_GRANTED)&&(checkSelfPermission(WRITE_EXTERNAL_STORAGE)==PackageManager.PERMISSION_GRANTED)){
            return true;
        }
        if((shouldShowRequestPermissionRationale(CAMERA))||(shouldShowRequestPermissionRationale(WRITE_EXTERNAL_STORAGE))){
            cargarDialogoRecomendacion();
        }else{
            requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE,CAMERA},100);
        }
        return false;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode==100){
            if (grantResults.length==2 && grantResults[0]==PackageManager.PERMISSION_GRANTED && grantResults[1]==PackageManager.PERMISSION_GRANTED){
                bSeleccionarImagen.setEnabled(true);
            }else{
                solicitarPermisosManual();
            }
        }
    }
    private void solicitarPermisosManual() {
        final CharSequence[] opciones={"Si", "No"};
        final AlertDialog.Builder alertOpciones=new AlertDialog.Builder(ActualizarImagen.this);

        alertOpciones.setTitle("¿Desea configurar los permisos de forma manual?");
        alertOpciones.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                if (opciones[i].equals("Si")){
                    //Toast.makeText(getApplicationContext(), "Tomar Foto", Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent ();
                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri=Uri.fromParts("package",getPackageName(),null);
                    intent.setData(uri);
                    startActivity(intent);
                }else{
                    Toast.makeText(getApplicationContext(), "Los permisos no fueron aceptados", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }

            }
        });
        alertOpciones.show();
    }
    private void cargarDialogoRecomendacion() {
        AlertDialog.Builder dialogo=new AlertDialog.Builder(ActualizarImagen.this);
        dialogo.setTitle("Permisos Desactivados");
        dialogo.setMessage("Debe aceptar los permisos para el correcto funcionamiento de la aplicación");
        dialogo.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE,CAMERA},100);
            }
        });
        dialogo.show();
    }
    private Bitmap redimensionarImagen(Bitmap bitmap, float anchoNuevo, float altoNuevo) {

        int ancho=bitmap.getWidth();
        int alto=bitmap.getHeight();

        if(ancho>anchoNuevo || alto>altoNuevo){
            float escalaAncho=anchoNuevo/ancho;
            float escalaAlto= altoNuevo/alto;

            Matrix matrix=new Matrix();
            matrix.postScale(escalaAncho,escalaAlto);

            return Bitmap.createBitmap(bitmap,0,0,ancho,alto,matrix,false);

        }else{
            return bitmap;
        }
    }
    @Override //====To avoid memory peaks====
    protected void onDestroy() {
        super.onDestroy();
    }
}