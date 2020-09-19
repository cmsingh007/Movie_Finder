package com.esoxjem.moviefinder.details;

import com.esoxjem.moviefinder.Movie;
import com.esoxjem.moviefinder.Review;
import com.esoxjem.moviefinder.Video;

import java.util.List;

/**
 * @author arun
 */
interface MovieDetailsView {
    void showDetails(Movie movie);

    void showTrailers(List<Video> trailers);

    void showReviews(List<Review> reviews);

    void showFavorited();

    void showUnFavorited();
}
