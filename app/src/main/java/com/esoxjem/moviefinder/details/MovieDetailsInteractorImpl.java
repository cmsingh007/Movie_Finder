package com.esoxjem.moviefinder.details;

import com.esoxjem.moviefinder.Review;
import com.esoxjem.moviefinder.ReviewsWrapper;
import com.esoxjem.moviefinder.Video;
import com.esoxjem.moviefinder.VideoWrapper;
import com.esoxjem.moviefinder.network.TmdbWebService;

import java.util.List;

import io.reactivex.Observable;

/**
 * @author arun
 */
class MovieDetailsInteractorImpl implements MovieDetailsInteractor {

    private TmdbWebService tmdbWebService;

    MovieDetailsInteractorImpl(TmdbWebService tmdbWebService) {
        this.tmdbWebService = tmdbWebService;
    }

    @Override
    public Observable<List<Video>> getTrailers(final String id) {
        return tmdbWebService.trailers(id).map(VideoWrapper::getVideos);
    }

    @Override
    public Observable<List<Review>> getReviews(final String id) {
        return tmdbWebService.reviews(id).map(ReviewsWrapper::getReviews);
    }

}
