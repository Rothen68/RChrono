package com.stephane.rothen.rchrono;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Classe controleur permettant d'ouvrir et de fermer la base de donnée
 *
 * Created by Stéphane on 17/02/2015.
 */
public class DAOBase {
    /**
     * base de donnée SQLite
     */
    protected SQLiteDatabase m_db=null;
    /**
     * Gestionnaire de connexion à la base de donnée
     */
    protected DatabaseHelper m_helper=null;


    /**
     * Constructeur
     *
     * @param context: Context de l'application
     */
    public DAOBase(Context context) {
        m_helper=new DatabaseHelper(context);
    }

    /**
     * Ouvre la base de donnée et la renvois
     * @return : base de donnée SQLite ouverte
     */
    public SQLiteDatabase open()
    {
        m_db=m_helper.getReadableDatabase();
        return m_db;
    }

    /**
     * Ferme la base de donnée
     */
    public void close()
    {
        if(m_db!=null)
            m_db.close();
    }

    /**
     * Renvois la base de donnée
     * @return
     */
    public SQLiteDatabase getDb()
    {
        return m_db;
    }


    /**
     * Renvois la séquence à la position donnée
     * @param position
     * @return
     */

    public Sequence getSequenceAt(int position)
    {

        if(m_db!=null)
        {
            if(position>=0 && position<getNbreSequence())
            {
                String requete =  "SELECT * FROM " + DatabaseHelper.LSTSEQUENCES + " INNER JOIN " + DatabaseHelper.SEQUENCE + " ON " +
                                DatabaseHelper.LSTSEQUENCES + "." + DatabaseHelper.LSTSEQUENCES_ID_SEQUENCE + " = " +
                                DatabaseHelper.SEQUENCE + "." + DatabaseHelper.SEQUENCE_ID + " WHERE " + DatabaseHelper.LSTSEQUENCES_ID_LISTEQUENCES + " = " + String.valueOf(position+1)+" ;";
                Cursor c =m_db.rawQuery(requete,null);
                //si plus d'un element trouvé => erreur
                if (c.getCount()==1)
                {
                    // récupération des données de la séquence
                    c.moveToNext();
                    String nom = c.getString(c.getColumnIndex(DatabaseHelper.SEQUENCE_NOM));
                    int nbreRepetition = c.getInt(c.getColumnIndex(DatabaseHelper.SEQUENCE_NOMBREREPETITON));
                    int syntheseVocale = c.getInt(c.getColumnIndex(DatabaseHelper.SEQUENCE_SYNTHESEVOCALE));
                    int idSequence = c.getInt(c.getColumnIndex(DatabaseHelper.SEQUENCE_ID));
                    c.close();
                    Sequence seq=new Sequence(nom, nbreRepetition, new SyntheseVocale(syntheseVocale));
                    requete = "SELECT * FROM " + DatabaseHelper.ELEMENTSEQUENCE + " INNER JOIN " + DatabaseHelper.EXERCICE + " ON " +
                            DatabaseHelper.ELEMENTSEQUENCE + "." + DatabaseHelper.ELEMENTSEQUENCE_ID_EXERCICE + " = " +
                            DatabaseHelper.EXERCICE + "." + DatabaseHelper.EXERCICE_ID +
                            " WHERE " + DatabaseHelper.ELEMENTSEQUENCE_ID_SEQUENCE + " = " + String.valueOf(idSequence) + " ORDER BY " + DatabaseHelper.ELEMENTSEQUENCE_POSITION +
                            " ;";
                    c = m_db.rawQuery(requete,null);
                    if(c.getCount()>0) {
                        while (c.moveToNext()) {
                            nom = c.getString(c.getColumnIndex(DatabaseHelper.EXERCICE_NOM));
                            String description = c.getString(c.getColumnIndex(DatabaseHelper.EXERCICE_DESCRIPTION));
                            int dureeParDefaut = c.getInt(c.getColumnIndex(DatabaseHelper.EXERCICE_DUREEPARDEFAUT));
                            int idExercice = c.getInt(c.getColumnIndex(DatabaseHelper.EXERCICE_ID));
                            syntheseVocale = c.getInt(c.getColumnIndex(DatabaseHelper.ELEMENTSEQUENCE_SYNTHESEVOCALE));
                            int duree = c.getInt(c.getColumnIndex(DatabaseHelper.ELEMENTSEQUENCE_DUREE));
                            int notification = c.getInt(c.getColumnIndex(DatabaseHelper.ELEMENTSEQUENCE_NOTIFICATION));
                            String fichierAudio = c.getString(c.getColumnIndex(DatabaseHelper.ELEMENTSEQUENCE_FICHIERAUDIONOTIFICATION));
                            int idElementSequence = c.getInt(c.getColumnIndex(DatabaseHelper.ELEMENTSEQUENCE_ID));
                            int jouerPlaylist = c.getInt(c.getColumnIndex(DatabaseHelper.EXERCICE_JOUERPLAYLIST));

                            Cursor cPlaylist = m_db.rawQuery("SELECT * FROM " + DatabaseHelper.PLAYLIST + " WHERE " + DatabaseHelper.PLAYLIST_ID_EXERCICE + " = ? ;", new String[]{String.valueOf(idExercice)});
                            Playlist pDefaut = new Playlist();
                            if (jouerPlaylist > 0)
                                pDefaut.setJouerPlaylist(true);
                            else
                                pDefaut.setJouerPlaylist(false);
                            while (cPlaylist.moveToNext()) {
                                pDefaut.ajouterMorceau(cPlaylist.getString(c.getColumnIndex(DatabaseHelper.PLAYLIST_ID_MORCEAU)));
                            }
                            cPlaylist.close();
                            cPlaylist = m_db.rawQuery("SELECT * FROM " + DatabaseHelper.PLAYLIST + " WHERE " + DatabaseHelper.PLAYLIST_ID_ELEMENTSEQUENCE + " = ? ;", new String[]{String.valueOf(idElementSequence)});
                            Playlist pElement = new Playlist();
                            if (jouerPlaylist > 0)
                                pElement.setJouerPlaylist(true);
                            else
                                pElement.setJouerPlaylist(false);

                            while (cPlaylist.moveToNext()) {
                                pElement.ajouterMorceau(cPlaylist.getString(c.getColumnIndex(DatabaseHelper.PLAYLIST_ID_MORCEAU)));
                            }
                            ElementSequence element = new ElementSequence(nom, description, dureeParDefaut, pDefaut, duree, pElement, new Notification(notification, fichierAudio), new SyntheseVocale(syntheseVocale));
                            seq.ajouterElement(element);
                        }
                    }
                    return seq;

                }
                else
                {
                    return null;
                }
            }
            else
            {
                return null;
            }
        }
        else
        {
            return null;
        }
    }

    /**
     * Renvois le nombre de séquences dans la ListeSequences
     * @return
     */

    public int getNbreSequence() {
        Cursor c = m_db.rawQuery("SELECT * FROM " + DatabaseHelper.LSTSEQUENCES + " ;",null);
        return c.getCount();
    }

    /**
     * Sauvegarde le tableau de séquences dans la base de donnée
     * @param tab
     */
    public void saveLstSequence ( ArrayList<Sequence> tab)
    {
        m_db.execSQL(DatabaseHelper.LSTSEQUENCES_TABLE_DROP);
        m_db.execSQL(DatabaseHelper.LSTSEQUENCES_TABLE_CREATE);
        int idSequence=0;
        for (Sequence seq : tab)
        {
            // Recherche si la séquence existe deja dans la table Sequence
            Cursor c = m_db.rawQuery("SELECT * FROM " + DatabaseHelper.SEQUENCE + " WHERE " + DatabaseHelper.SEQUENCE_NOM + " = ? ;", new String[]{seq.getNomSequence()});
            if( c.getCount()==1)
            {
                c.moveToNext();
                idSequence=c.getInt(c.getColumnIndex(DatabaseHelper.SEQUENCE_ID));
                updateSequence(seq,idSequence);
            }
            else
            {
                idSequence=insertSequence(seq);
            }
            ContentValues map = new ContentValues();
            map.put(DatabaseHelper.LSTSEQUENCES_ID_SEQUENCE,String.valueOf(idSequence));
            m_db.insert(DatabaseHelper.LSTSEQUENCES,null,map);

        }

    }

    /**
     * Met à jour la séquence dont l'id correspont à idSequence avec la séquence passée en parametre
     * @param seq
     * @param idSequence
     */
    private void updateSequence(Sequence seq, int idSequence) {

        ContentValues map = new ContentValues();
        map.put(DatabaseHelper.SEQUENCE_NOMBREREPETITON,String.valueOf(seq.getNombreRepetition()));
        map.put(DatabaseHelper.SEQUENCE_SYNTHESEVOCALE,String.valueOf(seq.getSyntheseVocale().getSyntheseVocaleForBdd()));
        String where = DatabaseHelper.SEQUENCE_ID + "=" + idSequence;
        m_db.update(DatabaseHelper.SEQUENCE,map,where,null);

    }

    /**
     * insert la séquence passée en parametre dans la base de donnée et renvois sa position
     * @param seq
     * @return
     *      position de la séquence insérée
     */
    private int insertSequence(Sequence seq)
    {

        ContentValues map = new ContentValues();
        map.put(DatabaseHelper.SEQUENCE_NOM,seq.getNomSequence());
        map.put(DatabaseHelper.SEQUENCE_NOMBREREPETITON,String.valueOf(seq.getNombreRepetition()));
        map.put(DatabaseHelper.SEQUENCE_SYNTHESEVOCALE,String.valueOf(seq.getSyntheseVocale().getSyntheseVocaleForBdd()));

        long row = m_db.insert(DatabaseHelper.SEQUENCE,null,map);
        return (int)row;

    }


}
