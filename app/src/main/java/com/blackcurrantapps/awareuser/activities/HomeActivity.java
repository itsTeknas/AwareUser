package com.blackcurrantapps.awareuser.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.blackcurrantapps.awareuser.R;
import com.blackcurrantapps.awareuser.util.RoundedImageView;
import com.google.firebase.database.FirebaseDatabase;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeActivity extends AppCompatActivity implements MainActivityConnect {

    private String cell = "";

    private final PrimaryDrawerItem rides = new PrimaryDrawerItem().withName("Rides").withIcon(GoogleMaterial.Icon.gmd_dashboard);
    private final PrimaryDrawerItem group = new PrimaryDrawerItem().withName("Group").withIcon(GoogleMaterial.Icon.gmd_group);

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.mainActivityRootLayout)
    CoordinatorLayout mainActivityRootLayout;
    @BindView(R.id.frame_container)
    FrameLayout frameContainer;

    private FragmentManager fragmentManager;
    private Drawer drawer = null;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);

        cell = getIntent().getStringExtra("CELL");

        if (savedInstanceState == null) {
            try {
                FirebaseDatabase.getInstance().setPersistenceEnabled(true);
            } catch (Exception ignore) {
            }
        }

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (fragmentManager == null) {
            fragmentManager = getSupportFragmentManager();
            fragmentManager.addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
                @Override
                public void onBackStackChanged() {
                    refreshbackIcon();
                }
            });
        }
        createDrawer(savedInstanceState);
    }

    private void createDrawer(Bundle savedInstanceState) {

        @SuppressLint("InflateParams") View header = getLayoutInflater().inflate(R.layout.header_name_email, null);

        TextView name = (TextView) header.findViewById(R.id.name);
        RoundedImageView profilePic = (RoundedImageView) header.findViewById(R.id.profilePic);
        name.setText(cell);

        DrawerBuilder drawerBuilder;

        drawerBuilder = new DrawerBuilder()
                .withActivity(HomeActivity.this)
                .withToolbar(toolbar)
                .withHeader(header)
                .withSelectedItem(0)
                .addDrawerItems(
                        rides,
                        group
                )
                .withOnDrawerNavigationListener(new Drawer.OnDrawerNavigationListener() {
                    @Override
                    public boolean onNavigationClickListener(View clickedView) {
                        onUpwardNavigation();
                        return true;
                    }
                })
                .withSavedInstance(savedInstanceState)
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {

                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        Log.v("Drawer Click", "" + position);
                        switch (position) {
                            case 1:

                                break;
                            case 2:

                                break;
                        }
                        return false;
                    }
                });

        drawer = drawerBuilder.build();
        drawer.setSelectionAtPosition(1, true);
    }

    @Override
    public void addFragment(Fragment fragment, boolean isRoot) {
        android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        if (isRoot) {
            //clear entire back stack
            if (fragmentManager.getBackStackEntryCount() > 0) {
                FragmentManager.BackStackEntry first = fragmentManager.getBackStackEntryAt(0);
                fragmentManager.popBackStack(first.getId(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }
            fragmentTransaction.setCustomAnimations(R.anim.slide_in_bottom, R.anim.fade_out);
        } else {
            fragmentTransaction.setCustomAnimations(R.anim.slide_in_from_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right);
            fragmentTransaction.addToBackStack(fragment.getTag());
        }
        fragmentTransaction.replace(R.id.frame_container, fragment, fragment.getClass().getName());
        fragmentTransaction.commit();
    }

    private void refreshbackIcon() {

        supportInvalidateOptionsMenu();

        if (drawer != null && getSupportActionBar() != null) {
            if (fragmentManager.getBackStackEntryCount() > 0) {
                drawer.getActionBarDrawerToggle().setDrawerIndicatorEnabled(false);
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            } else {
                getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                drawer.getActionBarDrawerToggle().setDrawerIndicatorEnabled(true);
            }
        }
    }

    @Override
    public void onUpwardNavigation() {
        fragmentManager.popBackStack();
    }

    @Override
    public void showActivityToast(String message) {
        Snackbar.make(mainActivityRootLayout, message, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void setToolbarTitle(String title) {
        if (getSupportActionBar() != null)
            getSupportActionBar().setTitle(title);
    }

}
