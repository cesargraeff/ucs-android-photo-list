package br.ucs.lista.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputEditText;

import java.io.File;
import java.util.List;

import br.ucs.lista.R;
import br.ucs.lista.activity.EditActivity;
import br.ucs.lista.model.Foto;
import lombok.Setter;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {


    private List<Foto> fotoList;
    private Context context;
    private static int EDIT = 10;


    public Adapter(List<Foto> fotoList) {
        this.fotoList = fotoList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_layout, viewGroup, false);
        context = viewGroup.getContext();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        viewHolder.setData(fotoList.get(position), this.context);
    }

    @Override
    public int getItemCount() {
        return fotoList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView txtTitulo;
        private TextView txtDesc;
        private ImageView img;

        private ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            txtTitulo = itemView.findViewById(R.id.txtTitulo);
            txtDesc = itemView.findViewById(R.id.txtDesc);
            img = itemView.findViewById(R.id.img);
        }

        @Override
        public void onClick(View v) {
            Context context = v.getContext();
            Intent intent = new Intent(context, EditActivity.class);
            Foto foto = fotoList.get(getLayoutPosition());
            intent.putExtra("foto", foto);
            ((Activity) context).startActivityForResult(intent, EDIT);

        }

        public void setData(Foto foto, Context context) {
            txtTitulo.setText(foto.getTitulo());
            txtDesc.setText(foto.getDesc());
            try {
                Glide.with(context)
                        .load(Uri.fromFile(new File(context.getFilesDir(), foto.getPath())))
                        .into(img);
            } catch (Exception e) {
                Glide.with(context)
                        .load(R.drawable.ic_image_not_found)
                        .into(img);
            }
        }
    }
}
