package com.SozioTech.posletics;

import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.material.navigation.NavigationView;

public class CustomNavigation extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        NavigationView navigationview =new NavigationView(this);
        navigationview.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new MyPoseFragment()).commit();


        return false;
    }
}
