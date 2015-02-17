package com.stephane.rothen.rchrono;

import java.util.ArrayList;

/**
 * Classe métier permettant de stocker les donnees d'une séquence
 *
 * Created by Stéphane on 14/02/2015.
 */
public class Sequence {
    /**
     * Nom de la séquence
     */
    protected String m_nomSequence;

    /**
     * Nombre de répétitions de la séquence :  1 = la séquence est lu une fois
     */
    protected int m_nombreRepetition;

    /**
     * Synthèse vocale de la séquence
     *
     * @see com.stephane.rothen.rchrono.SyntheseVocale
     */
    protected SyntheseVocale m_syntheseVocale;

    /**
     * tableau contenant les exercices à executer durant la séquence
     *
     * @see com.stephane.rothen.rchrono.ElementSequence
     */
    protected ArrayList<ElementSequence> m_tabElement;

    /**
     * Constructeur
     * @param nomSequence
     * @param nombreRepetition
     * @param syntheseVocale
     */
    public Sequence(String nomSequence, int nombreRepetition, SyntheseVocale syntheseVocale) {
        this.m_nomSequence = nomSequence;
        this.m_nombreRepetition = nombreRepetition;
        this.m_syntheseVocale = syntheseVocale;
        m_tabElement=new ArrayList<>();
    }

    public Sequence()
    {

    }

    public String getNomSequence() {
        return m_nomSequence;
    }

    public void setNomSequence(String nomSequence) {
        this.m_nomSequence = nomSequence;
    }

    public int getNombreRepetition() {
        return m_nombreRepetition;
    }

    public void setM_nombreRepetition(int nombreRepetition) {
        this.m_nombreRepetition = nombreRepetition;
    }

    public SyntheseVocale getSyntheseVocale() {
        return m_syntheseVocale;
    }

    public void setM_syntheseVocale(SyntheseVocale syntheseVocale) {
        this.m_syntheseVocale = syntheseVocale;
    }

    public ArrayList<ElementSequence> getTabElement() {
        return m_tabElement;
    }

    public void setTabElement(ArrayList<ElementSequence> tabElement) {
        this.m_tabElement = tabElement;
    }

    public void ajouterElement ( ElementSequence e)
    {
        m_tabElement.add(e);
    }
}
