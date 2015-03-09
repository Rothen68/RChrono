package com.stephane.rothen.rchrono;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.CountDownTimer;
import android.os.IBinder;


/**
 * Created by stéphane on 23/02/2015.
 */
public class ChronoService extends Service {

    public static final String SER_ACTION="action";



    public static final String SER_TEMPS_RESTANT="temps_restant";
    public static final String SER_UPDATE_LISTVIEW="update_ListView";
    public static final String SER_FIN_LISTESEQUENCE ="fin_liste_sequence";

    /**
     * Permet la communication depuis l'interface
     * @see com.stephane.rothen.rchrono.ChronometreActivity#mConnexion
     * @see com.stephane.rothen.rchrono.ChronoService.MonBinder
     */
    private final IBinder mBinder = new MonBinder();

    /**
     * Instance de la classe Chronometre
     * @see com.stephane.rothen.rchrono.Chronometre
     */
    private Chronometre mChrono;
    /**
     * Stocke l'état actif ou pas du timer
     * @see com.stephane.rothen.rchrono.ChronoService#mTimer
     */
    private Boolean chronoStart=false;
    /**
     * Instance de la classe CountDownTimer permettant de gérer le temps
     */
    private CountDownTimer mTimer;

    /**
     * durée restante dans la séquence active
     */
    private int m_dureeRestanteSequenceActive;

    /**
     * durée restante totale
     */
    private int m_dureeRestanteTotale;


    /**
     * Classe permettant la communication depuis l'interface
     *
     * @see com.stephane.rothen.rchrono.ChronoService#mBinder
     * @see com.stephane.rothen.rchrono.ChronometreActivity#mConnexion
     */
    public class MonBinder extends Binder  {
        ChronoService getService(){
            return ChronoService.this;
        }
    }



    @Override
    public void onCreate() {
        super.onCreate();

    }


    @Override
    public IBinder onBind(Intent intent) {

        return mBinder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if( mTimer!=null)
        {
            mTimer.cancel();
            mTimer=null;
        }

    }

    /**
     * Fonction gérant le lancement du chrono
     */
    public void startChrono()
    {
        if(!chronoStart)
        {
            chronoStart=true;
            m_dureeRestanteTotale=mChrono.getDureeRestanteTotale();
            lancerTimer();
            updateListView();
        }
    }

    /**
     * Retourne l'état du chrono, actif ou inactif
     * @return
     *      true : actif
     *      false : inactif
     */
    public boolean getChronoStart()
    {
        return chronoStart;
    }

    /**
     * Permet d'arreter le chrono
     */
    public void stopChrono()
    {
        if(chronoStart)
        {
            chronoStart=false;
            if(mTimer!=null)
                mTimer.cancel();
        }
    }

    /**
     * Remet le chronometre à zéro et transmet les demande d'actualisation de l'interface
     */
    public void resetChrono()
    {
        chronoStart=false;
        mChrono.resetChrono();
        if(mTimer!=null)
            mTimer.cancel();
        updateChrono(mChrono.getDureeExerciceActif());
        updateListView();
        Intent i = new Intent();
        i.setAction(SER_FIN_LISTESEQUENCE);
        sendBroadcast(i);
    }

    /**
     * Permet de positionner les curseurs du chronometre à une position définie
     * @param sequence
     *          index de la séquence active
     * @param exercice
     *          index de l'exercice actif
     *
     * @see com.stephane.rothen.rchrono.ChronoService#mChrono
     * @see com.stephane.rothen.rchrono.Chronometre#setChronoAt(int, int)
     */
    public void setChronoAt(int sequence, int exercice)
    {
        mChrono.setChronoAt(sequence,exercice);
    }

    /**
     * Permet d'affecter un chronometre au service
     * @param c
     *      instance de la classe Chronometre
     *@see com.stephane.rothen.rchrono.ChronoService#mChrono
     */
    public void setChronometre(Chronometre c)
    {
        mChrono=c;
    }


    /**
     * Envois une demande d'actualisation de la zone de texte txtChrono de l'interface
     * @param delais
     *      valeur à afficher
     *@see com.stephane.rothen.rchrono.ChronometreActivity#txtChrono
     * @see com.stephane.rothen.rchrono.ChronometreActivity#myReceiver
     */
    public void updateChrono(int delais)
    {
        mChrono.setDureeRestanteExerciceActif(delais);
        Intent i = new Intent();
        int type = mChrono.getTypeAffichage();
        i.setAction(SER_TEMPS_RESTANT);
        switch (type)
        {
            case Chronometre.AFFICHAGE_TEMPS_EX:
                i.putExtra(SER_TEMPS_RESTANT,delais);
                break;
            case Chronometre.AFFICHAGE_TEMPS_SEQ:
                i.putExtra(SER_TEMPS_RESTANT,m_dureeRestanteSequenceActive);
                break;
            case Chronometre.AFFICHAGE_TEMPS_TOTAL:
                i.putExtra(SER_TEMPS_RESTANT,m_dureeRestanteTotale);
                break;
            default :
                break;
        }
        sendBroadcast(i);
    }

    /**
     * Envois une demande d'actualisation de la ListView de l'interface
     *
     *@see com.stephane.rothen.rchrono.ChronometreActivity#mLv
     * @see com.stephane.rothen.rchrono.ChronometreActivity#myReceiver
     */
    public void updateListView()
    {
        Intent i = new Intent();
        i.setAction(SER_UPDATE_LISTVIEW);
        int exercice =mChrono.getIndexExerciceActif();
        int seq = mChrono.getIndexSequenceActive();
        if(exercice>=0) {
            int position = 1;
            for (int j = 0; j < seq; j++) {
                position++;
                for (ElementSequence e : mChrono.getListeSequence().get(j).getTabElement()) {
                    position++;
                }
            }
            position = position + exercice;
            i.putExtra(SER_UPDATE_LISTVIEW, position);
        }
        else
            i.putExtra(SER_UPDATE_LISTVIEW,0);
        sendBroadcast(i);

    }


    /**
     * Fonction appelée pour lancer le timer
     * @see com.stephane.rothen.rchrono.ChronoService#mTimer
     */
    private void lancerTimer()
    {
        //todo modifier la facon dont le timer se créé : utiliser le temps total plutot que le temps de chaque exercice et faire en sorte que l'interface récupere la valeur du chrono depuis la classe chronometre
        int duree = mChrono.getDureeRestanteExerciceActif();
        m_dureeRestanteSequenceActive=mChrono.getDureeRestanteSequenceActive();
        mTimer = new CountDownTimer(duree*1000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                updateChrono((int)((millisUntilFinished)/1000));
                m_dureeRestanteSequenceActive--;
                m_dureeRestanteTotale--;
            }

            @Override
            public void onFinish() {
                if(mChrono.next()) {
                    updateListView();
                    lancerTimer();
                    m_dureeRestanteTotale=mChrono.getDureeRestanteTotale();

                }
                else
                {
                    resetChrono();
                }
            }
        }.start();

    }

}
