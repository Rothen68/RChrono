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

    public Notification(int notificationFromBdd,String fichier) {
        switch (notificationFromBdd) {
            case 1:
                m_vibreur = true;
                break;
            case 2:
                m_popup = true;
                break;
            case 3:
                m_vibreur = true;
                m_popup = true;
                break;
            case 4:
                m_sonnerie = true;
                break;
            case 5:
                m_sonnerie = true;
                m_vibreur = true;
                break;
            case 6:
                m_sonnerie = true;
                m_popup = true;
                break;
            case 7:
                m_sonnerie = true;
                m_popup = true;
                m_vibreur = true;
                break;
            default:
                break;
        }
        Uri.Builder b = new Uri.Builder();
        b.encodedPath(fichier);
        m_fichierSonnerie = b.build();
        //TODO: vérifier syntaxe Uri.builder
    }

    public int getNotificationForBdd()
    {
        return 0;
    }

    public Uri getFichierSonnerie()
    {
        return m_fichierSonnerie;
    }
}
