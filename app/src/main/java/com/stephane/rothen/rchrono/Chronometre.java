package com.stephane.rothen.rchrono;

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
    private int m_indexMorceauActif;

    /**
     * Position dans le morceau actif
     *
     * @see com.stephane.rothen.rchrono.Chronometre#m_indexMorceauActif
     */
    private int m_positionDansMorceauActif;
}
