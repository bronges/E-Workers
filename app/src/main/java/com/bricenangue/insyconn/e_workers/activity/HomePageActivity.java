package com.bricenangue.insyconn.e_workers.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bricenangue.insyconn.e_workers.R;
import com.bricenangue.insyconn.e_workers.alertdialog.DialogTimeLogger;
import com.bricenangue.insyconn.e_workers.interfaces.OnDialogSelectorListener;
import com.bricenangue.insyconn.e_workers.service.UserSharedPreference;
import com.facebook.accountkit.Account;
import com.facebook.accountkit.AccountKit;
import com.facebook.accountkit.AccountKitCallback;
import com.facebook.accountkit.AccountKitError;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class HomePageActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private TextView textViewNavName, textViewNavEmail, textViewLoggedAt, textViewTotalHours;
    private ImageView profilePicture;
    private DialogTimeLogger timeLogger;
    private UserSharedPreference userSharedPreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        userSharedPreference=new UserSharedPreference(this);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        textViewLoggedAt=(TextView) findViewById(R.id.textView_hompage_logIn_timeOfDay_default_text);
        textViewTotalHours =(TextView) findViewById(R.id.textView_hompage_logIn_since_text);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        navigationView.setNavigationItemSelectedListener(this);

        View headerLayout = navigationView.getHeaderView(0);

        timeLogger =new DialogTimeLogger(this, new OnDialogSelectorListener() {
            @Override
            public void onSelectedOption(int selectedIndex) {
                switch (selectedIndex){
                    case 0:
                        //arriving. log system current time hh:mm:ss
                        Date date = new Date(System.currentTimeMillis());
                        DateFormat formatter = new SimpleDateFormat("HH:mm:ss");
                        String dateFormatted = formatter.format(date);
                        userSharedPreference.setisArrived(true);
                        textViewLoggedAt.setText(dateFormatted);



                        break;
                    case 1:
                        //stop timer until next arriving check if same day. save in preference reset everyday
                        break;
                    case 2:
                        //stop timer and save total hours
                        userSharedPreference.setisArrived(false);
                        break;
                }

            }
        });

        textViewNavEmail = (TextView) headerLayout.findViewById(R.id.textView_nav_profile_email);
         textViewNavName = (TextView) headerLayout.findViewById(R.id.textView_nav_profile_name);
         profilePicture = (ImageView) headerLayout.findViewById(R.id.imageView_nav_profile_Picture);

        AccountKit.getCurrentAccount(new AccountKitCallback<Account>() {
            @Override
            public void onSuccess(Account account) {
                //fetch user information
                if (account.getEmail()!=null){
                    Toast.makeText(getApplicationContext(), account.getEmail() + "\n  "+ account.getId(),Toast.LENGTH_SHORT).show();

                }else{
                    Toast.makeText(getApplicationContext(), account.getPhoneNumber().toString(),Toast.LENGTH_SHORT).show();
                    textViewNavName.setText(account.getPhoneNumber().toString());

                }
            }

            @Override
            public void onError(AccountKitError accountKitError) {
                Toast.makeText(getApplicationContext(),
                        accountKitError.getErrorType().getMessage(),Toast.LENGTH_SHORT).show();

            }
        });
    }

    public void onTimeLogger(View view){
        Dialog dialog = timeLogger.onCreateDialogTimeLogger(userSharedPreference.getisArrived());
        dialog.show();
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
        getMenuInflater().inflate(R.menu.home_page, menu);
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
            launchSettingsActivity();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // Handle the camera action
        } else if (id == R.id.nav_projects) {

            launchProjectManagerActivity();

        } else if (id == R.id.nav_company) {

            launchCompanyActivity();

        } else if (id == R.id.nav_settings) {

            launchSettingsActivity();

        } else if (id == R.id.nav_inbox) {

            launchInboxActivity();

        } else if (id == R.id.nav_infos) {

        } else if (id == R.id.nav_logout) {

            onLogout();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void launchProjectManagerActivity() {
        startActivity(new Intent(this,ProjectManagerActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));


    }
    private void launchCompanyActivity() {
        startActivity(new Intent(this,CompanyActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));

    }
    private void launchSettingsActivity() {
        startActivity(new Intent(this,SettingsEWorkersActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));

    }

    private void launchInboxActivity() {
        startActivity(new Intent(this,InboxActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));

    }
    private void onLogout(){
        AccountKit.logOut();
        finish();
    }


}
