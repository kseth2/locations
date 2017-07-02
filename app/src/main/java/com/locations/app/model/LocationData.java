package com.locations.app.model;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

public class LocationData extends RealmObject {

    @SerializedName("ID")
    private long id;

    @SerializedName("Name")
    private String name;

    @SerializedName("Latitude")
    private double latitude;

    @SerializedName("Longitude")
    private double longitude;

    @SerializedName("Address")
    private String address;

    @SerializedName("ArrivalTime")
    private String arrivalTime;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(String arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LocationData locationData = (LocationData) o;

        if (id != locationData.id) return false;
        if (Double.compare(locationData.latitude, latitude) != 0) return false;
        if (Double.compare(locationData.longitude, longitude) != 0) return false;
        if (!name.equals(locationData.name)) return false;
        if (!address.equals(locationData.address)) return false;
        return arrivalTime.equals(locationData.arrivalTime);

    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = (int) (id ^ (id >>> 32));
        result = 31 * result + name.hashCode();
        temp = Double.doubleToLongBits(latitude);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(longitude);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + address.hashCode();
        result = 31 * result + arrivalTime.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "LocationData{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", address='" + address + '\'' +
                ", arrivalTime='" + arrivalTime + '\'' +
                '}';
    }
}