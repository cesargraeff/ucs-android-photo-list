package br.ucs.lista.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import br.ucs.lista.R;
import br.ucs.lista.adapter.Adapter;
import br.ucs.lista.banco.DBSQLiteHelper;
import br.ucs.lista.model.Foto;

public class ScrollingActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ImageButton btnAdd;
    private EditText busca;

    private final int PERMISSAO_REQUEST = 2;
    private final int CAMERA = 3;
    private final int EDIT = 10;
    private final int DELETE = 11;

    private final int ACTION_DELETE = 100;
    private final int ACTION_SAVE = 200;

    private AtomicInteger atomicInteger = new AtomicInteger(100);

    private DBSQLiteHelper db;

    private List<Foto> fotoList;
    private Adapter adapter;
    private File file = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);

        db = new DBSQLiteHelper(this);

        btnAdd = findViewById(R.id.btnAdd);
        recyclerView = findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        busca = findViewById(R.id.busca);

        verificarPerm();
        addListeners();

        fotoList = db.getAllFotos();
        adapter = new Adapter(fotoList);

        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void addListeners() {
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent takePictureIntent = new
                        Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    try {
                        file = criaArquivo();
                    } catch (IOException ex) {
                        Toast.makeText(v.getContext(),"Any error",Toast.LENGTH_LONG).show();
                    }
                    if (file != null) {
                        Uri photoURI = FileProvider.getUriForFile(getBaseContext(),
                                getBaseContext().getApplicationContext().getPackageName() +
                                        ".provider", file);
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                        startActivityForResult(takePictureIntent, CAMERA);
                    }
                }
            }
        });
       busca.setOnKeyListener(new View.OnKeyListener() {
           @Override
           public boolean onKey(View v, int keyCode, KeyEvent event) {
               EditText editText = (EditText) v;
               if (keyCode == EditorInfo.IME_ACTION_SEARCH ||
                       keyCode == EditorInfo.IME_ACTION_DONE ||
                       event.getAction() == KeyEvent.ACTION_DOWN &&
                               event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {

                   updateList(editText.getText().toString());
               }

                   return false;
           }
       });
    }

    private void updateList(String s) {
        if(StringUtils.trimToNull(s)!= null){
            fotoList.removeIf(e -> e.getId() != null);
            fotoList.addAll(db.getAllFotos(s));
            adapter.notifyDataSetChanged();
        }
        else {
            this.updateList();
        }
    }

    private void updateList(){
        fotoList.removeIf(e -> e.getId() != null);
        fotoList.addAll(db.getAllFotos());
        adapter.notifyDataSetChanged();
    }

    private void verificarPerm() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        PERMISSAO_REQUEST);
            }
        }

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        PERMISSAO_REQUEST);
            }
        }
    }

    private File criaArquivo() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File imagem = new File(this.getFilesDir() + File.separator + "LISTA_" + timeStamp + ".jpg");
        return imagem;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == CAMERA) {
            sendBroadcast(new Intent(
                    Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                    Uri.fromFile(file))
            );

            Toast.makeText(this, file.getAbsolutePath(), Toast.LENGTH_LONG).show();

            Foto foto = new Foto(file.getName());
            Intent intent = new Intent(this, EditActivity.class);
            intent.putExtra("foto", foto);
            startActivityForResult(intent, EDIT);

        }

        if (resultCode == RESULT_OK && requestCode == EDIT) {
            int type = data.getExtras().getInt("type", 0);
            Foto foto = (Foto) data.getExtras().get("foto");

            switch (type){
                case  ACTION_SAVE:

                    if(foto.getId() == null){
                        // ADD
                        db.addFoto(foto);
                        this.updateList();
                    }
                    else {
                        db.updateFoto(foto);
                        Optional<Foto> f = fotoList.stream().filter(e -> e.getId().equals(foto.getId())).findFirst();
                        f.ifPresent(e -> {
                            e.setTitulo(foto.getTitulo());
                            e.setDesc(foto.getDesc());
                        });
                        adapter.notifyDataSetChanged();
                    }

                    Toast.makeText(this,"Operação realizada con sucesso",Toast.LENGTH_LONG).show();

                    break;
                case ACTION_DELETE:

                    File file = new File(this.getFilesDir(), foto.getPath());
                    try {
                        if (file.exists()) {
                            file.getCanonicalFile().delete();
                            getApplicationContext().deleteFile(file.getName());
                        }
                    } catch (IOException e) {
                        Toast.makeText(this, "Falha ao remover arquivo", Toast.LENGTH_LONG).show();
                    }

                    db.deleteFoto(foto);
                    fotoList.removeIf(e -> e.getId().equals(foto.getId()));
                    adapter.notifyDataSetChanged();

                    Toast.makeText(this,"Operação realizada con sucesso",Toast.LENGTH_LONG).show();
                    break;
                default:
            }
        }
    }
}
