package com.esoxjem.moviefinder.listing;

import com.esoxjem.moviefinder.listing.sorting.SortingDialogFragment;
import com.esoxjem.moviefinder.listing.sorting.SortingModule;

import dagger.Subcomponent;

/**
 * @author arunsasidharan
 */
@ListingScope
@Subcomponent(modules = {ListingModule.class, SortingModule.class})
public interface ListingComponent {
    MoviesListingFragment inject(MoviesListingFragment fragment);

    SortingDialogFragment inject(SortingDialogFragment fragment);
}
