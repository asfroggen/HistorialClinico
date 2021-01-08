package com.example.historialclinico.MenuMedico.Pacientes;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.historialclinico.MenuMedico.Pacientes.Analisis.BiometriaMedico;
import com.example.historialclinico.MenuMedico.Pacientes.Analisis.OrinaMedico;
import com.example.historialclinico.MenuMedico.Pacientes.Analisis.QuimicaMedico;
import com.example.historialclinico.MenuPaciente.Analisis.Biometria;
import com.example.historialclinico.MenuPaciente.Analisis.Orina;
import com.example.historialclinico.MenuPaciente.Analisis.Quimica;
import com.example.historialclinico.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;

public class PacienteDetalle extends AppCompatActivity {

    String idPaciente,nombre,sexo,fechaNacimiento,correo,imagen;
    Calendar fecha;
    int anoActual;
    long edad;
    String edadTexto,sexoTexto;

    ImageView ivBack,ivPerfilPaciente;
    TextView tvNombre,tvEdad,tvSexo;
    FrameLayout fragment_container;
    BottomNavigationView bottom_nav;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paciente_detalle);

        ivBack=findViewById(R.id.ivBack);
        ivPerfilPaciente=findViewById(R.id.ivPerfilPaciente);
        tvNombre=findViewById(R.id.tvNombre);
        tvEdad=findViewById(R.id.tvEdad);
        tvSexo=findViewById(R.id.tvSexo);
        fragment_container=findViewById(R.id.fragment_container);
        bottom_nav=findViewById(R.id.bottom_nav);

        Bundle bundle = this.getIntent().getExtras();
        assert bundle != null;

        idPaciente = bundle.getString("id");
        nombre = bundle.getString("nombre");
        fechaNacimiento = bundle.getString("fechaNacimiento");
        correo = bundle.getString("correo");
        sexo = bundle.getString("sexo");
        imagen = bundle.getString("imagen");

        edadTexto="Edad: "+obtenerEdad().getYears()+" años y "+obtenerEdad().getMonths()+" meses";
        sexoTexto="Sexo: "+sexo;

        tvNombre.setText(nombre);
        tvEdad.setText(edadTexto);
        tvSexo.setText(sexoTexto);

        bottom_nav.setOnNavigationItemSelectedListener(navListener);

        if (!imagen.equals("No hay")){
            ivPerfilPaciente.setImageBitmap(StringToBitMap(imagen));
        }else{
            ivPerfilPaciente.setImageResource(R.mipmap.user_icon_dark);
        }

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        QuimicaMedico quimica =new QuimicaMedico();

        Bundle bundle1=new Bundle();
        bundle1.putString("idPaciente",idPaciente);
        quimica.setArguments(bundle1);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,quimica).commit();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener=new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment=null;

            switch (item.getItemId()){
                case R.id.nav_quimica:
                    selectedFragment= new QuimicaMedico();
                    break;
                case R.id.nav_biometria:
                    selectedFragment= new BiometriaMedico();
                    break;
                case R.id.nav_orinas:
                    selectedFragment= new OrinaMedico();
                    break;
            }

            Bundle bundle1=new Bundle();
            bundle1.putString("idPaciente",idPaciente);
            assert selectedFragment != null;
            selectedFragment.setArguments(bundle1);

            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,selectedFragment).commit();
            return true;
        }
    };


    @RequiresApi(api = Build.VERSION_CODES.O)
    private Period obtenerEdad(){
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate fechaNac = LocalDate.parse(fechaNacimiento, fmt);
        LocalDate ahora = LocalDate.now();

        Period periodo = Period.between(fechaNac, ahora);
        return periodo;
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
}