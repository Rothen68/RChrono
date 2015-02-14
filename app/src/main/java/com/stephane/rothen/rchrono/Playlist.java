package com.stephane.rothen.rchrono;

import android.net.Uri;

import java.util.ArrayList;

/**
 * Classe métier permettant de stocker les données de la playlist
 *
 * Created by Stéphane on 14/02/2015.
 */
public class Playlist {
    /**
     * Tableau contenant la liste des liens vers les morceaux à jouer dans la playlist
     */
    protected ArrayList<Uri> m_listeMorceaux;
    /**
     * La playlist est jouée pendant l'exercice
     */
    protected boolean m_jouerPlaylist;
}
