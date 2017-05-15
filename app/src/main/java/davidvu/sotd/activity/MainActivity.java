package davidvu.sotd.activity;


import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import davidvu.sotd.R;
import davidvu.sotd.fragment.HomeFragment;
import davidvu.sotd.fragment.SkillFragment;


public class MainActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;


    // index to identify current nav menu item
    public static int navItemIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        createToolbar();

        //Check if it is Android 5.0 or higher
        if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP){
            // Call material design APIs here
        }else{
             // Material Design pre API 21
        }

        /**
         * final ActionBar ab = getSupportActionBar();
         ab.setHomeAsUpIndicator(R.drawable.ic_menu);
         ab.setDisplayHomeAsUpEnabled(true);
         */

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                menuItem.setChecked(true);
                mDrawerLayout.closeDrawers();
                return true;
            }
        });
    }

    private void createToolbar(){
        Toolbar mtoolbar = (Toolbar) findViewById(R.id.mCustomToolbar);
        setSupportActionBar(mtoolbar);
        getSupportActionBar().setTitle("Skill of the Day");
        mtoolbar.setNavigationIcon(R.drawable.ic_dehaze_black_24dp);;

        Drawable drawable = ContextCompat.getDrawable(getApplicationContext(),R.drawable.ic_sort_black_24dp);
        mtoolbar.setOverflowIcon(drawable);
    }
 ////
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     *  private Fragment getHomeFragment() {
     switch (navItemIndex) {
     case 0:
     // home
     HomeFragment homeFragment = new HomeFragment();
     return homeFragment;
     case 1:
     // photos
     SkillFragment skillFragment = new SkillFragment();
     return skillFragment;
     default:
     return new HomeFragment();
     }
     }
     */
}
