package com.hmsapp.service;

import com.hmsapp.entity.City;
import com.hmsapp.entity.Country;
import com.hmsapp.entity.Property;
import com.hmsapp.entity.User;
import com.hmsapp.payload.PropertyDto;
import com.hmsapp.payload.UserDto;
import com.hmsapp.repository.CityRepository;
import com.hmsapp.repository.CountryRepository;
import com.hmsapp.repository.PropertyRepository;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PropertyService {

    private PropertyRepository propertyRepository;
    private CountryRepository countryRepository;
    private CityRepository cityRepository;
    private ModelMapper modelMapper;

    public PropertyService(PropertyRepository propertyRepository, CountryRepository countryRepository, CityRepository cityRepository, ModelMapper modelMapper) {
        this.propertyRepository = propertyRepository;
        this.countryRepository = countryRepository;
        this.cityRepository = cityRepository;
        this.modelMapper = modelMapper;
    }


    PropertyDto mapToDto(Property property) {
        PropertyDto dto = modelMapper.map(property, PropertyDto.class);
        return dto;
    }

    Property mapToEntity(PropertyDto dto) {
        Property property = modelMapper.map(dto, Property.class);
        return property;
    }

    public ResponseEntity<?> addProperty(PropertyDto propertyDto) {
           // Add property to database
        Country country = countryRepository.findById(propertyDto.getCountryId())
             .orElseThrow(() -> new RuntimeException("Country not found"));
        City city = cityRepository.findById(propertyDto.getCityId())
               .orElseThrow(() -> new RuntimeException("City not found"));

        Property property = new Property();
        property.setName(propertyDto.getName());
        property.setNoOfGuests(propertyDto.getNoOfGuests());
        property.setNoOfBedrooms(propertyDto.getNoOfBedrooms());
        property.setNoOfBathrooms(propertyDto.getNoOfBathrooms());
        property.setCountry(country);
        property.setCity(city);

         //Property property = mapToEntity(propertyDto);
         //property.setCountry(country);
         //property.setCity(city);

//        Property saved = propertyRepository.save(property);
//        return propertyService.addProperty(property);

        Property saved = propertyRepository.save(property);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    public ResponseEntity<?> searchProperty(String searchParam) {
        // Search property in database based on city and country
        // Use join concept
        List<Property> properties = propertyRepository.searchProperty(searchParam);
        if (properties.isEmpty()){
            return new ResponseEntity<>("No property found", HttpStatus.NOT_FOUND);
        }
            List<PropertyDto> propertyDtos = properties.stream()
                    .map(this::mapToDto)
                    .collect(Collectors.toList());

            return new ResponseEntity<>(propertyDtos, HttpStatus.OK);
    }
    @Transactional
    public void deleteProperty(Long propertyId) {
        Property property = propertyRepository.findById(propertyId).orElseThrow(() -> new RuntimeException("Property not found"));
        // Remove the references to the city and country if needed
        property.setCity(null);
        property.setCountry(null);

        // Now delete the property
        propertyRepository.delete(property);
    }


    public void updateProperty(Long id, PropertyDto propertyDto) {
        // Retrieve the property from the database
        Property property = propertyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Property with ID " + id + " and Name "+propertyDto.getName()+" not found"));

        // Update the basic fields
        property.setName(propertyDto.getName());
        property.setNoOfGuests(propertyDto.getNoOfGuests());
        property.setNoOfBedrooms(propertyDto.getNoOfBedrooms());
        property.setNoOfBathrooms(propertyDto.getNoOfBathrooms());

        // Update the country and city relationships
        if (propertyDto.getCountryId() != null) {
            Country country = countryRepository.findById(propertyDto.getCountryId())
                    .orElseThrow(() -> new RuntimeException("Country not found"));
            property.setCountry(country);
        }

        if (propertyDto.getCityId() != null) {
            City city = cityRepository.findById(propertyDto.getCityId())
                    .orElseThrow(() -> new RuntimeException("City not found"));
            property.setCity(city);
        }
        // Save the updated property
        propertyRepository.save(property);
    }

    public PropertyDto getPropertyById(Long propertyId) {
        Optional<Property> opProp = propertyRepository.findById(propertyId);
        Property property = opProp.get();
        return mapToDto(property);
    }
}
