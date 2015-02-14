package com.stephane.rothen.rchrono;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.sql.SQLException;

/**
 * Classe controleur gérant la base de donnée interne
 *
 * Created by Stéphane on 14/02/2015.
 */
public class DatabaseHandler extends SQLiteOpenHelper {

    public static final String DB_NAME = "RCHRONO.db";
    public static final int DB_VERSION = 1;

    /**
     * Table Exercice
     */
    public static final String EXERCICE_ID = "ID_EXERCICE";
    public static final String EXERCICE_NOM = "NOM";
    public static final String EXERCICE_DESCRIPTION = "DESCRIPTION";
    public static final String EXERCICE_DUREEPARDEFAUT = "DUREEPARDEFAUT";
    public static final String EXERCICE = "EXERCICE";
    public static final String EXERCICE_TABLE_CREATE = "CREATE TABLE " + EXERCICE + " (" + EXERCICE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                                                                                            EXERCICE_NOM + " TEXT, "+
                                                                                            EXERCICE_DESCRIPTION + " TEXT, "+
                                                                                            EXERCICE_DUREEPARDEFAUT + " INTEGER);";

    /**
     * Table Sequence
     */
    public static final String SEQUENCE_ID = "ID_SEQUENCE";
    public static final String SEQUENCE_NOM = "NOM";
    public static final String SEQUENCE_NOMBREREPETITON = "NOMBREREPETITON";
    public static final String SEQUENCE_SYNTHESEVOCALE = "SYNTHESEVOCALE";
    public static final String SEQUENCE= "SEQUENCE";
    public static final String SEQUENCE_TABLE_CREATE = "CREATE TABLE " + SEQUENCE + "(" + SEQUENCE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                                                                                            SEQUENCE_NOM+ " TEXT, "+
                                                                                            SEQUENCE_NOMBREREPETITON + " INTEGER,"+
                                                                                            SEQUENCE_SYNTHESEVOCALE + " INTEGER);";

    /**
     * Table ListeSequence
     */
    public static final String LSTSEQUENCES = "LISTESEQUENCE";
    public static final String LSTSEQUENCES_ID_LISTEQUENCES = "ID_LISTESEQUENCE";
    public static final String LSTSEQUENCES_ID_SEQUENCE = "ID_SEQUENCE";
    public static final String LSTSEQUENCES_TABLE_CREATE =  "CREATE TABLE " + LSTSEQUENCES + "(" + LSTSEQUENCES_ID_LISTEQUENCES + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                                                                                                    LSTSEQUENCES_ID_SEQUENCE + " INTEGER);";

    /**
     * Table Morceau
     */
    public static final String MORCEAU = "MORCEAU";
    public static final String MORCEAU_ID = "ID_MORCEAU";
    public static final String MORCEAU_ID_ELEMENTSEQUENCE = "ID_ELEMENTSEQUENCE";
    public static final String MORCEAU_TITRE = "TITRE";
    public static final String MORCEAU_CHEMIN = "CHEMIN";
    public static final String MORCEAU_TABLE_CREATE = "CREATE TABLE "+ MORCEAU + "(" + MORCEAU_ID+ " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                                                                                        MORCEAU_ID_ELEMENTSEQUENCE + " INTEGER,"+
                                                                                        MORCEAU_TITRE + " TEXT, "+
                                                                                        MORCEAU_CHEMIN + " TEXT); ";



    public static final String ELEMENTSEQUENCE = "ELEMENTSEQUENCE";
    public static final String ELEMENTSEQUENCE_ID = "ID_ELEMENTSEQUENCE";
    public static final String ELEMENTSEQUENCE_ID_EXERCICE = "ID_EXERCICE";
    public static final String ELEMENTSEQUENCE_ID_SEQUENCE = "ID_SEQUENCE";
    public static final String ELEMENTSEQUENCE_POSITION = "POSITION";
    public static final String ELEMENTSEQUENCE_SYNTHESEVOCALE ="SYNTHESEVOCALE";
    public static final String ELEMENTSEQUENCE_DUREE ="DUREE";
    public static final String ELEMENTSEQUENCE_NOTIFICATION ="NOTIFICATION";
    public static final String ELEMENTSEQUENCE_FICHIERAUDIONOTIFICATION ="FICHIERAUDIONOTIFICATION";
    public static final String ELEMENTSEQUENCE_TABLE_CREATE ="CREATE TABLE " + ELEMENTSEQUENCE + "(" + ELEMENTSEQUENCE_ID+ " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                                                                                                        ELEMENTSEQUENCE_ID_EXERCICE + " INTEGER,"+
                                                                                                        ELEMENTSEQUENCE_ID_SEQUENCE + " INTEGER,"+
                                                                                                        ELEMENTSEQUENCE_POSITION + " INTEGER,"+
                                                                                                        ELEMENTSEQUENCE_SYNTHESEVOCALE + " INTEGER,"+
                                                                                                        ELEMENTSEQUENCE_DUREE + " INTEGER,"+
                                                                                                        ELEMENTSEQUENCE_NOTIFICATION + " INTEGER,"+
                                                                                                        ELEMENTSEQUENCE_FICHIERAUDIONOTIFICATION + " TEXT);";




    public DatabaseHandler(Context context)
    {
        super (context,DB_NAME,null,DB_VERSION);

    }



    public DatabaseHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);

    }

    public DatabaseHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        try {
            db.execSQL(EXERCICE_TABLE_CREATE);
        }catch(SQLiteException e){
            Log.d("SQL", "Erreur exercice :" + e.toString());
        }
        try {
            db.execSQL(SEQUENCE_TABLE_CREATE);
        }catch(SQLiteException e){
            Log.d("SQL", "Erreur sequence :" + e.toString());
        }
        try {
            db.execSQL(LSTSEQUENCES_TABLE_CREATE);
        }catch(SQLiteException e){
            Log.d("SQL", "Erreur ListeSequence :" + e.toString());
        }
        try {
            db.execSQL(MORCEAU_TABLE_CREATE);
        }catch(SQLiteException e){
            Log.d("SQL", "Erreur morceau :" + e.toString());
        }
        try {
            db.execSQL(ELEMENTSEQUENCE_TABLE_CREATE);
        }catch(SQLiteException e){
            Log.d("SQL", "Erreur ElementSequence :" + e.toString());
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
