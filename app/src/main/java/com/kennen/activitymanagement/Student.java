package com.kennen.activitymanagement;

import android.support.v4.app.INotificationSideChannel;

import java.io.Serializable;
import java.util.Calendar;

public class Student implements Serializable
{
    private String name;
    private String id;
    private String rollCallTime;
    private boolean isRollCall;
    private String phoneNumber;

    public Student()
    {
    }

    public Student(String name, String id, String rollCallTime, boolean isRollCall, String phoneNumber)
    {
        this.name = name;
        this.id = id;
        this.rollCallTime = rollCallTime;
        this.isRollCall = isRollCall;
        this.phoneNumber = phoneNumber;
    }

    public String getName()
    {
        return name;
    }

    public String getId()
    {
        return id;
    }

    public String getRollCallTime()
    {
        return rollCallTime;
    }

    public boolean isRollCall()
    {
        return isRollCall;
    }

    public String getPhoneNumber()
    {
        return phoneNumber;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public void setRollCallTime(String rollCallTime)
    {
        this.rollCallTime = rollCallTime;
    }

    public void setRollCall(boolean rollCall)
    {
        isRollCall = rollCall;
    }

    public void setPhoneNumber(String phoneNumber)
    {
        this.phoneNumber = phoneNumber;
    }
}
