package com.jupiter.application.web.rest.dto;

import com.jupiter.application.domain.Cab;

import javax.validation.constraints.NotNull;

/**
 * Created by sreenidhisreesha on 10/6/14.
 */
public class CabDTO {

    @NotNull
    private Long id;

    @NotNull
    private float latitude;

    @NotNull
    private float longitude;

    public CabDTO() {

    }

    public CabDTO(long id, float latitude, float longitude) {
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
