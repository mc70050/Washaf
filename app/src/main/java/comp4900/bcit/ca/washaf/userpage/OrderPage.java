package comp4900.bcit.ca.washaf.userpage;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import comp4900.bcit.ca.washaf.R;
import comp4900.bcit.ca.washaf.User;
import comp4900.bcit.ca.washaf.userpage.fragments.CustomerAccountFrag;
import comp4900.bcit.ca.washaf.userpage.fragments.CustomerMainFrag;
import comp4900.bcit.ca.washaf.userpage.fragments.CustomerOrderFrag;
import comp4900.bcit.ca.washaf.userpage.fragments.OrderFrag;

public class OrderPage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(OrderPage.this, Chatbox.class);
                startActivity(i);
            }
        });


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        user = (User) getIntent().getExtras().getSerializable("user");
        loadOrderFirst();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
            startActivity(new Intent(this, CustomerPage.class).putExtras(saveDataToFragment()));
            overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.customer_page, menu);
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

        if (id == R.id.nav_order_bag) {
            Log.d("Navi", "clicked main");
            loadMain();
        } else if (id == R.id.nav_order) {
            // Handle the camera action
            Log.d("Navi", "clicked order");
            loadOrder();
        } else if (id == R.id.nav_logout) {
            Log.d("Navi", "clicked logout");
            Toast.makeText(getBaseContext(), "You have been successfully logged out.", Toast.LENGTH_LONG).show();
            FirebaseAuth.getInstance().signOut();
            finish();
        } else if (id == R.id.nav_account) {
            Log.d("Navi", "clicked account");
            loadAccount();
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
            CustomerMainFrag f = new CustomerMainFrag();
            f.setArguments(saveDataToFragment());
            ft.replace(R.id.customer_content, f, "tag");
        } else if (!(fm.findFragmentByTag("tag") instanceof CustomerMainFrag)) {
            CustomerMainFrag f = new CustomerMainFrag();
            f.setArguments(saveDataToFragment());
            ft.replace(R.id.customer_content, f, "tag");
        } else {

        }

        ft.commit();
    }

    private void loadOrder() {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        if (fm.findFragmentByTag("tag") == null) {
            CustomerOrderFrag f = new CustomerOrderFrag();
            f.setArguments(saveDataToFragment());
            ft.replace(R.id.customer_content, f, "tag");
        } else if (!(fm.findFragmentByTag("tag") instanceof CustomerOrderFrag)) {
            Log.d("loadOrder", "fragment is not main");
            CustomerOrderFrag f = new CustomerOrderFrag();
            f.setArguments(saveDataToFragment());
            ft.replace(R.id.customer_content, f, "tag");
        } else {

        }

        ft.commit();
    }

    private void loadAccount() {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        if (fm.findFragmentByTag("tag") == null) {
            CustomerAccountFrag f = new CustomerAccountFrag();
            f.setArguments(saveDataToFragment());
            ft.replace(R.id.customer_content, f, "tag");
        } else if (!(fm.findFragmentByTag("tag") instanceof CustomerAccountFrag)) {
            Log.d("loadOrder", "fragment is not main");
            CustomerAccountFrag f = new CustomerAccountFrag();
            f.setArguments(saveDataToFragment());
            ft.replace(R.id.customer_content, f, "tag");
        } else {

        }

        ft.commit();
    }

    private void loadOrderFirst() {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        if (fm.findFragmentByTag("tag") == null) {
            OrderFrag f = new OrderFrag();
            f.setArguments(saveDataToFragment());
            ft.replace(R.id.customer_content, f, "tag");
        } else if (!(fm.findFragmentByTag("tag") instanceof OrderFrag)) {
            Log.d("loadOrder", "fragment is not main");
            OrderFrag f = new OrderFrag();
            f.setArguments(saveDataToFragment());
            ft.replace(R.id.customer_content, f, "tag");
        } else {

        }

        ft.commit();
    }
}
