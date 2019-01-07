package com.crm.elmolino.tracker_crm;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.speech.RecognizerIntent;
import android.support.design.widget.TabLayout;

import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.v4.view.ViewPager;
import android.os.Bundle;

import android.text.TextUtils;
import android.util.Log;

import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity  {

    private static final String TAG = "MainActivity";

    private SectionsPageAdapter mSectionsPageAdapter;

    private ViewPager mViewPager;

    private Mapa map;


    private Menu menu;


    //SEARCH VIEW


    private MaterialSearchView searchView;
    private TabLayout tabLayout;






    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {

        switch (requestCode) {
            case 1:
                if ( grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //Toast.makeText(getContext(),"GPS permission granted",Toast.LENGTH_LONG).show();

                    map.iniciar_Mapa();
                    //getLoaderManager().initLoader(1, null,this);
                    //getActivity().getSupportLoaderManager().initLoader(0, null, );

                } else {
                    //Toast.makeText(getContext(),"GPS permission denied",Toast.LENGTH_LONG).show();
                    // show user that permission was denied. inactive the location based feature or force user to close the app
                }
                return;
        }
    }







    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        searchviewcode();



        Log.d(TAG, "onCreate: Starting.");

        map = new Mapa();

        menu = new Menu(MainActivity.this);

        if (ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},1);
            ActivityCompat.requestPermissions(this,new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION},1);

            //googleMap.getUiSettings().setZoomControlsEnabled(true);
        }










        //getSupportActionBar().setCustomView(new CustomView(this));
        //getSupportActionBar().setDisplayShowCustomEnabled(true);

        //icons = new ArrayList();

        mSectionsPageAdapter = new SectionsPageAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);

        mViewPager.setOffscreenPageLimit(5);


        setupViewPager(mViewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);




        //tabLayout.getTabAt(1).setIcon(R.drawable.ic_account_circle_black_48dp);

        //icons = new ArrayList();





        ImageView imageView = new ImageView(getApplicationContext());
        imageView.setImageResource(R.drawable.stories_icon);
        tabLayout.getTabAt(0).setCustomView(imageView);


        imageView = new ImageView(getApplicationContext());
        imageView.setImageResource(R.drawable.usuario_icon);
        tabLayout.getTabAt(1).setCustomView(imageView);

        imageView = new ImageView(getApplicationContext());
        imageView.setImageResource(R.drawable.map_icon);
        tabLayout.getTabAt(2).setCustomView(imageView);

        imageView = new ImageView(getApplicationContext());
        imageView.setImageResource(R.drawable.bell_icon);
        tabLayout.getTabAt(3).setCustomView(imageView);

        imageView = new ImageView(getApplicationContext());
        imageView.setImageResource(R.drawable.menu2_icon);
        tabLayout.getTabAt(4).setCustomView(imageView);




        //for (int i = 0; i < tabLayout.getTabCount(); i++) {
        //    ImageView imageView = new ImageView(getApplicationContext());
        //    imageView.setImageResource(icons[i]);
        //    tabLayout.getTabAt(i).setCustomView(imageView);
        //}


        //tabLayout.getTabAt(2).setIcon(R.drawable.mapa_icon98);

        //for (int i = 0; i < tabLayout.getTabCount(); i++) {
        //    tabLayout.getTabAt(i).setIcon(R.drawable.ic_launcher_background);
        //}


        mViewPager.setCurrentItem(2);


    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                menu.dialog.show();
                return true;



            default:
                return super.onOptionsItemSelected(item);
        }
    }




    private void setupViewPager(ViewPager viewPager) {
        SectionsPageAdapter adapter = new SectionsPageAdapter(getSupportFragmentManager());
        adapter.addFragment(new Historias(), "Historias");
        adapter.addFragment(new Perfil(), "Perfil");
        adapter.addFragment(map, "Map");
        adapter.addFragment(new Notificacion(), "Notificacion");
        adapter.addFragment(new MenuOpciones(), "MenuOpciones");
        viewPager.setAdapter(adapter);
    }





    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        getMenuInflater().inflate( R.menu.menu_dash, menu);


        //getMenuInflater().inflate(R.menu.menu_main, menu);

        MenuItem item = menu.findItem(R.id.action_search);
        searchView.setMenuItem(item);


        ActionBar actionbar = getSupportActionBar ();
        actionbar.setDisplayHomeAsUpEnabled ( true );
        actionbar.setHomeAsUpIndicator ( R.drawable.plus );


        //BUSQUEDA
        /*
        searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        */
        //BUSQUEDA CON SUGERENCIAS





        return true;

    }



    @Override
    public void onBackPressed() {
        if (searchView.isSearchOpen()) {
            searchView.closeSearch();

        } else {
            super.onBackPressed();
        }
    }



    private void searchviewcode() {
        searchView = (MaterialSearchView) findViewById(R.id.search_view);

        searchView.setSuggestions(getResources().getStringArray(R.array.query_suggestions));
        searchView.setEllipsize(true);
        searchView.setVoiceSearch(true);

        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                //Toast.makeText(MainActivity.this, "Mostrando "+ query, Toast.LENGTH_SHORT).show();



                Intent intent = new Intent(getApplicationContext(), Cliente_info.class);
                intent.putExtra("name", query);
                startActivity(intent);

                searchView.closeSearch();


                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {
                //Toast.makeText(MainActivity.this, "MOSTRANDO", Toast.LENGTH_SHORT).show();
                //tabLayout.setVisibility(View.GONE);
                searchView.setVisibility(View.VISIBLE);

            }

            @Override
            public void onSearchViewClosed() {
                //Toast.makeText(MainActivity.this, "CERRANDO", Toast.LENGTH_SHORT).show();
                //tabLayout.setVisibility(View.VISIBLE);
                searchView.setVisibility(View.GONE);

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == MaterialSearchView.REQUEST_VOICE && resultCode == RESULT_OK) {
            ArrayList<String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (matches != null && matches.size() > 0) {
                String searchWrd = matches.get(0);
                if (!TextUtils.isEmpty(searchWrd)) {
                    searchView.setQuery(searchWrd, false);
                }
            }
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }






}

