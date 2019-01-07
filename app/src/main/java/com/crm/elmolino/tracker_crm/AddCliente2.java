package com.crm.elmolino.tracker_crm;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Base64;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static java.util.Arrays.asList;

public class AddCliente2 extends AppCompatActivity {

    private ExpandableListView listView;
    private ExpandableListAdapter listAdapter;
    private List<String> listDataHeader;
    private List<List<Datos>> listdata;
    private List<Boolean> activos;
    private List<Integer> icons;

    private List<String> lista_distritos;
    private List<String> lista_departamentos;
    private List<String> lista_provincias;

    Map<String, List<String>> depart_prov;
    Map<String, List<String>> prov_dist;

    List<String> aqp_distritos;

    private LatLng last_ubication=null;

    Spinner distritos;

    int cont_direccion = 1;

    int x = 0;
    int y = 0;
    //Dialog dialog;
    ImageView foto_actual;
    ImageView foto;

    String mCurrentPhotoPath;

    Dialog dialogo_m;

    View view_text;
    LayoutInflater inflater;

    HttpRequest solicitud;


    class Datos {

        View object=null;
        ArrayAdapter<String> adapter=null;
        String name="";
        String value="";
        String hint="";
        Bitmap icon;

        Boolean enable;


        public void setEnable(Boolean enable) {
            this.enable = enable;
        }

        public Datos(List<String> data, Boolean enable) {
            this.name="spinner";
            final Spinner distritos = new Spinner(getApplicationContext());
            adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, data);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            distritos.setAdapter(adapter);
            distritos.setEnabled(enable);




            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.height=150;
            distritos.setLayoutParams(params);

            object = distritos;



        }

        public Datos(String data) {
            this.name="texto";
            this.hint=data;

        }



        public Datos(Integer data) {
            this.name="foto";

            icon = BitmapFactory.decodeResource(getApplicationContext().getResources(),data);

            ImageView imageview = new ImageView(getApplicationContext());

            imageview.setAdjustViewBounds(true);
            imageview.setScaleType(ImageView.ScaleType.FIT_CENTER);
            imageview.setMaxWidth(400);
            imageview.setMaxHeight(400);

            imageview.setImageResource(data);

            object = imageview;


        }


    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_cliente1);



        //LayoutInflater inflater = (LayoutInflater)getApplicationContext() .getSystemService(Context.LAYOUT_INFLATER_SERVICE);




        listView = (ExpandableListView) findViewById(R.id.lvExp);
        listView.setDivider(null);
        listView.setDividerHeight(0);
        //listView.setDescendantFocusability(ViewGroup.FOCUS_AFTER_DESCENDANTS);


        initData();
        listAdapter = new ExpandableListAdapter(this);
        listView.setAdapter(listAdapter);

        listAdapter.notifyDataSetChanged();


        solicitud = new HttpRequest();

        solicitud.setActivity(getApplicationContext());

        //dialog = new Dialog (AddCliente2.this);


        Button guardar = new Button(getApplicationContext());
        guardar.setText("GUARDAR");



        guardar.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {

                   //NOMBRE
                   String nombre = "";
                   final String id_cliente;
                   final String url_cliente = "http://192.168.1.152:8080/cliente";
                   final String url_telefono = "http://192.168.1.152:8080/cliente_telefono";
                   final String url_direccion = "http://192.168.1.152:8080/cliente_direccion";


                   //List<JSONObject> data = new ArrayList<JSONObject>();

                   //GUARDAR
                   for (int i = 0; i < listdata.get(0).size(); i++) {
                       nombre = nombre + listdata.get(0).get(i).value + " ";
                   }

                   boolean detener = true;
                   int i = 1;


                   JSONObject json_cliente = new JSONObject();
                   try {
                       json_cliente.put("nombre", nombre);
                       json_cliente.put("activo", true);
                       solicitud.setData_cliente(json_cliente);
                       //jsonObject.put("longitud",location.getLongitude());

                   } catch (JSONException e) {
                       Toast.makeText(getApplicationContext(), "PARSE ERROR CLIENTE", Toast.LENGTH_SHORT).show();
                       // handle exception
                   }



                   //TELEFONOS
                   //List<String> telefonos = new ArrayList<String>();

                   List<JSONObject> json_telefonos = new ArrayList<JSONObject>();

                   while (detener) {
                       //telefonos.add(listdata.get(i).get(0).value);

                       JSONObject json_telefono = new JSONObject();
                       try {
                           json_telefono.put("numero", listdata.get(i).get(0).value);
                           json_telefono.put("activo", true);
                           json_telefonos.add(json_telefono);
                           //jsonObject.put("longitud",location.getLongitude());
                       } catch (JSONException e) {
                           Toast.makeText(getApplicationContext(), "PARSE ERROR TELEFONO", Toast.LENGTH_SHORT).show();
                           // handle exception
                       }


                       i++;


                       if (listdata.get(i).get(0).hint != "Telefono") {
                           detener = false;
                       }
                   }


                   solicitud.setData_telefonos(json_telefonos);



                   //DIRECCIONES

                   List<JSONObject> json_direcciones = new ArrayList<JSONObject>();
                   for(;i<listdata.size();i++){

                       //FOTO
                       String foto = "";
                       final ImageView imagen = (ImageView) listdata.get(i).get(0).object;
                       Bitmap bitmap = ((BitmapDrawable) imagen.getDrawable()).getBitmap();

                       ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                       bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                       byte[] byteArray = byteArrayOutputStream.toByteArray();
                       foto = Base64.encodeToString(byteArray, Base64.DEFAULT);


                       //UBIGEO
                       String ubigeo = "0401";
                       final Spinner distritos = (Spinner) listdata.get(i).get(3).object;
                       int pos = distritos.getSelectedItemPosition();
                       if (pos <= 9) {
                           ubigeo = ubigeo + "0" + pos;
                       } else {
                           ubigeo = ubigeo + pos;
                       }

                       //COORDENADAS
                       String lat = "";
                       String lng = "";

                       String coordenadas = listdata.get(i).get(4).value.trim();
                       String[] separated = coordenadas.split(",");
                       lat = separated[0];
                       lng = separated[1];


                       //DESCRIPCION
                       String descripcion = "";
                       descripcion = listdata.get(i).get(5).value.trim();


                       JSONObject json_direccion = new JSONObject();
                       try {
                           json_direccion.put("id_ubigeo", ubigeo);
                           json_direccion.put("foto", foto);
                           json_direccion.put("latitud", lat);
                           json_direccion.put("longitud", lng);
                           json_direccion.put("descripcion", descripcion);
                           json_direccion.put("activo", true);

                           json_direcciones.add(json_direccion);
                           //jsonObject.put("longitud",location.getLongitude());

                       } catch (JSONException e) {
                           Toast.makeText(getApplicationContext(), "PARSE ERROR DIRECCION", Toast.LENGTH_SHORT).show();
                           // handle exception
                       }
                   }

                   solicitud.setData_direcciones(json_direcciones);


                   solicitud.crear_cliente(url_cliente,new HttpRequest.VolleyCallback() {
                               @Override
                               public void onSuccessResponse(String id) {


                                   Toast.makeText(AddCliente2.this, "Cliente creado "+id, Toast.LENGTH_SHORT).show();




                                   //GUARDANDO TELEFONO
                                   for(int i=0; i<solicitud.getData_telefonos().size(); i++){


                                       try {
                                           solicitud.getData_telefonos().get(i).put("id_cliente",id);

                                       } catch (JSONException e) {
                                           //e.printStackTrace();
                                       }

                                       solicitud.guardar(url_telefono,0,i);

                                   }


                                   //GUARDANDO DIRECCIONES
                                   for(int i=0; i<solicitud.getData_direcciones().size(); i++){

                                       try {
                                           solicitud.getData_direcciones().get(i).put("id_cliente",id);

                                       } catch (JSONException e) {
                                           //e.printStackTrace();
                                       }

                                       solicitud.guardar(url_direccion,1,i);
                                   }
                           }
                   }
                   );









               }

        });


        listView.addFooterView(guardar);


        dialogo_m = new Dialog(AddCliente2.this);

    }


    private void initData() {
        listDataHeader = new ArrayList<>();
        //activos = new ArrayList<>();
        listdata = new ArrayList<>();

        icons = new ArrayList<Integer>(asList(
                R.drawable.add_user,
                R.drawable.add_phone,
                R.drawable.add_location));

        listDataHeader.add("Datos Personales");
        listDataHeader.add("Telefono");
        listDataHeader.add("Direccion");


        //activos.add(true);
        //activos.add(true);
        //activos.add(true);



        EditText texto;


        List<Datos> datos_personales = new ArrayList<Datos>();
        datos_personales.add(new Datos("Nombre"));


        datos_personales.add(new Datos("Apellido"));


        datos_personales.add(new Datos("Segundo Apellido"));

        List<Datos> telefonos = new ArrayList<Datos>();
        texto = new EditText(getApplicationContext());texto.setText("Telefono");
        telefonos.add(new Datos("Telefono"));



        //final Spinner distritos = new Spinner(getApplicationContext());


        //distritos.setAdapter(adapter);
        //distritos.setEnabled(enable);
        //object = distritos;

        ArrayAdapter adapter;

        lista_departamentos = new ArrayList<String>(asList("DEPARTAMENTO","Arequipa"));

        lista_provincias = new ArrayList<String>(asList("PROVINCIA"));

        lista_distritos = new ArrayList<String>(asList("DISTRITO"));




        //HASH_LOGIC ANDROID
        depart_prov = new HashMap<String,List<String>>();
        depart_prov.put("Arequipa",new ArrayList<String>(asList("PROVINCIA","Arequipa")));
        //depart_prov.put("Lima",new ArrayList<String>(asList("PROVINCIA","Callao", "Molina", "etc")));
        //depart_prov.put("Tacna",new ArrayList<String>(asList("PROVINCIA","Angamos", "Arica", "Tarapaca")));
        prov_dist = new HashMap<String, List<String>>();
        aqp_distritos = Arrays.asList(getResources().getStringArray(R.array.distritos));
        prov_dist.put("Arequipa",new ArrayList<String>(aqp_distritos));



        List<Datos> direccion = new ArrayList<Datos>();
        direccion.add(new Datos(R.drawable.panaderia));
        direccion.add(new Datos(lista_departamentos,true));
        direccion.add(new Datos(lista_provincias,false));
        direccion.add(new Datos(lista_distritos,false));

        //HEREEEEE

        direccion.add(new Datos("Coordenadas"));
        //direccion.add(new Datos("Coordenadas",R.drawable.menu_7_check_in));
        direccion.add(new Datos("Descripccion"));


        listdata.add(datos_personales);
        listdata.add(telefonos);
        listdata.add(direccion);


    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        switch(requestCode) {
            case 0:
                if(resultCode == RESULT_OK){

                    //Bitmap photo = (Bitmap) data.getExtras().get("data");

                    //ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                    //photo.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

                    //foto_actual.setImageBitmap(photo);

                    Bitmap photo = null;

                    try {




                        photo = MediaStore.Images.Media.getBitmap(this.getContentResolver(), Uri.parse(mCurrentPhotoPath));


                        //ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                        //photo.compress(Bitmap.CompressFormat.JPEG, 10, bytes);


                        //photo = Bitmap.createBitmap(photo, 0, 0, 400, 400);
                        //foto_actual.setImageBitmap(photo);
                        foto.setImageBitmap(Bitmap.createScaledBitmap(photo, 300, 400, false));


                    } catch (IOException e) {
                        //e.printStackTrace();
                    }


                }

                break;
            case 1:
                if(resultCode == RESULT_OK){

                    Uri imageUri = data.getData();
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                        foto_actual.setImageBitmap(bitmap);


                    } catch (IOException e) {

                        //e.printStackTrace();
                    }


                }
                break;
        }
    }


    public class ExpandableListAdapter extends BaseExpandableListAdapter {
        private Context context;


        private File createImageFile() throws IOException {
            // Create an image file name
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String imageFileName = "JPEG_" + timeStamp + "_";

            File storageDir = getApplicationContext().getExternalFilesDir(null);


            //File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            File image = File.createTempFile(
                    imageFileName,  // prefix
                    ".jpg",         // suffix
                    storageDir      // directory
            );

            // Save a file: path for use with ACTION_VIEW intents
            mCurrentPhotoPath = "file:" + image.getAbsolutePath();
            return image;
        }

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
            String headerTitle = (String) getGroup(i);
            if (view == null) {
                LayoutInflater inflater = LayoutInflater.from(context);
                //LayoutInflater inflater = (LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.list_direction, null);
            }

            final TextView lblListHeader = (TextView) view.findViewById(R.id.lblListHeader);
            lblListHeader.setTypeface(null, Typeface.BOLD);

            lblListHeader.setText(headerTitle);

            listView.expandGroup(i);


            lblListHeader.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (lblListHeader.getText().toString() == "Direccion" || lblListHeader.getText().toString() == "Otra Direccion") {

                        Toast.makeText(context, "Obteniendo Ubicacion", Toast.LENGTH_SHORT).show();
                    }


                }
            });

            /*
            ImageButton ampliar = (ImageButton) view.findViewById(R.id.ampliar);
            if (activos.get(i)) {
                ampliar.setBackgroundResource(R.drawable.add_contraer);
                listView.expandGroup(i);
            } else {
                ampliar.setBackgroundResource(R.drawable.add_ampliar);
                listView.collapseGroup(i);
            }
            */
            //ampliar.setTag(R.id.position, i);


            /*
            ImageButton get_locations = (ImageButton) view.findViewById(R.id.get_location);


            */


            /*
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
            });*/



            ImageButton add = (ImageButton)view.findViewById(R.id.add);

            add.setBackgroundResource(icons.get(i));

            add.setTag(R.id.position,i);
            add.setTag(R.id.name,headerTitle);




            add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    listView.requestFocus();
                    //listAdapter.notifyDataSetChanged();

                    int pos = Integer.valueOf(v.getTag(R.id.position).toString());


                    if(v.getTag(R.id.name).toString()=="Telefono"){

                        //String direccion = "Direccion" + pos;


                        listDataHeader.add(pos+1,"Otro Telefono");
                        //activos.add(pos+1,true);
                        List<Datos> telefono = new ArrayList<Datos>();
                        telefono.add(new Datos("Telefono"));

                        listdata.add(pos+1,telefono);
                        icons.add(pos+1,R.drawable.add_minus);

                        //listHash.put(listDataHeader.get(pos+1),direcciones);
                        listAdapter.notifyDataSetChanged();

                    }


                    if(v.getTag(R.id.name).toString()=="Otro Telefono"){

                        //Toast.makeText(context, "Removing "+pos, Toast.LENGTH_SHORT).show();
                        listDataHeader.remove(pos);
                        listdata.remove(pos);
                        //activos.remove(pos);
                        icons.remove(pos);

                        listAdapter.notifyDataSetChanged();

                    }

                    if(v.getTag(R.id.name).toString()=="Direccion"){

                        listDataHeader.add(pos+1,"Otra Direccion");
                        //activos.add(pos+1,false);
                        List<Datos> direccion = new ArrayList<Datos>();
                        direccion.add(new Datos(R.drawable.panaderia));
                        direccion.add(new Datos(lista_departamentos,true));
                        direccion.add(new Datos(lista_provincias,false));
                        direccion.add(new Datos(lista_distritos,false));

                        //direccion.add(new Datos("Coordenadas",R.drawable.menu_7_check_in));
                        direccion.add(new Datos("Coordenadas"));
                        direccion.add(new Datos("Descripcion"));

                        listdata.add(direccion);


                        icons.add(pos+1,R.drawable.add_minus);

                        //listHash.put(listDataHeader.get(pos+1),direcciones);
                        listAdapter.notifyDataSetChanged();

                    }

                    if(v.getTag(R.id.name).toString()=="Otra Direccion"){

                        //Toast.makeText(context, "Removing "+pos, Toast.LENGTH_SHORT).show();
                        listDataHeader.remove(pos);
                        listdata.remove(pos);
                        //activos.remove(pos);
                        icons.remove(pos);
                        listAdapter.notifyDataSetChanged();
                    }
                }
            });

            return view;
        }

        @Override
        public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {


            Datos actual = (Datos) getChild(i,i1);

            String headerTitle = (String) getGroup(i);


            if(actual.name=="texto") {

                //if (view == null) {

                    LayoutInflater inflater = LayoutInflater.from(context);
                    //LayoutInflater inflater = (LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    view = inflater.inflate(R.layout.list_item, null);
                //}

                final EditText texto = (EditText) view.findViewById(R.id.lblListItem);
                texto.setHint(actual.hint);
                texto.setText(actual.value);

                texto.setTag(R.id.x,i);
                texto.setTag(R.id.y,i1);

                texto.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    public void onFocusChange(View v, boolean hasFocus) {
                        if (!hasFocus) {

                            String value = texto.getText().toString();
                            int x = Integer.valueOf(texto.getTag(R.id.x).toString());
                            int y = Integer.valueOf(texto.getTag(R.id.y).toString());
                            listdata.get(x).get(y).value = value;

                        }
                    }
                });

                if(actual.hint=="Telefono") {
                    texto.setInputType(InputType.TYPE_CLASS_PHONE);

                }
                if(actual.hint=="Coordenadas"){

                    //EditText text = new EditText(getApplicationContext());
                    texto.setRawInputType(InputType.TYPE_CLASS_NUMBER |InputType.TYPE_NUMBER_FLAG_DECIMAL);
                    final LinearLayout linea = (LinearLayout) view.findViewById(R.id.linea);


                    /*
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.FILL_PARENT);
                    params.weight = 1.0f;
                    texto.setLayoutParams(params);
                    */
                    ImageButton get_location = new ImageButton(getApplicationContext());
                    get_location.setImageResource(R.drawable.menu_7_check_in);

                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    params.width=150;
                    params.height=150;

                    //params.gravity = Gravity.RIGHT;

                    // Set the ImageButton image scale type for fourth ImageButton
                    //ib4.setScaleType(ImageView.ScaleType.FIT_XY);

                    get_location.setLayoutParams(params);
                    //lista_vendedores.setLayoutParams(params);
                    //linea.addView(texto);
                    linea.addView(get_location);

                    get_location.setTag(R.id.x,i);
                    get_location.setTag(R.id.y,i1);
                    get_location.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            //AlertDialog.Builder builder = new AlertDialog.Builder(AddCliente2.this);
                            //builder.setTitle("ELEGE LA UBICACION");

                            final LocationManager manager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );

                            if (ActivityCompat.checkSelfPermission(getApplicationContext(),
                                    Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                                    ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
                                            == PackageManager.PERMISSION_GRANTED && manager.isProviderEnabled( LocationManager.GPS_PROVIDER )==true )  {




                            //int pos =  Integer.valueOf(v.getTag().toString());
                            int x = Integer.valueOf(texto.getTag(R.id.x).toString());
                            int y = Integer.valueOf(texto.getTag(R.id.y).toString());


                            final Dialog dialog = new Dialog(AddCliente2.this);


                            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            /////make map clear
                            dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                            dialog.setContentView(R.layout.dialogmap);////your custom content



                            //View contenido = LayoutInflater.from(AddCliente2.this).inflate(R.layout.dialogmap, null);
                            MapView mMapView = (MapView) dialog.findViewById(R.id.map);


                            Display display =((WindowManager)getSystemService(context.WINDOW_SERVICE)).getDefaultDisplay();
                            int width = display.getWidth();
                            int height=display.getHeight();
                            dialog.getWindow().setLayout((6*width)/7,(4*height)/5);



                            MapsInitializer.initialize(AddCliente2.this);

                            mMapView.onCreate(dialog.onSaveInstanceState());
                            mMapView.onResume();


                            //MapView mMapView = (MapView) contenido.findViewById(R.id.map);


                            final Button guardar = (Button) dialog.findViewById(R.id.guardar);
                            guardar.setTag(x);
                            guardar.setTag(y);
                            guardar.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    //int pos =  Integer.valueOf(v.getTag().toString());
                                    int x = Integer.valueOf(texto.getTag(R.id.x).toString());
                                    int y = Integer.valueOf(texto.getTag(R.id.y).toString());


                                    if(last_ubication!=null){
                                        listdata.get(x).get(y).value = last_ubication.latitude+","+last_ubication.longitude;
                                        listAdapter.notifyDataSetChanged();
                                    }


                                    dialog.dismiss();

                                    //Toast.makeText(AddCliente2.this, "GUARDANDO ", Toast.LENGTH_SHORT).show();
                                }
                            });



                            mMapView.getMapAsync(new OnMapReadyCallback() {


                                @Override
                                public void onMapReady(final GoogleMap googleMap) {

                                    googleMap.getUiSettings().setZoomControlsEnabled(true);

                                    final Marker mymarker;

                                    googleMap.setMyLocationEnabled(true);


                                    LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
                                    Criteria criteria = new Criteria();

                                    /*
                                    criteria.setAccuracy(Criteria.ACCURACY_FINE);
                                    criteria.setAltitudeRequired(false);
                                    criteria.setBearingRequired(false);
                                    criteria.setCostAllowed(true);
                                    criteria.setPowerRequirement(Criteria.POWER_LOW);
                                    */

                                    String provider = locationManager.getBestProvider(criteria, true);

                                    //Toast.makeText(context, provider, Toast.LENGTH_SHORT).show();
                                    Location location = locationManager.getLastKnownLocation(provider);




                                    //Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);





                                    if (location != null) {
                                        LatLng myPosition = new LatLng(location.getLatitude(),location.getLongitude());

                                        last_ubication = myPosition;

                                        mymarker = googleMap.addMarker(new MarkerOptions().position(myPosition).title("Posicion").draggable(false));
                                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myPosition,15));

                                        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

                                            @Override
                                            public void onMapClick(LatLng arg0) {
                                                // TODO Auto-generated method stub

                                                last_ubication = arg0;
                                                mymarker.setPosition(arg0);
                                                String position = String.valueOf(arg0.latitude)+String.valueOf(arg0.longitude);
                                                mymarker.setTitle(position);

                                                googleMap.animateCamera(CameraUpdateFactory.newLatLng(arg0));
                                                mymarker.showInfoWindow();

                                            }
                                        });



                                    }
                                    else{

                                        //Intent intent = new Intent("android.location.GPS_ENABLED_CHANGE");
                                        //intent.putExtra("enabled", true);
                                        //sendBroadcast(intent);

                                        Toast.makeText(context, "google maps no activo", Toast.LENGTH_SHORT).show();
                                    }

                                }


                            });




                                dialog.show();

                            }
                            else{
                                Toast.makeText(context, "GPS NO ACTIVADO", Toast.LENGTH_SHORT).show();
                            }




                        }
                    });


                    return linea;


                }

                return texto;
            }




            if(actual.name=="foto"){



                /*
                final ImageView foto = new ImageView(getApplicationContext());

                foto.setAdjustViewBounds(true);
                foto.setScaleType(ImageView.ScaleType.FIT_CENTER);
                foto.setMaxWidth(300);
                foto.setMaxHeight(400);

                foto.setImageBitmap(listdata.get(i).get(i1).icon);
                */

                //foto = new ImageView(getApplicationContext());

                foto = (ImageView) actual.object;

                //ImageButton get_location = new ImageButton(getApplicationContext());
                //fotodd.setImageResource(R.drawable.menu_7_check_in);


                foto.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        foto_actual = foto;
                        final AlertDialog.Builder dialogo = new AlertDialog.Builder(AddCliente2.this);
                        dialogo.setTitle("Cambiar foto");

                        final ListView opciones = new ListView(AddCliente2.this);
                        String[] stringArray = new String[]{"Tomar foto", "Escoger fotografía del álbum"};
                        ArrayAdapter<String> modeAdapter = new ArrayAdapter<String>(AddCliente2.this, android.R.layout.simple_list_item_1, android.R.id.text1, stringArray);
                        opciones.setAdapter(modeAdapter);

                        dialogo.setView(opciones);


                        dialogo.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                dialogo_m.dismiss();
                            }

                        });

                    dialogo_m = dialogo.create();
                    dialogo_m.show();
                    opciones.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view,
                                                int position, long id) {

                            Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                            if (position == 0) {

                                File photoFile = null;


                                try {
                                    photoFile = createImageFile();
                                } catch (IOException e) {
                                    Toast.makeText(AddCliente2.this, "ERROR1", Toast.LENGTH_SHORT).show();
                                    //e.printStackTrace();
                                }
                                //photoFile = new File(Environment.getExternalStorageDirectory(), "POST_IMAGE.jpg");

                                //mCurrentPhotoPath =  photoFile.getAbsolutePath();

                                //Toast.makeText(AddCliente2.this, mCurrentPhotoPath, Toast.LENGTH_SHORT).show();

                                // Continue only if the File was successfully created
                                if (photoFile != null) {

                                    //takePicture.putExtra(View, view);

                                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                                        takePicture.putExtra(MediaStore.EXTRA_OUTPUT,  Uri.fromFile(photoFile));
                                    } else {

                                        Uri photoUri = FileProvider.getUriForFile(getApplicationContext(), getApplicationContext().getPackageName() + ".provider", photoFile);
                                    takePicture.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);

                                    }

                                    //takePicture.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                                    if (takePicture.resolveActivity(getApplicationContext().getPackageManager()) != null) {
                                        startActivityForResult(takePicture, 0);//zero can be replaced with any action code
                                    }


                                }


                                //Toast.makeText(AddCliente2.this, "Abriendo la Camara", Toast.LENGTH_SHORT).show();
                            }
                            if (position == 1) {

                                Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                startActivityForResult(pickPhoto , 1);//one can be replaced with any action code

                                //Toast.makeText(AddCliente2.this, "Abriendo el album", Toast.LENGTH_SHORT).show();
                            }

                            dialogo_m.dismiss();

                        }

                    });


                }

                });


            }



            if(actual.name=="spinner"){

            if(i1==1){

                final Spinner departamento = (Spinner)actual.object;
                departamento.setSelection(1);
                departamento.setEnabled(false);



                departamento.setTag(R.id.x,i);
                departamento.setTag(R.id.y,i1);

                departamento.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                        List<String> content = depart_prov.get(departamento.getSelectedItem().toString());
                        int x = Integer.valueOf(departamento.getTag(R.id.x).toString());
                        int y = Integer.valueOf(departamento.getTag(R.id.y).toString());

                        listdata.get(x).get(y+1).setEnable(false);
                        listdata.get(x).get(y+2).setEnable(false);
                        //listdata.get(x).get(y+1).object.setEnabled(false);
                        //listdata.get(x).get(y+2).object.setEnabled(false);


                        if (content!=null){

                            listdata.get(x).get(y+1).adapter.clear();
                            listdata.get(x).get(y+1).adapter.addAll(content);
                            listdata.get(x).get(y+1).adapter.notifyDataSetChanged();

                            Spinner provincia = (Spinner)listdata.get(x).get(y+1).object;
                            provincia.setSelection(1);
                            provincia.setEnabled(false);
                        }
                        else{
                            //Toast.makeText(context, "NO TIENE CONTENIDO", Toast.LENGTH_SHORT).show();
                            listdata.get(x).get(y+1).setEnable(false);
                        }

                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> parentView) {
                    }
                });


            }


            if(i1==2){



                final Spinner provincia = (Spinner)actual.object;

                provincia.setTag(R.id.x,i);
                provincia.setTag(R.id.y,i1);

                provincia.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                        List<String> content = prov_dist.get(provincia.getSelectedItem().toString());
                        int x = Integer.valueOf(provincia.getTag(R.id.x).toString());
                        int y = Integer.valueOf(provincia.getTag(R.id.y).toString());

                        if (content!=null){

                            listdata.get(x).get(y+1).adapter.clear();
                            listdata.get(x).get(y+1).adapter.addAll(content);
                            listdata.get(x).get(y+1).adapter.notifyDataSetChanged();

                            Spinner distrito = (Spinner)listdata.get(x).get(y+1).object;
                            distrito.setSelection(0);
                            distrito.setEnabled(true);

                        }
                        else{
                            //Toast.makeText(context, "NO TIENE CONTENIDO", Toast.LENGTH_SHORT).show();
                            listdata.get(x).get(y+1).setEnable(false);
                        }

                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> parentView) {
                    }
                });



            }

            }


            return actual.object;

        }

        @Override
        public boolean isChildSelectable(int i, int i1) {
            return true;
        }
    }




}
