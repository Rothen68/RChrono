package com.stephane.rothen.rchrono.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.stephane.rothen.rchrono.R;

/**
 * View permettant de stocker un item Exercice de la ListView
 * Created by stéphane on 09/03/2015.
 */
public class ItemListeExercice extends LinearLayout {
    public ItemListeExercice(Context context) {
        super(context);
        init();
    }

    public ItemListeExercice(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ItemListeExercice(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    ImageView mFleche;
    ImageButton mbtnSuppr;

    TextView mText;

    private void init (){
        LayoutInflater.from(getContext()).inflate(R.layout.lv_exercice_focused_layout,this, true);
        setOrientation(HORIZONTAL);
        mText = (TextView) findViewById(R.id.txtLvExercice);
        mFleche = (ImageView) findViewById(R.id.imageView);
        mbtnSuppr = (ImageButton) findViewById(R.id.btnSuppr);
    }

    public void setUpView(String txt,boolean visibiliteFleche, boolean visibiliteBouton){
        mText.setText(txt);
        mFleche.setVisibility((visibiliteFleche)?VISIBLE:INVISIBLE);
        mbtnSuppr.setVisibility((visibiliteBouton)?VISIBLE:INVISIBLE);

    }
}
