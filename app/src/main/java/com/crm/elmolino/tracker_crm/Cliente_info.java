package com.crm.elmolino.tracker_crm;

import android.support.design.widget.TabLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.MenuItem;

import android.widget.ImageView;

public class Cliente_info extends AppCompatActivity {


    private SectionsPageAdapter mSectionsPageAdapter;

    private ViewPager mViewPager;
    private TabLayout tabLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cliente_info);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        String name = getIntent().getExtras().getString("name");

        getSupportActionBar ().setTitle(name);



        mSectionsPageAdapter = new SectionsPageAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setOffscreenPageLimit(4);
        setupViewPager(mViewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);






        ImageView imageView = new ImageView(getApplicationContext());
        imageView.setImageResource(R.drawable.stories_icon);
        tabLayout.getTabAt(0).setCustomView(imageView);


        imageView = new ImageView(getApplicationContext());
        imageView.setImageResource(R.drawable.usuario_icon);
        tabLayout.getTabAt(1).setCustomView(imageView);


        imageView = new ImageView(getApplicationContext());
        imageView.setImageResource(R.drawable.bell_icon);
        tabLayout.getTabAt(2).setCustomView(imageView);

        imageView = new ImageView(getApplicationContext());
        imageView.setImageResource(R.drawable.menu2_icon);
        tabLayout.getTabAt(3).setCustomView(imageView);




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




    private void setupViewPager(ViewPager viewPager) {
        SectionsPageAdapter adapter = new SectionsPageAdapter(getSupportFragmentManager());
        adapter.addFragment(new Historias(), "Historias");
        adapter.addFragment(new Perfil(), "Perfil");
        adapter.addFragment(new Notificacion(), "Notificacion");
        adapter.addFragment(new MenuOpciones(), "MenuOpciones");
        viewPager.setAdapter(adapter);
    }



    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        getMenuInflater().inflate( R.menu.menu_cliente, menu);

        ActionBar actionbar = getSupportActionBar ();
        actionbar.setDisplayHomeAsUpEnabled ( true );
        actionbar.setHomeAsUpIndicator ( R.drawable.menu_11_back );




        return true;

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:

                onBackPressed();

                //Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                //intent.putExtra("name", query);
                //startActivity(intent);

                return true;



            default:
                return super.onOptionsItemSelected(item);
        }
    }



}
