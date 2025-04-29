package com.hmsapp.controller;

import com.hmsapp.entity.User;
import com.hmsapp.exception.DuplicateReviewException;
import com.hmsapp.exception.ResourceNotFoundException;
import com.hmsapp.payload.ReviewDto;
import com.hmsapp.service.ReviewService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/review")
public class ReviewController {
    private ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping("/addReview/{propertyId}")
    public ResponseEntity<ReviewDto> addReview(
            @RequestBody ReviewDto dto,
            @PathVariable Long propertyId,
            @AuthenticationPrincipal User user) throws DuplicateReviewException, ResourceNotFoundException {

        ReviewDto reviewDto = reviewService.addReview(dto, propertyId, user);
        return new ResponseEntity<>(reviewDto, HttpStatus.OK);

    }

    @GetMapping("user/viewReviews")
    public ResponseEntity<List<ReviewDto>> viewMyReviews(
            @AuthenticationPrincipal User user
    ){
        List<ReviewDto> reviews = reviewService.viewReviews(user);
        return new ResponseEntity<>(reviews, HttpStatus.FOUND);
    }

    @GetMapping("/property/{propertyId}")
    public ResponseEntity<List<ReviewDto>> viewReviewsForProperty(
            @PathVariable Long propertyId
    ) throws ResourceNotFoundException {
        List<ReviewDto> reviews = reviewService.viewReviewsForProperty(propertyId);
        return new ResponseEntity<>(reviews, HttpStatus.FOUND);
    }

    @DeleteMapping("/delete/{reviewId}")
    public ResponseEntity<String> deleteReview(
            @PathVariable Long reviewId,  // Correctly map reviewId from the path
            @AuthenticationPrincipal User user  // Use the authenticated user
    ) throws ResourceNotFoundException {
        reviewService.deleteReview(reviewId, user);  // Delegate the deletion logic to the service
        return new ResponseEntity<>("Review deleted successfully.", HttpStatus.OK);
    }



}
