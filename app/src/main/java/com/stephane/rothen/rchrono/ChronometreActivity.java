package com.stephane.rothen.rchrono;

import android.content.ContentResolver;
import android.database.Cursor;
import android.database.sqlite.SQLiteCursorDriver;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQuery;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import java.util.ArrayList;



public class ChronometreActivity extends ActionBarActivity {


    private Chronometre c;
    private ListView lv;
    private ArrayList<Morceau> listMusic;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chronometre);
        c = new Chronometre(getApplication());
        listMusic = new ArrayList<>();

        ((Button)findViewById(R.id.btnScanUri)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanMusiques();
            }
        });
    }

    private void scanMusiques()
    {
        ContentResolver musicResolver = getContentResolver();
        Uri musicUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor musicCursor = musicResolver.query(musicUri,null,null,null,null);

        if(musicCursor!=null && musicCursor.moveToFirst())
        {
            int titleColumn = musicCursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            int artistColumn = musicCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
            int idColumn = musicCursor.getColumnIndex(MediaStore.Audio.Media._ID);
            do{
                long thisId = musicCursor.getLong(idColumn);
                String thisTitle = musicCursor.getString(titleColumn);
                String thisArtist = musicCursor.getString(artistColumn);
                listMusic.add(new Morceau(thisId,thisTitle,thisArtist));
            }while(musicCursor.moveToNext());



        }
        Playlist pl = new Playlist();
        for(Morceau m : listMusic)
            pl.ajouterMorceau(m);

        Exercice e = new Exercice("test","test2",15,pl);

        c.getLibrairieExercice().ajoutExercice(e);
        c.saveLibrairieExercice();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_chronometre, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}
