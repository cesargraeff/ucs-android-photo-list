package br.ucs.lista.banco;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

import br.ucs.lista.model.Foto;

public class DBSQLiteHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "FotosDB";
    private static final String TABELA_FOTOS = "fotos";

    private static final String ID = "id";
    private static final String TITULO = "titulo";
    private static final String DESCRICAO = "descricao";
    private static final String PATH = "path";

    private static final String[] COLUNAS = {ID, TITULO, DESCRICAO, PATH};

    public DBSQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE "+TABELA_FOTOS+" ("+
                ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"+
                TITULO+" TEXT,"+
                DESCRICAO+" TEXT,"+
                PATH+" TEXT)";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        this.onCreate(db);
    }

    public void addFoto(Foto foto) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TITULO, foto.getTitulo());
        values.put(DESCRICAO, foto.getDesc());
        values.put(PATH, foto.getPath());
        db.insert(TABELA_FOTOS, null, values);
        db.close();
    }

    public Foto getFoto(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABELA_FOTOS, // a. tabela
                COLUNAS, // b. colunas
                " id = ?", // c. colunas para comparar
                new String[] { String.valueOf(id) }, // d. parâmetros
                null, // e. group by
                null, // f. having
                null, // g. order by
                null); // h. limit
        if (cursor == null) {
            return null;
        } else {
            cursor.moveToFirst();
            Foto foto = cursorToFoto(cursor);
            return foto;
        }
    }

    private Foto cursorToFoto(Cursor cursor) {
        Foto foto = new Foto();
        foto.setId(Integer.parseInt(cursor.getString(0)));
        foto.setTitulo(cursor.getString(1));
        foto.setDesc(cursor.getString(2));
        foto.setPath(cursor.getString(3));
        return foto;
    }

    public ArrayList<Foto> getAllFotos() {
        ArrayList<Foto> listaFotos = new ArrayList<Foto>();
        String query = "SELECT * FROM " + TABELA_FOTOS + " ORDER BY " + TITULO;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                Foto foto = cursorToFoto(cursor);
                listaFotos.add(foto);
            } while (cursor.moveToNext());
        }
        return listaFotos;
    }

    public ArrayList<Foto> getAllFotos(String search) {
        ArrayList<Foto> listaFotos = new ArrayList<Foto>();
        String query = "SELECT * FROM " + TABELA_FOTOS + " WHERE " +TITULO+ " LIKE '%" +search+ "%' ORDER BY " + TITULO;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                Foto foto = cursorToFoto(cursor);
                listaFotos.add(foto);
            } while (cursor.moveToNext());
        }
        return listaFotos;
    }

    public int updateFoto(Foto foto) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TITULO, foto.getTitulo());
        values.put(DESCRICAO, foto.getDesc());
        values.put(PATH, foto.getPath());
        int i = db.update(TABELA_FOTOS,
                values,
                ID+" = ?",
                new String[] { String.valueOf(foto.getId()) });
        db.close();
        return i;
    }

    public int deleteFoto(Foto foto) {
        SQLiteDatabase db = this.getWritableDatabase();
        int i = db.delete(TABELA_FOTOS,
                ID+" = ?",
                new String[] { String.valueOf(foto.getId()) });
        db.close();
        return i;
    }
}
