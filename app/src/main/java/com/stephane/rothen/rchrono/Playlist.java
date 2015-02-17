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

    public Playlist(){
        m_listeMorceaux = new ArrayList<>();
    };

    void setJouerPlaylist ( boolean b)
    {
        m_jouerPlaylist=b;
    }

    void ajouterMorceau(String morceau)
    {
        Uri.Builder builder = new Uri.Builder();
        builder.path(morceau);
        m_listeMorceaux.add(builder.build());
    }
}
