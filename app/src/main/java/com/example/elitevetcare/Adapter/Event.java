package com.example.elitevetcare.Adapter;

import com.example.elitevetcare.Model.CurrentData.CurrentAppointment;
import com.example.elitevetcare.Model.ObjectModel.Appointment;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class Event
{
    public static ArrayList<Event> eventsList = new ArrayList<>();

    public static ArrayList<Event> eventsForDate(LocalDate date)
    {
        ArrayList<Event> events = new ArrayList<>();

        for(Event event : eventsList)
        {
            if(event.getDate().equals(date))
                events.add(event);
        }

        return events;
    }

    public static ArrayList<Event> eventsForDateAndTime(LocalDate date, LocalTime time)
    {
        ArrayList<Event> events = new ArrayList<>();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("H:mm");
        DateTimeFormatter DateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        if ( CurrentAppointment.getAcceptedAppointmentList() == null ){
            return new ArrayList<>();
        }
        for(Appointment event : CurrentAppointment.getAcceptedAppointmentList())
        {
            LocalDate EventDate = LocalDate.parse(event.getAppointmentDate());
            LocalTime EventTime = (LocalTime.parse(event.getAppointmentTime(), formatter));
            int eventHour = EventTime.getHour();
            int eventMinute = EventTime.getMinute();
            int cellHour = time.getHour();
            int cellMinute = time.getMinute();

            if(EventDate.equals(date) && eventHour == cellHour && eventMinute == cellMinute)
                events.add(new Event(event.getServicePackage(),EventDate,EventTime, event));
        }

        return events;
    }


    private String name;
    private LocalDate date;
    private LocalTime time;
    private Appointment appointment;

    public Appointment getAppointment() {
        return appointment;
    }

    public void setAppointment(Appointment appointment) {
        this.appointment = appointment;
    }

    public Event(String name, LocalDate date, LocalTime time, Appointment appointment) {
        this.name = name;
        this.date = date;
        this.time = time;
        this.appointment = appointment;
    }

    public Event(String name, LocalDate date, LocalTime time)
    {
        this.name = name;
        this.date = date;
        this.time = time;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public LocalDate getDate()
    {
        return date;
    }

    public void setDate(LocalDate date)
    {
        this.date = date;
    }

    public LocalTime getTime()
    {
        return time;
    }

    public void setTime(LocalTime time)
    {
        this.time = time;
    }
}
