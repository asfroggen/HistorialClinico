package com.example.historialclinico.MenuPaciente.Analisis;

import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.historialclinico.MenuPaciente.Medicos.AdaptadorMedicos;
import com.example.historialclinico.MenuPaciente.Medicos.Medico;
import com.example.historialclinico.R;

import java.util.ArrayList;
import java.util.stream.Collectors;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdaptadorAnalisis extends RecyclerView.Adapter<AdaptadorAnalisis.ViewHolderDatosM> {
    ArrayList<Analisis> listaMedicos;
    ArrayList<Analisis> originalItems;

    private OnItemClickListener mListener;

    public interface OnItemClickListener{
        void onItemClick(int position);
        void onDeleteClick(int position);
        void onEditClick(int position);
    }
    public void setOnItemClickListener(OnItemClickListener lsitener){
        mListener=lsitener;
    }
    public AdaptadorAnalisis(ArrayList<Analisis> listaMedicos) {
        this.listaMedicos = listaMedicos;
        this.originalItems=new ArrayList<>();
        originalItems.addAll(listaMedicos);
    }

    @NonNull
    @Override
    public AdaptadorAnalisis.ViewHolderDatosM onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_analisis,null,false);

        return new AdaptadorAnalisis.ViewHolderDatosM(view, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull AdaptadorAnalisis.ViewHolderDatosM holder, int position) {
        String fecha="Fecha: "+listaMedicos.get(position).getFechaAnalisis();
        holder.tvNombreAnalisis.setText(listaMedicos.get(position).getNombreAnalisis());
        holder.tvFechaAnalisis.setText(fecha);
        if (listaMedicos.get(position).getTipoAnalisis().equals("biometria")){
            holder.ivImagenAnalisis.setImageResource(R.mipmap.biometria_icon);
        }else if(listaMedicos.get(position).getTipoAnalisis().equals("quimica")){
            holder.ivImagenAnalisis.setImageResource(R.mipmap.quimica_icon);
        }else{
            holder.ivImagenAnalisis.setImageResource(R.mipmap.orina_icon);
        }
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
                ArrayList<Analisis> collect = (ArrayList<Analisis>) listaMedicos.stream()
                        .filter(i -> i.getNombreAnalisis().contains(strSearch))
                        .collect(Collectors.toList());
                listaMedicos.clear();;
                listaMedicos.addAll(collect);
            }else{
                listaMedicos.clear();
                for(Analisis i:originalItems){
                    if(i.getNombreAnalisis().contains(strSearch)){
                        listaMedicos.add(i);
                    }
                }
            }
        }
        notifyDataSetChanged();
    }

    public static class ViewHolderDatosM extends RecyclerView.ViewHolder {

        TextView tvNombreAnalisis,tvFechaAnalisis;
        CircleImageView ivImagenAnalisis;
        ImageView bEditarAnalisis,bEliminarAnalisis;

        public ViewHolderDatosM(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);
            tvNombreAnalisis=itemView.findViewById(R.id.tvNombreAnalisis);
            tvFechaAnalisis=itemView.findViewById(R.id.tvFechaAnalisis);
            bEditarAnalisis=itemView.findViewById(R.id.bEditarAnalisis);
            bEliminarAnalisis=itemView.findViewById(R.id.bEliminarAnalisis);
            ivImagenAnalisis=itemView.findViewById(R.id.ivImagenAnalisis);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(listener!=null){
                        int position=getAdapterPosition();
                        if(position!=RecyclerView.NO_POSITION){
                            listener.onItemClick(position);
                        }
                    }
                }
            });
            bEliminarAnalisis.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(listener!=null){
                        int position=getAdapterPosition();
                        if(position!=RecyclerView.NO_POSITION){
                            listener.onDeleteClick(position);
                        }
                    }
                }
            });
            bEditarAnalisis.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(listener!=null){
                        int position=getAdapterPosition();
                        if(position!=RecyclerView.NO_POSITION){
                            listener.onEditClick(position);
                        }
                    }
                }
            });
        }
    }
}
