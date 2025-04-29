package com.hmsapp.service;

import com.hmsapp.entity.Country;
import com.hmsapp.payload.CountryDto;
import com.hmsapp.repository.CountryRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class CountryService {
    private CountryRepository countryRepository;
    private ModelMapper modelmapper;

    public CountryService(CountryRepository countryRepository, ModelMapper mapper, ModelMapper modelmapper) {
        this.countryRepository = countryRepository;
        this.modelmapper = modelmapper;
    }
    CountryDto mapToDto(Country country) {
        CountryDto dto = modelmapper.map(country, CountryDto.class);
        return dto;
    }

    Country mapToEntity(CountryDto countryDto) {
        Country country = modelmapper.map(countryDto, Country.class);
        return country;
    }

    public CountryDto addCountry(CountryDto dto) {
        // Implement logic to save country to the database
        Country country = mapToEntity(dto);
        Country saved = countryRepository.save(country);
        return mapToDto(saved);
    }

    public void deleteCountry(Long id) {
        countryRepository.deleteById(id);
    }

    public CountryDto updateCountry(Long id, CountryDto countryDto) {
        // Find the city entity by ID or throw an exception
        Country country = countryRepository.findById(id).orElseThrow(() -> new RuntimeException("country not found"));
        // Update the entity's fields
        country.setName(countryDto.getName());
        // Save the updated entity
        Country updatedCountry = countryRepository.save(country);
        // Map the updated entity back to DTO
        return mapToDto(updatedCountry);
    }

}
