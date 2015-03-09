package com.stephane.rothen.rchrono;


import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;



public class ChronometreActivity extends ActionBarActivity {

    /**
     * Objet de l'interface, ListView qui contient la liste des séquences et des exercices
     */
    private ListView mLv;
    /**
     * Objet de l'interface, Button qui est l'instance du bouton Start/Pause
     */
    private Button mbtnStart;
    /**
     * Objet de l'inteface, Button qui est l'instance du bouton Reset
     */
    private Button mbtnReset;
    /**
     * Objet de l'interface, TextView qui est l'instance de la zone de texte permettant d'afficer le temps restant pour l'exercice en cours
     */
    private TextView txtChrono;

    /**
     * Instance de la classe Chronometre
     * @see com.stephane.rothen.rchrono.Chronometre
     */
    private Chronometre mChrono;
    /**
     * Objet permettant de récupérer l'instance du service ChronoService
     * @see com.stephane.rothen.rchrono.ChronoService
     */
    private ChronoService chronoService;
    /**
     * Objet permettant la communication entre le service et l'activity
     * @see com.stephane.rothen.rchrono.ChronometreActivity.MyReceiver
     *
     */
    private MyReceiver myReceiver;
    /**
     * Objet permettant de remplir la ListView
     * @see com.stephane.rothen.rchrono.CustomAdapter
     *
     */
    private CustomAdapter mAdapter;
    /**
     * Objet permettant de gérer la communication de l'interface vers le service, il initialise chronoService
     * @see com.stephane.rothen.rchrono.ChronometreActivity#chronoService
     */
    private ServiceConnection mConnexion;



    /**
     * Méthode appelée lors du lancement de l'application.
     * <p>Les données membres de la classe ainsi que l'affichage sont initialisé.</p>
     *
     *
     * @param savedInstanceState
     *
     * @see com.stephane.rothen.rchrono.ChronoService
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chronometre);

        mChrono = new Chronometre(getApplication());

        //Initialisation des éléments de l'interface
        mbtnStart = (Button) findViewById(R.id.btnStart);
        mbtnStart.setAllCaps(false);
        mbtnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chronoService != null) {
                    if (chronoService.getChronoStart()) {
                        chronoService.stopChrono();
                        mbtnStart.setText(R.string.chronometre_start);
                    } else {
                        chronoService.startChrono();
                        mbtnStart.setText(R.string.chronometre_pause);
                    }
                }


            }
        });
        mbtnReset = (Button) findViewById(R.id.btnReset);
        mbtnReset.setAllCaps(false);
        mbtnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chronoService != null) {
                    chronoService.resetChrono();
                }
            }
        });
        txtChrono=(TextView) findViewById(R.id.txtChrono);
        mLv = (ListView) findViewById(R.id.listView);
        mAdapter=new CustomAdapter(this);
        mLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            /**
             * Lors d'un appuis court sur un item de la ListView, arrête le chrono et le place sur l'exercice sélectionné, ou en cas de séquence sélectionnée, sur le premier exercice de la séquence
             * @param parent
             * @param view
             * @param position
             * @param id
             *
             * @see com.stephane.rothen.rchrono.Chronometre#setChronoAt(int)
             * @see com.stephane.rothen.rchrono.ChronoService
             */
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                chronoService.stopChrono();
                int posExercice = mChrono.setChronoAt(position);
                if(posExercice>-1)
                    afficheListView(posExercice);
                mbtnStart.setText(R.string.chronometre_start);
                chronoService.updateChrono(mChrono.getDureeExerciceActif());
            }
        });
        mLv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                chronoService.stopChrono();
                //TODO : ouvrir la fenetre ListeSequenceActivity
                return true;
            }
        });

        afficheListView(0);


        //initialisation du receiver qui permet la communication vers l'interface depuis chronoService
        myReceiver=new MyReceiver();
        IntentFilter ifilter = new IntentFilter();
        ifilter.addAction(ChronoService.SER_TEMPS_RESTANT);
        ifilter.addAction(ChronoService.SER_UPDATE_LISTVIEW);
        ifilter.addAction(ChronoService.SER_FIN_LISTESEQUENCE);
        registerReceiver(myReceiver, ifilter);


        // permet de faire basculer l'affichage entre la durée restante de l'exercice, la durée restante de la séquence ou la durée restante totale
        ((TextView) findViewById(R.id.txtChrono)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int type = mChrono.getTypeAffichage();
                switch (type)
                {
                    case Chronometre.AFFICHAGE_TEMPS_EX:
                        mChrono.setTypeAffichage(Chronometre.AFFICHAGE_TEMPS_SEQ);
                        ((TextView)findViewById(R.id.txtDescChrono)).setText(R.string.descChronometre_Sequence);
                        break;
                    case Chronometre.AFFICHAGE_TEMPS_SEQ:
                        mChrono.setTypeAffichage(Chronometre.AFFICHAGE_TEMPS_TOTAL);
                        ((TextView)findViewById(R.id.txtDescChrono)).setText(R.string.descChronometre_Total);
                        break;
                    case Chronometre.AFFICHAGE_TEMPS_TOTAL:
                        mChrono.setTypeAffichage(Chronometre.AFFICHAGE_TEMPS_EX);
                        ((TextView)findViewById(R.id.txtDescChrono)).setText(R.string.descChronometre_Exercice);
                        break;
                    default :
                        break;
                }
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_chronometre, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        return super.onOptionsItemSelected(item);
    }


    /**
     * Lancement du ChronoService dans onStart()
     */
    @Override
    protected void onStart() {
        super.onStart();
        //Lancement du service ChronoService
        mConnexion =new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {

                chronoService =  ((ChronoService.MonBinder) service).getService();
                chronoService.setChronometre(mChrono);
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                chronoService=null;
            }
        };
        Intent intent = new Intent(getApplicationContext(),ChronoService.class);
        intent.putExtra(ChronoService.SER_ACTION, 0);
        bindService(intent,mConnexion,BIND_AUTO_CREATE);
    }

    /**
     * Arrêt du service ChronoService dans onStop()
     */
    @Override
    protected void onStop() {
        super.onStop();
        stopService(new Intent(this,ChronoService.class));
        chronoService=null;
        mConnexion=null;


    }

    /**
     * Initialise le ListView Adapter mAdapter et l'affecte à la ListView mLv
     * @param positionFocus
     *      Permet de définir quel item de la ListView est mis en surbrillance
     *
     * @see com.stephane.rothen.rchrono.ChronometreActivity#mAdapter
     * @see com.stephane.rothen.rchrono.ChronometreActivity#mLv
     *
     */
    private void afficheListView(int positionFocus)
    {
        if( mAdapter.getCount()>0)
        {
            mAdapter.deleteAll();
        }
        mAdapter.setFocusPosition(positionFocus);
        //Parcours la liste des séquences et des exercices pour chaque séquence et les ajoute dans mAdapter
        for (int i=0 ; i <mChrono.getListeSequence().size();i++)
        {
            Sequence s = mChrono.getListeSequence().get(i);
            //si s est la séquence active, afficher le nombre de répétitions restantes
            if( mChrono.getIndexSequenceActive()==i)
            {
                if (mChrono.getNbreRepetition()==0)
                {
                    mAdapter.addSectionHeaderItem(s.getNomSequence() + " - " + convertSversHMS(mChrono.getDureeRestanteSequenceActive()));
                }
                else
                {
                    mAdapter.addSectionHeaderItem(s.getNomSequence()+ " - " + mChrono.getNbreRepetition() + "x" + " - " + convertSversHMS(mChrono.getDureeRestanteSequenceActive()));
                }
            }
            //si s est avant la séquence active, donc est déjà passée, met 0 à la durée de la séquence
            else if (mChrono.getIndexSequenceActive()>i) {
                mAdapter.addSectionHeaderItem(s.getNomSequence() + " - " + convertSversHMS(0));
            }
            else
            {
                mAdapter.addSectionHeaderItem(s.getNomSequence()+ " - " + s.getNombreRepetition() + "x" + " - " + convertSversHMS(s.getDureeSequence()) );
            }
            for (int j = 0 ; j <s.getTabElement().size();j++)
            {
                mAdapter.addItem(s.getTabElement().get(j).getNomExercice() + " - " + convertSversHMS( s.getTabElement().get(j).getDureeExercice()));
            }
        }
        mLv.setAdapter(mAdapter);
        final int position = positionFocus;
        mLv.postDelayed(new Runnable() {
            @Override
            public void run() {

                //TODO améliorer le changement de position dans la ListView
                if(mLv.getFirstVisiblePosition()>position||mLv.getLastVisiblePosition()<=position)
                {
                    int milieu = (int) ((mLv.getLastVisiblePosition()-mLv.getFirstVisiblePosition())/2);
                    int pos=position+milieu;
                    if(pos>mLv.getCount())
                    {
                        mLv.smoothScrollToPosition(mLv.getCount());
                    }
                    else
                        mLv.smoothScrollToPosition(pos);
                }
            }
        },100L);

    }


    /**
     * Met à jour le texte dans la TextView txtChrono
     * @param texte
     *          text à afficher dans la TextView
     *@see com.stephane.rothen.rchrono.ChronometreActivity#txtChrono
     */
    public void setTxtChrono(String texte)
    {
        txtChrono.setText(texte);
        txtChrono.invalidate();
    }

    /**
     * Fonction permettant de convertir une durée en seconde stocké dans un entier en une chaine de caractere au format HH : MM : SS
     * @param s
     *      durée en seconde à convertir
     * @return
     *      chaine de caractere contenant la durée convertie
     */
    private String convertSversHMS(int s){
        int heure = (int)s/3600;
        int minute =(int) (s-3600*heure)/60;
        int seconde =(int) s- 3600*heure - 60* minute;
        String valeur = new String();
        if( heure<10)
            valeur = valeur + "0" ;
        valeur = valeur + String.valueOf(heure) + " : ";

        if(minute<10)
            valeur = valeur + "0";
        valeur=valeur + String.valueOf(minute) + " : ";

        if(seconde<10)
            valeur = valeur + "0";
        valeur= valeur + String.valueOf(seconde);
        return valeur;
    }


    /**
     * Classe privée MyReceiver
     * <p>Elle permet de récupérer et de traiter des broadcast venant de chronoService</p>
     * @see com.stephane.rothen.rchrono.ChronoService
     */
    private class MyReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction())
            {
                case ChronoService.SER_TEMPS_RESTANT :
                    int tempsRestant = intent.getIntExtra(ChronoService.SER_TEMPS_RESTANT,-1);
                    if( tempsRestant!=-1)
                    {
                        setTxtChrono(convertSversHMS(tempsRestant));
                    }
                    break;
                case ChronoService.SER_UPDATE_LISTVIEW:
                    int position = intent.getIntExtra(ChronoService.SER_UPDATE_LISTVIEW,-1);
                    if ( position !=-1) {
                        afficheListView(position);

                    }
                    break;
                case ChronoService.SER_FIN_LISTESEQUENCE:
                    mbtnStart.setText(R.string.chronometre_start);
                    mbtnStart.invalidate();
                    afficheListView(0);
            }
        }
    }







    //Permet de récupérer tout le contenu multimedia du téléphone
    /*
    private void scanMusiques()
    {
        ArrayList<Morceau> listMusic = new ArrayList<>();
        ContentResolver musicResolver = getContentResolver();
        Uri musicUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor musicCursor = musicResolver.query(musicUri,null,null,null,null);

        if(musicCursor!=null && musicCursor.moveToFirst())
        {
            int titleColumn = musicCursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            int artistColumn = musicCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
            int idColumn = musicCursor.getColumnIndex(MediaStore.Audio.Media._ID);
            do{
                long thisId = musicCursor.getLong(idColumn);
                String thisTitle = musicCursor.getString(titleColumn);
                String thisArtist = musicCursor.getString(artistColumn);
                listMusic.add(new Morceau(thisId,thisTitle,thisArtist));
            }while(musicCursor.moveToNext());



        }
        Playlist pl = new Playlist();
        for(Morceau m : listMusic)
            pl.ajouterMorceau(m);

        Exercice e = new Exercice("test","test2",15,pl);

        mChrono.getLibrairieExercice().ajoutExercice(e);
        mChrono.saveLibrairieExercice();
    }*/
}
