package com.example.historialclinico.MenuMedico.Home;

import android.graphics.Typeface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.historialclinico.R;

import org.w3c.dom.Text;

import java.text.DecimalFormat;

public class HomeMedicoFragment extends Fragment {

    public HomeMedicoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    TextView tvIMC, tvNivelPeso, tvMale, tvFemale;
    EditText tvAltura, tvPeso;
    ImageView ivMale, ivFemale, plusAltura, plusPeso, minusAltura, minusPeso;
    Button bCalcular;

    double altura, peso;
    String sexo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_home_medico, container, false);

        tvAltura=view.findViewById(R.id.tvAltura);
        tvPeso=view.findViewById(R.id.tvPeso);
        tvIMC=view.findViewById(R.id.tvIMC);
        tvNivelPeso=view.findViewById(R.id.tvNivelPeso);
        tvMale=view.findViewById(R.id.tvMale);
        tvFemale=view.findViewById(R.id.tvFemale);

        ivMale=view.findViewById(R.id.ivMale);
        ivFemale=view.findViewById(R.id.ivFemale);
        plusAltura=view.findViewById(R.id.plusAltura);
        plusPeso=view.findViewById(R.id.plusPeso);
        minusAltura=view.findViewById(R.id.minusAltura);
        minusPeso=view.findViewById(R.id.minusPeso);

        bCalcular=view.findViewById(R.id.bCalcular);

        iniciarBotones();
        linkBotones();

        return view;
    }

    void calcularIMC(){
        double imc=peso/(altura*altura);
        tvIMC.setText(new DecimalFormat("#.##").format(imc));
            if (imc<18.5){
                tvNivelPeso.setText("Del debajo del peso normal");
            } else if (18.5<imc && imc<24.9) {
                tvNivelPeso.setText("En el peso ideal");
            }else if (25<imc && imc<29.9){
                tvNivelPeso.setText("Encima del peso ideal: Sobrepeso");
            }else{
                tvNivelPeso.setText("Encima del peso ideal: Obesidad");
            }
    }

    void linkBotones(){
        altura= Double.parseDouble(tvAltura.getText().toString());
        peso= Double.parseDouble(tvPeso.getText().toString());
    }

    void iniciarBotones(){
        plusAltura.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                altura= Double.parseDouble(tvAltura.getText().toString());
                altura+=0.01;
                tvAltura.setText(new DecimalFormat("#.##").format(altura));
            }
        });

        plusPeso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                peso= Double.parseDouble(tvPeso.getText().toString());
                peso+=0.1;
                tvPeso.setText(new DecimalFormat("#.#").format(peso));
            }
        });

        minusAltura.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                altura= Double.parseDouble(tvAltura.getText().toString());
                altura-=0.01;
                tvAltura.setText(new DecimalFormat("#.##").format(altura));
            }
        });

        minusPeso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                peso= Double.parseDouble(tvPeso.getText().toString());
                peso-=0.1;
                tvPeso.setText(new DecimalFormat("#.#").format(peso));
            }
        });

        ivFemale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sexo="female";
                ivFemale.setImageResource(R.mipmap.female);
                ivMale.setImageResource(R.mipmap.male_grau);
                tvFemale.setTypeface(Typeface.DEFAULT_BOLD);
                tvMale.setTypeface(Typeface.DEFAULT);
            }
        });

        ivMale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sexo="male";
                ivFemale.setImageResource(R.mipmap.female_grau);
                ivMale.setImageResource(R.mipmap.male);
                tvMale.setTypeface(Typeface.DEFAULT_BOLD);
                tvFemale.setTypeface(Typeface.DEFAULT);
            }
        });

        bCalcular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calcularIMC();
            }
        });
    }
}