package com.hmsapp.controller;

import com.hmsapp.payload.CityDto;
import com.hmsapp.service.CityService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/city")
public class CityController {

    private CityService cityService;

    public CityController(CityService cityService) {
        this.cityService = cityService;
    }

    @PostMapping("/addCity")
    public ResponseEntity<CityDto> addCity(
            @RequestBody CityDto cityDto

    ){
        CityDto cit = cityService.addCity(cityDto);
        return new ResponseEntity<>(cit, HttpStatus.CREATED);
    }

    @DeleteMapping("/deleteCity")
    public ResponseEntity<String> deleteCity(
            @RequestParam Long id
    ){
        cityService.deleteCity(id);
        return new ResponseEntity<>("deleted", HttpStatus.OK);
    }

    @PutMapping("/updateCity")
    public ResponseEntity<CityDto> updateCity(
            @RequestBody CityDto cityDto,
            @RequestParam Long id
    ) {

        CityDto updatedCityDto = cityService.updateCity(id, cityDto);


        return new ResponseEntity<>(updatedCityDto, HttpStatus.OK);
    }
}
