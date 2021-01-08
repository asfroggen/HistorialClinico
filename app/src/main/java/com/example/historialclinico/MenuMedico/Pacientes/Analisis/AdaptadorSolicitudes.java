package com.example.historialclinico.MenuMedico.Pacientes.Analisis;

import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.historialclinico.MenuMedico.Pacientes.Paciente;
import com.example.historialclinico.R;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.stream.Collectors;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdaptadorSolicitudes extends RecyclerView.Adapter<AdaptadorSolicitudes.ViewHolderDatosM> {

    ArrayList<Paciente> listaMedicos;
    ArrayList<Paciente> originalItems;
    String fechaNacimiento;

    private AdaptadorSolicitudes.OnItemClickListener mListener;

    public interface OnItemClickListener{
        void OnItemClick(int position);
    }
    public void setOnItemClickListener(AdaptadorSolicitudes.OnItemClickListener listener){
        mListener=listener;
    }
    public AdaptadorSolicitudes(ArrayList<Paciente> listaMedicos) {
        this.listaMedicos = listaMedicos;
        this.originalItems=new ArrayList<>();
        originalItems.addAll(listaMedicos);
    }

    @NonNull
    @Override
    public AdaptadorSolicitudes.ViewHolderDatosM onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_medico,null,false);

        return new AdaptadorSolicitudes.ViewHolderDatosM(view,mListener);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull AdaptadorSolicitudes.ViewHolderDatosM holder, int position) {
        fechaNacimiento=listaMedicos.get(position).getFechaNacimientoPacientes();
        String edad="Edad: "+obtenerEdad().getYears()+" años y "+obtenerEdad().getMonths()+" meses";
        String sexo="Sexo: "+listaMedicos.get(position).getSexoPacientes();
        holder.tvNombreMedico.setText(listaMedicos.get(position).getNombrePacientes());
        holder.tvInfoMedico.setText(edad);
        if (listaMedicos.get(position).getImagenPacientes()!=null){
            holder.ivImagenMedico.setImageBitmap(listaMedicos.get(position).getImagenPacientes());
        }else{
            holder.ivImagenMedico.setImageResource(R.mipmap.user_icon_dark);
        }
        holder.tvUbicacionMedico.setText(sexo);
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    private Period obtenerEdad(){
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate fechaNac = LocalDate.parse(fechaNacimiento, fmt);
        LocalDate ahora = LocalDate.now();

        Period periodo = Period.between(fechaNac, ahora);
        return periodo;
    }

    @Override
    public int getItemCount() {
        return listaMedicos.size();
    }

    public void filter(final String strSearch){
        if(strSearch.length()==0){
            listaMedicos.clear();
            listaMedicos.addAll(originalItems);
        }else{
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
                ArrayList<Paciente> collect = (ArrayList<Paciente>) listaMedicos.stream()
                        .filter(i -> i.getNombrePacientes().contains(strSearch))
                        .collect(Collectors.toList());
                listaMedicos.clear();;
                listaMedicos.addAll(collect);
            }else{
                listaMedicos.clear();
                for(Paciente i:originalItems){
                    if(i.getNombrePacientes().contains(strSearch)){
                        listaMedicos.add(i);
                    }
                }
            }
        }
        notifyDataSetChanged();
    }

    public static class ViewHolderDatosM extends RecyclerView.ViewHolder {

        TextView tvNombreMedico,tvInfoMedico,tvUbicacionMedico;
        CircleImageView ivImagenMedico;
        ImageView ivBorrarPaciente;

        public ViewHolderDatosM(@NonNull View itemView, AdaptadorSolicitudes.OnItemClickListener listener) {
            super(itemView);
            tvNombreMedico=itemView.findViewById(R.id.tvNombreMedico);
            tvInfoMedico=itemView.findViewById(R.id.tvEspecialidadMedico);
            tvUbicacionMedico=itemView.findViewById(R.id.tvUbicacionMedico);
            ivImagenMedico=itemView.findViewById(R.id.ivImagenMedico);
            ivBorrarPaciente=itemView.findViewById(R.id.ivBorrarPaciente);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(listener!=null){
                        int position=getAdapterPosition();
                        if(position!=RecyclerView.NO_POSITION){
                            listener.OnItemClick(position);
                        }
                    }
                }
            });

        }
    }

}
