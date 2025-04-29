package com.hmsapp.service;

import com.hmsapp.entity.City;
import com.hmsapp.payload.CityDto;
import com.hmsapp.repository.CityRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class CityService {
    private CityRepository cityRepository;
    private ModelMapper modellMapper;

    public CityService(CityRepository cityRepository, ModelMapper modellMapper) {
        this.cityRepository = cityRepository;
        this.modellMapper = modellMapper;
    }


    CityDto mapToDto(City city) {
        CityDto dto = modellMapper.map(city, CityDto.class);
        return dto;
    }

    City mapToEntity(CityDto cityDto) {
        City city = modellMapper.map(cityDto, City.class);
        return city;
    }

    public CityDto addCity(CityDto cityDto) {
        City city = mapToEntity(cityDto);

        City cit = cityRepository.save(city);
        return mapToDto(cit);
        //countryRepository.save(country);
    }

    public void deleteCity(Long id) {
        cityRepository.deleteById(id);
    }

    public CityDto updateCity(Long id, CityDto cityDto) {
        // Find the city entity by ID or throw an exception
        City city = cityRepository.findById(id).orElseThrow(() -> new RuntimeException("City not found"));
        // Update the entity's fields
        city.setName(cityDto.getName());
        // Save the updated entity
        City updatedCity = cityRepository.save(city);
        // Map the updated entity back to DTO
        return mapToDto(updatedCity);
    }


}
