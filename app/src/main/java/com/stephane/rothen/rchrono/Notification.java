package com.stephane.rothen.rchrono;

import android.net.Uri;

/**
 * Notification est la classe métier gérant les notifications pour un exercice
 *
 * Created by Stéphane on 14/02/2015.
 */
public class Notification {
    /**
     * La notification est du type vibreur
     */
    protected boolean m_vibreur;
    /**
     * La notification est du type Popup
     */
    protected boolean m_popup;
    /**
     * La notification est du type sonnerie
     */
    protected boolean m_sonnerie;
    /**
     * Chemin vers le fichier de sonnerie si la notification est du type sonnerie
     * @see com.stephane.rothen.rchrono.Notification#m_sonnerie
     */
    protected Uri m_fichierSonnerie;


}
