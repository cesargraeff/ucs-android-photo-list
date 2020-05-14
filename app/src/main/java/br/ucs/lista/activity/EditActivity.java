package br.ucs.lista.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputEditText;

import org.apache.commons.lang3.StringUtils;

import java.io.File;

import br.ucs.lista.R;
import br.ucs.lista.banco.DBSQLiteHelper;
import br.ucs.lista.model.Foto;

public class EditActivity extends AppCompatActivity {

    private ImageView img;
    private TextInputEditText txtTitulo;
    private EditText textDesc;
    private Button btnSalvar;
    private Button btnExcluir;
    private Foto foto;

    private final int ACTION_DELETE = 100;
    private final int ACTION_SAVE = 200;

    private DBSQLiteHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        db = new DBSQLiteHelper(this);

        img = findViewById(R.id.img);
        txtTitulo = findViewById(R.id.txtTitulo);
        textDesc = findViewById(R.id.txtDesc);
        btnExcluir = findViewById(R.id.btnExcluir);
        btnSalvar = findViewById(R.id.btnSalvar);

        addListeners();

        Intent intent = getIntent();
        foto = (Foto) intent.getExtras().get("foto");
        setData(foto);
    }

    private void addListeners() {
        btnExcluir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();

                foto.setDesc(textDesc.getText().toString());
                foto.setTitulo(txtTitulo.getText().toString());

                intent.putExtra("foto", foto);
                intent.putExtra("type", ACTION_DELETE);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (StringUtils.trimToNull(txtTitulo.getText().toString()) == null ||
                        StringUtils.trimToNull(textDesc.getText().toString()) == null){
                    Toast.makeText(v.getContext(), "Favor informar todos os dados", Toast.LENGTH_LONG).show();
                }
                else{
                    Intent intent = new Intent();

                    foto.setDesc(textDesc.getText().toString());
                    foto.setTitulo(txtTitulo.getText().toString());

                    intent.putExtra("foto", foto);
                    intent.putExtra("type", ACTION_SAVE);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        });

;
    }

    private void setData(Foto foto) {
        txtTitulo.setText(foto.getTitulo());
        textDesc.setText(foto.getDesc());
        Glide.with(this)
                .load(Uri.fromFile(new File(this.getFilesDir(), foto.getPath())))
                .centerCrop() //
                .into(img);
    }
}
