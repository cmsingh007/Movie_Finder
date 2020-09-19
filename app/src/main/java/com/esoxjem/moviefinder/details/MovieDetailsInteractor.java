package com.esoxjem.moviefinder.details;

import com.esoxjem.moviefinder.Review;
import com.esoxjem.moviefinder.Video;

import java.util.List;

import io.reactivex.Observable;

/**
 * @author arun
 */
public interface MovieDetailsInteractor {
    Observable<List<Video>> getTrailers(String id);

    Observable<List<Review>> getReviews(String id);
}
