package com.jupiter.application.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * A cab.
 */
@Document
public class Cab extends AbstractAuditingEntity implements Serializable {

    @NotNull
    @Id
    private long id;

    @NotNull
    @Field("latitude")
    @Indexed
    private float latitude;

    @NotNull
    @Field("longitude")
    @Indexed
    private float longitude;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Cab cab = (Cab) o;

        if (Float.compare(cab.id, id) != 0) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return (id != +0.0f ? Float.floatToIntBits(id) : 0);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
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
