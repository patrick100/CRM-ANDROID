package com.crm.elmolino.tracker_crm;

import android.content.Context;
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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by Usuario52 on 31/01/2018.
 */

public class HttpRequest {

    public String id="";

    Context activity;

    public String rpta="";


    public boolean id_obtenido = false;

    public JSONObject data_cliente;
    public List<JSONObject> data_telefonos;
    public List<JSONObject> data_direcciones;

    public JSONObject getData_cliente() {
        return data_cliente;
    }

    public void setData_cliente(JSONObject data_cliente) {
        this.data_cliente = data_cliente;
    }

    public List<JSONObject> getData_telefonos() {
        return data_telefonos;
    }

    public void setData_telefonos(List<JSONObject> data_telefonos) {
        this.data_telefonos = data_telefonos;
    }

    public List<JSONObject> getData_direcciones() {
        return data_direcciones;
    }

    public void setData_direcciones(List<JSONObject> data_direcciones) {
        this.data_direcciones = data_direcciones;
    }



    public Context getActivity() {
        return activity;
    }

    public void setActivity(Context activity) {
        this.activity = activity;
    }



    public interface VolleyCallback {
        void onSuccessResponse(String result);
    }


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


    public void crear_cliente(String url,final VolleyCallback callback){

        RequestQueue queue = Volley.newRequestQueue(activity);

        //JSONObject jsonObject = null;

        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST, url, data_cliente,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        //rpta = response.toString();
                        //Toast.makeText(activity,"LLEGO", Toast.LENGTH_SHORT).show();
                        try {
                            id = response.getString("rpta");
                            callback.onSuccessResponse(id);
                            //return rpta;

                        } catch (JSONException e) {
                            //e.printStackTrace();
                        }
                    }

                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError respuesta) {
                Toast.makeText(activity,ParseError(respuesta), Toast.LENGTH_LONG).show();
            }}){

        };

        /*
        jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        */
        queue.add(jsObjRequest);
    }




    public void guardar(String url,int tipo,int pos){

        JSONObject data=null;
        if(tipo==0){
            data = data_telefonos.get(pos);
        }
        if(tipo==1){
            data = data_direcciones.get(pos);
        }


        RequestQueue queue = Volley.newRequestQueue(activity);
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST, url, data,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        rpta = response.toString();
                        Toast.makeText(activity,rpta, Toast.LENGTH_SHORT).show();
                    }

                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError respuesta) {
                Toast.makeText(activity,ParseError(respuesta), Toast.LENGTH_LONG).show();
            }}){

        };


        jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


        queue.add(jsObjRequest);
    }

}



