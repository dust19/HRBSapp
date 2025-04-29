package com.hmsapp.service;

import com.hmsapp.entity.Property;
import com.hmsapp.entity.Reviews;
import com.hmsapp.entity.User;
import com.hmsapp.exception.DuplicateReviewException;
import com.hmsapp.exception.ResourceNotFoundException;
import com.hmsapp.payload.ReviewDto;
import com.hmsapp.repository.PropertyRepository;
import com.hmsapp.repository.ReviewsRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReviewService {
    private ReviewsRepository reviewsRepository;
    private ModelMapper modelMapper;
    private PropertyRepository propertiesRepository;

    public ReviewService(ReviewsRepository reviewsRepository, ModelMapper modelMapper, PropertyRepository propertiesRepository) {
        this.reviewsRepository = reviewsRepository;
        this.modelMapper = modelMapper;
        this.propertiesRepository = propertiesRepository;
    }

    ReviewDto mapToDto(Reviews reviews){
        ReviewDto reviewDto = modelMapper.map(reviews,ReviewDto.class);
        return reviewDto;
    }

    Reviews mapToEntity(ReviewDto reviewDto){
        Reviews review = modelMapper.map(reviewDto,Reviews.class);
        return review;
    }

    public ReviewDto addReview(ReviewDto dto, Long propertyId, User user)
            throws DuplicateReviewException, ResourceNotFoundException {
        // Check if property exists and review for the same user doesn't already exist
        Property property = propertiesRepository.findById(propertyId)
                .orElseThrow(() -> new ResourceNotFoundException("Property not found with ID: " + propertyId));

        if (reviewsRepository.findByPropertyAndUser(property, user) != null) {
            throw new DuplicateReviewException("You have already reviewed this property.");
        }

        Reviews review = mapToEntity(dto);
        review.setProperty(property);
        review.setUser(user);

        Reviews savedReview = reviewsRepository.save(review);
        return mapToDto(savedReview);
    }

    public List<ReviewDto> viewReviews(User user) {

        List<Reviews> reviews = reviewsRepository.findByUser(user);
        return modelMapper.map(reviews,List.class);
    }

    public List<ReviewDto> viewReviewsForProperty(Long propertyId) throws ResourceNotFoundException {
        Property property = propertiesRepository.findById(propertyId)
               .orElseThrow(() -> new ResourceNotFoundException("Property not found with ID: " + propertyId));

        List<Reviews> reviews = reviewsRepository.findByProperty(property);
        return modelMapper.map(reviews,List.class);
    }

//    public void deleteReview(Long reviewId, User user) throws ResourceNotFoundException {
//        // Fetch the review by ID
//        //Use reviewsRepository.findById(reviewId) to check if the review exists in the database.
//        Optional<Reviews> reviewOptional = reviewsRepository.findById(reviewId);
//
//        // Check if the review exists
//        if (reviewOptional.isPresent()) {
//            Reviews review = reviewOptional.get();
//
//            // Check if the logged-in user is the owner of the review
//            //Compare the user.getId() (from the logged-in user) with review.getUser().getId() (the user ID associated with the review).
//            //If the IDs don't match, throw a ResourceNotFoundException or a custom UnauthorizedActionException if you want a clearer error.
//            if (!review.getUser().getId().equals(user.getId())) {
//                throw new ResourceNotFoundException("You are not authorized to delete this review.");
//            }
//
//            // If the user owns the review, delete it
//            reviewsRepository.delete(review);
//        } else {
//            throw new ResourceNotFoundException("Review not found with ID: " + reviewId);
//        }
//    }
public void deleteReview(Long reviewId, User user) throws ResourceNotFoundException {
    // Fetch the review by ID
    Optional<Reviews> reviewOptional = reviewsRepository.findById(reviewId);

    if (reviewOptional.isPresent()) {
        Reviews review = reviewOptional.get();

        // Check if the logged-in user owns the review
        if (!review.getUser().getId().equals(user.getId())) {
            throw new ResourceNotFoundException("You are not authorized to delete this review.");
        }

        // If authorized, delete the review
        reviewsRepository.delete(review);
    } else {
        throw new ResourceNotFoundException("Review not found with ID: " + reviewId);
    }
}


}
