package com.jupiter.application.web.rest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.jupiter.application.domain.Cab;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * Created by sreenidhisreesha on 10/6/14.
 */
public class CabDTO {


    private Long id;

    private Float latitude;

    private Float longitude;

    public CabDTO() {

    }

    public CabDTO(Long id, float latitude, float longitude) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
    }


    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("CabDTO{");
        sb.append("latitude='").append(latitude).append('\'');
        sb.append(", longitude='").append(longitude).append('\'');
        sb.append(", id='").append(id).append('\'');
        sb.append('}');
        return sb.toString();
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }
}
