package com.esoxjem.moviefinder.listing;

import com.esoxjem.moviefinder.Movie;

import java.util.List;

/**
 * @author arun
 */
interface MoviesListingView {
    void showMovies(List<Movie> movies);

    void loadingStarted();

    void loadingFailed(String errorMessage);

    void onMovieClicked(Movie movie);
}
