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

public class VerOrina extends AppCompatActivity {

    double CreatininaVD,AlbuminaVD,AcidoVD;

    String nombre, fecha, creatinina, albumina, acido, idAnalisis;
    String CreatininaDiag,AlbuminaDiag,AcidoDiag;
    String Diagnostico;
    int[] x;

    Usuario usuario;

    ImageView ivBack;
    TextView tvNombreAnalisis,tvCreatinina,tvAlbumina,tvAcido,tvInterpretacionDesc,tvCreatininaValor,tvAlbuminaValor,tvAcidoValor;
    ProgressBar pbCreatinina,pbAlbumina,pbAcido;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_orina);

        usuario =(Usuario)getApplicationContext();

        x=new int[41];
        x= IntStream.rangeClosed(0, 40).toArray();

        Intent is = getIntent();

        idAnalisis = is.getStringExtra("idAnalisis");
        nombre = is.getStringExtra("nombre");
        fecha = is.getStringExtra("fecha");
        creatinina = is.getStringExtra("creatinina");
        albumina = is.getStringExtra("albumina");
        acido = is.getStringExtra("acido");

        tvNombreAnalisis=findViewById(R.id.tvNombre);
        tvCreatinina=findViewById(R.id.tvCreatinina);
        tvAlbumina=findViewById(R.id.tvAlbumina);
        tvAcido=findViewById(R.id.tvAcido);
        tvInterpretacionDesc=findViewById(R.id.tvInterpretacionDesc);

        tvCreatininaValor=findViewById(R.id.tvCreatitinaValor);
        tvAlbuminaValor=findViewById(R.id.tvAlbuminaValor);
        tvAcidoValor=findViewById(R.id.tvAcidoValor);

        pbCreatinina=findViewById(R.id.pbCreatinina);
        pbAlbumina=findViewById(R.id.pbAlbumina);
        pbAcido=findViewById(R.id.pbAcido);

        ivBack=findViewById(R.id.ivBack);
        tvNombreAnalisis.setText(nombre);

        tvCreatininaValor.setText(creatinina);
        tvAlbuminaValor.setText(albumina);
        tvAcidoValor.setText(acido);

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        validarDatos(Integer.parseInt(creatinina),Integer.parseInt(albumina),Integer.parseInt(acido));
        sistemaBiometria(Integer.parseInt(creatinina),Integer.parseInt(albumina),Integer.parseInt(acido));
        mostrarResultados();
        mostrarDiagnostico();

        Log.d("TAG", String.valueOf(CreatininaVD));
        Log.d("TAG2", String.valueOf(AlbuminaVD));
        Log.d("TAG3", String.valueOf(AcidoVD));
    }

    void mostrarDiagnostico(){
        if(0<=CreatininaVD && CreatininaVD<=12){
            Diagnostico="Los niveles de creatinina indican una posible insuficiencia renal, ";
        }else if (12<CreatininaVD && CreatininaVD<=26){
            Diagnostico="Los niveles de creatinina son normales, ";
        }else{
            Diagnostico="Los niveles de creatinina indican una posible infección en la vías urinarias, ";
        }
        //Eritrocitos
        if(0<=AlbuminaVD && AlbuminaVD<=12){
            Diagnostico=Diagnostico+"los niveles de albúmina son normales y ";
        }else if (12<AlbuminaVD && AlbuminaVD<=26){
            Diagnostico=Diagnostico+"los niveles de albúmina están en el limite y ";
        }else{
            Diagnostico=Diagnostico+"un nivel alto de albúmina puede indicar una enfermedad renal y ";
        }
        //Plaquetas
        if(0<=AcidoVD && AcidoVD<=6){
            Diagnostico=Diagnostico+"finalmente, los niveles de ácido venilmandélico son normales.";
        }else if (6<AcidoVD && AcidoVD<=14){
            Diagnostico=Diagnostico+"finalmente, los niveles de ácido venilmandélico están en el limite.";
        }else{
            Diagnostico=Diagnostico+"finalmente, un nivel alto de ácido venilmandélico podria indicar una patología de la glándula suprarrenal.";
        }
        tvInterpretacionDesc.setText(Diagnostico);
    }

    void mostrarResultados(){
        //Creatinina
        if(0<=CreatininaVD && CreatininaVD<=12){
            CreatininaDiag="Bajos";
            tvCreatinina.setText(CreatininaDiag);
            pbCreatinina.setProgress((int) ((CreatininaVD/40)*100));
        }else if (12<CreatininaVD && CreatininaVD<=26){
            CreatininaDiag="Normales";
            tvCreatinina.setText(CreatininaDiag);
            pbCreatinina.setProgress((int) ((CreatininaVD/40)*100));
        }else{
            CreatininaDiag="Altos";
            tvCreatinina.setText(CreatininaDiag);
            pbCreatinina.setProgress((int) ((CreatininaVD/40)*100));
        }
        //Albumina
        if(0<=AlbuminaVD && AlbuminaVD<=12){
            AlbuminaDiag="Normales";
            tvAlbumina.setText(AlbuminaDiag);
            pbAlbumina.setProgress((int) ((AlbuminaVD/40)*100));
        }else if (12<AlbuminaVD && AlbuminaVD<=26){
            AlbuminaDiag="Algo altos";
            tvAlbumina.setText(AlbuminaDiag);
            pbAlbumina.setProgress((int) ((AlbuminaVD/40)*100));
        }else{
            AlbuminaDiag="Altos";
            tvAlbumina.setText(AlbuminaDiag);
            pbAlbumina.setProgress((int) ((AlbuminaVD/40)*100));
        }
        //Acido
        if(0<=AcidoVD && AcidoVD<=12){
            AcidoDiag="Normales";
            tvAcido.setText(AcidoDiag);
            pbAcido.setProgress((int) ((AcidoVD/40)*100));
        }else if (12<AcidoVD && AcidoVD<=26){
            AcidoDiag="Algo altos";
            tvAcido.setText(AcidoDiag);
            pbAcido.setProgress((int) ((AcidoVD/40)*100));
        }else{
            AcidoDiag="Altos";
            tvAcido.setText(AcidoDiag);
            pbAcido.setProgress((int) ((AcidoVD/40)*100));
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    void sistemaBiometria(int creatinina, int albumina, int acido){
        double a;
        double b;
        double c;

        double aux;

        double[] entrada_creatinina_bajo;
        double[] entrada_creatinina_normal;
        double[] entrada_creatinina_alto;
        double[] entrada_albumina_normal;
        double[] entrada_albumina_alto;
        double[] entrada_albumina_muy_alto;
        double[] entrada_acido_normal;
        double[] entrada_acido_alto;
        double[] entrada_acido_muy_alto;

        double[] salida_creatinina_IR;
        double[] salida_creatinina_normal;
        double[] salida_creatinina_IVU;
        double[] salida_albumina_normal;
        double[] salida_albumina_limite;
        double[] salida_albumina_ER;
        double[] salida_acido_normal;
        double[] salida_acido_limite;
        double[] salida_acido_PGS;

        double[] FISsalida_creatinina_IR;
        double[] FISsalida_creatinina_normal;
        double[] FISsalida_creatinina_IVU;
        double[] FISsalida_albumina_normal;
        double[] FISsalida_albumina_limite;
        double[] FISsalida_albumina_ER;
        double[] FISsalida_acido_normal;
        double[] FISsalida_acido_limite;
        double[] FISsalida_acido_PGS;

        entrada_creatinina_bajo=new double[19];
        entrada_creatinina_normal=new double[19];
        entrada_creatinina_alto=new double[19];
        entrada_albumina_normal=new double[19];
        entrada_albumina_alto=new double[19];
        entrada_albumina_muy_alto=new double[19];
        entrada_acido_normal=new double[801];
        entrada_acido_alto=new double[801];
        entrada_acido_muy_alto=new double[801];

        salida_creatinina_IR=new double[41];
        salida_creatinina_normal=new double[41];
        salida_creatinina_IVU=new double[41];
        salida_albumina_normal=new double[41];
        salida_albumina_limite=new double[41];
        salida_albumina_ER=new double[41];
        salida_acido_normal=new double[41];
        salida_acido_limite=new double[41];
        salida_acido_PGS=new double[41];

        FISsalida_creatinina_IR=new double[9];
        FISsalida_creatinina_normal=new double[9];
        FISsalida_creatinina_IVU=new double[9];
        FISsalida_albumina_normal=new double[9];
        FISsalida_albumina_limite=new double[9];
        FISsalida_albumina_ER=new double[9];
        FISsalida_acido_normal=new double[9];
        FISsalida_acido_limite=new double[9];
        FISsalida_acido_PGS=new double[9];

        a=-.51;
        c=14;
        entrada_creatinina_bajo=sigmoide(a,c,x.length);
        a=4;
        c=20;
        entrada_creatinina_normal=gausiana(a,c,x.length);
        a=.51;
        c=26;
        entrada_creatinina_alto=sigmoide(a,c,x.length);

        a=-.51;
        c=12;
        entrada_albumina_normal=sigmoide(a,c,x.length);
        a=4;
        c=20;
        entrada_albumina_alto=gausiana(a,c,x.length);
        a=.51;
        c=30;
        entrada_albumina_muy_alto=sigmoide(a,c,x.length);

        a=-.51;
        c=5;
        entrada_acido_normal=sigmoide(a,c,x.length);
        a=2;
        c=9;
        entrada_acido_alto=gausiana(a,c,x.length);
        a=.51;
        c=12;
        entrada_acido_muy_alto=sigmoide(a,c,x.length);

        a=-1;
        c=8;
        salida_creatinina_IR=sigmoide(a,c,x.length);
        a=6;
        b=6;
        c=20;
        salida_creatinina_normal=campana(a,b,c,x.length);
        a=1;
        c=28;
        salida_creatinina_IVU=sigmoide(a,c,x.length);

        a=-1;
        c=12;
        salida_albumina_normal=sigmoide(a,c,x.length);
        a=6;
        b=6;
        c=20;
        salida_albumina_limite=campana(a,b,c,x.length);
        a=1;
        c=30;
        salida_albumina_ER=sigmoide(a,c,x.length);

        a=-1;
        c=5;
        salida_acido_normal=sigmoide(a,c,x.length);
        a=2;
        b=6;
        c=10;
        salida_acido_limite=campana(a,b,c,x.length);
        a=2;
        c=14;
        salida_acido_PGS=sigmoide(a,c,x.length);

        int A=creatinina-1;
        int B=albumina-1;
        int C=acido-1;

        //1
        aux=min(entrada_creatinina_normal[A],entrada_albumina_normal[B]);
        FISsalida_creatinina_normal[0]=min(aux,entrada_acido_normal[C]);
        aux=min(entrada_creatinina_normal[A],entrada_albumina_normal[B]);
        FISsalida_albumina_normal[0]=min(aux,entrada_acido_normal[C]);
        aux=min(entrada_creatinina_normal[A],entrada_albumina_normal[B]);
        FISsalida_acido_normal[0]=min(aux,entrada_acido_normal[C]);
        //2
        aux=min(entrada_creatinina_normal[A],entrada_albumina_normal[B]);
        FISsalida_creatinina_normal[1]=min(aux,entrada_acido_alto[C]);
        aux=min(entrada_creatinina_normal[A],entrada_albumina_normal[B]);
        FISsalida_albumina_normal[1]=min(aux,entrada_acido_alto[C]);
        aux=min(entrada_creatinina_normal[A],entrada_albumina_normal[B]);
        FISsalida_acido_limite[0]=min(aux,entrada_acido_alto[C]);
        //3
        aux=min(entrada_creatinina_normal[A],entrada_albumina_normal[B]);
        FISsalida_creatinina_normal[2]=min(aux,entrada_acido_muy_alto[C]);
        aux=min(entrada_creatinina_normal[A],entrada_albumina_normal[B]);
        FISsalida_albumina_normal[2]=min(aux,entrada_acido_muy_alto[C]);
        aux=min(entrada_creatinina_normal[A],entrada_albumina_normal[B]);
        FISsalida_acido_PGS[0]=min(aux,entrada_acido_muy_alto[C]);
        //4
        aux=min(entrada_creatinina_normal[A],entrada_albumina_alto[B]);
        FISsalida_creatinina_normal[3]=min(aux,entrada_acido_normal[C]);
        aux=min(entrada_creatinina_normal[A],entrada_albumina_alto[B]);
        FISsalida_albumina_limite[0]=min(aux,entrada_acido_normal[C]);
        aux=min(entrada_creatinina_normal[A],entrada_albumina_alto[B]);
        FISsalida_acido_normal[1]=min(aux,entrada_acido_normal[C]);
        //5
        aux=min(entrada_creatinina_normal[A],entrada_albumina_alto[B]);
        FISsalida_creatinina_normal[4]=min(aux,entrada_acido_alto[C]);
        aux=min(entrada_creatinina_normal[A],entrada_albumina_alto[B]);
        FISsalida_albumina_limite[1]=min(aux,entrada_acido_alto[C]);
        aux=min(entrada_creatinina_normal[A],entrada_albumina_alto[B]);
        FISsalida_acido_limite[1]=min(aux,entrada_acido_alto[C]);
        //6
        aux=min(entrada_creatinina_normal[A],entrada_albumina_alto[B]);
        FISsalida_creatinina_normal[5]=min(aux,entrada_acido_muy_alto[C]);
        aux=min(entrada_creatinina_normal[A],entrada_albumina_alto[B]);
        FISsalida_albumina_limite[2]=min(aux,entrada_acido_muy_alto[C]);
        aux=min(entrada_creatinina_normal[A],entrada_albumina_alto[B]);
        FISsalida_acido_PGS[1]=min(aux,entrada_acido_muy_alto[C]);
        //7
        aux=min(entrada_creatinina_normal[A],entrada_albumina_muy_alto[B]);
        FISsalida_creatinina_normal[6]=min(aux,entrada_acido_normal[C]);
        aux=min(entrada_creatinina_normal[A],entrada_albumina_muy_alto[B]);
        FISsalida_albumina_ER[0]=min(aux,entrada_acido_normal[C]);
        aux=min(entrada_creatinina_normal[A],entrada_albumina_muy_alto[B]);
        FISsalida_acido_normal[2]=min(aux,entrada_acido_normal[C]);
        //8
        aux=min(entrada_creatinina_normal[A],entrada_albumina_muy_alto[B]);
        FISsalida_creatinina_normal[7]=min(aux,entrada_acido_alto[C]);
        aux=min(entrada_creatinina_normal[A],entrada_albumina_muy_alto[B]);
        FISsalida_albumina_ER[1]=min(aux,entrada_acido_alto[C]);
        aux=min(entrada_creatinina_normal[A],entrada_albumina_muy_alto[B]);
        FISsalida_acido_limite[2]=min(aux,entrada_acido_alto[C]);
        //9
        aux=min(entrada_creatinina_normal[A],entrada_albumina_muy_alto[B]);
        FISsalida_creatinina_normal[8]=min(aux,entrada_acido_muy_alto[C]);
        aux=min(entrada_creatinina_normal[A],entrada_albumina_muy_alto[B]);
        FISsalida_albumina_ER[2]=min(aux,entrada_acido_muy_alto[C]);
        aux=min(entrada_creatinina_normal[A],entrada_albumina_muy_alto[B]);
        FISsalida_acido_PGS[2]=min(aux,entrada_acido_muy_alto[C]);

        //10
        aux=min(entrada_creatinina_bajo[A],entrada_albumina_normal[B]);
        FISsalida_creatinina_IR[0]=min(aux,entrada_acido_normal[C]);
        aux=min(entrada_creatinina_bajo[A],entrada_albumina_normal[B]);
        FISsalida_albumina_normal[3]=min(aux,entrada_acido_normal[C]);
        aux=min(entrada_creatinina_bajo[A],entrada_albumina_normal[B]);
        FISsalida_acido_normal[3]=min(aux,entrada_acido_normal[C]);
        //11
        aux=min(entrada_creatinina_bajo[A],entrada_albumina_normal[B]);
        FISsalida_creatinina_IR[1]=min(aux,entrada_acido_alto[C]);
        aux=min(entrada_creatinina_bajo[A],entrada_albumina_normal[B]);
        FISsalida_albumina_normal[4]=min(aux,entrada_acido_alto[C]);
        aux=min(entrada_creatinina_bajo[A],entrada_albumina_normal[B]);
        FISsalida_acido_limite[3]=min(aux,entrada_acido_alto[C]);
        //12
        aux=min(entrada_creatinina_bajo[A],entrada_albumina_normal[B]);
        FISsalida_creatinina_IR[2]=min(aux,entrada_acido_muy_alto[C]);
        aux=min(entrada_creatinina_bajo[A],entrada_albumina_normal[B]);
        FISsalida_albumina_normal[5]=min(aux,entrada_acido_muy_alto[C]);
        aux=min(entrada_creatinina_bajo[A],entrada_albumina_normal[B]);
        FISsalida_acido_PGS[3]=min(aux,entrada_acido_muy_alto[C]);
        //13
        aux=min(entrada_creatinina_bajo[A],entrada_albumina_alto[B]);
        FISsalida_creatinina_IR[3]=min(aux,entrada_acido_normal[C]);
        aux=min(entrada_creatinina_bajo[A],entrada_albumina_alto[B]);
        FISsalida_albumina_limite[3]=min(aux,entrada_acido_normal[C]);
        aux=min(entrada_creatinina_bajo[A],entrada_albumina_alto[B]);
        FISsalida_acido_normal[4]=min(aux,entrada_acido_normal[C]);
        //14
        aux=min(entrada_creatinina_bajo[A],entrada_albumina_alto[B]);
        FISsalida_creatinina_IR[4]=min(aux,entrada_acido_alto[C]);
        aux=min(entrada_creatinina_bajo[A],entrada_albumina_alto[B]);
        FISsalida_albumina_limite[4]=min(aux,entrada_acido_alto[C]);
        aux=min(entrada_creatinina_bajo[A],entrada_albumina_alto[B]);
        FISsalida_acido_limite[4]=min(aux,entrada_acido_alto[C]);
        //15
        aux=min(entrada_creatinina_bajo[A],entrada_albumina_alto[B]);
        FISsalida_creatinina_IR[5]=min(aux,entrada_acido_muy_alto[C]);
        aux=min(entrada_creatinina_bajo[A],entrada_albumina_alto[B]);
        FISsalida_albumina_limite[5]=min(aux,entrada_acido_muy_alto[C]);
        aux=min(entrada_creatinina_bajo[A],entrada_albumina_alto[B]);
        FISsalida_acido_PGS[4]=min(aux,entrada_acido_muy_alto[C]);
        //16
        aux=min(entrada_creatinina_bajo[A],entrada_albumina_muy_alto[B]);
        FISsalida_creatinina_IR[6]=min(aux,entrada_acido_normal[C]);
        aux=min(entrada_creatinina_bajo[A],entrada_albumina_muy_alto[B]);
        FISsalida_albumina_ER[3]=min(aux,entrada_acido_normal[C]);
        aux=min(entrada_creatinina_bajo[A],entrada_albumina_muy_alto[B]);
        FISsalida_acido_normal[5]=min(aux,entrada_acido_normal[C]);
        //17
        aux=min(entrada_creatinina_bajo[A],entrada_albumina_muy_alto[B]);
        FISsalida_creatinina_IR[7]=min(aux,entrada_acido_alto[C]);
        aux=min(entrada_creatinina_bajo[A],entrada_albumina_muy_alto[B]);
        FISsalida_albumina_ER[4]=min(aux,entrada_acido_alto[C]);
        aux=min(entrada_creatinina_bajo[A],entrada_albumina_muy_alto[B]);
        FISsalida_acido_limite[5]=min(aux,entrada_acido_alto[C]);
        //18
        aux=min(entrada_creatinina_bajo[A],entrada_albumina_muy_alto[B]);
        FISsalida_creatinina_IR[8]=min(aux,entrada_acido_muy_alto[C]);
        aux=min(entrada_creatinina_bajo[A],entrada_albumina_muy_alto[B]);
        FISsalida_albumina_ER[5]=min(aux,entrada_acido_muy_alto[C]);
        aux=min(entrada_creatinina_bajo[A],entrada_albumina_muy_alto[B]);
        FISsalida_acido_PGS[5]=min(aux,entrada_acido_muy_alto[C]);

        //19
        aux=min(entrada_creatinina_bajo[A],entrada_albumina_normal[B]);
        FISsalida_creatinina_IVU[0]=min(aux,entrada_acido_normal[C]);
        aux=min(entrada_creatinina_bajo[A],entrada_albumina_normal[B]);
        FISsalida_albumina_normal[6]=min(aux,entrada_acido_normal[C]);
        aux=min(entrada_creatinina_bajo[A],entrada_albumina_normal[B]);
        FISsalida_acido_normal[6]=min(aux,entrada_acido_normal[C]);
        //20
        aux=min(entrada_creatinina_bajo[A],entrada_albumina_normal[B]);
        FISsalida_creatinina_IVU[1]=min(aux,entrada_acido_alto[C]);
        aux=min(entrada_creatinina_bajo[A],entrada_albumina_normal[B]);
        FISsalida_albumina_normal[7]=min(aux,entrada_acido_alto[C]);
        aux=min(entrada_creatinina_bajo[A],entrada_albumina_normal[B]);
        FISsalida_acido_limite[6]=min(aux,entrada_acido_alto[C]);
        //21
        aux=min(entrada_creatinina_bajo[A],entrada_albumina_normal[B]);
        FISsalida_creatinina_IVU[2]=min(aux,entrada_acido_muy_alto[C]);
        aux=min(entrada_creatinina_bajo[A],entrada_albumina_normal[B]);
        FISsalida_albumina_normal[8]=min(aux,entrada_acido_muy_alto[C]);
        aux=min(entrada_creatinina_bajo[A],entrada_albumina_normal[B]);
        FISsalida_acido_PGS[6]=min(aux,entrada_acido_muy_alto[C]);
        //22
        aux=min(entrada_creatinina_bajo[A],entrada_albumina_alto[B]);
        FISsalida_creatinina_IVU[3]=min(aux,entrada_acido_normal[C]);
        aux=min(entrada_creatinina_bajo[A],entrada_albumina_alto[B]);
        FISsalida_albumina_limite[6]=min(aux,entrada_acido_normal[C]);
        aux=min(entrada_creatinina_bajo[A],entrada_albumina_alto[B]);
        FISsalida_acido_normal[7]=min(aux,entrada_acido_normal[C]);
        //23
        aux=min(entrada_creatinina_bajo[A],entrada_albumina_alto[B]);
        FISsalida_creatinina_IVU[4]=min(aux,entrada_acido_alto[C]);
        aux=min(entrada_creatinina_bajo[A],entrada_albumina_alto[B]);
        FISsalida_albumina_limite[7]=min(aux,entrada_acido_alto[C]);
        aux=min(entrada_creatinina_bajo[A],entrada_albumina_alto[B]);
        FISsalida_acido_limite[7]=min(aux,entrada_acido_alto[C]);
        //24
        aux=min(entrada_creatinina_bajo[A],entrada_albumina_alto[B]);
        FISsalida_creatinina_IVU[5]=min(aux,entrada_acido_muy_alto[C]);
        aux=min(entrada_creatinina_bajo[A],entrada_albumina_alto[B]);
        FISsalida_albumina_limite[8]=min(aux,entrada_acido_muy_alto[C]);
        aux=min(entrada_creatinina_bajo[A],entrada_albumina_alto[B]);
        FISsalida_acido_PGS[7]=min(aux,entrada_acido_muy_alto[C]);
        //25
        aux=min(entrada_creatinina_bajo[A],entrada_albumina_muy_alto[B]);
        FISsalida_creatinina_IVU[6]=min(aux,entrada_acido_normal[C]);
        aux=min(entrada_creatinina_bajo[A],entrada_albumina_muy_alto[B]);
        FISsalida_albumina_ER[6]=min(aux,entrada_acido_normal[C]);
        aux=min(entrada_creatinina_bajo[A],entrada_albumina_muy_alto[B]);
        FISsalida_acido_normal[8]=min(aux,entrada_acido_normal[C]);
        //26
        aux=min(entrada_creatinina_bajo[A],entrada_albumina_muy_alto[B]);
        FISsalida_creatinina_IVU[7]=min(aux,entrada_acido_alto[C]);
        aux=min(entrada_creatinina_bajo[A],entrada_albumina_muy_alto[B]);
        FISsalida_albumina_ER[7]=min(aux,entrada_acido_alto[C]);
        aux=min(entrada_creatinina_bajo[A],entrada_albumina_muy_alto[B]);
        FISsalida_acido_limite[8]=min(aux,entrada_acido_alto[C]);
        //27
        aux=min(entrada_creatinina_bajo[A],entrada_albumina_muy_alto[B]);
        FISsalida_creatinina_IVU[8]=min(aux,entrada_acido_muy_alto[C]);
        aux=min(entrada_creatinina_bajo[A],entrada_albumina_muy_alto[B]);
        FISsalida_albumina_ER[8]=min(aux,entrada_acido_muy_alto[C]);
        aux=min(entrada_creatinina_bajo[A],entrada_albumina_muy_alto[B]);
        FISsalida_acido_PGS[8]=min(aux,entrada_acido_muy_alto[C]);

        double Corte_creatinina_R1=maxArray(FISsalida_creatinina_IR);
        double Corte_creatinina_R2=maxArray(FISsalida_creatinina_normal);
        double Corte_creatinina_R3=maxArray(FISsalida_creatinina_IVU);

        double[] Cr1;
        double[] Cr2;
        double[] Cr3;

        Cr1=new double[41];
        Cr2=new double[41];
        Cr3=new double[41];

        Cr1=salida_creatinina_IR;
        Cr2=salida_creatinina_normal;
        Cr3=salida_creatinina_IVU;

        for (int k=0;k<Cr1.length;k++){
            if(salida_creatinina_IR[k]>=Corte_creatinina_R1){
                Cr1[k]=Corte_creatinina_R1;
            }
            if(salida_creatinina_normal[k]>=Corte_creatinina_R2){
                Cr2[k]=Corte_creatinina_R2;
            }
            if(salida_creatinina_IVU[k]>=Corte_creatinina_R3){
                Cr3[k]=Corte_creatinina_R3;
            }
        }

        double[] casi1;
        double[] CCreatinina;

        CCreatinina=new double[Cr1.length];

        casi1=maxCorte(Cr1,Cr2);
        CCreatinina=maxCorte(casi1,Cr3);

        double Corte_albumina_R1=maxArray(FISsalida_albumina_normal);
        double Corte_albumina_R2=maxArray(FISsalida_albumina_limite);
        double Corte_albumina_R3=maxArray(FISsalida_albumina_ER);

        Cr1=salida_albumina_normal;
        Cr2=salida_albumina_limite;
        Cr3=salida_albumina_ER;

        for (int k=0;k<Cr1.length;k++){
            if(salida_albumina_normal[k]>=Corte_albumina_R1){
                Cr1[k]=Corte_albumina_R1;
            }
            if(salida_albumina_limite[k]>=Corte_albumina_R2){
                Cr2[k]=Corte_albumina_R2;
            }
            if(salida_albumina_ER[k]>=Corte_albumina_R3){
                Cr3[k]=Corte_albumina_R3;
            }
        }

        double[] CAlbumina;

        CAlbumina=new double[Cr1.length];

        casi1=maxCorte(Cr1,Cr2);
        CAlbumina=maxCorte(casi1,Cr3);

        double Corte_acido_R1=maxArray(FISsalida_acido_normal);
        double Corte_acido_R2=maxArray(FISsalida_acido_limite);
        double Corte_acido_R3=maxArray(FISsalida_acido_PGS);

        Cr1=salida_acido_normal;
        Cr2=salida_acido_limite;
        Cr3=salida_acido_PGS;

        for (int k=0;k<Cr1.length;k++){
            if(salida_albumina_normal[k]>=Corte_acido_R1){
                Cr1[k]=Corte_acido_R1;
            }
            if(salida_albumina_limite[k]>=Corte_acido_R2){
                Cr2[k]=Corte_acido_R2;
            }
            if(salida_albumina_ER[k]>=Corte_acido_R3){
                Cr3[k]=Corte_acido_R3;
            }
        }

        double[] CAcido;

        CAcido=new double[Cr1.length];

        casi1=maxCorte(Cr1,Cr2);
        CAcido=maxCorte(casi1,Cr3);

        //Defuzificacion
        double[] numerador;
        double[] denominador;

        double num,den;
        numerador=new double[CCreatinina.length];
        denominador=new double[CCreatinina.length];

        for (int o=0; o<CCreatinina.length;o++){
            numerador[o]=(o)*CCreatinina[o];
            denominador[o]=CCreatinina[o];
        }

        num=sumArray(numerador);
        den=sumArray(denominador);
        CreatininaVD=(num/den);

        for (int o=0; o<CAlbumina.length;o++){
            numerador[o]=(o)*CAlbumina[o];
            denominador[o]=CAlbumina[o];
        }

        num=sumArray(numerador);
        den=sumArray(denominador);
        AlbuminaVD=(num/den);

        for (int o=0; o<CAcido.length;o++){
            numerador[o]=(o)*CAcido[o];
            denominador[o]=CAcido[o];
        }

        num=sumArray(numerador);
        den=sumArray(denominador);
        AcidoVD=(num/den);
    }

    void validarDatos(int leucocitosE, int eritrocitosE, int plaquetasE){
        if(leucocitosE>39){
            creatinina="39";
        }
        if (leucocitosE<0){
            creatinina="1";
        }
        if(eritrocitosE>39){
            albumina="39";
        }
        if (eritrocitosE<0){
            albumina="1";
        }
        if(plaquetasE>39){
            acido="39";
        }
        if(plaquetasE<0){
            acido="1";
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