package br.ucs.lista.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import br.ucs.lista.R;
import br.ucs.lista.model.Foto;
import lombok.Setter;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {


    private List<Foto> fotoList;
    

    public Adapter(List<Foto> fotoList) {
        this.fotoList = fotoList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_layout,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        viewHolder.setData(fotoList.get(position));
    }

    @Override
    public int getItemCount() {
        return fotoList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView txtTitulo;

        private ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            txtTitulo = itemView.findViewById(R.id.txtTitulo);
        }

        @Override
        public void onClick(View v) {
            Toast.makeText(v.getContext(),"Você selecionou " + fotoList.get(getLayoutPosition()).getTitulo(),Toast.LENGTH_LONG).show();
        }

        public void setData(Foto foto) {
            txtTitulo.setText(foto.getTitulo());
        }
    }
}
