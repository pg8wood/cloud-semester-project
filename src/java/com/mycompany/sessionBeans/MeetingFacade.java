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
    private List<Date> dateList;

    public MeetingFacade() {
        super(Meeting.class);
        dateList = new ArrayList();
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

    /**
     * Turns a String of comma separated times into
     * an ArrayList of Date objects
     * @param times
     * @return ArrayList<Date> the list of times 
     */
    public ArrayList<Date> deserializeResponseTimes(String times) {
        ArrayList<Date> dList = new ArrayList();

        Scanner serializedStringScan = new Scanner(times);
        serializedStringScan.useDelimiter(",");

        while (serializedStringScan.hasNext()) {
            String nextTime = serializedStringScan.next();
            
            if (nextTime.equals("")) {
                return dList;
            } 
            
            Scanner dateScan = new Scanner(nextTime);
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

            int year = dateScan.nextInt() - 1900;

            // Store the parsed Dates into the list
            Date newDate = new Date(year, getMonthInt(monthName), dayNumber, hour, minute);
            if (dList.isEmpty()) {
                dList.add(newDate);
            } else {
                System.out.println("\n\n\nLooking to see if list contains " + newDate.toString());
                for (int i = 0; i < dList.size(); i++) {
                    
                    String compareString = dList.get(i).toString().substring(4, dList.get(i).toString().length() - 5);
                    String newCompareString = newDate.toString().substring(4, newDate.toString().length() - 5);
                    
                    System.out.println("Comparing [" + compareString + "] and [" + newCompareString + "]");
                    

                    // Don't add duplicate entries to the list
                    if (compareString.equals(newCompareString)) {
                        System.out.println("Date already in list. Continuing...");
                        break;
                    } else {
//                        System.out.println("String weren't equal.");
                        if (i == dList.size() - 1) {
                            dList.add(newDate);
                        }
                    }

                }
            }
       }
        return dList;
    }

    /**
     * Turns a String of comma separated times into
     * an ArrayList of Date objects
     * @param times
     * @return ArrayList<Date> the list of times 
     */
    public ArrayList<Date> deserialize(String times) {
        ArrayList<Date> dList = new ArrayList();

        Scanner serializedStringScan = new Scanner(times);
        serializedStringScan.useDelimiter(",");

        while (serializedStringScan.hasNext()) {
            Scanner dateScan = new Scanner(serializedStringScan.next());
            /* Serialized dates are stored in the database in the form: 
           java.util.Date.toString(),java.util.date.toString,... */    
            try {
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

            int year = dateScan.nextInt() - 1900;

            // Store the parsed Dates into the list
            Date newDate = new Date(year, getMonthInt(monthName), dayNumber, hour, minute);

            dList.add(newDate);
            }
            catch (Exception e) {
            }

        }
        return dList;
    }

    /** 
     * Converts a String day name to its corresponding day number
     * @param dayName
     * @return Integer the number of the day 
     */
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

     /** 
     * Converts a String month name to its corresponding month number
     * @param dayName
     * @return Integer the number of the day 
     */
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
     * Gets a String representation of the meeting's time
     *
     * @param meetingDate the date of the meeting to examine
     * @return String the meeting's time in String format
     */
    public String getMeetingString(Date meetingDate) {
        StringBuilder sb = new StringBuilder();

        if (meetingDate.getHours() > 12) {
            sb.append(Integer.toString(meetingDate.getHours()))
                    .append(":")
                    .append(String.format("%02d", meetingDate.getMinutes()))
                    .append(" AM");
        } else {
            sb.append(Integer.toString(meetingDate.getHours() % 12))
                    .append(":")
                    .append(String.format("%02d", meetingDate.getMinutes()))
                    .append(" PM");
        }

        return sb.toString();
    }

    /**
     * Gets all the available timeslots for a given meeting.
     *
     * @param meeting The meeting to examine
     * @return ArrayList<Date> the list of available timeslots
     */
    public ArrayList<Date> getTimeslotsForMeeting(Meeting meeting) {
        deserializeTimeslots(meeting);
        return (ArrayList<Date>) dateList;
    }

    /**
     * Gets all the participants for a given meeting.
     *
     * @param meeting The meeting to examine
     * @return ArrayList<User> the list of users participating
     */
    public ArrayList<User> getParticipantList(Meeting meeting) {
        System.out.println(meeting);
        if(meeting.getId() != null){
            int meetingId = meeting.getId();
            List<MeetingUsers> meetingUsers = (List<MeetingUsers>) getEntityManager().createNamedQuery("MeetingUsers.findByMeetingIdAndResponse").setParameter("meetingId", meetingId).setParameter("response", true).getResultList();
            ArrayList<User> participantList = new ArrayList<User>();
            for (MeetingUsers x : meetingUsers) {
                participantList.add(x.getUser());
            }
            return participantList;
        }
        return new ArrayList<>();
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
     *
     * @param id the meeting's id
     * @return Meeting the meeting
     */
    public Meeting getMeetingById(int id) {
        return ((Meeting) getEntityManager().createNamedQuery("Meeting.findById").setParameter("id", id).getSingleResult());
    }

    /**
     * Gets a meeting with maximum id
     *
     * @return Meeting the meeting
     */
    public int getMeetingMaxId() {
        try {
            getEntityManager().createNativeQuery("SELECT ID FROM Meeting ORDER BY ID DESC LIMIT 1").getSingleResult();
        } catch (Exception ex) {
            return -1;
        }
        return ((Long) getEntityManager().createNativeQuery("SELECT ID FROM Meeting ORDER BY ID DESC LIMIT 1").getSingleResult()).intValue();
    }

    /**
     * Gets a specified meeting that a user is a part of
     *
     * @param user the User
     * @return List of Meetings that belongs to the user
     */
    public List<Meeting> getMeetingsByOwnerId(User user) {
        return ((List<Meeting>) getEntityManager().createNamedQuery("Meeting.findByOwnerId").setParameter("owner_id", user).getResultList());
    }

    /**
     * Gets a specified meeting that a user is a part of
     *
     * @param user the User
     * @return max id of meeting that user owns
     */
    public int getMaxIdOfOwner(User user) {
        List<Meeting> list = (List<Meeting>) getEntityManager().createNamedQuery("Meeting.findByOwnerId").setParameter("owner_id", user).getResultList();

        int max = -1;
        for (int i = 0; i < list.size() - 1; i++) {
            if (list.get(i).getId() > max) {
                max = list.get(i).getId();
            }
        }

        return max;
    }

}
