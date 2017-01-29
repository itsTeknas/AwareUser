package com.blackcurrantapps.awareuser.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
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
import com.blackcurrantapps.awareuser.fragments.Group;
import com.blackcurrantapps.awareuser.fragments.RidesFrag;
import com.blackcurrantapps.awareuser.util.RoundedImageView;
import com.digits.sdk.android.Digits;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeActivity extends AppCompatActivity implements MainActivityConnect {

    private String cell = "";
    private String own_bssid = "";
    private String groupKey = "";

    private final PrimaryDrawerItem rides = new PrimaryDrawerItem().withName("Rides").withIcon(GoogleMaterial.Icon.gmd_dashboard);
    private final PrimaryDrawerItem group = new PrimaryDrawerItem().withName("Group").withIcon(GoogleMaterial.Icon.gmd_group);
    private final PrimaryDrawerItem logout = new PrimaryDrawerItem().withName("Logout").withIcon(GoogleMaterial.Icon.gmd_exit_to_app);

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.mainActivityRootLayout)
    CoordinatorLayout mainActivityRootLayout;
    @BindView(R.id.frame_container)
    FrameLayout frameContainer;

    private FragmentManager fragmentManager;
    private Drawer drawer = null;
    DatabaseReference databaseReference;

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

        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("users").child(cell).keepSynced(true);
        addFragment(new RidesFrag(),true);
    }

    private void createDrawer(Bundle savedInstanceState) {

        @SuppressLint("InflateParams") View header = getLayoutInflater().inflate(R.layout.header_name_email, null);

        TextView name = (TextView) header.findViewById(R.id.name);
        RoundedImageView profilePic = (RoundedImageView) header.findViewById(R.id.profilePic);
        Picasso.with(HomeActivity.this).load(R.mipmap.ic_account_circle_white_48dp).into(profilePic);
        name.setText(cell);

        DrawerBuilder drawerBuilder;

        drawerBuilder = new DrawerBuilder()
                .withActivity(HomeActivity.this)
                .withToolbar(toolbar)
                .withHeader(header)
                .withSelectedItem(0)
                .addDrawerItems(
                        rides,
                        group,
                        logout
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
                                addFragment(new RidesFrag(),true);
                                break;
                            case 2:
                                addFragment(new Group(),true);
                                break;
                            case 3:
                                Digits.clearActiveSession();
                                startActivity(new Intent(HomeActivity.this,MainActivity.class));
                                finish();
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
    public DatabaseReference getDatabaseReference() {
        return databaseReference;
    }

    @Override
    public String getCell() {
        return cell;
    }


    @Override
    public void setToolbarTitle(String title) {
        if (getSupportActionBar() != null)
            getSupportActionBar().setTitle(title);
    }

}
