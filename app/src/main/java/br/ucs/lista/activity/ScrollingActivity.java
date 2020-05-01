package br.ucs.lista.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.ucs.lista.R;
import br.ucs.lista.adapter.Adapter;
import br.ucs.lista.model.Foto;

public class ScrollingActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    ImageButton btnAdd;
    File file = null;
    private final int GALERIA_IMAGENS = 1;
    private final int PERMISSAO_REQUEST = 2;
    private final int CAMERA = 3;
    private static int EDIT = 10;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);

        btnAdd = findViewById(R.id.btnAdd);
        recyclerView = findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        verificarPerm();
        addListeners();

        final List<Foto> fotoList = mockList();
        final Adapter adapter = new Adapter(fotoList);

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
    }

    private List<Foto> mockList() {
        List<Foto> fotoList = new ArrayList<>();
        fotoList.add(new Foto("10","10"));
        fotoList.add(new Foto("11", "11"));
        fotoList.add(new Foto("11", "11"));
        fotoList.add(new Foto("13", "13"));
        fotoList.add(new Foto("14", "14"));
        fotoList.add(new Foto("15", "15"));
        fotoList.add(new Foto("16", "16"));
        fotoList.add(new Foto("17", "17"));
        fotoList.add(new Foto("18", "18"));
        fotoList.add(new Foto("19", "19"));
        fotoList.add(new Foto("20", "20"));
        fotoList.add(new Foto("21", "21"));
        return  fotoList;
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
        if (resultCode == RESULT_OK && requestCode == GALERIA_IMAGENS) {
            Uri selectedImage = data.getData();
            String[] filePath = {MediaStore.Images.Media.DATA};
            Cursor c = getContentResolver().query(selectedImage, filePath, null,
                    null, null);
            c.moveToFirst();
            int columnIndex = c.getColumnIndex(filePath[0]);
            String picturePath = c.getString(columnIndex);
            c.close();
            file = new File(picturePath);
        }
        if (resultCode == RESULT_OK && requestCode == CAMERA) {
            sendBroadcast(new Intent(
                    Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                    Uri.fromFile(file))
            );

            Toast.makeText(this, file.getAbsolutePath(), Toast.LENGTH_LONG).show();

        }
    }
}
