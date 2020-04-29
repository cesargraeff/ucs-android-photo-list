package br.ucs.lista.activity;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

import br.ucs.lista.R;
import br.ucs.lista.adapter.Adapter;
import br.ucs.lista.model.Foto;

public class ScrollingActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);


        recyclerView = findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        final List<Foto> fotoList = new ArrayList<>();
        fotoList.add(new Foto("10"));
        fotoList.add(new Foto("11"));
        fotoList.add(new Foto("12"));
        fotoList.add(new Foto("13"));
        fotoList.add(new Foto("14"));
        fotoList.add(new Foto("15"));
        fotoList.add(new Foto("16"));
        fotoList.add(new Foto("17"));
        fotoList.add(new Foto("18"));
        fotoList.add(new Foto("19"));
        fotoList.add(new Foto("20"));
        fotoList.add(new Foto("21"));

        final Adapter adapter = new Adapter(fotoList);

        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
}
