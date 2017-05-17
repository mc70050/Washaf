package comp4900.bcit.ca.washaf.userpage;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import comp4900.bcit.ca.washaf.R;
import comp4900.bcit.ca.washaf.User;
import comp4900.bcit.ca.washaf.userpage.fragments.CustomerAccountFrag;
import comp4900.bcit.ca.washaf.userpage.fragments.EmployeeMainFrag;

public class EmployeePage extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(EmployeePage.this, Chatbox.class);
                startActivity(i);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        user = (User)getIntent().getSerializableExtra("user");
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        loadMain();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.employee_page, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_check_order) {
            // Handle the camera action
        } else if (id == R.id.nav_logout) {
            Toast.makeText(getBaseContext(), "You have been successfully logged out.", Toast.LENGTH_LONG).show();
            EmployeeMainFrag.logout();
            FirebaseAuth.getInstance().signOut();
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private Bundle saveDataToFragment() {
        Bundle bun = new Bundle();
        bun.putSerializable("user", user);
        return bun;
    }

    private void loadMain() {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        if (fm.findFragmentByTag("tag") == null) {
            EmployeeMainFrag f = new EmployeeMainFrag();
            f.setArguments(saveDataToFragment());
            ft.replace(R.id.employee_content, f, "tag");
        } else if (!(fm.findFragmentByTag("tag") instanceof EmployeeMainFrag)) {
            Log.d("loadOrder", "fragment is not main");
            EmployeeMainFrag f = new EmployeeMainFrag();
            f.setArguments(saveDataToFragment());
            ft.replace(R.id.employee_content, f, "tag");
        } else {

        }

        ft.commit();
    }
}
