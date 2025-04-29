package com.hmsapp.payload;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PropertyDto {
    private String name;
    private Integer noOfGuests;
    private Integer noOfBedrooms;
    private Integer noOfBathrooms;
    private Long countryId;
    private Long cityId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getNoOfGuests() {
        return noOfGuests;
    }

    public void setNoOfGuests(Integer noOfGuests) {
        this.noOfGuests = noOfGuests;
    }

    public Integer getNoOfBedrooms() {
        return noOfBedrooms;
    }

    public void setNoOfBedrooms(Integer noOfBedrooms) {
        this.noOfBedrooms = noOfBedrooms;
    }

    public Integer getNoOfBathrooms() {
        return noOfBathrooms;
    }

    public void setNoOfBathrooms(Integer noOfBathrooms) {
        this.noOfBathrooms = noOfBathrooms;
    }

    public Long getCountryId() {
        return countryId;
    }

    public void setCountryId(Long countryId) {
        this.countryId = countryId;
    }

    public Long getCityId() {
        return cityId;
    }

    public void setCityId(Long cityId) {
        this.cityId = cityId;
    }
}
