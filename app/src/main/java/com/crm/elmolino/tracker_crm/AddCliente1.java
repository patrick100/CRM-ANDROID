package com.crm.elmolino.tracker_crm;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;

public class AddCliente1 extends AppCompatActivity {

    private ExpandableListView listView;
    private ExpandableListAdapter listAdapter;
    private List<String> listDataHeader;
    private List<List<Datos>> listdata;
    private List<Boolean> activos;
    private List<Integer> icons;

    int cont_direccion = 1;

    int x=0;
    int y=0;


    class Datos
    {
        String name;

        String value;

        public Datos(String name,String value)
        {
            this.name = name;
            this.value = value;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_cliente1);


        listView = (ExpandableListView)findViewById(R.id.lvExp);

        listView.setDivider(null);
        listView.setDividerHeight(0);


        initData();
        listAdapter = new ExpandableListAdapter(this);
        listView.setAdapter(listAdapter);

        listAdapter.notifyDataSetChanged();


        /*ADMIN SELECCIONE LA LISTA DE SUS VENDEDORES*/

        //TextView id = new TextView(getApplicationContext());

        //id.setText("ID_VENDEDOR");


        //Spinner lista_vendedores = new Spinner(getApplicationContext());


        //Spinner spinner = (Spinner) findViewById(R.id.spinner);
        //Create an ArrayAdapter using the string array and a default spinner layout


        //ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
        //        R.array.distritos, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        //adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Apply the adapter to the spinner
        //lista_vendedores.setAdapter(adapter);


        //LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        //params.weight = 1.0f;
        //params.gravity = Gravity.LEFT;

        //lista_vendedores.setLayoutParams(params);




        //LinearLayout lin = new LinearLayout(getApplicationContext());
        //lin.addView(lista_vendedores);
        //lin.addView(id);

        //listView.addHeaderView(lin);



        TextView empty = new TextView(this);
        empty.setHeight(150);

        listView.addFooterView(empty);


        Button siguiente = new Button(getApplicationContext());
        siguiente.setText("SIGUIENTE");


        siguiente.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

               Toast.makeText(AddCliente1.this, "Cambiando", Toast.LENGTH_SHORT).show();

               Intent intent = new Intent(AddCliente1.this, AddCliente2.class);

               //intent.putExtra("name", "patrick");
               //startActivity(intent);


               //ActivityA;
               startActivityForResult(intent, 22);

               //ActivityB;
               //intentC.addFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
               //startActivity(intentC);


           }

       });



        listView.addFooterView(siguiente);








    }


    private void initData() {
        listDataHeader = new ArrayList<>();
        activos = new ArrayList<>();
        listdata = new ArrayList<>();

        icons = new ArrayList<Integer>(asList(

                R.drawable.add_user,
                R.drawable.add_phone,
                R.drawable.add_location));



        //listHash = new HashMap<>();
        listDataHeader.add("Datos Personales");
        listDataHeader.add("Telefono");


        activos.add(true);
        activos.add(true);




        List<Datos> datos_personales = new ArrayList<Datos>();

        datos_personales.add(new Datos("Nombre",""));
        //datos_personales.add(new Datos("Segundo Nombre",""));
        datos_personales.add(new Datos("Apellido",""));
        datos_personales.add(new Datos("Segundo Apellido",""));


        List<Datos> telefonos = new ArrayList<Datos>();

        telefonos.add(new Datos("Telefono",""));


        listdata.add(datos_personales);
        listdata.add(telefonos);
    }






    public class ExpandableListAdapter extends BaseExpandableListAdapter {
        private Context context;


        public ExpandableListAdapter(Context context) {
            this.context = context;
        }




        @Override
        public int getGroupCount() {
            return listDataHeader.size();
        }

        @Override
        public int getChildrenCount(int i) {
            return listdata.get(i).size();
        }

        @Override
        public Object getGroup(int i) {
            return listDataHeader.get(i);
        }



        @Override
        public Object getChild(int i, int i1) {

            return listdata.get(i).get(i1);

            //return listHash.get(listDataHeader.get(i)).get(i1); // i = Group Item , i1 = ChildItem
        }

        @Override
        public long getGroupId(int i) {
            return i;
        }

        @Override
        public long getChildId(int i, int i1) {
            return i1;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {

            //pos_group = i;
            String headerTitle = (String)getGroup(i);
            if(view == null)
            {
                LayoutInflater inflater = LayoutInflater.from(context);
                //LayoutInflater inflater = (LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.list_group,null);
            }

            final TextView lblListHeader = (TextView)view.findViewById(R.id.lblListHeader);
            lblListHeader.setTypeface(null, Typeface.BOLD);

            lblListHeader.setText(headerTitle);



            ImageButton ampliar  = (ImageButton)view.findViewById(R.id.ampliar);
            if(activos.get(i)){
                ampliar.setBackgroundResource(R.drawable.add_contraer);
                listView.expandGroup(i);
            }
            else{
                ampliar.setBackgroundResource(R.drawable.add_ampliar);
                listView.collapseGroup(i);
            }

            ampliar.setTag(R.id.position,i);




            ampliar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int pos = Integer.valueOf(v.getTag(R.id.position).toString());
                    //Toast.makeText(context,v.getTag(R.id.position).toString() , Toast.LENGTH_SHORT).show();

                    if(activos.get(pos)) {
                        listView.collapseGroup(pos);
                        activos.set(pos,false);
                        v.setBackgroundResource(R.drawable.add_ampliar);



                    }
                    else{
                        listView.expandGroup(pos);
                        activos.set(pos,true);
                        v.setBackgroundResource(R.drawable.add_contraer);
                    }
                }
            });



            ImageButton add = (ImageButton)view.findViewById(R.id.add);

            add.setBackgroundResource(icons.get(i));

            add.setTag(R.id.position,i);
            add.setTag(R.id.name,headerTitle);




            add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    listView.requestFocus();


                    int pos = Integer.valueOf(v.getTag(R.id.position).toString());

                    if(v.getTag(R.id.name).toString()=="Telefono"){

                        //String direccion = "Direccion" + pos;


                        listDataHeader.add(pos+1,"Otro Telefono");
                        activos.add(pos+1,true);




                        List<Datos> telefono = new ArrayList<Datos>();

                        telefono.add(new Datos("Telefono",""));



                        listdata.add(pos+1,telefono);

                        icons.add(pos+1,R.drawable.add_minus);

                        //listHash.put(listDataHeader.get(pos+1),direcciones);
                        listAdapter.notifyDataSetChanged();

                    }

                    if(v.getTag(R.id.name).toString()=="Otro Telefono"){

                        Toast.makeText(context, "Removing "+pos, Toast.LENGTH_SHORT).show();
                        listDataHeader.remove(pos);
                        listdata.remove(pos);
                        activos.remove(pos);
                        icons.remove(pos);

                        listAdapter.notifyDataSetChanged();

                    }


                }
            });



            return view;
        }

        @Override
        public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {


            Datos actual = (Datos)getChild(i,i1);


            if(view == null)
            {

                LayoutInflater inflater = LayoutInflater.from(context);
                //LayoutInflater inflater = (LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.list_item,null);

                /*
                if(actual.getName()=="Latitud"){

                    LinearLayout lin = (LinearLayout)view.findViewById(R.id.items);
                    Button location = new Button(getApplicationContext());
                    location.setText("obtener");
                    lin.addView(location);
                }*/
            }

            final EditText txtListChild = (EditText)view.findViewById(R.id.lblListItem);



            txtListChild.setHint(actual.getName());
            txtListChild.setText(actual.getValue());

            if(actual.getName()=="Telefono"){
                txtListChild.setInputType(InputType.TYPE_CLASS_PHONE);
            }else{

                txtListChild.setInputType(InputType.TYPE_CLASS_TEXT);
            }

            txtListChild.setTag(R.id.x,i);
            txtListChild.setTag(R.id.y,i1);


            txtListChild.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                public void onFocusChange(View v, boolean hasFocus) {
                    if (!hasFocus) {

                    String value = txtListChild.getText().toString();
                    int x = Integer.valueOf(txtListChild.getTag(R.id.x).toString());
                    int y = Integer.valueOf(txtListChild.getTag(R.id.y).toString());
                    listdata.get(x).get(y).setValue(value);

                    }
                }
            });



            return view;
        }

        @Override
        public boolean isChildSelectable(int i, int i1) {
            return true;
        }
    }


}
