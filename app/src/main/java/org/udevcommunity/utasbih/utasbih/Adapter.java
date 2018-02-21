package org.udevcommunity.utasbih.utasbih;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Taym on 26/11/2015.
 */
class Adapter extends ArrayAdapter<String>{


    public Adapter(Context context, String[] tasbih) {
        super(context,R.layout.costom_row, tasbih);

    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater= LayoutInflater.from(getContext());

        View view=inflater.inflate(R.layout.costom_row, parent, false);

        String s = getItem(position);
        TextView textView=(TextView)view.findViewById(R.id.salatTypeTV);
        ImageView imageView=(ImageView)view.findViewById(R.id.imageView);
        //
        textView.setText(s);
        imageView.setImageResource(R.drawable.mosque);
        return view;
    }
}