package com.hmsapp.controller;

import com.hmsapp.entity.City;
import com.hmsapp.entity.Country;
import com.hmsapp.entity.Property;
import com.hmsapp.entity.PropertyImage;
import com.hmsapp.payload.PropertyDto;
import com.hmsapp.repository.CityRepository;
import com.hmsapp.repository.CountryRepository;
import com.hmsapp.repository.PropertyImageRepository;
import com.hmsapp.repository.PropertyRepository;
import com.hmsapp.service.BucketService;
import com.hmsapp.service.PropertyService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/v1/property")
public class PropertyController {

    private PropertyRepository propertyRepository;
    private PropertyService propertyService;
    private CityRepository cityRepository;
    private CountryRepository countryRepository;
    private BucketService bucketService;
    private PropertyImageRepository propertyImageRepository;

    public PropertyController(PropertyRepository propertyRepository, PropertyService propertyService, CityRepository cityRepository, CountryRepository countryRepository, BucketService bucketService, PropertyImageRepository propertyImageRepository) {
        this.propertyRepository = propertyRepository;
        this.propertyService = propertyService;
        this.cityRepository = cityRepository;
        this.countryRepository = countryRepository;
        this.bucketService = bucketService;
        this.propertyImageRepository = propertyImageRepository;
    }
    //

//    @PostMapping("/addProperty")
//    public ResponseEntity<?> addProperty(@RequestBody PropertyDto propertyDto) {
//        Country country = countryRepository.findById(propertyDto.getCountryId())
//                .orElseThrow(() -> new RuntimeException("Country not found"));
//        City city = cityRepository.findById(propertyDto.getCityId())
//                .orElseThrow(() -> new RuntimeException("City not found"));
//
//        Property property = new Property();
//        property.setName(propertyDto.getName());
//        property.setNoOfGuests(propertyDto.getNoOfGuests());
//        property.setNoOfBedrooms(propertyDto.getNoOfBedrooms());
//        property.setNoOfBathrooms(propertyDto.getNoOfBathrooms());
//        property.setCountry(country);
//        property.setCity(city);
//
//        return propertyService.addProperty(property);
//    }


    @PostMapping("/addProperty")
    public ResponseEntity<?> addProperty(@RequestBody PropertyDto propertyDto){
       ResponseEntity<?> property = propertyService.addProperty(propertyDto);
        return property;
    }

    @DeleteMapping("/deleteProperty/{propertyId}")
    public ResponseEntity<String> deleteProperty(@PathVariable Long propertyId) {
        propertyService.deleteProperty(propertyId);
        return new ResponseEntity<>("Deleted", HttpStatus.OK);
    }


    //http://localhost:8080/api/v1/property/{searchParam}
    @GetMapping("/{searchParam}")
    public ResponseEntity<?> searchProperty(@PathVariable String searchParam){
        ResponseEntity<?> property = propertyService.searchProperty(searchParam);
        return property;
    }
    @PutMapping("/updateProperty/{id}")
    public ResponseEntity<PropertyDto> updateProperty(
            @PathVariable Long id,
            @RequestBody PropertyDto propertyDto
    ) {
        propertyService.updateProperty(id, propertyDto);
        return new ResponseEntity<>(propertyDto, HttpStatus.OK);
    }

    @GetMapping("/PropertyId/{propertyId}")
    public ResponseEntity<PropertyDto> getPropertyById(
            @PathVariable Long propertyId
    ){
        PropertyDto propertyDto = propertyService.getPropertyById(propertyId);
        return new ResponseEntity<>(propertyDto, HttpStatus.OK);
    }

    @PostMapping("/upload/file/{bucketName}/property/{propertyId}")
    public String uploadPropertyPhotos(@RequestParam MultipartFile file,
                                       @PathVariable String bucketName,
                                       @PathVariable long propertyId){
        String imageUrl = bucketService.uploadFile(file, bucketName);
        PropertyImage propertyImage = new PropertyImage();
        propertyImage.setUrl(imageUrl);

        //set Fk
        Property property = propertyRepository.findById(propertyId).get();
        propertyImage.setProperty(property);
        propertyImageRepository.save(propertyImage);
        return "image is now uploaded";

    }

    //http://localhost:8080/api/v1/property/get/property/images?id=1
    @GetMapping("/get/property/images")
    public ResponseEntity<?>getPropertyImages(@RequestParam long id){
        Property property = propertyRepository.findById(id).get();
        List<PropertyImage> propertyImages = propertyImageRepository.findByProperty(property);
        if(propertyImages.isEmpty()){
            return new ResponseEntity<>("No images found for this property",HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(propertyImages,HttpStatus.FOUND);

    }
}
