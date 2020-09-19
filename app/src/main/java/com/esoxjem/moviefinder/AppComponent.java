package com.esoxjem.moviefinder;

import com.esoxjem.moviefinder.details.DetailsComponent;
import com.esoxjem.moviefinder.details.DetailsModule;
import com.esoxjem.moviefinder.favorites.FavoritesModule;
import com.esoxjem.moviefinder.listing.ListingComponent;
import com.esoxjem.moviefinder.listing.ListingModule;
import com.esoxjem.moviefinder.network.NetworkModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * @author arunsasidharan
 * @author pulkitkumar
 */
@Singleton
@Component(modules = {
        AppModule.class,
        NetworkModule.class,
        FavoritesModule.class})
public interface AppComponent {
    DetailsComponent plus(DetailsModule detailsModule);

    ListingComponent plus(ListingModule listingModule);
}
