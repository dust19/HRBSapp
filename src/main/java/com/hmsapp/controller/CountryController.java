package com.hmsapp.controller;

import com.hmsapp.entity.Country;
import com.hmsapp.payload.CountryDto;
import com.hmsapp.service.CountryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/country")
public class CountryController {

    private CountryService countryService;

    public CountryController(CountryService countryService) {
        this.countryService = countryService;
    }
    @PostMapping("/addCountry")
    public ResponseEntity<CountryDto> addCountry(
            @RequestBody CountryDto countryDto

    ){
        CountryDto cont = countryService.addCountry(countryDto);
        return new ResponseEntity<>(cont, HttpStatus.CREATED);
    }

    @DeleteMapping("/deleteCountry")
    public ResponseEntity<String> deleteCountry(
            @RequestParam Long id
    ){
        countryService.deleteCountry(id);
        return new ResponseEntity<>("deleted", HttpStatus.OK);
    }

    @PutMapping("/updateCountry")
    public ResponseEntity<CountryDto> updateCountry(
            @RequestBody CountryDto countryDto,
            @RequestParam Long id
    ) {
        // Call the service to update the city and get the updated DTO
        CountryDto updatedCountryDto = countryService.updateCountry(id, countryDto);

        // Return the updated DTO in the response
        return new ResponseEntity<>(updatedCountryDto, HttpStatus.OK);
    }
}
