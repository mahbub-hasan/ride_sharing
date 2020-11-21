package com.oss_net.choloeksathe.activities.driver;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.oss_net.choloeksathe.R;
import com.oss_net.choloeksathe.activities.RegistrationActivity;
import com.oss_net.choloeksathe.fragments.driver.CarShareMainFragment;
import com.oss_net.choloeksathe.fragments.driver.DriverHomeFragment;
import com.oss_net.choloeksathe.utils.CommonTask;

public class DriverCurrentSessionActivity extends AppCompatActivity {
    Fragment fragment ;
    FragmentTransaction fragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_current_session);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        fragment = new DriverHomeFragment();
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.driverSession, fragment);
        fragmentTransaction.commit();


    }

}
