package br.ucs.lista.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.io.File;

import br.ucs.lista.R;
import br.ucs.lista.model.Foto;

public class EditActivity extends AppCompatActivity {

    private ImageView img;
    private EditText txtTitulo;
    private EditText textDesc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        img = findViewById(R.id.img);
        txtTitulo = findViewById(R.id.txtTitulo);
        textDesc = findViewById(R.id.txtDesc);

        Intent intent = getIntent();

        Foto foto = (Foto) intent.getExtras().get("foto");
        txtTitulo.setText(foto.getTitulo());
        textDesc.setText(foto.getDesc());

        Glide.with(this)
                .load(Uri.fromFile(new File(this.getFilesDir(), "LISTA_20200430_213507.jpg")))
                .centerCrop() //
                .into(img);
    }
}
