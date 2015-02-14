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
}
