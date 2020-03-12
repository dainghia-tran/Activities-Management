package com.kennen.activitymanagement;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

public class Activity implements Serializable
{
    private String name;
    private String organizeDate;
    private String location;
    private String description;
    private String dbChild;
    private List<Student> studentsList;

    public Activity()
    {
    }

    public String getDbChild()
    {
        dbChild = organizeDate.replace("/", "-").replace(":", "h").replaceAll("\\s+","");
        return dbChild;
    }

    public void setDbChild(String dbChild)
    {
        this.dbChild = dbChild;
    }

    public Activity(String name, String organizeDate, String location, String description, List<Student> studentsList)
    {
        this.name = name;
        this.organizeDate = organizeDate;
        this.location = location;
        this.description = description;
        this.studentsList = studentsList;
    }

    public List<Student> getUnRollCalled()
    {
        return studentsList.stream().filter(Student::isRollCall).collect(Collectors.toList());
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getOrganizeDate()
    {
        return organizeDate;
    }

    public void setOrganizeDate(String organizeDate)
    {
        this.organizeDate = organizeDate;
    }

    public String getLocation()
    {
        return location;
    }

    public void setLocation(String location)
    {
        this.location = location;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public List<Student> getStudentsList()
    {
        return studentsList;
    }

    public void setStudentsList(ArrayList<Student> studentsList)
    {
        this.studentsList = studentsList;
    }
}
