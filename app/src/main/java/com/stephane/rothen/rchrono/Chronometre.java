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
     * Nombre de répétitions restantes pour la séquence active
     */
    protected int m_nbreRepetition;

    /**
     * Index de l'exercice dans la séquence active
     *
     * @see com.stephane.rothen.rchrono.Chronometre#m_indexSequenceActive
     */
    protected int m_indexExerciceActif;

    /**
     * Position dans l'exercice actif
     */
    protected  int m_positionDansExerciceActif;

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
     * Constructeur
     * @param c
     *      Context de l'application, pour l'acces à la base de donnée
     *
     *@see com.stephane.rothen.rchrono.DAOBase
     */
    public Chronometre(Context c) {
        //connexion à la base de donnée
        m_bddHelper = new DAOBase(c);
        m_bddHelper.open();
        m_tabSequence = new ArrayList<>();
        initLibrairieExercices();
        initListeSequences();
        m_indexExerciceActif=0;
        m_indexSequenceActive=0;
        m_nbreRepetition=m_tabSequence.get(m_indexSequenceActive).getNombreRepetition();
        m_positionDansExerciceActif = m_tabSequence.get(m_indexSequenceActive).getTabElement().get(m_indexExerciceActif).getDureeExercice();


    }

    /**
     * initialise la liste des séquence depuis la base de donnée
     * @see com.stephane.rothen.rchrono.DAOBase
     *
     */
    private void initListeSequences() {
        /*int nbreSequences = m_bddHelper.getNbreSequence();
        Sequence s;
        for (int i = 0 ; i<nbreSequences ; i++)
        {
            s=m_bddHelper.getSequenceAt(i);
            if ( s!=null)
                m_tabSequence.add(s);
        }*/
        ElementSequence e = new ElementSequence("Exercice 1","",10,null,10,null,null,null);
        ElementSequence e2 = new ElementSequence("Exercice 2","",60,null,5,null,null,null);
        ElementSequence e3 = new ElementSequence("Exercice 3","",30,null,5,null,null,null);
        Sequence s = new Sequence("Sequence 1", 2,null);
        s.ajouterElement(e);
        s.ajouterElement(e2);
        Sequence s2 = new Sequence("Sequence 2", 1,null);
        s2.ajouterElement(e3);
        m_tabSequence.add(s);
        m_tabSequence.add(s2);

    }

    /**
     * Initialise et récupère la Librairie des Exercices depuis la base de donnée
     *
     * @see com.stephane.rothen.rchrono.Exercice
     * @see com.stephane.rothen.rchrono.LibrairieExercices
     * @see com.stephane.rothen.rchrono.DAOBase#restoreLibrairieExercice()
     */
    private void initLibrairieExercices()
    {
        m_libExercices = new LibrairieExercices(m_bddHelper.restoreLibrairieExercice());
    }

    /**
     * sauvegarde la liste des séquences dans la base de donnée
     * @see com.stephane.rothen.rchrono.Sequence
     * @see com.stephane.rothen.rchrono.DAOBase#saveLstSequence(java.util.ArrayList)
     */
    public void saveListeSequence()
    {
        m_bddHelper.saveLstSequence(m_tabSequence);
    }

    /**
     * Sauvegarde la Librairie des Exercices dans la base de donnée
     * @see
     * @see com.stephane.rothen.rchrono.DAOBase#saveLibrairieExercice(java.util.ArrayList)
     */
    public void saveLibrairieExercice()
    {
        m_bddHelper.saveLibrairieExercice(m_libExercices.getLibrairie());
    }


    public LibrairieExercices getLibrairieExercice()
    {
        return m_libExercices;
    }


    public int getIndexSequenceActive()
    {
        return m_indexSequenceActive;
    }

    public int getIndexExerciceActif()
    {
        return m_indexExerciceActif;
    }


    /**
     * Met à jour les curseurs et renvois true si la liste des séquences n'est pas finie
     * <p>m_indexExerciceActif, m_indexSequenceActive, m_nbreRepetition</p>
     *
     * @return
     *      true : valeurs mises à jour
     *      false : valeurs mises à 0, fin de la liste des séquences
     *
     * @see com.stephane.rothen.rchrono.Chronometre#m_indexExerciceActif
     * @see com.stephane.rothen.rchrono.Chronometre#m_indexSequenceActive
     * @see com.stephane.rothen.rchrono.Chronometre#m_nbreRepetition
     * @see com.stephane.rothen.rchrono.Chronometre#m_tabSequence
     *

     */
    public boolean next()
    {
        m_indexExerciceActif++;
        if(m_indexExerciceActif>=m_tabSequence.get(m_indexSequenceActive).getTabElement().size())
        {
            m_indexExerciceActif=0;
            m_nbreRepetition--;
            if (m_nbreRepetition<=0)
            {
                m_indexSequenceActive++;
                if(m_indexSequenceActive>=m_tabSequence.size())
                {
                    m_indexExerciceActif=0;
                    m_indexSequenceActive=0;
                    m_positionDansExerciceActif=m_tabSequence.get(m_indexSequenceActive).getTabElement().get(m_indexExerciceActif).getDureeExercice();
                    return false;
                }
                m_nbreRepetition = m_tabSequence.get(m_indexSequenceActive).getNombreRepetition();
            }
        }
        m_positionDansExerciceActif = m_tabSequence.get(m_indexSequenceActive).getTabElement().get(m_indexExerciceActif).getDureeExercice();
        return true;
    }


    /**
     * Réinitialise le chronometre
     * @see com.stephane.rothen.rchrono.Chronometre#m_indexExerciceActif
     * @see com.stephane.rothen.rchrono.Chronometre#m_indexSequenceActive
     * @see com.stephane.rothen.rchrono.Chronometre#m_nbreRepetition
     */
    public void resetChrono()
    {
        m_indexSequenceActive=0;
        m_indexExerciceActif=0;
        m_positionDansExerciceActif=m_tabSequence.get(m_indexSequenceActive).getTabElement().get(m_indexExerciceActif).getDureeExercice();
        m_nbreRepetition=m_tabSequence.get(m_indexSequenceActive).getNombreRepetition();
    }

    /**
     * Permet de positionner les curseurs du chronometre à une position définie
     * @param indexSequence
     *          index de la séquence active
     * @param indexExercice
     *          index de l'exercice actif
     */
    public void setChronoAt(int indexSequence,int indexExercice)
    {
        if (indexSequence<m_tabSequence.size() && indexSequence >=0)
        {
            if (indexExercice<m_tabSequence.get(indexSequence).getTabElement().size() && indexExercice>=0)
            {
                m_indexSequenceActive = indexSequence;
                m_indexExerciceActif = indexExercice;
            }
        }
    }

    /**
     * Renvois la durée de l'exercice actif
     * @return
     *      durée de l'exercice actif
     *
     *@see com.stephane.rothen.rchrono.Chronometre#m_indexExerciceActif
     *
     */
    public int getDureeExerciceActif()
    {
        return m_tabSequence.get(m_indexSequenceActive).getTabElement().get(m_indexExerciceActif).getDureeExercice();
    }

    /**
     * permet de définir le temps restant dans l'exercice actif
     * @param position
     *      temps restant
     *@see com.stephane.rothen.rchrono.Chronometre#m_indexExerciceActif
     */
    public void setDureeRestanteExerciceActif(int position)
    {
        m_positionDansExerciceActif=position;
    }

    /**
     * retourne le temps restant de l'exercice actif
     * @return
     *      temps restant
     *@see com.stephane.rothen.rchrono.Chronometre#m_indexExerciceActif
     */
    public int getDureeRestanteExerciceActif()
    {
        return m_positionDansExerciceActif;
    }

    /**
     * Renvois la liste des séquences
     * @return
     *      liste des séquences
     *
     *@see com.stephane.rothen.rchrono.Chronometre#m_tabSequence
     */
    public ArrayList<Sequence> getListeSequence()
    {
        return m_tabSequence;
    }


    public int getNbreRepetition(){return m_nbreRepetition;}

    /**
     * Renvois la durée restante de la séquence active
     * @return
     *      durée restante
     *@see com.stephane.rothen.rchrono.Chronometre#m_indexSequenceActive
     *
     */
    public int getDureeRestanteSequenceActive() {
        Sequence s = m_tabSequence.get(m_indexSequenceActive);
        int duree = 0;
        for (ElementSequence e : s.getTabElement())
        {
            duree = duree + e.getDureeExercice();
        }
        duree = duree * (m_nbreRepetition-1);
        for (int  i = m_indexExerciceActif; i < s.getTabElement().size();i++)
            duree = duree + s.getTabElement().get(i).getDureeExercice();

        return duree;
    }
}
