package com.esoxjem.moviefinder.listing;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.test.espresso.IdlingResource;

import com.esoxjem.moviefinder.BuildConfig;
import com.esoxjem.moviefinder.Constants;
import com.esoxjem.moviefinder.Movie;
import com.esoxjem.moviefinder.R;
import com.esoxjem.moviefinder.details.MovieDetailsActivity;
import com.esoxjem.moviefinder.details.MovieDetailsFragment;
import com.esoxjem.moviefinder.util.EspressoIdlingResource;
import com.esoxjem.moviefinder.util.RxUtils;
import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdView;
import com.facebook.ads.AudienceNetworkAds;
import com.facebook.ads.InterstitialAd;
import com.facebook.ads.InterstitialAdListener;
import android.os.Handler;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.jakewharton.rxbinding2.support.v7.widget.RxSearchView;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.BindView;
import io.reactivex.disposables.Disposable;


public class MoviesListingActivity extends AppCompatActivity
        implements MoviesListingFragment.Callback,
        NavigationView.OnNavigationItemSelectedListener {

    public static final String DETAILS_FRAGMENT = "DetailsFragment";
    private boolean twoPaneMode;
    private Disposable searchViewTextSubscription;
    public static final int ITEMS_PER_AD = 7;

    private AdView adView;
    private InterstitialAd interstitialAd;
    int seconds = 0;
    Button button;
    int n = 8;

    Toolbar toolbar;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    private long backPressedTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setToolbar();

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.nav_drawer_open, R.string.nav_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_home2);


        AudienceNetworkAds.initialize(this);


        interstitialAd = new InterstitialAd(this, "599656480451430_599662010450877");
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                final Handler handler = new Handler();
                handler.post(new Runnable() {
                    @Override
                    public void run() {

                        interstitialAd.setAdListener(new InterstitialAdListener() {
                            @Override
                            public void onInterstitialDisplayed(Ad ad) {
                            }

                            @Override
                            public void onInterstitialDismissed(Ad ad) {
                            }

                            @Override
                            public void onError(Ad ad, AdError adError) {
                            }

                            @Override
                            public void onAdLoaded(Ad ad) {
                                interstitialAd.show();
                            }

                            @Override
                            public void onAdClicked(Ad ad) {
                            }

                            @Override
                            public void onLoggingImpression(Ad ad) {
                            }
                        });
                        interstitialAd.loadAd();

                        seconds = (n * n) * 1000;
                        n = n + 3;

                        handler.postDelayed(this, seconds);
                    }
                });

            }
        }, 17 * 1000);


        if (findViewById(R.id.movie_details_container) != null) {
            twoPaneMode = true;

            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.movie_details_container, new MovieDetailsFragment())
                        .commit();
            }
        } else {
            twoPaneMode = false;
        }
    }

    private void setToolbar() {

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.movie_guide);
            getSupportActionBar().setDisplayShowTitleEnabled(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        final MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) searchItem.getActionView();
        final MoviesListingFragment mlFragment = (MoviesListingFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_listing);
        searchItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                MoviesListingFragment mlFragment = (MoviesListingFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_listing);
                mlFragment.searchViewBackButtonClicked();
                return true;
            }
        });

        searchViewTextSubscription = RxSearchView.queryTextChanges(searchView)
                .debounce(500, TimeUnit.MILLISECONDS)
                .subscribe(charSequence -> {
                    if (charSequence.length() > 0) {
                        mlFragment.searchViewClicked(charSequence.toString());
                    }
                });

        return true;
    }

    @Override
    public void onMoviesLoaded(Movie movie) {
        if (twoPaneMode) {
            loadMovieFragment(movie);
        } else {
            // Do not load in single pane view
        }
    }

    @Override
    public void onMovieClicked(Movie movie) {
        if (twoPaneMode) {
            loadMovieFragment(movie);
        } else {
            startMovieActivity(movie);
        }
    }

    private void startMovieActivity(Movie movie) {
        Intent intent = new Intent(this, MovieDetailsActivity.class);
        Bundle extras = new Bundle();
        extras.putParcelable(Constants.MOVIE, movie);
        intent.putExtras(extras);
        startActivity(intent);
    }

    private void loadMovieFragment(Movie movie) {
        MovieDetailsFragment movieDetailsFragment = MovieDetailsFragment.getInstance(movie);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.movie_details_container, movieDetailsFragment, DETAILS_FRAGMENT)
                .commit();
    }

    @VisibleForTesting
    @NonNull
    public IdlingResource getCountingIdlingResource() {
        return EspressoIdlingResource.getIdlingResource();
    }

    @Override
    protected void onDestroy() {
        RxUtils.unsubscribe(searchViewTextSubscription);
        super.onDestroy();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {

            case R.id.nav_home2:
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(MoviesListingActivity.this).setPositiveButton("Ok", null);
                View mView = getLayoutInflater().inflate(R.layout.alertdialog_layout, null);
                TextView desc = (TextView) mView.findViewById(R.id.aboutDesc);


                desc.setText("Movie Finder App\nVersion " + BuildConfig.VERSION_NAME + "\nDeveloped by Code Apps Inc." +
                        "\n\nThird Party license:\nhttps://newsapi.org\nhttps://www.themoviedb.org\nhttps://square.github.io/retrofit\n" +
                        "https://bumptech.github.io/glide");

                mBuilder.setView(mView);
                AlertDialog dialog = mBuilder.create();
                dialog.show();
                break;
            case R.id.nav_home3:
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://play.google.com/store/apps/dev?id=7734111515836731840")));

                break;

            case R.id.nav_rate:
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://play.google.com/store/apps/details?id=com.esoxjem.moviefinder")));
                break;
        }

        drawerLayout.closeDrawer(GravityCompat.START);

        return true;
    }

    @Override
    public void onBackPressed() {

        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
            else {

                if (backPressedTime + 2000 > System.currentTimeMillis()) {
                    finish();
                } else
                    Toast.makeText(this, "Press back again to exit!", Toast.LENGTH_SHORT).show();
                backPressedTime = System.currentTimeMillis();
            }
    }
}
