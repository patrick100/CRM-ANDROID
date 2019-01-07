package com.crm.elmolino.tracker_crm;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;


import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.ui.IconGenerator;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;




public class Mapa extends Fragment {

    private SupportMapFragment mapFragment;

    public GoogleMap mMap;
    public Marker marker;
    private LatLngBounds.Builder bounds;

    ArrayList<Marker> marcardores_vendedores;
    ArrayList<Marker> marcardores_ruta;

    long time;

    private ListView modeList;

    String nombre;

    public Polyline line;

    double lat = 0;
    double lng = 0;

    private boolean ruta_creada;

    //String url = "http://192.168.1.152:8080/ultimas_ubicaciones";
    //String url = "http://192.168.0.10:8080/ultimas_ubicaciones";
    String url =  "http://molinomercedes.com.pe/CRM/ultimas_ubicaciones";
    String url_ruta =  "";

    Bitmap smallMarker;

    FragmentManager fm;

    private View view;

    private Dialog dialog;



    public String ParseError(VolleyError respuesta){
        String message = null;
        if (respuesta instanceof NetworkError) {
            message = "No hay Internet";
        } else if (respuesta instanceof ServerError) {
            message = "Servidor no encontrado";
        } else if (respuesta instanceof AuthFailureError) {
            message = "Coneccion a Internet";
        } else if (respuesta instanceof ParseError) {
            message = "Error de parseo";
        } else if (respuesta instanceof NoConnectionError) {
            message = "NO coneccion a Internet";
        } else if (respuesta instanceof TimeoutError) {
            message = "Connection TimeOut!";
        }

        return message;

    }


    public void clean_ruta(){

        for(int i=0; i<marcardores_ruta.size(); i++){
            marcardores_ruta.get(i).remove();
        }

        if(ruta_creada){line.remove();ruta_creada = false;}

    }

    private String getDate(long time) {
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(time);
        String date = DateFormat.format("dd-MM-yyyy HH:mm:ss", cal).toString();
        return date;
    }


    public void mostrar_marcadores(String Respuesta){

        clean_ruta();
        clean_marcadores();


        //LatLng coordenadas = new LatLng(lat, lng);
        //CameraUpdate miUbicacion = CameraUpdateFactory.newLatLngZoom(coordenadas, 16);
        try {

            JSONArray ubicaciones = new JSONArray(Respuesta);

            for (int i = 0; i < ubicaciones.length(); i++) {
                lat = Double.parseDouble(ubicaciones.getJSONObject(i).getString("latitud"));
                lng = Double.parseDouble(ubicaciones.getJSONObject(i).getString("longitud"));

                nombre = ubicaciones.getJSONObject(i).getString("name");
                //nombre = ubicaciones.getJSONObject(i).getString("name");

                time =  Long.parseLong(ubicaciones.getJSONObject(i).getString("instante"));



                MarkerOptions options = new MarkerOptions();

                IconGenerator iconFactory = new IconGenerator(getContext());
                iconFactory.setStyle(IconGenerator.STYLE_BLUE);
                options.icon(BitmapDescriptorFactory.fromBitmap(iconFactory.makeIcon(nombre)));

                options.title(getDate(time));

                options.position(new LatLng(lat,lng));
                Marker marker = mMap.addMarker(options);

                bounds.include(new LatLng(lat, lng));


                marcardores_vendedores.add(marker);
                //.icon(BitmapDescriptorFactory.fromResource(R.mipmap.player)));
            }


            mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds.build(), 50));

            //mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 12.0f));

        } catch (JSONException e) {
            Toast.makeText(getContext(), Respuesta, Toast.LENGTH_LONG).show();
            Toast.makeText(getContext(), "MAP PARSE", Toast.LENGTH_SHORT).show();
        }

    }




    public void clean_marcadores(){

        for(int i=0; i<marcardores_vendedores.size(); i++){
            marcardores_vendedores.get(i).remove();
        }

        marcardores_vendedores.clear();

    }




    public  void mostrar_ruta(String Respuesta){

        clean_marcadores();
        clean_ruta();

        try {
            JSONArray  ubicaciones = new JSONArray(Respuesta);

            PolylineOptions options = new PolylineOptions().width(10).color(Color.RED).geodesic(true);
            for (int i = 0; i < ubicaciones.length(); i++) {
                lat = Double.parseDouble(ubicaciones.getJSONObject(i).getString("latitud"));
                lng = Double.parseDouble(ubicaciones.getJSONObject(i).getString("longitud"));
                time =  Long.parseLong(ubicaciones.getJSONObject(i).getString("instante"));

                if(i==0){
                    marker = mMap.addMarker(new MarkerOptions().position(new LatLng(lat,lng)));
                    marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));
                    String temp = "INICIO "+getDate(time);

                    marker.setTitle(temp);
                    marcardores_ruta.add(marker);
                    bounds.include(new LatLng(lat, lng));
                }

                if(i==ubicaciones.length()-1){
                    marker = mMap.addMarker(new MarkerOptions().position(new LatLng(lat,lng)));
                    marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
                    String temp = "FINAL "+getDate(time);
                    marker.setTitle(temp);
                    marcardores_ruta.add(marker);
                    bounds.include(new LatLng(lat, lng));
                }




                //Marker marker = mMap.addMarker(new MarkerOptions().position(new LatLng(lat,lng)));

                //marker.setIcon(BitmapDescriptorFactory.fromBitmap(smallMarker));


                //marcardores_ruta.add(marker);

                options.add(new LatLng(lat,lng));
            }


            line = mMap.addPolyline(options);
            ruta_creada = true;


            mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds.build(), 50));



        } catch (JSONException e) {
            //e.printStackTrace();
        }


    }



    public void solicitud_marcadores(String url) {

        RequestQueue queue = Volley.newRequestQueue(this.getContext());

        StringRequest jsObjRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        mostrar_marcadores(response);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError respuesta) {

                Toast.makeText(getContext(),ParseError(respuesta), Toast.LENGTH_LONG).show();
            }
        });

        jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(
                20000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


        queue.add(jsObjRequest);

    }











    public void solicitud_ruta(String url) {

        RequestQueue queue = Volley.newRequestQueue(this.getContext());

        StringRequest jsObjRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        mostrar_ruta(response);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError respuesta) {

                Toast.makeText(getContext(),ParseError(respuesta), Toast.LENGTH_LONG).show();
            }
        });
        jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        queue.add(jsObjRequest);

    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.activity_mapa, container, false);

        //return inflater.inflate(R.layout.activity_mapa, container, false);


        view.findViewById(R.id.rutas).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {


                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Rutas de Vendedores");



                modeList = new ListView(getActivity());
                String[] stringArray = new String[] { "Victor", "Juan" ,"Christian","Raul","Gamero","Valdivia","Pancca"};
                ArrayAdapter<String> modeAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, android.R.id.text1, stringArray);
                modeList.setAdapter(modeAdapter);


                // ListView Item Click Listener

                builder.setView(modeList);
                dialog = builder.create();

                dialog.show();



                modeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {


                int id_position     = position+1;

                if(id_position<=4){
                    url_ruta = "http://molinomercedes.com.pe/CRM/ubicaciones_rango?idVendedor="+ id_position + "&horas=8";
                }
                else{

                    id_position = id_position+6;
                    url_ruta = "http://molinomercedes.com.pe/CRM/ubicaciones_rango?idVendedor="+ id_position + "&horas=8";
                }




                //Toast.makeText(getActivity().getApplicationContext(),url , Toast.LENGTH_SHORT).show();

                //Toast.makeText(getContext(), url, Toast.LENGTH_SHORT).show();
                solicitud_ruta(url_ruta);

                dialog.dismiss();




                // ListView Clicked item index


                // ListView Clicked item value
                //String  itemValue    = (String) modeList.getItemAtPosition(position);

                // Show Alert


            }

            });

            }
        });


        view.findViewById(R.id.filtros).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Toast.makeText(getContext(), "Proximamente", Toast.LENGTH_SHORT).show();
            }
        });


        view.findViewById(R.id.posiciones).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                solicitud_marcadores(url);
            }
        });






        return view;
    }




    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {

        switch (requestCode) {
            case 1:
                if ( grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //Toast.makeText(getContext(),"GPS permission granted",Toast.LENGTH_LONG).show();

                    iniciar_Mapa();
                    //getLoaderManager().initLoader(1, null,this);
                    //getActivity().getSupportLoaderManager().initLoader(0, null, );

                } else {
                    //Toast.makeText(getContext(),"GPS permission denied",Toast.LENGTH_LONG).show();
                    // show user that permission was denied. inactive the location based feature or force user to close the app
                }
                return;
        }
    }





    public void iniciar_Mapa(){

        if (mapFragment == null) {
            mapFragment = SupportMapFragment.newInstance();
            fm.beginTransaction().replace(R.id.map, mapFragment).commit();

        } else {
            mapFragment.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(final GoogleMap googleMap) {
                    mMap = googleMap;


                    if (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) ==
                            PackageManager.PERMISSION_GRANTED &&
                            ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) ==
                                    PackageManager.PERMISSION_GRANTED) {


                        final LocationManager manager = (LocationManager)  getContext().getSystemService((getContext().LOCATION_SERVICE));


                        if (! manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
                            Toast.makeText(getContext(), "NO TIENES EL GPS ACTIVADO", Toast.LENGTH_SHORT).show();
                        }


                        googleMap.setMyLocationEnabled(true);
                        //googleMap.getUiSettings().setMyLocationButtonEnabled(true);
                        //googleMap.getUiSettings().setZoomControlsEnabled(true);


                        //CAMBIAR LA POSITION



                        View locationButton = ((View) view.findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
                        // and next place it, on bottom right (as Google Maps app)
                        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)
                                locationButton.getLayoutParams();
                        // position on right bottom
                        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
                        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
                        layoutParams.setMargins(0, 0, 30, 250);



                        //clean_marcadores();


                        /*
                        mMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
                            @Override
                            public boolean onMyLocationButtonClick() {
                                Toast.makeText(getContext(), "CLICK", Toast.LENGTH_SHORT).show();
                                return false;
                            }
                        });*/


                        mMap.clear();
                        solicitud_marcadores(url);


                    }else {

                        requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},1);
                        requestPermissions(new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION},1);

                    }
                }

            });
        }



    }



    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //Bundle bundle = this.getArguments();

        //if (bundle != null) {
        //    tipo = bundle.getInt("TIPO");
        //int value2 = bundle.getInt("VALUE2", -1);
        //}


        fm = getChildFragmentManager();

        mapFragment = (SupportMapFragment) fm.findFragmentById(R.id.map);


        marcardores_vendedores = new ArrayList();
        marcardores_ruta = new ArrayList();

        bounds = new LatLngBounds.Builder();



        //mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);


        //BitmapDrawable bitmapdraw=(BitmapDrawable)getResources().getDrawable(R.drawable.dot);
        //Bitmap b=bitmapdraw.getBitmap();
        //smallMarker = Bitmap.createScaledBitmap(b, 100, 100, false);


        iniciar_Mapa();

    }





}