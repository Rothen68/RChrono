package com.stephane.rothen.rchrono;

/**
 * Classe métier héritant d'Exercice et stockant les caractéristiques spécifiques d'un exercice
 *
 * Created by Stéphane on 14/02/2015.
 */
public class ElementSequence extends Exercice {

    /**
     * Duree de l'exercice
     */
    protected int m_dureeExercice;

    /**
     * Playlist de l'exercice
     *
     * @see com.stephane.rothen.rchrono.Playlist
     */
    protected Playlist m_playlistExercice;

    /**
     * Nofitications de l'exercice
     *
     * @see com.stephane.rothen.rchrono.Notification
     */
    protected Notification m_notification;

    /**
     * Synthèse vocale de l'exercice
     *
     * @see com.stephane.rothen.rchrono.SyntheseVocale
     */
    protected SyntheseVocale m_syntheseVocale;

    public ElementSequence(String nomExercice, String descriptionExercice, int dureeParDefaut, Playlist playlistParDefaut, int dureeExercice, Playlist playlistExercice, Notification notification, SyntheseVocale syntheseVocale) {
        super(nomExercice, descriptionExercice, dureeParDefaut, playlistParDefaut);
        this.m_dureeExercice = dureeExercice;
        this.m_playlistExercice = playlistExercice;
        this.m_notification = notification;
        this.m_syntheseVocale = syntheseVocale;
    }


    public int getDureeExercice()
    {
        return m_dureeExercice;
    }

    public SyntheseVocale getSyntheseVocale()
    {
        return m_syntheseVocale;
    }

    public Notification getNotification()
    {
        return m_notification;
    }

}
