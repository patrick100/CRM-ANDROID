package com.crm.elmolino.tracker_crm;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Usuario52 on 30/01/2018.
 */

public class Menu {


    public  Dialog dialog;

    private Activity myactivity;

    public Menu(final Activity activity){

        myactivity = activity;

        View menu_data = LayoutInflater.from(activity).inflate(R.layout.menu_grid, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        //builder.setTitle("Rutas de Vendedores");

        GridView gridview = menu_data.findViewById(R.id.gridview);
        //GridView gridview = new GridView(activity);


        gridview.setAdapter(new Menu.ImageAdapter(activity));




        builder.setView(gridview);
        dialog = builder.create();

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {

                if(position==1){

                    Intent intent = new Intent(myactivity, Cliente.class);
                     //intent.putExtra("name", query);
                    myactivity.startActivity(intent);

                }

                //Toast.makeText(activity, "Hello" + position,
                //        Toast.LENGTH_SHORT).show();
            }
        });

    }


    public class ImageAdapter extends BaseAdapter {
        private Context mContext;

        public ImageAdapter(Context c) {
            mContext = c;
        }

        public int getCount() {
            return icons.length;
        }

        public Object getItem(int position) {
            return null;
        }

        public long getItemId(int position) {
            return 0;
        }

        // create a new ImageView for each item referenced by the Adapter
        public View getView(int position, View convertView, ViewGroup parent) {

            //ImageButton imageView;
            if (convertView == null) {

                final LayoutInflater layoutInflater = LayoutInflater.from(mContext);
                convertView = layoutInflater.inflate(R.layout.menu_value, null);


                // if it's not recycled, initialize some attributes
                //imageView = new ImageButton(mContext);
                //imageView.setLayoutParams(new GridView.LayoutParams(250, 300));
                //imageView.setScaleType(ImageButton.ScaleType.CENTER_CROP);
                //imageView.setPadding(8, 8, 8, 8);


            }
            //else {
            //    imageView = (ImageButton) convertView;
            //}


            final ImageView icon = (ImageView)convertView.findViewById(R.id.menu_icon);
            final TextView name = (TextView)convertView.findViewById(R.id.name);


            icon.setImageResource(icons[position]);
            name.setText(names[position]);

            //nameTextView.setText(mContext.getString(book.getName()));
            //authorTextView.setText(mContext.getString(book.getAuthor()));


            return convertView;

            //imageView.setImageResource(mThumbIds[position]);
            //return imageView;
        }

        // references to our images
        private Integer[] icons = {
                R.drawable.menu_1_add,
                R.drawable.menu_2_add_user,
                R.drawable.menu_3_comentario,
                R.drawable.menu_4_sale_cart,
                R.drawable.menu_5_calendar,
                R.drawable.menu_6_demo,
                R.drawable.menu_7_check_in,
                R.drawable.menu_8_price,
                R.drawable.menu_9_messenger,
                R.drawable.menu_10_receta,
                R.drawable.menu_11_back,
        };

        private String[] names = {
                "Añadir",
                "Nuevo",
                "Comentario",
                "Venta",
                "Calendario",
                "Demo",
                "Check-in",
                "Precio",
                "Mensaje",
                "Recetas",
                "Atras",

        };



    }




}