package com.crm.elmolino.tracker_crm;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class Cliente extends AppCompatActivity{



    //SEARCH VIEW
    private SearchView searchView;
    ListView lista;

    ArrayAdapter<String> modeAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cliente);



        lista =(ListView) findViewById(R.id.lista);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        String[] stringArray = new String[] { "Victor", "Juan" ,"Christian","Raul","aa","bbc","abcsa" };
        modeAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, android.R.id.text1, stringArray);
        lista.setAdapter(modeAdapter);

        lista.setTextFilterEnabled(true);



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Toast.makeText(Cliente.this, "ADDING USER", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getApplicationContext(), AddCliente2.class);
                //intent.putExtra("name", query);
                //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                getApplicationContext().startActivity(intent);



                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                //        .setAction("Action", null).show();
            }
        });
    }



    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        getMenuInflater().inflate( R.menu.menu_main, menu);


        //getMenuInflater().inflate(R.menu.menu_main, menu);

        MenuItem item = menu.findItem(R.id.action_search);
        //searchView = (SearchView) item.getActionView();

        //searchView.setMenuItem(item);


        ActionBar actionbar = getSupportActionBar ();
        actionbar.setDisplayHomeAsUpEnabled ( true );
        actionbar.setHomeAsUpIndicator ( R.drawable.menu_11_back );

        //searchView.setIconifiedByDefault(false);
        //searchView.setOnQueryTextListener(this);
        //searchView.setSubmitButtonEnabled(true);
        //searchView.setQueryHint("Search Here");




        searchView = (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String s) {

                modeAdapter.getFilter().filter(s);
                return true;
            }
        });


        return true;




    }



    /*
    @Override
    public void onBackPressed() {
        if (searchView.isSearchOpen()) {
            searchView.closeSearch();

        } else {
            super.onBackPressed();
        }
    }*/


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


}
