package com.stephane.rothen.rchrono;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.TreeSet;

/**
 * Classe permettant de faire la liaison entre une instance de ListView et une instance de Chronometre
 *
 *
 * Created by stéphane on 23/02/2015.
 */
public class CustomAdapter extends BaseAdapter {
    /**
     * Tableau contenant les données à afficher
      */
    private ArrayList<String> m_Data= new ArrayList<>();
    /**
     * TreeSet contenant les positions des séquences dans le tableau de donnée
     * @see com.stephane.rothen.rchrono.CustomAdapter#m_Data
     */
    private TreeSet<Integer> sectionHeader = new TreeSet<>();
    /**
     * Permet de creer les deux View à afficher dans la ListView selon que ce soit une séquence ou un exercice
     */
    private LayoutInflater m_inflater;
    private static final int TYPE_SEPARATOR = 0;
    private static final int TYPE_ITEM = 1;
    /**
     * Stocke l'index de l'item qui à le focus
     */
    private int mfocusPosition=0;


    /**
     * Constructeur
     * <p>Initialise le LayoutInflater avec le context de l'application</p>
     * @param context
     *      Context de l'application
     *
     *@see com.stephane.rothen.rchrono.CustomAdapter#m_inflater
     */
    public CustomAdapter(Context context) {
        m_inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    /**
     * renvois le nombre d'éléments à afficher
     * @return
     *      nombre d'éléments à afficher
     *@see com.stephane.rothen.rchrono.CustomAdapter#m_Data
     */
    @Override
    public int getCount() {
        return m_Data.size();
    }

    /**
     * Ajoute un item dans le tableau de donnée
     * @param item
     *      item représentant un Exercice
     *@see com.stephane.rothen.rchrono.CustomAdapter#m_Data
     * @see com.stephane.rothen.rchrono.Exercice
     *
     */
    public void addItem(final String item)
    {
        m_Data.add(item);
        notifyDataSetChanged();
    }

    /**
     * Ajoute un item de section dans le tableau de donnée
     * @param item
     *      item représentant une Sequence
     *@see com.stephane.rothen.rchrono.CustomAdapter#m_Data
     * @see com.stephane.rothen.rchrono.Sequence
     */
    public void addSectionHeaderItem(final String item)
    {
        m_Data.add(item);
        sectionHeader.add(m_Data.size()-1);
        notifyDataSetChanged();

    }

    /**
     * Définit l'index de l'item qui a le focus
     * @param focus
     * index de l'item
     */
    public void setFocusPosition(int focus)
    {
        mfocusPosition=focus;
    }

    /**
     * Vide le tableau de donnée
     * @see com.stephane.rothen.rchrono.CustomAdapter#m_Data
     */
    public void deleteAll()
    {
        m_Data.clear();
        sectionHeader.clear();
    }

    /**
     * retourne le nombre de type de View
     * @return
     *      2
     */
    @Override
    public int getViewTypeCount() {
        return 2;
    }

    /**
     * Renvois le type de View en fonction de la position
     * @param position
     *      position dans m_Data
     * @return
     *      type de View
     *
     *@see com.stephane.rothen.rchrono.CustomAdapter#TYPE_ITEM
     * @see com.stephane.rothen.rchrono.CustomAdapter#TYPE_SEPARATOR
     */
    @Override
    public int getItemViewType(int position) {
        return sectionHeader.contains(position)?TYPE_SEPARATOR:TYPE_ITEM;
    }

    /**
     * Renvois la valeur de l'item à la position donnée
     * @param position
     *      position de l'item dans m_Data
     * @return
     *      String contenant l'item récupéré
     */
    @Override
    public Object getItem(int position) {
        return m_Data.get(position);
    }


    /**
     * Renvois l'id de l'item à la position donnée
     * @param position
     *      position dans m_Data
     * @return
     *      id de l'item
     */
    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * Renvois la View à ajouter à la ListView
     * @param position
     *      position dans m_Data
     * @param convertView
     *      View
     * @param parent
     * @return
     *      View initialisée
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int rowType = getItemViewType(position);
        ViewHolder holder =null;

            holder=new ViewHolder();
            switch (rowType)
            {
                case TYPE_ITEM:
                    if(position == mfocusPosition)
                    {
                        convertView = m_inflater.inflate(R.layout.lv_exercice_focused_layout,null);
                    }
                    else
                    {
                        convertView = m_inflater.inflate(R.layout.lv_exercice_layout,null);
                    }
                    holder.textView=((TextView)convertView.findViewById(R.id.txtLvExercice));
                    break;
                case TYPE_SEPARATOR:
                    convertView = m_inflater.inflate(R.layout.lv_seq_layout,null);
                    holder.textView=((TextView)convertView.findViewById(R.id.txtLvSequence));
                    break;
                default:
                    break;
            }
        holder.textView.setText(m_Data.get(position));
        return convertView;
    }

    /**
     * Classe contenant le textView de la vue à afficher
     * @see com.stephane.rothen.rchrono.CustomAdapter#getView(int, android.view.View, android.view.ViewGroup)
     */
    public static class ViewHolder{
        public TextView textView;
    }
}
