package com.stephane.rothen.rchrono;

/**
 * SyntheseVocale est la classe métier gérant la synthese vocale
 *
 * Created by Stéphane on 14/02/2015.
 */
public class SyntheseVocale {
    /**
     * Le nom est dit par la synthèse vocale
     */
    protected boolean m_nom=false;
    /**
     * La durée est dite par la synthèse vocale
     */
    protected boolean m_duree=false;

    /**
     * initialise l'objet depuis un entier contenu dans la base de donnée
     * @param syntheseVocaleFromBdd : valeur lut dans la base de donnée
     */
    public SyntheseVocale(int syntheseVocaleFromBdd)
    {
        switch (syntheseVocaleFromBdd)
        {
            case 1:
                m_nom=true;
                break;
            case 2:
                m_duree=true;
                break;
            case 3:
                m_duree=true;
                m_nom=true;
                break;
            default:
                break;
        }
    }

    /**
     * Renvois un entier pour stocker l'information de synthese vocale dans la base de donnée
     * @return valeur à stocker dans la base de donnée
     */
    public int getSyntheseVocaleForBdd()
    {
        if(m_nom && m_duree) return 3;

        if(m_nom && !m_duree) return 1;

        if(!m_nom && m_duree) return 2;

        return 0;
    }

}
