package com.example.historialclinico.MenuMedico.Pacientes.Analisis;

import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.historialclinico.MenuPaciente.Analisis.Analisis;
import com.example.historialclinico.R;

import java.util.ArrayList;
import java.util.stream.Collectors;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdaptadorAnalisisMedico extends RecyclerView.Adapter<AdaptadorAnalisisMedico.ViewHolderDatosM> {
    ArrayList<Analisis> listaMedicos;
    ArrayList<Analisis> originalItems;

    private AdaptadorAnalisisMedico.OnItemClickListener mListener;

    public interface OnItemClickListener{
        void onItemClick(int position);
    }
    public void setOnItemClickListener(AdaptadorAnalisisMedico.OnItemClickListener lsitener){
        mListener=lsitener;
    }
    public AdaptadorAnalisisMedico(ArrayList<Analisis> listaMedicos) {
        this.listaMedicos = listaMedicos;
        this.originalItems=new ArrayList<>();
        originalItems.addAll(listaMedicos);
    }

    @NonNull
    @Override
    public AdaptadorAnalisisMedico.ViewHolderDatosM onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_analisis_medico,null,false);

        return new AdaptadorAnalisisMedico.ViewHolderDatosM(view, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull AdaptadorAnalisisMedico.ViewHolderDatosM holder, int position) {
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

        public ViewHolderDatosM(@NonNull View itemView, AdaptadorAnalisisMedico.OnItemClickListener listener) {
            super(itemView);
            tvNombreAnalisis=itemView.findViewById(R.id.tvNombreAnalisis);
            tvFechaAnalisis=itemView.findViewById(R.id.tvFechaAnalisis);
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
        }
    }
}
