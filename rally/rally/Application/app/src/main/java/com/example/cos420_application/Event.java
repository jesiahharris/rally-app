package com.example.cos420_application;

public class Event {

    String name; //event name
    String location; //event location
    String date; //date of event
    String time; //time of event
    String capacity; //capacity of event

    // default constructor
    public Event()
    {
        name = "";
        location = "";
        date = "";
        time = "";
        capacity = "";
    }

    // parameterized constructor
    public Event(String name, String location, String date, String time, String capacity) {
        this.name = name;
        this.location = location;
        this.date = date;
        this.time = time;
        this.capacity = capacity;
    }

    //getters and setters

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getCapacity() {
        return capacity;
    }

    public void setCapacity(String capacity) {
        this.capacity = capacity;
    }

    @Override
    public String toString() {
        return "Event{" +
                "name='" + name + '\'' +
                ", location='" + location + '\'' +
                ", date='" + date + '\'' +
                ", time=" + time +
                ", capacity=" + capacity +
                '}';
    }
}
