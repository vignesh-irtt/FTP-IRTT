package com.vignesh.study;

import android.app.Fragment;
import android.app.FragmentManager;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;


public class MainActivity extends ActionBarActivity
        implements NavigationDrawerCallbacks {
    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;
    private Toolbar mToolbar;
    FragmentManager fragmentManager = getFragmentManager();
    Fragment fr=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
        try {
            setSupportActionBar(mToolbar);
            getSupportActionBar().setTitle("Home");
            fragmentManager.beginTransaction().replace(R.id.container, fr= new Home_Fragment()).commit();
        }
        catch (Exception e) {
                //Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_LONG).show();
        }
        mNavigationDrawerFragment = (NavigationDrawerFragment)
                    getFragmentManager().findFragmentById(R.id.fragment_drawer);

        // Set up the drawer.
        mNavigationDrawerFragment.setup(R.id.fragment_drawer, (DrawerLayout) findViewById(R.id.drawer), mToolbar);
        // populate the navigation drawer
        mNavigationDrawerFragment.setUserData("Vignesh Palanisamy", "mail2pvicky@gmail.com", BitmapFactory.decodeResource(getResources(), R.drawable.user));
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        try
        {
            switch (position)
            {
                case 0:
                    fragmentManager.beginTransaction().replace(R.id.container, fr= new Home_Fragment()).commit();
                    getSupportActionBar().setTitle("Home");
                    break;
                case 1:
                    fragmentManager.beginTransaction().replace(R.id.container, fr = new News_Fragment()).commit();
                    getSupportActionBar().setTitle("News");
                    break;
                case 2:
                    fragmentManager.beginTransaction().replace(R.id.container, fr = new ItemFragment()).commit();
                    getSupportActionBar().setTitle("Files");
                    break;
            }
        }
        catch (Exception e)
        {

           // Toast.makeText(this,e.toString(),Toast.LENGTH_LONG).show();
        }
    }
    @Override
    public void onBackPressed() {
        if (mNavigationDrawerFragment.isDrawerOpen())
            mNavigationDrawerFragment.closeDrawer();
        else
            super.onBackPressed();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
            return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings)
        {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}