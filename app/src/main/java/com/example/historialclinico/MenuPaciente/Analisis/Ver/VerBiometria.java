package com.example.historialclinico.MenuPaciente.Analisis.Ver;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.historialclinico.R;
import com.example.historialclinico.Usuario;

import java.util.Arrays;
import java.util.Collections;
import java.util.stream.IntStream;

public class VerBiometria extends AppCompatActivity {

    double LeucocitosVD,EritrocitosVD,PlaquetasVD;

    String nombre, fecha, leucocitos, eritrocitos, plaquetas, idAnalisis;
    String LeucocitosDiag,EritrocitosDiag,PlaquetasDiag;
    String Diagnostico;
    int[] x;

    Usuario usuario;

    ImageView ivBack;
    TextView tvNombreAnalisis,tvLeucocitos,tvEritrocitos,tvPlaquetas,tvInterpretacionDesc, tvLeucocitosValor,tvEritrocitosValor,tvPlaquetasValor;
    ProgressBar pbLeucocitos,pbEritrocitos,pbPlaquetas;
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_biometria);
        usuario =(Usuario)getApplicationContext();

        x=new int[19];
        x= IntStream.rangeClosed(0, 18).toArray();

        Intent is = getIntent();

        idAnalisis = is.getStringExtra("idAnalisis");
        nombre = is.getStringExtra("nombre");
        fecha = is.getStringExtra("fecha");
        leucocitos = is.getStringExtra("leucocitos");
        eritrocitos = is.getStringExtra("eritrocitos");
        plaquetas = is.getStringExtra("plaquetas");

        tvNombreAnalisis=findViewById(R.id.tvNombre);
        tvLeucocitos=findViewById(R.id.tvLeucocitos);
        tvEritrocitos=findViewById(R.id.tvEritrocitos);
        tvPlaquetas=findViewById(R.id.tvPlaquetas);
        tvInterpretacionDesc=findViewById(R.id.tvInterpretacionDesc);

        tvLeucocitosValor=findViewById(R.id.tvLeucocitosValor);
        tvEritrocitosValor=findViewById(R.id.tvEritrocitosValor);
        tvPlaquetasValor=findViewById(R.id.tvPlaquetasValor);

        pbEritrocitos=findViewById(R.id.pbEritrocitos);
        pbLeucocitos=findViewById(R.id.pbLeucocitos);
        pbPlaquetas=findViewById(R.id.pbPlaquetas);

        ivBack=findViewById(R.id.ivBack);
        tvNombreAnalisis.setText(nombre);

        tvLeucocitosValor.setText(leucocitos);
        tvEritrocitosValor.setText(eritrocitos);
        tvPlaquetasValor.setText(plaquetas);

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        validarDatos(Integer.parseInt(leucocitos),Integer.parseInt(eritrocitos),Integer.parseInt(plaquetas));
        sistemaBiometria(Integer.parseInt(leucocitos),Integer.parseInt(eritrocitos),Integer.parseInt(plaquetas));
        mostrarResultados();
        mostrarDiagnostico();

        Log.d("TAG", String.valueOf(LeucocitosVD));
        Log.d("TAG2", String.valueOf(EritrocitosVD));
        Log.d("TAG3", String.valueOf(PlaquetasVD));

    }

    void mostrarDiagnostico(){
        if(0<=LeucocitosVD && LeucocitosVD<=12){
            Diagnostico="Los niveles de leucocitos indican una posible respuesta inmune baja, ";
        }else if (12<LeucocitosVD && LeucocitosVD<=26){
            Diagnostico="Los niveles de leucocitos son normales, ";
        }else{
            Diagnostico="Los niveles de leucocitos indican un posible proceso infeccioso, ";
        }
        //Eritrocitos
        if(0<=EritrocitosVD && EritrocitosVD<=12){
            Diagnostico=Diagnostico+"los niveles bajos de eritrocitos pueden ser indicios de anemia y ";
        }else if (12<EritrocitosVD && EritrocitosVD<=26){
            Diagnostico=Diagnostico+"los niveles de eritrocitos son normales y ";
        }else{
            Diagnostico=Diagnostico+"un nivel alto de eritrocitos puede indicar falta de oxigenación en la sangre y ";
        }
        //Plaquetas
        if(0<=PlaquetasVD && PlaquetasVD<=12){
            Diagnostico=Diagnostico+"finalmente, la falta de plaquetas en la sangre puede indicar una trombopenia.";
        }else if (12<PlaquetasVD && PlaquetasVD<=26){
            Diagnostico=Diagnostico+"finalmente, los niveles de plaquetas son normales.";
        }else{
            Diagnostico=Diagnostico+"finalmente, un nivel alto de plaquetas en la sangre puede indicar una trombocitosis.";
        }
        tvInterpretacionDesc.setText(Diagnostico);
    }

    void mostrarResultados(){
        //Leucocitos
        if(0<=LeucocitosVD && LeucocitosVD<=12){
            LeucocitosDiag="Bajos";
            tvLeucocitos.setText(LeucocitosDiag);
            pbLeucocitos.setProgress((int) ((LeucocitosVD/40)*100));
        }else if (12<LeucocitosVD && LeucocitosVD<=26){
            LeucocitosDiag="Normales";
            tvLeucocitos.setText(LeucocitosDiag);
            pbLeucocitos.setProgress((int) ((LeucocitosVD/40)*100));
        }else{
            LeucocitosDiag="Altos";
            tvLeucocitos.setText(LeucocitosDiag);
            pbLeucocitos.setProgress((int) ((LeucocitosVD/40)*100));
        }
        //Eritrocitos
        if(0<=EritrocitosVD && EritrocitosVD<=12){
            EritrocitosDiag="Bajos";
            tvEritrocitos.setText(EritrocitosDiag);
            pbEritrocitos.setProgress((int) ((EritrocitosVD/40)*100));
        }else if (12<EritrocitosVD && EritrocitosVD<=26){
            EritrocitosDiag="Normales";
            tvEritrocitos.setText(EritrocitosDiag);
            pbEritrocitos.setProgress((int) ((EritrocitosVD/40)*100));
        }else{
            EritrocitosDiag="Altos";
            tvEritrocitos.setText(EritrocitosDiag);
            pbEritrocitos.setProgress((int) ((EritrocitosVD/40)*100));
        }
        //Plaquetas
        if(0<=PlaquetasVD && PlaquetasVD<=12){
            PlaquetasDiag="Bajos";
            tvPlaquetas.setText(PlaquetasDiag);
            pbPlaquetas.setProgress((int) ((PlaquetasVD/40)*100));
        }else if (12<PlaquetasVD && PlaquetasVD<=26){
            PlaquetasDiag="Normales";
            tvPlaquetas.setText(PlaquetasDiag);
            pbPlaquetas.setProgress((int) ((PlaquetasVD/40)*100));
        }else{
            PlaquetasDiag="Altos";
            tvPlaquetas.setText(PlaquetasDiag);
            pbPlaquetas.setProgress((int) ((PlaquetasVD/40)*100));
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    void sistemaBiometria(int leucocitos, int eritrocitos, int plaquetas){
        double a;
        double b;
        double c;

        double aux;

        double[] entrada_leucocitos_bajo;
        double[] entrada_leucocitos_normal;
        double[] entrada_leucocitos_alto;
        double[] entrada_eritrocitos_bajo;
        double[] entrada_eritrocitos_normal;
        double[] entrada_eritrocitos_alto;
        double[] entrada_plaqueta_bajo;
        double[] entrada_plaqueta_normal;
        double[] entrada_plaqueta_alto;

        double[] salida_leucocitos_RIB;
        double[] salida_leucocitos_normal;
        double[] salida_leucocitos_infeccion;
        double[] salida_eritrocitos_anemia;
        double[] salida_eritrocitos_normal;
        double[] salida_eritrocitos_FO;
        double[] salida_plaquetas_trombopenia;
        double[] salida_plaquetas_normal;
        double[] salida_plaquetas_trombocitosis;

        double[] FISsalida_leucocitos_RIB;
        double[] FISsalida_leucocitos_normal;
        double[] FISsalida_leucocitos_infeccion;
        double[] FISsalida_eritrocitos_anemia;
        double[] FISsalida_eritrocitos_normal;
        double[] FISsalida_eritrocitos_FO;
        double[] FISsalida_plaquetas_trombopenia;
        double[] FISsalida_plaquetas_normal;
        double[] FISsalida_plaquetas_trombocitosis;

        entrada_leucocitos_bajo=new double[19];
        entrada_leucocitos_normal=new double[19];
        entrada_leucocitos_alto=new double[19];
        entrada_eritrocitos_bajo=new double[19];
        entrada_eritrocitos_normal=new double[19];
        entrada_eritrocitos_alto=new double[19];
        entrada_plaqueta_bajo=new double[801];
        entrada_plaqueta_normal=new double[801];
        entrada_plaqueta_alto=new double[801];

        salida_leucocitos_RIB=new double[41];
        salida_leucocitos_normal=new double[41];
        salida_leucocitos_infeccion=new double[41];
        salida_eritrocitos_anemia=new double[41];
        salida_eritrocitos_normal=new double[41];
        salida_eritrocitos_FO=new double[41];
        salida_plaquetas_trombopenia=new double[41];
        salida_plaquetas_normal=new double[41];
        salida_plaquetas_trombocitosis=new double[41];

        FISsalida_leucocitos_RIB=new double[9];
        FISsalida_leucocitos_normal=new double[9];
        FISsalida_leucocitos_infeccion=new double[9];
        FISsalida_eritrocitos_anemia=new double[9];
        FISsalida_eritrocitos_normal=new double[9];
        FISsalida_eritrocitos_FO=new double[9];
        FISsalida_plaquetas_trombopenia=new double[9];
        FISsalida_plaquetas_normal=new double[9];
        FISsalida_plaquetas_trombocitosis=new double[9];
        a=-1;
        c=5;
        entrada_leucocitos_bajo=sigmoide(a,c,x.length);
        a=4;
        c=9;
        entrada_leucocitos_normal=gausiana(a,c,x.length);
        a=1;
        c=14;
        entrada_leucocitos_alto=sigmoide(a,c,x.length);

        a=-1;
        c=4;
        entrada_eritrocitos_bajo=sigmoide(a,c,x.length);
        a=2;
        c=5.5;
        entrada_eritrocitos_normal=gausiana(a,c,x.length);
        a=1;
        c=8;
        entrada_eritrocitos_alto=sigmoide(a,c,x.length);

        x= new int[801];
        x=IntStream.rangeClosed(0, 800).toArray();
        a=-.1;
        c=150;
        entrada_plaqueta_bajo=sigmoide(a,c,x.length);
        a=100;
        c=300;
        entrada_plaqueta_normal=gausiana(a,c,x.length);
        a=.1;
        c=450;
        entrada_plaqueta_alto=sigmoide(a,c,x.length);

        x= new int[41];
        x=IntStream.rangeClosed(0, 40).toArray();

        a=-1;
        c=10;
        salida_leucocitos_RIB=sigmoide(a,c,x.length);
        a=6;
        b=6;
        c=20;
        salida_leucocitos_normal=campana(a,b,c,x.length);
        a=1;
        c=30;
        salida_leucocitos_infeccion=sigmoide(a,c,x.length);

        a=-1;
        c=10;
        salida_eritrocitos_anemia=sigmoide(a,c,x.length);
        a=6;
        b=6;
        c=20;
        salida_eritrocitos_normal=campana(a,b,c,x.length);
        a=1;
        c=30;
        salida_eritrocitos_FO=sigmoide(a,c,x.length);

        a=-1;
        c=10;
        salida_plaquetas_trombopenia=sigmoide(a,c,x.length);
        a=6;
        b=6;
        c=20;
        salida_plaquetas_normal=campana(a,b,c,x.length);
        a=1;
        c=30;
        salida_plaquetas_trombocitosis=sigmoide(a,c,x.length);

        int A=leucocitos-1;
        int B=eritrocitos-1;
        int C=plaquetas-1;

        //1
        aux=min(entrada_leucocitos_normal[A],entrada_eritrocitos_normal[B]);
        FISsalida_leucocitos_normal[0]=min(aux,entrada_plaqueta_normal[C]);
        aux=min(entrada_leucocitos_normal[A],entrada_eritrocitos_normal[B]);
        FISsalida_eritrocitos_normal[0]=min(aux,entrada_plaqueta_normal[C]);
        aux=min(entrada_leucocitos_normal[A],entrada_eritrocitos_normal[B]);
        FISsalida_plaquetas_normal[0]=min(aux,entrada_plaqueta_normal[C]);
        //2
        aux=min(entrada_leucocitos_normal[A],entrada_eritrocitos_normal[B]);
        FISsalida_leucocitos_normal[1]=min(aux,entrada_plaqueta_bajo[C]);
        aux=min(entrada_leucocitos_normal[A],entrada_eritrocitos_normal[B]);
        FISsalida_eritrocitos_normal[1]=min(aux,entrada_plaqueta_bajo[C]);
        aux=min(entrada_leucocitos_normal[A],entrada_eritrocitos_normal[B]);
        FISsalida_plaquetas_trombopenia[0]=min(aux,entrada_plaqueta_bajo[C]);
        //3
        aux=min(entrada_leucocitos_normal[A],entrada_eritrocitos_normal[B]);
        FISsalida_leucocitos_normal[2]=min(aux,entrada_plaqueta_alto[C]);
        aux=min(entrada_leucocitos_normal[A],entrada_eritrocitos_normal[B]);
        FISsalida_eritrocitos_normal[2]=min(aux,entrada_plaqueta_alto[C]);
        aux=min(entrada_leucocitos_normal[A],entrada_eritrocitos_normal[B]);
        FISsalida_plaquetas_trombocitosis[0]=min(aux,entrada_plaqueta_alto[C]);
        //4
        aux=min(entrada_leucocitos_normal[A],entrada_eritrocitos_bajo[B]);
        FISsalida_leucocitos_normal[3]=min(aux,entrada_plaqueta_normal[C]);
        aux=min(entrada_leucocitos_normal[A],entrada_eritrocitos_bajo[B]);
        FISsalida_eritrocitos_anemia[0]=min(aux,entrada_plaqueta_normal[C]);
        aux=min(entrada_leucocitos_normal[A],entrada_eritrocitos_bajo[B]);
        FISsalida_plaquetas_normal[1]=min(aux,entrada_plaqueta_normal[C]);
        //5
        aux=min(entrada_leucocitos_normal[A],entrada_eritrocitos_bajo[B]);
        FISsalida_leucocitos_normal[4]=min(aux,entrada_plaqueta_bajo[C]);
        aux=min(entrada_leucocitos_normal[A],entrada_eritrocitos_bajo[B]);
        FISsalida_eritrocitos_anemia[1]=min(aux,entrada_plaqueta_bajo[C]);
        aux=min(entrada_leucocitos_normal[A],entrada_eritrocitos_bajo[B]);
        FISsalida_plaquetas_trombopenia[1]=min(aux,entrada_plaqueta_bajo[C]);
        //6
        aux=min(entrada_leucocitos_normal[A],entrada_eritrocitos_bajo[B]);
        FISsalida_leucocitos_normal[5]=min(aux,entrada_plaqueta_alto[C]);
        aux=min(entrada_leucocitos_normal[A],entrada_eritrocitos_bajo[B]);
        FISsalida_eritrocitos_anemia[2]=min(aux,entrada_plaqueta_alto[C]);
        aux=min(entrada_leucocitos_normal[A],entrada_eritrocitos_bajo[B]);
        FISsalida_plaquetas_trombocitosis[1]=min(aux,entrada_plaqueta_alto[C]);
        //7
        aux=min(entrada_leucocitos_normal[A],entrada_eritrocitos_alto[B]);
        FISsalida_leucocitos_normal[6]=min(aux,entrada_plaqueta_normal[C]);
        aux=min(entrada_leucocitos_normal[A],entrada_eritrocitos_alto[B]);
        FISsalida_eritrocitos_FO[0]=min(aux,entrada_plaqueta_normal[C]);
        aux=min(entrada_leucocitos_normal[A],entrada_eritrocitos_alto[B]);
        FISsalida_plaquetas_normal[2]=min(aux,entrada_plaqueta_normal[C]);
        //8
        aux=min(entrada_leucocitos_normal[A],entrada_eritrocitos_alto[B]);
        FISsalida_leucocitos_normal[7]=min(aux,entrada_plaqueta_bajo[C]);
        aux=min(entrada_leucocitos_normal[A],entrada_eritrocitos_alto[B]);
        FISsalida_eritrocitos_FO[1]=min(aux,entrada_plaqueta_bajo[C]);
        aux=min(entrada_leucocitos_normal[A],entrada_eritrocitos_alto[B]);
        FISsalida_plaquetas_trombopenia[2]=min(aux,entrada_plaqueta_bajo[C]);
        //9
        aux=min(entrada_leucocitos_normal[A],entrada_eritrocitos_alto[B]);
        FISsalida_leucocitos_normal[8]=min(aux,entrada_plaqueta_alto[C]);
        aux=min(entrada_leucocitos_normal[A],entrada_eritrocitos_alto[B]);
        FISsalida_eritrocitos_FO[2]=min(aux,entrada_plaqueta_alto[C]);
        aux=min(entrada_leucocitos_normal[A],entrada_eritrocitos_alto[B]);
        FISsalida_plaquetas_trombocitosis[2]=min(aux,entrada_plaqueta_alto[C]);
        //10
        aux=min(entrada_leucocitos_bajo[A],entrada_eritrocitos_normal[B]);
        FISsalida_leucocitos_RIB[0]=min(aux,entrada_plaqueta_normal[C]);
        aux=min(entrada_leucocitos_bajo[A],entrada_eritrocitos_normal[B]);
        FISsalida_eritrocitos_normal[3]=min(aux,entrada_plaqueta_normal[C]);
        aux=min(entrada_leucocitos_bajo[A],entrada_eritrocitos_normal[B]);
        FISsalida_plaquetas_normal[3]=min(aux,entrada_plaqueta_normal[C]);
        //11
        aux=min(entrada_leucocitos_bajo[A],entrada_eritrocitos_normal[B]);
        FISsalida_leucocitos_RIB[1]=min(aux,entrada_plaqueta_bajo[C]);
        aux=min(entrada_leucocitos_bajo[A],entrada_eritrocitos_normal[B]);
        FISsalida_eritrocitos_normal[4]=min(aux,entrada_plaqueta_bajo[C]);
        aux=min(entrada_leucocitos_bajo[A],entrada_eritrocitos_normal[B]);
        FISsalida_plaquetas_trombopenia[3]=min(aux,entrada_plaqueta_bajo[C]);
        //12
        aux=min(entrada_leucocitos_bajo[A],entrada_eritrocitos_normal[B]);
        FISsalida_leucocitos_RIB[2]=min(aux,entrada_plaqueta_alto[C]);
        aux=min(entrada_leucocitos_bajo[A],entrada_eritrocitos_normal[B]);
        FISsalida_eritrocitos_normal[5]=min(aux,entrada_plaqueta_alto[C]);
        aux=min(entrada_leucocitos_bajo[A],entrada_eritrocitos_normal[B]);
        FISsalida_plaquetas_trombocitosis[3]=min(aux,entrada_plaqueta_alto[C]);
        //13
        aux=min(entrada_leucocitos_bajo[A],entrada_eritrocitos_bajo[B]);
        FISsalida_leucocitos_RIB[3]=min(aux,entrada_plaqueta_normal[C]);
        aux=min(entrada_leucocitos_bajo[A],entrada_eritrocitos_bajo[B]);
        FISsalida_eritrocitos_anemia[3]=min(aux,entrada_plaqueta_normal[C]);
        aux=min(entrada_leucocitos_bajo[A],entrada_eritrocitos_bajo[B]);
        FISsalida_plaquetas_normal[4]=min(aux,entrada_plaqueta_normal[C]);
        //14
        aux=min(entrada_leucocitos_bajo[A],entrada_eritrocitos_bajo[B]);
        FISsalida_leucocitos_RIB[4]=min(aux,entrada_plaqueta_bajo[C]);
        aux=min(entrada_leucocitos_bajo[A],entrada_eritrocitos_bajo[B]);
        FISsalida_eritrocitos_anemia[4]=min(aux,entrada_plaqueta_bajo[C]);
        aux=min(entrada_leucocitos_bajo[A],entrada_eritrocitos_bajo[B]);
        FISsalida_plaquetas_trombopenia[4]=min(aux,entrada_plaqueta_bajo[C]);
        //15
        aux=min(entrada_leucocitos_bajo[A],entrada_eritrocitos_bajo[B]);
        FISsalida_leucocitos_RIB[5]=min(aux,entrada_plaqueta_alto[C]);
        aux=min(entrada_leucocitos_bajo[A],entrada_eritrocitos_bajo[B]);
        FISsalida_eritrocitos_anemia[5]=min(aux,entrada_plaqueta_alto[C]);
        aux=min(entrada_leucocitos_bajo[A],entrada_eritrocitos_bajo[B]);
        FISsalida_plaquetas_trombocitosis[4]=min(aux,entrada_plaqueta_alto[C]);
        //16
        aux=min(entrada_leucocitos_bajo[A],entrada_eritrocitos_alto[B]);
        FISsalida_leucocitos_RIB[6]=min(aux,entrada_plaqueta_normal[C]);
        aux=min(entrada_leucocitos_bajo[A],entrada_eritrocitos_alto[B]);
        FISsalida_eritrocitos_FO[3]=min(aux,entrada_plaqueta_normal[C]);
        aux=min(entrada_leucocitos_bajo[A],entrada_eritrocitos_alto[B]);
        FISsalida_plaquetas_normal[5]=min(aux,entrada_plaqueta_normal[C]);
        //17
        aux=min(entrada_leucocitos_bajo[A],entrada_eritrocitos_alto[B]);
        FISsalida_leucocitos_RIB[7]=min(aux,entrada_plaqueta_bajo[C]);
        aux=min(entrada_leucocitos_bajo[A],entrada_eritrocitos_alto[B]);
        FISsalida_eritrocitos_FO[4]=min(aux,entrada_plaqueta_bajo[C]);
        aux=min(entrada_leucocitos_bajo[A],entrada_eritrocitos_alto[B]);
        FISsalida_plaquetas_trombopenia[5]=min(aux,entrada_plaqueta_bajo[C]);
        //18
        aux=min(entrada_leucocitos_bajo[A],entrada_eritrocitos_alto[B]);
        FISsalida_leucocitos_RIB[8]=min(aux,entrada_plaqueta_alto[C]);
        aux=min(entrada_leucocitos_bajo[A],entrada_eritrocitos_alto[B]);
        FISsalida_eritrocitos_FO[5]=min(aux,entrada_plaqueta_alto[C]);
        aux=min(entrada_leucocitos_bajo[A],entrada_eritrocitos_alto[B]);
        FISsalida_plaquetas_trombocitosis[5]=min(aux,entrada_plaqueta_alto[C]);
        //19
        aux=min(entrada_leucocitos_alto[A],entrada_eritrocitos_normal[B]);
        FISsalida_leucocitos_infeccion[0]=min(aux,entrada_plaqueta_normal[C]);
        aux=min(entrada_leucocitos_alto[A],entrada_eritrocitos_normal[B]);
        FISsalida_eritrocitos_normal[6]=min(aux,entrada_plaqueta_normal[C]);
        aux=min(entrada_leucocitos_alto[A],entrada_eritrocitos_normal[B]);
        FISsalida_plaquetas_normal[6]=min(aux,entrada_plaqueta_normal[C]);
        //20
        aux=min(entrada_leucocitos_alto[A],entrada_eritrocitos_normal[B]);
        FISsalida_leucocitos_infeccion[1]=min(aux,entrada_plaqueta_bajo[C]);
        aux=min(entrada_leucocitos_alto[A],entrada_eritrocitos_normal[B]);
        FISsalida_eritrocitos_normal[7]=min(aux,entrada_plaqueta_bajo[C]);
        aux=min(entrada_leucocitos_alto[A],entrada_eritrocitos_normal[B]);
        FISsalida_plaquetas_trombopenia[6]=min(aux,entrada_plaqueta_bajo[C]);
        //21
        aux=min(entrada_leucocitos_alto[A],entrada_eritrocitos_normal[B]);
        FISsalida_leucocitos_infeccion[2]=min(aux,entrada_plaqueta_alto[C]);
        aux=min(entrada_leucocitos_alto[A],entrada_eritrocitos_normal[B]);
        FISsalida_eritrocitos_normal[8]=min(aux,entrada_plaqueta_alto[C]);
        aux=min(entrada_leucocitos_alto[A],entrada_eritrocitos_normal[B]);
        FISsalida_plaquetas_trombocitosis[6]=min(aux,entrada_plaqueta_alto[C]);
        //22
        aux=min(entrada_leucocitos_alto[A],entrada_eritrocitos_bajo[B]);
        FISsalida_leucocitos_infeccion[3]=min(aux,entrada_plaqueta_normal[C]);
        aux=min(entrada_leucocitos_alto[A],entrada_eritrocitos_bajo[B]);
        FISsalida_eritrocitos_anemia[6]=min(aux,entrada_plaqueta_normal[C]);
        aux=min(entrada_leucocitos_alto[A],entrada_eritrocitos_bajo[B]);
        FISsalida_plaquetas_normal[7]=min(aux,entrada_plaqueta_normal[C]);
        //23
        aux=min(entrada_leucocitos_alto[A],entrada_eritrocitos_bajo[B]);
        FISsalida_leucocitos_infeccion[4]=min(aux,entrada_plaqueta_bajo[C]);
        aux=min(entrada_leucocitos_alto[A],entrada_eritrocitos_bajo[B]);
        FISsalida_eritrocitos_anemia[7]=min(aux,entrada_plaqueta_bajo[C]);
        aux=min(entrada_leucocitos_alto[A],entrada_eritrocitos_bajo[B]);
        FISsalida_plaquetas_trombopenia[7]=min(aux,entrada_plaqueta_bajo[C]);
        //24
        aux=min(entrada_leucocitos_alto[A],entrada_eritrocitos_bajo[B]);
        FISsalida_leucocitos_infeccion[5]=min(aux,entrada_plaqueta_alto[C]);
        aux=min(entrada_leucocitos_alto[A],entrada_eritrocitos_bajo[B]);
        FISsalida_eritrocitos_anemia[8]=min(aux,entrada_plaqueta_alto[C]);
        aux=min(entrada_leucocitos_alto[A],entrada_eritrocitos_bajo[B]);
        FISsalida_plaquetas_trombocitosis[7]=min(aux,entrada_plaqueta_alto[C]);
        //25
        aux=min(entrada_leucocitos_alto[A],entrada_eritrocitos_alto[B]);
        FISsalida_leucocitos_infeccion[6]=min(aux,entrada_plaqueta_normal[C]);
        aux=min(entrada_leucocitos_alto[A],entrada_eritrocitos_alto[B]);
        FISsalida_eritrocitos_FO[6]=min(aux,entrada_plaqueta_normal[C]);
        aux=min(entrada_leucocitos_alto[A],entrada_eritrocitos_alto[B]);
        FISsalida_plaquetas_normal[8]=min(aux,entrada_plaqueta_normal[C]);
        //26
        aux=min(entrada_leucocitos_alto[A],entrada_eritrocitos_alto[B]);
        FISsalida_leucocitos_infeccion[7]=min(aux,entrada_plaqueta_bajo[C]);
        aux=min(entrada_leucocitos_alto[A],entrada_eritrocitos_alto[B]);
        FISsalida_eritrocitos_FO[7]=min(aux,entrada_plaqueta_bajo[C]);
        aux=min(entrada_leucocitos_alto[A],entrada_eritrocitos_alto[B]);
        FISsalida_plaquetas_trombopenia[8]=min(aux,entrada_plaqueta_bajo[C]);
        //27
        aux=min(entrada_leucocitos_alto[A],entrada_eritrocitos_alto[B]);
        FISsalida_leucocitos_infeccion[8]=min(aux,entrada_plaqueta_alto[C]);
        aux=min(entrada_leucocitos_alto[A],entrada_eritrocitos_alto[B]);
        FISsalida_eritrocitos_FO[8]=min(aux,entrada_plaqueta_alto[C]);
        aux=min(entrada_leucocitos_alto[A],entrada_eritrocitos_alto[B]);
        FISsalida_plaquetas_trombocitosis[8]=min(aux,entrada_plaqueta_alto[C]);

        double Corte_leucocitos_R1=maxArray(FISsalida_leucocitos_RIB);
        double Corte_leucocitos_R2=maxArray(FISsalida_leucocitos_normal);
        double Corte_leucocitos_R3=maxArray(FISsalida_leucocitos_infeccion);

        double[] Cr1;
        double[] Cr2;
        double[] Cr3;

        Cr1=new double[41];
        Cr2=new double[41];
        Cr3=new double[41];

        Cr1=salida_leucocitos_RIB;
        Cr2=salida_leucocitos_normal;
        Cr3=salida_leucocitos_infeccion;

        for (int k=0;k<Cr1.length;k++){
            if(salida_leucocitos_RIB[k]>=Corte_leucocitos_R1){
                Cr1[k]=Corte_leucocitos_R1;
            }
            if(salida_leucocitos_normal[k]>=Corte_leucocitos_R2){
                Cr2[k]=Corte_leucocitos_R2;
            }
            if(salida_leucocitos_infeccion[k]>=Corte_leucocitos_R3){
                Cr3[k]=Corte_leucocitos_R3;
            }
        }

        double[] casi1;
        double[] CLeucocitos;

        CLeucocitos=new double[Cr1.length];

        casi1=maxCorte(Cr1,Cr2);
        CLeucocitos=maxCorte(casi1,Cr3);

        double Corte_eritrocitos_R1=maxArray(FISsalida_eritrocitos_anemia);
        double Corte_eritrocitos_R2=maxArray(FISsalida_eritrocitos_normal);
        double Corte_eritrocitos_R3=maxArray(FISsalida_eritrocitos_FO);

        Cr1=salida_eritrocitos_anemia;
        Cr2=salida_eritrocitos_normal;
        Cr3=salida_eritrocitos_FO;

        for (int k=0;k<Cr1.length;k++){
            if(salida_eritrocitos_anemia[k]>=Corte_eritrocitos_R1){
                Cr1[k]=Corte_eritrocitos_R1;
            }
            if(salida_eritrocitos_normal[k]>=Corte_eritrocitos_R2){
                Cr2[k]=Corte_eritrocitos_R2;
            }
            if(salida_eritrocitos_FO[k]>=Corte_eritrocitos_R3){
                Cr3[k]=Corte_eritrocitos_R3;
            }
        }

        double[] CEritrocitos;

        CEritrocitos=new double[Cr1.length];

        casi1=maxCorte(Cr1,Cr2);
        CEritrocitos=maxCorte(casi1,Cr3);

        double Corte_plaquetas_R1=maxArray(FISsalida_plaquetas_trombopenia);
        double Corte_plaquetas_R2=maxArray(FISsalida_plaquetas_normal);
        double Corte_plaquetas_R3=maxArray(FISsalida_plaquetas_trombocitosis);

        Cr1=salida_plaquetas_trombopenia;
        Cr2=salida_plaquetas_normal;
        Cr3=salida_plaquetas_trombocitosis;

        for (int k=0;k<Cr1.length;k++){
            if(salida_plaquetas_trombopenia[k]>=Corte_plaquetas_R1){
                Cr1[k]=Corte_plaquetas_R1;
            }
            if(salida_plaquetas_normal[k]>=Corte_plaquetas_R2){
                Cr2[k]=Corte_plaquetas_R2;
            }
            if(salida_plaquetas_trombocitosis[k]>=Corte_plaquetas_R3){
                Cr3[k]=Corte_plaquetas_R3;
            }
        }

        double[] CPlaquetas;

        CPlaquetas=new double[Cr1.length];

        casi1=maxCorte(Cr1,Cr2);
        CPlaquetas=maxCorte(casi1,Cr3);

        //Defuzificacion
        double[] numerador;
        double[] denominador;

        double num,den;
        numerador=new double[CLeucocitos.length];
        denominador=new double[CLeucocitos.length];

        for (int o=0; o<CLeucocitos.length;o++){
            numerador[o]=(o)*CLeucocitos[o];
            denominador[o]=CLeucocitos[o];
        }

        num=sumArray(numerador);
        den=sumArray(denominador);
        LeucocitosVD=(num/den);

        for (int o=0; o<CEritrocitos.length;o++){
            numerador[o]=(o)*CEritrocitos[o];
            denominador[o]=CEritrocitos[o];
        }

        num=sumArray(numerador);
        den=sumArray(denominador);
        EritrocitosVD=(num/den);

        for (int o=0; o<CPlaquetas.length;o++){
            numerador[o]=(o)*CPlaquetas[o];
            denominador[o]=CPlaquetas[o];
        }

        num=sumArray(numerador);
        den=sumArray(denominador);
        PlaquetasVD=(num/den);
    }

    void validarDatos(int leucocitosE, int eritrocitosE, int plaquetasE){
        if(leucocitosE>18){
            leucocitos="18";
        }
        if (leucocitosE<0){
            leucocitos="1";
        }
        if(eritrocitosE>18){
            eritrocitos="18";
        }
        if (eritrocitosE<0){
            eritrocitos="1";
        }
        if(plaquetasE>799){
            plaquetas="799";
        }
        if(plaquetasE<0){
            plaquetas="1";
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    double[] sigmoide(double a, double c, int tam){
        double[] y;
        y=new double[tam];
        for (int i=0;i<tam;i++){
            y[i] = (1./(1 + Math.exp(-a*(x[i]-c))));
        }
        return y;
    }

    double[] gausiana(double a, double c, int tam){
        double[] y;
        y=new double[tam];
        for (int i=0;i<tam;i++){
            y[i] = Math.exp((-.5)*Math.pow((x[i]-c)/a,2));
        }
        return y;
    }

    double[] campana(double a, double b, double c, int tam){
        double[] y;
        y=new double[tam];
        for (int i=0;i<tam;i++){
            y[i]=1./(1+Math.pow((Math.abs((x[i]-c)/a)),(2*b)));
        }
        return y;
    }

    double maxArray(double[] entrada){
        double maximo=entrada[0];
        for (double v : entrada) {
            if (maximo < v){
                maximo = v;
            }
        }
        return maximo;
    }

    double minArray(double[] entrada){
        double minimo=entrada[0];

        for (double v : entrada) {
            if (minimo > v)
                minimo = v;
        }
        return minimo;
    }

    double min(double a, double b){
        return Math.min(a, b);
    }

    double max(double a, double b){
        return Math.max(a, b);
    }

    double[] maxCorte(double[] first, double[] second){
        double[] listo= new double[first.length];

        for (int i=0;i<(first.length);i++){
            if(first[i]>=second[i]){
                listo[i]=first[i];
            }else{
                listo[i]=second[i];
            }
        }
        return listo;
    }

    double sumArray(double[] array){
        double suma=0;
        for (double v : array) {
            suma += v;
        }
        return suma;
    }

}