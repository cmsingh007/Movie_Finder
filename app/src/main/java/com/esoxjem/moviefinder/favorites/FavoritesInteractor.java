package com.esoxjem.moviefinder.favorites;

import com.esoxjem.moviefinder.Movie;

import java.util.List;

/**
 * @author arun
 */
public interface FavoritesInteractor {
    void setFavorite(Movie movie);

    boolean isFavorite(String id);

    List<Movie> getFavorites();

    void unFavorite(String id);
}
