package com.example.historialclinico.MenuPaciente.Medicos;

import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.historialclinico.R;

import java.util.ArrayList;
import java.util.stream.Collectors;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdaptadorMedicos extends RecyclerView.Adapter<AdaptadorMedicos.ViewHolderDatosM> {

    ArrayList<Medico> listaMedicos;
    ArrayList<Medico> originalItems;

    private OnItemClickListener mListener;

    public interface OnItemClickListener{
         void OnItemClick(int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener){
        mListener=listener;
    }
    public AdaptadorMedicos(ArrayList<Medico> listaMedicos) {
        this.listaMedicos = listaMedicos;
        this.originalItems=new ArrayList<>();
        originalItems.addAll(listaMedicos);
    }

    @NonNull
    @Override
    public AdaptadorMedicos.ViewHolderDatosM onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_medico,null,false);

        return new ViewHolderDatosM(view,mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull AdaptadorMedicos.ViewHolderDatosM holder, int position) {
        String especialidad="Especialidad: "+listaMedicos.get(position).getEspecialidadMedico();
        String ubicacion="Ubicación: "+listaMedicos.get(position).getUbicacionMedico();

        holder.tvNombreMedico.setText(listaMedicos.get(position).getNombreMedico());
        holder.tvInfoMedico.setText(especialidad);
        if (listaMedicos.get(position).getImagenMedico()!=null){
            holder.ivImagenMedico.setImageBitmap(listaMedicos.get(position).getImagenMedico());
        }else{
            holder.ivImagenMedico.setImageResource(R.mipmap.user_icon_dark);
        }
        holder.tvUbicacionMedico.setText(ubicacion);
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
                ArrayList<Medico> collect = (ArrayList<Medico>) listaMedicos.stream()
                        .filter(i -> i.getNombreMedico().contains(strSearch))
                        .collect(Collectors.toList());
                listaMedicos.clear();;
                listaMedicos.addAll(collect);
            }else{
                listaMedicos.clear();
                for(Medico i:originalItems){
                    if(i.getNombreMedico().contains(strSearch)){
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
        Button bAgregarMedico;

        public ViewHolderDatosM(@NonNull View itemView,OnItemClickListener listener) {
            super(itemView);
            tvNombreMedico=itemView.findViewById(R.id.tvNombreMedico);
            tvInfoMedico=itemView.findViewById(R.id.tvEspecialidadMedico);
            tvUbicacionMedico=itemView.findViewById(R.id.tvUbicacionMedico);
            ivImagenMedico=itemView.findViewById(R.id.ivImagenMedico);

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
