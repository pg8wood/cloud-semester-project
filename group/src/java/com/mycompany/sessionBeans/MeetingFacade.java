/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.sessionBeans;

import com.mycompany.entityClasses.Meeting;
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

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    // Instance fields
    private HashMap<Date, List<String>> timesByDateMap;

    public MeetingFacade() {
        super(Meeting.class);
        timesByDateMap = null;
    }

    // ----------------
    // Instance methods 
    // ----------------
    /**
     * Gets the meeting's timeslots and de-serializes them
     */
    public void deserializeTimeslots(Meeting meeting) {
        timesByDateMap = new HashMap<>();
        Scanner dateScanner = new Scanner(meeting.getTimeslots());
        dateScanner.useDelimiter(";");

        // Iterate over timeslot String getting dates
        while (dateScanner.hasNext()) {
            List<String> timesList = new ArrayList();
            Scanner timeScanner = new Scanner(dateScanner.next());
            timeScanner.useDelimiter(",");

            // Create a Date object to serve as the HashMap Key
            String dateString = timeScanner.next();
            Scanner dateScan = new Scanner(dateString);
            dateScan.useDelimiter("-");
            Date calendarDate = new Date();

            // dateString format is YYYY-MM-DD
            calendarDate.setYear(dateScan.nextInt());
            calendarDate.setMonth(dateScan.nextInt());
            calendarDate.setDate(dateScan.nextInt());

            // Store the times
            while (timeScanner.hasNext()) {
                timesList.add(timeScanner.next());
            }

            // Map the calendarDate to the list of times
            timesByDateMap.put(calendarDate, timesList);
        }
    }
    
     /**
     * Gets a Date object's string representation in the format YYYY-MM-DD
     * 
     * @param calendarDate the Date object to interpret
     * @return String the string representation of the Date object
     */
//    public String getDateString(Date calendarDate) {
//        StringBuilder sb = new StringBuilder();
//        sb.append(Integer.toString(calendarDate.YEAR));
//        sb.append("-");
//        sb.append(Integer.toString(calendarDate.MONTH));
//        sb.append("-");
//        sb.append(Integer.toString(calendarDate.DAY_OF_MONTH));
//        
//        return sb.toString();
//    }
    
    public HashMap<Date, List<String>> getTimeslotsForMeetingInvitation(Meeting invitation) {
        deserializeTimeslots(invitation);
        return timesByDateMap;
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

}
