package com.crm.elmolino.tracker_crm;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import static java.util.Arrays.asList;

public class AddCliente extends AppCompatActivity {

    ListView lista;

    ListAdapter adaptador;

    boolean iniciar = true;

    View vista;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_cliente);


        lista = (ListView)findViewById(R.id.lista);
        //GridView gridview = new GridView(activity);

        adaptador = new ListAdapter(getApplicationContext());
        lista.setAdapter(adaptador);





        Spinner spinner = new Spinner(getApplicationContext());





        //Spinner spinner = (Spinner) findViewById(R.id.spinner);
        //Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.distritos, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        lista.addFooterView(spinner);


        Button guardar = new Button(getApplicationContext());
        guardar.setText("GUARDAR");

        lista.addFooterView(guardar);



        /*
        final LayoutInflater layoutInflater = LayoutInflater.from(getApplicationContext());
        vista = layoutInflater.inflate(R.layout.menu_add, null);


        ImageButton minus = (ImageButton)vista.findViewById(R.id.add);
        //minus.setTag(1,"minus");
        //minus.setTag(2,telf_pos);

        minus.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {


                Toast.makeText(AddCliente.this, "CLICK!", Toast.LENGTH_SHORT).show();

                /*
                Toast.makeText(AddCliente.this,v.getTag().toString() , Toast.LENGTH_SHORT).show();

                if(v.getTag("1").toString()=="minus"){

                    int pos =Integer.parseInt(v.getTag("2").toString());
                    icons.remove(pos);
                    names.remove(pos);
                    icons_func.remove(pos);

                    telf_pos--;

                    adaptador.notifyDataSetChanged();

                }
                else{
                    Toast.makeText(AddCliente.this,"NO ES MINUS" , Toast.LENGTH_SHORT).show();
                }




            }

        });*/


    }




    public class ListAdapter extends BaseAdapter {
        private Context mContext;

        public ListAdapter(Context c) {
            mContext = c;
        }

        public int getCount() {
            return names.size();
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
                convertView = layoutInflater.inflate(R.layout.menu_add, null);


            }
            //else {
            //    imageView = (ImageButton) convertView;
            //}

            vista = convertView;



            final ImageView icon = (ImageView)convertView.findViewById(R.id.icon);
            final ImageButton icon2 = (ImageButton)convertView.findViewById(R.id.add);
            final TextView name = (TextView)convertView.findViewById(R.id.name);


            icon.setImageResource(icons.get(position));
            name.setHint(names.get(position));

            icon2.setImageResource(icons_func.get(position));

            icon2.setTag(R.id.name,names.get(position));

            icon2.setTag(R.id.position,position);

            //name.setInputType(InputType.TYPE_CLASS_PHONE);


            icon2.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {


                    //Toast.makeText(mContext, v.getTag(R.id.name).toString()+v.getTag(R.id.position).toString(), Toast.LENGTH_SHORT).show();
                    int pos =Integer.parseInt(v.getTag(R.id.position).toString());

                    if(v.getTag(R.id.name)!=null){


                        //Toast.makeText(mContext, v.getTag(R.id.name).toString(), Toast.LENGTH_SHORT).show();

                        if(v.getTag(R.id.name).toString()=="Telefono"){



                            icons.add(pos+1,icons.get(pos));
                            names.add(pos+1,"Otro Telefono");

                            icons_func.add(pos+1,R.drawable.add_minus);


                            //LinearLayout ln=(LinearLayout)vista.findViewById(R.id.entrada);


                            adaptador.notifyDataSetChanged();
                        }

                        if(v.getTag(R.id.name).toString()=="Otro Telefono"){

                            icons.remove(pos);
                            names.remove(pos);
                            icons_func.remove(pos);
                            adaptador.notifyDataSetChanged();

                        }


                    }




                        /*
                        LinearLayout ln=(LinearLayout)vista.findViewById(R.id.entrada);
                        ImageButton minus = new ImageButton(getApplicationContext());
                        minus.setMaxHeight(50);
                        minus.setMaxWidth(50);
                        minus.setScaleType(ImageView.ScaleType.FIT_CENTER);
                        minus.setImageResource(R.drawable.add_minus);
                        */




                    //ln.addView(minus);
                    //minus.setTag(telf_pos);
                }
            });




            return convertView;
        }



        private List<Integer> icons = new ArrayList<Integer>(asList(

                R.drawable.add_user,
                R.drawable.add_user,
                R.drawable.add_user,
                R.drawable.add_user,
                R.drawable.add_phone,
                R.drawable.add_location));


        private List<Integer> icons_func = new ArrayList<Integer>(asList(

                R.drawable.add_ampliar,
                R.drawable.add_ampliar,
                R.drawable.add_ampliar,
                R.drawable.add_ampliar,
                R.drawable.add_plus,
                R.drawable.add_ampliar));




        private List<String> names = new ArrayList<String>(asList(
                "Nombre",
                "Segundo Nombre",
                "Apellido",
                "Segundo Apellido",
                "Telefono",
                "Direccion"));




        /*
        private Integer[] icons = {
                R.drawable.add_user,
                R.drawable.add_phone,
                R.drawable.add_location


        };*/


        /*
        private String[] names = {
                "Nombre",
                "Telefono",
                "Direccion"

        };
        */



    }

}
