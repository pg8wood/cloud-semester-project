/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.sessionBeans;

import com.mycompany.entityClasses.Meeting;
import com.mycompany.entityClasses.MeetingUsers;
import com.mycompany.entityClasses.User;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class MeetingFacade extends AbstractFacade<Meeting> {

    @PersistenceContext(unitName = "groupPU")
    private EntityManager em;
    private Date selectedDate;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    // Instance fields
//    private HashMap<Date, List<String>> timesByDateMap;
    private List<Date> dateList;

    public MeetingFacade() {
        super(Meeting.class);
        dateList = new ArrayList();
//        timesByDateMap = null;
    }

    // ----------------
    // Instance methods 
    // ----------------
    /**
     * Gets the meeting's timeslots and de-serializes them
     */
    public void deserializeTimeslots(Meeting meeting) {
        dateList = new ArrayList();
        
        Scanner serializedStringScan = new Scanner(meeting.getTimeslots());
        serializedStringScan.useDelimiter(",");

        while (serializedStringScan.hasNext()) {
            Scanner dateScan = new Scanner(serializedStringScan.next());
            /* Serialized dates are stored in the database in the form: 
           java.util.Date.toString(),java.util.date.toString,... */
            String dayOfWeek = dateScan.next();
            String monthName = dateScan.next();
            int dayNumber = dateScan.nextInt();

            // Time is stored in the form HH:MM:SS
            Scanner timeScan = new Scanner(dateScan.next());
            timeScan.useDelimiter(":");
            int hour = timeScan.nextInt();
            int minute = timeScan.nextInt();
            // No need to store seconds

            // Skip time zone
            dateScan.next();

            int year = dateScan.nextInt();

            // Store the parsed Dates into the list
            dateList.add(new Date(year, getMonthInt(monthName), dayNumber, hour, minute));
        }
    }

    private int getDayOfWeekInt(String dayName) {
        switch (dayName) {
            case "Sun":
                return 0;
            case "Mon":
                return 1;
            case "Tue":
                return 2;
            case "Wed":
                return 3;
            case "Thu":
                return 4;
            case "Fri":
                return 5;
            case "Sat":
                return 6;
            default:
                return 0;
        }
    }

    private int getMonthInt(String monthName) {
        switch (monthName) {
            case "Jan":
                return 0;
            case "Feb":
                return 1;
            case "Mar":
                return 2;
            case "Apr":
                return 3;
            case "May":
                return 4;
            case "Jun":
                return 5;
            case "Jul":
                return 6;
            case "Aug":
                return 7;
            case "Sep":
                return 8;
            case "Oct":
                return 9;
            case "Nov":
                return 10;
            case "Dec":
                return 11;
            default:
                return 0;
        }
    }
    
    /**
     * Gets all the available timeslots for a given meeting.
     * 
     * @param meeting The meeting to examine
     * @return ArrayList<Date> the list of available timeslots
     */
    public ArrayList<Date> getTimeslotsForMeeting(Meeting meeting) {
        deserializeTimeslots(meeting);
        return (ArrayList<Date>)dateList;
    }
    
    /**
     * Gets all the participants for a given meeting.
     * 
     * @param meeting The meeting to examine
     * @return ArrayList<User> the list of users participating
     */
    public ArrayList<User> getParticipantList(Meeting meeting){
        int meetingId = meeting.getId();
        List<MeetingUsers> meetingUsers = (List<MeetingUsers>)getEntityManager().createNamedQuery("MeetingUsers.findByMeetingIdAndResponse").setParameter("meetingId", meetingId).setParameter("response", true).getResultList();
        ArrayList<User> participantList = new ArrayList<User>();
        for (MeetingUsers x : meetingUsers)
        {
            participantList.add(x.getUser());
        }
        return participantList;
    }

    public List<Date> getDateList() {
        return dateList;
    }

    public void setDateList(List<Date> dateList) {
        this.dateList = dateList;
    }

    public Date getSelectedDate() {
        return selectedDate;
    }

    public void setSelectedDate(Date selectedDate) {
        this.selectedDate = selectedDate;
    }
    
    

    /**
     * Gets a specified meeting that a user is a part of
     * @param id the meeting's id
     * @return Meeting the meeting
     */
    public Meeting getMeetingById(int id) {
        return ((Meeting) getEntityManager().createNamedQuery("Meeting.findById").setParameter("id", id).getSingleResult());
    }

}
