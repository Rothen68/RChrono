package com.stephane.rothen.rchrono;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

/**
 * Classe controleur gérant le chronometre
 *
 * Created by Stéphane on 14/02/2015.
 */
public class Chronometre {

    /**
     * Tableau de séquences à executer
     *
     * @see com.stephane.rothen.rchrono.Sequence
     */
    protected ArrayList<Sequence> m_tabSequence;

    /**
     * Index de la séquence active
     */
    protected int m_indexSequenceActive;

    /**
     * Index de l'exercice dans la séquence active
     *
     * @see com.stephane.rothen.rchrono.Chronometre#m_indexSequenceActive
     */
    protected int m_indexExerciceActif;

    /**
     * Index du morceau actif dans la playlist de l'exercice
     *
     * @see com.stephane.rothen.rchrono.Playlist
     */
    protected int m_indexMorceauActif;

    /**
     * Position dans le morceau actif
     *
     * @see com.stephane.rothen.rchrono.Chronometre#m_indexMorceauActif
     */
    protected int m_positionDansMorceauActif;

    /**
     * Librairie des exercies
     * @see com.stephane.rothen.rchrono.LibrairieExercices
     */
    protected LibrairieExercices m_libExercices;

    /**
     * Librairie des séquences
     * @see com.stephane.rothen.rchrono.LibrairieSequences
     */

    protected LibrairieSequences m_libSequences;

    /**
     * Gestionnaire de connexion à la base de donnée
     */
    protected DAOBase m_bddHelper=null;
    /**
     * Base de donnée
     */
    protected SQLiteDatabase m_bdd=null;


    public Chronometre(Context c) {
        //connexion à la base de donnée
        m_bddHelper = new DAOBase(c);
        m_bdd = m_bddHelper.open();
        m_tabSequence = new ArrayList<>();

        initLibrairieExercices();


    }

    /**
     * initialise la liste des séquence depuis la base de donnée
     */
    private void initListeSequences() {
        int nbreSequences = m_bddHelper.getNbreSequence();
        Sequence s;
        for (int i = 0 ; i<nbreSequences ; i++)
        {
            s=m_bddHelper.getSequenceAt(i);
            if ( s!=null)
                m_tabSequence.add(s);
        }

    }

    private void initLibrairieExercices()
    {
        m_libExercices = new LibrairieExercices(m_bddHelper.restoreLibrairieExercice());
    }

    /**
     * sauvegarde la liste des séquences dans la base de donnée
     */
    public void saveListeSequence()
    {
        m_bddHelper.saveLstSequence(m_tabSequence);
    }

    public void saveLibrairieExercice()
    {
        m_bddHelper.SaveLibrairieExercice(m_libExercices.getLibrairie());
    }


    public LibrairieExercices getLibrairieExercice()
    {
        return m_libExercices;
    }
}
