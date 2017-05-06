
/*
 * Created by Jeffrey Shih on 2017.04.05  * 
 * Copyright © 2017 Jeffrey Shih. All rights reserved. * 
 */
package com.mycompany.managers;

/**
 *
 * @author Jeff
 */
import com.mycompany.entityClasses.Meeting;
import com.mycompany.entityClasses.MeetingUsers;
import com.mycompany.entityClasses.User;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import org.primefaces.event.ScheduleEntryMoveEvent;
import org.primefaces.event.ScheduleEntryResizeEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.LazyScheduleModel;
import org.primefaces.model.ScheduleEvent;
import org.primefaces.model.ScheduleModel;

@ManagedBean(name = "calendarManager")
@SessionScoped
public class CalendarManager implements Serializable {

    private ScheduleModel eventModel;

    private ScheduleModel lazyEventModel;

    private ScheduleEvent event = new DefaultScheduleEvent();

    @EJB
    private com.mycompany.sessionBeans.MeetingFacade meetingFacade;
    @EJB
    private com.mycompany.sessionBeans.MeetingUsersFacade meetingUsersFacade;

    private Collection<Meeting> meetingsList;
    
    private Meeting selectedMeeting = new Meeting();

    public Meeting getSelectedMeeting() {
        return selectedMeeting;
    }

    public void setSelectedMeeting(Meeting selectedMeeting) {
        this.selectedMeeting = selectedMeeting;
    }

    public ScheduleModel getEventModel() {
        return eventModel;
    }

    public ScheduleModel getLazyEventModel(User user) {
        System.out.print(user);
        if (lazyEventModel == null) {
            lazyEventModel = new LazyScheduleModel() {

                @Override
                public void loadEvents(Date start, Date end) {

                    if (user.getMeetingCollection().isEmpty()) {
                        System.out.print("no meetings");
                    } else {
                        
//                        meetingsList = user.getMeetingCollection();
//                        for (Meeting m : meetingsList) {
//                            System.out.print(m.getDescription());
//                            Meeting updatedM = meetingFacade.getMeetingById(m.getId());
//                            
//                            if (updatedM.getFinaltime() != null && updatedM.getFinaltime().length() > 0) {
//                                System.out.print("Final Start Time: " + updatedM.getFinaltime());
//                                ArrayList<Date> dateObj = meetingFacade.deserialize(updatedM.getFinaltime());
//                                System.out.print("AFter deserialized: " + dateObj.get(0).toString());
//                                Date endTime = addHour(dateObj.get(0));
//                                addEvent(new DefaultScheduleEvent(updatedM.getTopic(), yearCheck(dateObj.get(0)), endTime));
//                                System.out.print("Shouldve Added");
//                            }
//                        }
                        
                        List<MeetingUsers> muL = meetingUsersFacade.getMeetings(user);
                        for(MeetingUsers mu: muL){
                            Meeting updatedM = meetingFacade.getMeetingById(mu.getMeeting().getId());
                            
                            if (updatedM.getFinaltime() != null && updatedM.getFinaltime().length() > 0) {
                                //System.out.print("Final Start Time: " + updatedM.getFinaltime());
                                ArrayList<Date> dateObj = meetingFacade.deserialize(updatedM.getFinaltime());
                                //System.out.print("AFter deserialized: " + dateObj.get(0).toString());
                                Date endTime = addHour(dateObj.get(0));
                                addEvent(new DefaultScheduleEvent(updatedM.getTopic(), yearCheck(dateObj.get(0)), endTime));
                                //System.out.print("Shouldve Added");
                            }
                        }
                    }

                }
            };
        }
        return lazyEventModel;
    }


    private Date yearCheck(Date date){
        Calendar cl = Calendar.getInstance(); 
        cl.setTime(date);
        int year = cl.get(Calendar.YEAR);
        if(year > 3000){
            cl.add(Calendar.YEAR, -1900);
        }
        System.out.print("Year check: " + cl.getTime().toString());
        return cl.getTime();
    }
    
    private Date addHour(Date date) {
        
        Calendar cl = Calendar.getInstance(); 
        cl.setTime(date);
        cl.add(Calendar.HOUR, 1);
        cl.add(Calendar.DAY_OF_WEEK, -1);
        cl.add(Calendar.DATE, 1);
        int year = cl.get(Calendar.YEAR);
        if(year > 3000){
            cl.add(Calendar.YEAR, -1900);
        }
        System.out.print("Add hour: " + cl.getTime().toString());
        return cl.getTime();
        
    }

    public ScheduleEvent getEvent() {
        return event;
    }

    public void setEvent(ScheduleEvent event) {
        this.event = event;
    }

    public void addEvent(ActionEvent actionEvent) {
        if (event.getId() == null) {
            eventModel.addEvent(event);
        } else {
            eventModel.updateEvent(event);
        }

        event = new DefaultScheduleEvent();
    }

    public void onEventSelect(SelectEvent selectEvent) {

        event = (ScheduleEvent) selectEvent.getObject();
        
        for(Meeting m: meetingsList){
            if(event.getTitle().equals(m.getTopic())){
                selectedMeeting = m;
                break;
            }
        }
       
    }
    
    public ArrayList<User> getParticipants(){
        Collection<MeetingUsers> participants = selectedMeeting.getMeetingUsersList();
        ArrayList<User> results = new ArrayList<>();
        if(participants == null || participants.size() < 1){
            return results;
        }
        for(MeetingUsers mu : participants){
                results.add(mu.getUser());
        }
        
        return results;
    }
    
    public String getSelectedParticipants(){
        String out = "";
        
        Collection<MeetingUsers> participants = selectedMeeting.getMeetingUsersList();
        if(participants == null || participants.size() < 1){
            return out;
        }
        for(MeetingUsers u: participants){
            out += u.getUser().getFirstName() + " " + u.getUser().getLastName() + ", ";
            
        }
        
        return out.substring(0,out.length() - 2);
    }
    
   
    public void onDateSelect(SelectEvent selectEvent) {

    }

    public String getTime(Date date) {
        SimpleDateFormat localDateFormat = new SimpleDateFormat("HH:mm:ss");
        String time = localDateFormat.format(date);
        return time;
    }

    public void onEventMove(ScheduleEntryMoveEvent event) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Event moved", "Day delta:" + event.getDayDelta() + ", Minute delta:" + event.getMinuteDelta());

        addMessage(message);
    }

    public void onEventResize(ScheduleEntryResizeEvent event) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Event resized", "Day delta:" + event.getDayDelta() + ", Minute delta:" + event.getMinuteDelta());

        addMessage(message);
    }

    private void addMessage(FacesMessage message) {
        FacesContext.getCurrentInstance().addMessage(null, message);
    }
}
