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

import java.util.stream.IntStream;

public class VerQuimica extends AppCompatActivity {

    double GlucosaVD,HierroVD,ColesterolVD;

    String nombre, fecha, glucosa, hierro, colesterol, creatinina, idAnalisis;
    String GlucosaDiag,HierroDiag,ColesterolDiag;
    String Diagnostico;
    int[] x;

    Usuario usuario;

    ImageView ivBack;
    TextView tvNombreAnalisis,tvGlucosa,tvHierro,tvColesterol,tvInterpretacionDesc,tvGlucosaValor,tvColesterolValor,tvHierroValor;
    ProgressBar pbHierro,pbColesterol,pbGlucosa;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_quimica);

        usuario =(Usuario)getApplicationContext();

        x=new int[251];
        x= IntStream.rangeClosed(0, 250).toArray();

        Intent is = getIntent();

        idAnalisis = is.getStringExtra("idAnalisis");
        nombre = is.getStringExtra("nombre");
        fecha = is.getStringExtra("fecha");
        glucosa = is.getStringExtra("glucosa");
        creatinina = "1";
        colesterol = is.getStringExtra("colesterol");
        hierro = is.getStringExtra("hierro");

        tvNombreAnalisis=findViewById(R.id.tvNombre);
        tvGlucosa=findViewById(R.id.tvGlucosa);
        tvColesterol=findViewById(R.id.tvColesterol);
        tvHierro=findViewById(R.id.tvHierro);

        tvInterpretacionDesc=findViewById(R.id.tvInterpretacionDesc);

        tvGlucosaValor=findViewById(R.id.tvGlucosaValor);
        tvColesterolValor=findViewById(R.id.tvColesterolValor);
        tvHierroValor=findViewById(R.id.tvHierroValor);

        pbGlucosa=findViewById(R.id.pbGlucosa);
        pbColesterol=findViewById(R.id.pbColesterol);
        pbHierro=findViewById(R.id.pbHierro);

        ivBack=findViewById(R.id.ivBack);
        tvNombreAnalisis.setText(nombre);

        tvGlucosaValor.setText(glucosa);
        tvColesterolValor.setText(colesterol);
        tvHierroValor.setText(hierro);

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        validarDatos(Integer.parseInt(glucosa),Integer.parseInt(creatinina),Integer.parseInt(colesterol),Integer.parseInt(hierro));
        //normalizar();
        sistemaBiometria(Integer.parseInt(glucosa),Integer.parseInt(creatinina),Integer.parseInt(colesterol),Integer.parseInt(hierro));
        mostrarResultados();
        mostrarDiagnostico();

        Log.d("TAG", String.valueOf(GlucosaVD));
        Log.d("TAG2", String.valueOf(ColesterolVD));
        Log.d("TAG3", String.valueOf(HierroVD));
    }

    void normalizar(){
        glucosa=String.valueOf(Integer.parseInt(glucosa)*250/150);
        creatinina=String.valueOf(Integer.parseInt(creatinina)*250/150);
        colesterol=String.valueOf(Integer.parseInt(colesterol)*250/150);
        hierro=String.valueOf(Integer.parseInt(hierro)*250/150);
    }


    void mostrarDiagnostico(){
        if(0<=GlucosaVD && GlucosaVD<=75){
            Diagnostico="Los niveles de glucosa baja podrían indicar hipotiroidismo, ";
        }else if (75<GlucosaVD && GlucosaVD<=120){
            Diagnostico="Los niveles de glucosa son normales, ";
        }else{
            Diagnostico="Los niveles de glucosa alta podrían indicar hipertiroidismo, ";
        }
        //Colesterol
        if(0<=ColesterolVD && ColesterolVD<=140){
            Diagnostico=Diagnostico+"los niveles de colesterol están en el rando normal ";
        }else if (140<ColesterolVD && ColesterolVD<=200){
            Diagnostico=Diagnostico+"los niveles de colesterol están en el limite ";
        }else{
            Diagnostico=Diagnostico+"un nivel alto de colesterol podría indicar arterioescleorosis ";
        }
        //Hierro
        if(0<=HierroVD && HierroVD<=60){
            Diagnostico=Diagnostico+"finalmente, la falta de hierro en la sangre puede indicar una afectación intestinal.";
        }else if (60<HierroVD && HierroVD <=170){
            Diagnostico=Diagnostico+"finalmente, los niveles de hierro son normales.";
        }else{
            Diagnostico=Diagnostico+"finalmente, un nivel alto de hierro en la sangre puede indicar hemocromatosis.";
        }
        tvInterpretacionDesc.setText(Diagnostico);
    }

    void mostrarResultados(){
        //Glucosa
        if(0<=GlucosaVD && GlucosaVD<=75){
            GlucosaDiag="Bajos";
            tvGlucosa.setText(GlucosaDiag);
            pbGlucosa.setProgress((int) ((GlucosaVD/250)*100));
        }else if (75<GlucosaVD && GlucosaVD<=120){
            GlucosaDiag="Normales";
            tvGlucosa.setText(GlucosaDiag);
            pbGlucosa.setProgress((int) ((GlucosaVD/250)*100));
        }else{
            GlucosaDiag="Altos";
            tvGlucosa.setText(GlucosaDiag);
            pbGlucosa.setProgress((int) ((GlucosaVD/250)*100));
        }
        //Colesterol
        if(0<=ColesterolVD && ColesterolVD<=140){
            ColesterolDiag="Normales";
            tvColesterol.setText(ColesterolDiag);
            pbColesterol.setProgress((int) ((ColesterolVD/250)*100));
        }else if (140<ColesterolVD && ColesterolVD<=200){
            ColesterolDiag="Algo altos";
            tvColesterol.setText(ColesterolDiag);
            pbColesterol.setProgress((int) ((ColesterolVD/250)*100));
        }else{
            ColesterolDiag="Altos";
            tvColesterol.setText(ColesterolDiag);
            pbColesterol.setProgress((int) ((ColesterolVD/250)*100));
        }
        //Hierro
        if(0<=HierroVD && HierroVD<=60){
            HierroDiag="Bajos";
            tvHierro.setText(HierroDiag);
            pbHierro.setProgress((int) ((HierroVD/250)*100));
        }else if (60<HierroVD && HierroVD<=170){
            HierroDiag="Normales";
            tvHierro.setText(HierroDiag);
            pbHierro.setProgress((int) ((HierroVD/250)*100));
        }else{
            HierroDiag="Altos";
            tvHierro.setText(HierroDiag);
            pbHierro.setProgress((int) ((HierroVD/250)*100));
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    void sistemaBiometria(int glucosa, int creatinina, int colesterol, int hierro){
        double a;
        double b;
        double c;

        double aux;

        double[] entrada_glucosa_bajo;
        double[] entrada_glucosa_normal;
        double[] entrada_glucosa_alto;
        double[] entrada_creatinina_bajo;
        double[] entrada_creatinina_normal;
        double[] entrada_creatinina_alto;
        double[] entrada_colesterol_normal;
        double[] entrada_colesterol_alto;
        double[] entrada_colesterol_muy_alto;
        double[] entrada_hierro_bajo;
        double[] entrada_hierro_normal;
        double[] entrada_hierro_alto;

        double[] salida_glucosa_hipotiroidismo;
        double[] salida_glucosa_normal;
        double[] salida_glucosa_hipertiroidismo;
        double[] salida_colesterol_normal;
        double[] salida_colesterol_arterio;
        double[] salida_colesterol_alto;
        double[] salida_hierro_AI;
        double[] salida_hierro_normal;
        double[] salida_hierro_hemocromatosis;

        double[] FISsalida_glucosa_AR;
        double[] FISsalida_glucosa_hipo;
        double[] FISsalida_glucosa_normal;
        double[] FISsalida_colesterol_AC;
        double[] FISsalida_colesterol_LA;
        double[] FISsalida_colesterol_normal;
        double[] FISsalida_hierro_AI;
        double[] FISsalida_hierro_HC;
        double[] FISsalida_hierro_normal;

        entrada_glucosa_bajo=new double[251];
        entrada_glucosa_normal=new double[251];
        entrada_glucosa_alto=new double[251];
        entrada_creatinina_bajo=new double[251];
        entrada_creatinina_normal=new double[251];
        entrada_creatinina_alto=new double[251];
        entrada_colesterol_normal=new double[251];
        entrada_colesterol_alto=new double[251];
        entrada_colesterol_muy_alto=new double[251];
        entrada_hierro_bajo=new double[251];
        entrada_hierro_normal=new double[251];
        entrada_hierro_alto=new double[251];

        salida_glucosa_hipotiroidismo=new double[251];
        salida_glucosa_normal=new double[251];
        salida_glucosa_hipertiroidismo=new double[251];
        salida_colesterol_normal=new double[251];
        salida_colesterol_arterio=new double[251];
        salida_colesterol_alto=new double[251];
        salida_hierro_AI=new double[251];
        salida_hierro_normal=new double[251];
        salida_hierro_hemocromatosis=new double[251];

        FISsalida_glucosa_AR=new double[9];
        FISsalida_glucosa_hipo=new double[9];
        FISsalida_glucosa_normal=new double[9];
        FISsalida_colesterol_AC=new double[9];
        FISsalida_colesterol_LA=new double[9];
        FISsalida_colesterol_normal=new double[9];
        FISsalida_hierro_AI=new double[9];
        FISsalida_hierro_HC=new double[9];
        FISsalida_hierro_normal=new double[9];

        a=-.1;
        c=110;
        entrada_glucosa_bajo=sigmoide(a,c,x.length);
        a=30;
        c=150;
        entrada_glucosa_normal=gausiana(a,c,x.length);
        a=.1;
        c=200;
        entrada_glucosa_alto=sigmoide(a,c,x.length);

        a=-.1;
        c=70;
        entrada_creatinina_bajo=sigmoide(a,c,x.length);
        a=30;
        c=125;
        entrada_creatinina_normal=gausiana(a,c,x.length);
        a=.1;
        c=210;
        entrada_creatinina_alto=sigmoide(a,c,x.length);

        a=-.1;
        c=130;
        entrada_colesterol_normal=sigmoide(a,c,x.length);
        a=20;
        c=170;
        entrada_colesterol_alto=gausiana(a,c,x.length);
        a=.1;
        c=200;
        entrada_colesterol_muy_alto=sigmoide(a,c,x.length);

        a=-.1;
        c=65;
        entrada_hierro_bajo=sigmoide(a,c,x.length);
        a=40;
        c=120;
        entrada_hierro_normal=gausiana(a,c,x.length);
        a=.1;
        c=170;
        entrada_hierro_alto=sigmoide(a,c,x.length);

        a=-.6;
        c=140;
        salida_glucosa_hipotiroidismo=sigmoide(a,c,x.length);
        a=30;
        b=6;
        c=180;
        salida_glucosa_normal=campana(a,b,c,x.length);
        a=.6;
        c=220;
        salida_glucosa_hipertiroidismo=sigmoide(a,c,x.length);

        a=-.5;
        c=130;
        salida_colesterol_normal=sigmoide(a,c,x.length);
        a=1;
        c=200;
        salida_colesterol_arterio=sigmoide(a,c,x.length);
        a=20;
        b=6;
        c=170;
        salida_colesterol_alto=campana(a,b,c,x.length);

        a=-.8;
        c=65;
        salida_hierro_AI=sigmoide(a,c,x.length);
        a=40;
        b=6;
        c=120;
        salida_hierro_normal=campana(a,b,c,x.length);
        a=.8;
        c=170;
        salida_hierro_hemocromatosis=sigmoide(a,c,x.length);

        int A=glucosa-1;
        int D=creatinina-1;
        int B=colesterol-1;
        int C=hierro-1;

        //1
        aux=min(entrada_glucosa_normal[A],entrada_colesterol_normal[B]);
        FISsalida_glucosa_normal[0]=min(aux,entrada_hierro_normal[C]);
        aux=min(entrada_glucosa_normal[A],entrada_colesterol_normal[B]);
        FISsalida_colesterol_normal[0]=min(aux,entrada_hierro_normal[C]);
        aux=min(entrada_glucosa_normal[A],entrada_colesterol_normal[B]);
        FISsalida_hierro_normal[0]=min(aux,entrada_hierro_normal[C]);
        //2
        aux=min(entrada_glucosa_normal[A],entrada_colesterol_normal[B]);
        FISsalida_glucosa_normal[1]=min(aux,entrada_hierro_bajo[C]);
        aux=min(entrada_glucosa_normal[A],entrada_colesterol_normal[B]);
        FISsalida_colesterol_normal[1]=min(aux,entrada_hierro_bajo[C]);
        aux=min(entrada_glucosa_normal[A],entrada_colesterol_normal[B]);
        FISsalida_hierro_AI[0]=min(aux,entrada_hierro_bajo[C]);
        //3
        aux=min(entrada_glucosa_normal[A],entrada_colesterol_normal[B]);
        FISsalida_glucosa_normal[2]=min(aux,entrada_hierro_alto[C]);
        aux=min(entrada_glucosa_normal[A],entrada_colesterol_normal[B]);
        FISsalida_colesterol_normal[2]=min(aux,entrada_hierro_alto[C]);
        aux=min(entrada_glucosa_normal[A],entrada_colesterol_normal[B]);
        FISsalida_hierro_HC[0]=min(aux,entrada_hierro_alto[C]);
        //4
        aux=min(entrada_glucosa_normal[A],entrada_colesterol_alto[B]);
        FISsalida_glucosa_normal[3]=min(aux,entrada_hierro_normal[C]);
        aux=min(entrada_glucosa_normal[A],entrada_colesterol_alto[B]);
        FISsalida_colesterol_LA[0]=min(aux,entrada_hierro_normal[C]);
        aux=min(entrada_glucosa_normal[A],entrada_colesterol_alto[B]);
        FISsalida_hierro_normal[1]=min(aux,entrada_hierro_normal[C]);
        //5
        aux=min(entrada_glucosa_normal[A],entrada_colesterol_alto[B]);
        FISsalida_glucosa_normal[4]=min(aux,entrada_hierro_bajo[C]);
        aux=min(entrada_glucosa_normal[A],entrada_colesterol_alto[B]);
        FISsalida_colesterol_LA[1]=min(aux,entrada_hierro_bajo[C]);
        aux=min(entrada_glucosa_normal[A],entrada_colesterol_alto[B]);
        FISsalida_hierro_AI[1]=min(aux,entrada_hierro_bajo[C]);
        //6
        aux=min(entrada_glucosa_normal[A],entrada_colesterol_alto[B]);
        FISsalida_glucosa_normal[5]=min(aux,entrada_hierro_alto[C]);
        aux=min(entrada_glucosa_normal[A],entrada_colesterol_alto[B]);
        FISsalida_colesterol_LA[2]=min(aux,entrada_hierro_alto[C]);
        aux=min(entrada_glucosa_normal[A],entrada_colesterol_alto[B]);
        FISsalida_hierro_HC[1]=min(aux,entrada_hierro_alto[C]);
        //7
        aux=min(entrada_glucosa_normal[A],entrada_colesterol_muy_alto[B]);
        FISsalida_glucosa_normal[6]=min(aux,entrada_hierro_normal[C]);
        aux=min(entrada_glucosa_normal[A],entrada_colesterol_muy_alto[B]);
        FISsalida_colesterol_AC[0]=min(aux,entrada_hierro_normal[C]);
        aux=min(entrada_glucosa_normal[A],entrada_colesterol_muy_alto[B]);
        FISsalida_hierro_normal[2]=min(aux,entrada_hierro_normal[C]);
        //8
        aux=min(entrada_glucosa_normal[A],entrada_colesterol_muy_alto[B]);
        FISsalida_glucosa_normal[7]=min(aux,entrada_hierro_bajo[C]);
        aux=min(entrada_glucosa_normal[A],entrada_colesterol_muy_alto[B]);
        FISsalida_colesterol_AC[1]=min(aux,entrada_hierro_bajo[C]);
        aux=min(entrada_glucosa_normal[A],entrada_colesterol_muy_alto[B]);
        FISsalida_hierro_AI[2]=min(aux,entrada_hierro_bajo[C]);
        //9
        aux=min(entrada_glucosa_normal[A],entrada_colesterol_muy_alto[B]);
        FISsalida_glucosa_normal[8]=min(aux,entrada_hierro_alto[C]);
        aux=min(entrada_glucosa_normal[A],entrada_colesterol_muy_alto[B]);
        FISsalida_colesterol_AC[2]=min(aux,entrada_hierro_alto[C]);
        aux=min(entrada_glucosa_normal[A],entrada_colesterol_muy_alto[B]);
        FISsalida_hierro_HC[2]=min(aux,entrada_hierro_alto[C]);

        //10
        aux=min(entrada_glucosa_bajo[A],entrada_colesterol_normal[B]);
        FISsalida_glucosa_hipo[0]=min(aux,entrada_hierro_normal[C]);
        aux=min(entrada_glucosa_bajo[A],entrada_colesterol_normal[B]);
        FISsalida_colesterol_normal[3]=min(aux,entrada_hierro_normal[C]);
        aux=min(entrada_glucosa_bajo[A],entrada_colesterol_normal[B]);
        FISsalida_hierro_normal[3]=min(aux,entrada_hierro_normal[C]);
        //11
        aux=min(entrada_glucosa_bajo[A],entrada_colesterol_normal[B]);
        FISsalida_glucosa_hipo[1]=min(aux,entrada_hierro_bajo[C]);
        aux=min(entrada_glucosa_bajo[A],entrada_colesterol_normal[B]);
        FISsalida_colesterol_normal[4]=min(aux,entrada_hierro_bajo[C]);
        aux=min(entrada_glucosa_bajo[A],entrada_colesterol_normal[B]);
        FISsalida_hierro_AI[3]=min(aux,entrada_hierro_bajo[C]);
        //12
        aux=min(entrada_glucosa_bajo[A],entrada_colesterol_normal[B]);
        FISsalida_glucosa_hipo[2]=min(aux,entrada_hierro_alto[C]);
        aux=min(entrada_glucosa_bajo[A],entrada_colesterol_normal[B]);
        FISsalida_colesterol_normal[5]=min(aux,entrada_hierro_alto[C]);
        aux=min(entrada_glucosa_bajo[A],entrada_colesterol_normal[B]);
        FISsalida_hierro_HC[3]=min(aux,entrada_hierro_alto[C]);
        //13
        aux=min(entrada_glucosa_bajo[A],entrada_colesterol_alto[B]);
        FISsalida_glucosa_hipo[3]=min(aux,entrada_hierro_normal[C]);
        aux=min(entrada_glucosa_bajo[A],entrada_colesterol_alto[B]);
        FISsalida_colesterol_LA[3]=min(aux,entrada_hierro_normal[C]);
        aux=min(entrada_glucosa_bajo[A],entrada_colesterol_alto[B]);
        FISsalida_hierro_normal[4]=min(aux,entrada_hierro_normal[C]);
        //14
        aux=min(entrada_glucosa_bajo[A],entrada_colesterol_alto[B]);
        FISsalida_glucosa_hipo[4]=min(aux,entrada_hierro_bajo[C]);
        aux=min(entrada_glucosa_bajo[A],entrada_colesterol_alto[B]);
        FISsalida_colesterol_LA[4]=min(aux,entrada_hierro_bajo[C]);
        aux=min(entrada_glucosa_bajo[A],entrada_colesterol_alto[B]);
        FISsalida_hierro_AI[4]=min(aux,entrada_hierro_bajo[C]);
        //15
        aux=min(entrada_glucosa_bajo[A],entrada_colesterol_alto[B]);
        FISsalida_glucosa_hipo[5]=min(aux,entrada_hierro_alto[C]);
        aux=min(entrada_glucosa_bajo[A],entrada_colesterol_alto[B]);
        FISsalida_colesterol_LA[5]=min(aux,entrada_hierro_alto[C]);
        aux=min(entrada_glucosa_bajo[A],entrada_colesterol_alto[B]);
        FISsalida_hierro_HC[4]=min(aux,entrada_hierro_alto[C]);
        //16
        aux=min(entrada_glucosa_bajo[A],entrada_colesterol_muy_alto[B]);
        FISsalida_glucosa_hipo[6]=min(aux,entrada_hierro_normal[C]);
        aux=min(entrada_glucosa_bajo[A],entrada_colesterol_muy_alto[B]);
        FISsalida_colesterol_AC[3]=min(aux,entrada_hierro_normal[C]);
        aux=min(entrada_glucosa_bajo[A],entrada_colesterol_muy_alto[B]);
        FISsalida_hierro_normal[5]=min(aux,entrada_hierro_normal[C]);
        //17
        aux=min(entrada_glucosa_bajo[A],entrada_colesterol_muy_alto[B]);
        FISsalida_glucosa_hipo[7]=min(aux,entrada_hierro_bajo[C]);
        aux=min(entrada_glucosa_bajo[A],entrada_colesterol_muy_alto[B]);
        FISsalida_colesterol_AC[4]=min(aux,entrada_hierro_bajo[C]);
        aux=min(entrada_glucosa_bajo[A],entrada_colesterol_muy_alto[B]);
        FISsalida_hierro_AI[5]=min(aux,entrada_hierro_bajo[C]);
        //18
        aux=min(entrada_glucosa_bajo[A],entrada_colesterol_muy_alto[B]);
        FISsalida_glucosa_hipo[8]=min(aux,entrada_hierro_alto[C]);
        aux=min(entrada_glucosa_bajo[A],entrada_colesterol_muy_alto[B]);
        FISsalida_colesterol_AC[5]=min(aux,entrada_hierro_alto[C]);
        aux=min(entrada_glucosa_bajo[A],entrada_colesterol_muy_alto[B]);
        FISsalida_hierro_HC[5]=min(aux,entrada_hierro_alto[C]);

        //19
        aux=min(entrada_glucosa_alto[A],entrada_colesterol_normal[B]);
        FISsalida_glucosa_AR[0]=min(aux,entrada_hierro_normal[C]);
        aux=min(entrada_glucosa_alto[A],entrada_colesterol_normal[B]);
        FISsalida_colesterol_normal[6]=min(aux,entrada_hierro_normal[C]);
        aux=min(entrada_glucosa_alto[A],entrada_colesterol_normal[B]);
        FISsalida_hierro_normal[6]=min(aux,entrada_hierro_normal[C]);
        //20
        aux=min(entrada_glucosa_alto[A],entrada_colesterol_normal[B]);
        FISsalida_glucosa_AR[1]=min(aux,entrada_hierro_bajo[C]);
        aux=min(entrada_glucosa_alto[A],entrada_colesterol_normal[B]);
        FISsalida_colesterol_normal[7]=min(aux,entrada_hierro_bajo[C]);
        aux=min(entrada_glucosa_alto[A],entrada_colesterol_normal[B]);
        FISsalida_hierro_AI[6]=min(aux,entrada_hierro_bajo[C]);
        //21
        aux=min(entrada_glucosa_alto[A],entrada_colesterol_normal[B]);
        FISsalida_glucosa_AR[2]=min(aux,entrada_hierro_alto[C]);
        aux=min(entrada_glucosa_alto[A],entrada_colesterol_normal[B]);
        FISsalida_colesterol_normal[8]=min(aux,entrada_hierro_alto[C]);
        aux=min(entrada_glucosa_alto[A],entrada_colesterol_normal[B]);
        FISsalida_hierro_HC[6]=min(aux,entrada_hierro_alto[C]);
        //22
        aux=min(entrada_glucosa_alto[A],entrada_colesterol_alto[B]);
        FISsalida_glucosa_AR[3]=min(aux,entrada_hierro_normal[C]);
        aux=min(entrada_glucosa_alto[A],entrada_colesterol_alto[B]);
        FISsalida_colesterol_LA[6]=min(aux,entrada_hierro_normal[C]);
        aux=min(entrada_glucosa_alto[A],entrada_colesterol_alto[B]);
        FISsalida_hierro_normal[7]=min(aux,entrada_hierro_normal[C]);
        //23
        aux=min(entrada_glucosa_alto[A],entrada_colesterol_alto[B]);
        FISsalida_glucosa_AR[4]=min(aux,entrada_hierro_bajo[C]);
        aux=min(entrada_glucosa_alto[A],entrada_colesterol_alto[B]);
        FISsalida_colesterol_LA[7]=min(aux,entrada_hierro_bajo[C]);
        aux=min(entrada_glucosa_alto[A],entrada_colesterol_alto[B]);
        FISsalida_hierro_AI[7]=min(aux,entrada_hierro_bajo[C]);
        //24
        aux=min(entrada_glucosa_alto[A],entrada_colesterol_alto[B]);
        FISsalida_glucosa_AR[5]=min(aux,entrada_hierro_alto[C]);
        aux=min(entrada_glucosa_alto[A],entrada_colesterol_alto[B]);
        FISsalida_colesterol_LA[8]=min(aux,entrada_hierro_alto[C]);
        aux=min(entrada_glucosa_alto[A],entrada_colesterol_alto[B]);
        FISsalida_hierro_HC[7]=min(aux,entrada_hierro_alto[C]);
        //25
        aux=min(entrada_glucosa_alto[A],entrada_colesterol_muy_alto[B]);
        FISsalida_glucosa_AR[6]=min(aux,entrada_hierro_normal[C]);
        aux=min(entrada_glucosa_alto[A],entrada_colesterol_muy_alto[B]);
        FISsalida_colesterol_AC[6]=min(aux,entrada_hierro_normal[C]);
        aux=min(entrada_glucosa_alto[A],entrada_colesterol_muy_alto[B]);
        FISsalida_hierro_normal[8]=min(aux,entrada_hierro_normal[C]);
        //26
        aux=min(entrada_glucosa_alto[A],entrada_colesterol_muy_alto[B]);
        FISsalida_glucosa_AR[7]=min(aux,entrada_hierro_bajo[C]);
        aux=min(entrada_glucosa_alto[A],entrada_colesterol_muy_alto[B]);
        FISsalida_colesterol_AC[7]=min(aux,entrada_hierro_bajo[C]);
        aux=min(entrada_glucosa_alto[A],entrada_colesterol_muy_alto[B]);
        FISsalida_hierro_AI[8]=min(aux,entrada_hierro_bajo[C]);
        //27
        aux=min(entrada_glucosa_alto[A],entrada_colesterol_muy_alto[B]);
        FISsalida_glucosa_AR[8]=min(aux,entrada_hierro_alto[C]);
        aux=min(entrada_glucosa_alto[A],entrada_colesterol_muy_alto[B]);
        FISsalida_colesterol_AC[8]=min(aux,entrada_hierro_alto[C]);
        aux=min(entrada_glucosa_alto[A],entrada_colesterol_muy_alto[B]);
        FISsalida_hierro_HC[8]=min(aux,entrada_hierro_alto[C]);

        double Corte_glucosa_R1=maxArray(FISsalida_glucosa_hipo);
        double Corte_glucosa_R2=maxArray(FISsalida_glucosa_normal);
        double Corte_glucosa_R3=maxArray(FISsalida_glucosa_AR);

        double[] Cr1;
        double[] Cr2;
        double[] Cr3;

        Cr1=new double[251];
        Cr2=new double[251];
        Cr3=new double[251];

        Cr1=salida_glucosa_hipotiroidismo;
        Cr2=salida_glucosa_normal;
        Cr3=salida_glucosa_hipertiroidismo;

        for (int k=0;k<Cr1.length;k++){
            if(salida_glucosa_hipotiroidismo[k]>=Corte_glucosa_R1){
                Cr1[k]=Corte_glucosa_R1;
            }
            if(salida_glucosa_normal[k]>=Corte_glucosa_R2){
                Cr2[k]=Corte_glucosa_R2;
            }
            if(salida_glucosa_hipertiroidismo[k]>=Corte_glucosa_R3){
                Cr3[k]=Corte_glucosa_R3;
            }
        }

        double[] casi1;
        double[] CGlucosa;

        CGlucosa=new double[Cr1.length];

        casi1=maxCorte(Cr1,Cr2);
        CGlucosa=maxCorte(casi1,Cr3);

        double Corte_colesterol_R1=maxArray(FISsalida_colesterol_normal);
        double Corte_colesterol_R2=maxArray(FISsalida_colesterol_LA);
        double Corte_colesterol_R3=maxArray(FISsalida_colesterol_AC);

        Cr1=salida_colesterol_normal;
        Cr2=salida_colesterol_alto;
        Cr3=salida_colesterol_arterio;

        for (int k=0;k<Cr1.length;k++){
            if(salida_colesterol_normal[k]>=Corte_colesterol_R1){
                Cr1[k]=Corte_colesterol_R1;
            }
            if(salida_colesterol_alto[k]>=Corte_colesterol_R2){
                Cr2[k]=Corte_colesterol_R2;
            }
            if(salida_colesterol_arterio[k]>=Corte_colesterol_R3){
                Cr3[k]=Corte_colesterol_R3;
            }
        }

        double[] CColesterol;

        CColesterol=new double[Cr1.length];

        casi1=maxCorte(Cr1,Cr2);
        CColesterol=maxCorte(casi1,Cr3);

        double Corte_hierro_R1=maxArray(FISsalida_hierro_AI);
        double Corte_hierro_R2=maxArray(FISsalida_hierro_normal);
        double Corte_hierro_R3=maxArray(FISsalida_hierro_HC);

        Cr1=salida_hierro_AI;
        Cr2=salida_hierro_normal;
        Cr3=salida_hierro_hemocromatosis;

        for (int k=0;k<Cr1.length;k++){
            if(salida_hierro_AI[k]>=Corte_hierro_R1){
                Cr1[k]=Corte_hierro_R1;
            }
            if(salida_hierro_normal[k]>=Corte_hierro_R2){
                Cr2[k]=Corte_hierro_R2;
            }
            if(salida_hierro_hemocromatosis[k]>=Corte_hierro_R3){
                Cr3[k]=Corte_hierro_R3;
            }
        }

        double[] CHierro;

        CHierro=new double[Cr1.length];

        casi1=maxCorte(Cr1,Cr2);
        CHierro=maxCorte(casi1,Cr3);

        //Defuzificacion
        double[] numerador;
        double[] denominador;

        double num,den;
        numerador=new double[CGlucosa.length];
        denominador=new double[CGlucosa.length];

        for (int o=0; o<CGlucosa.length;o++){
            numerador[o]=(o)*CGlucosa[o];
            denominador[o]=CGlucosa[o];
        }

        num=sumArray(numerador);
        den=sumArray(denominador);
        GlucosaVD=(num/den);

        for (int o=0; o<CColesterol.length;o++){
            numerador[o]=(o)*CColesterol[o];
            denominador[o]=CColesterol[o];
        }

        num=sumArray(numerador);
        den=sumArray(denominador);
        ColesterolVD=(num/den);

        for (int o=0; o<CHierro.length;o++){
            numerador[o]=(o)*CHierro[o];
            denominador[o]=CHierro[o];
        }

        num=sumArray(numerador);
        den=sumArray(denominador);
        HierroVD=(num/den);
    }

    void validarDatos(int glucosaE, int creatininaE, int colesterolE,int hierroE){
        if(glucosaE>149){
            glucosa="149";
        }
        if (glucosaE<0){
            glucosa="1";
        }
        if(creatininaE>=2){
            creatinina="1";
        }
        if (creatininaE<0){
            creatinina="1";
        }
        if(colesterolE>=249){
            colesterol="249";
        }
        if(colesterolE<0){
            colesterol="1";
        }
        if(hierroE>=249){
            hierro="249";
        }
        if(hierroE<0){
            hierro="1";
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