
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
import com.mycompany.entityClasses.User;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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

    public Date getRandomDate(Date base) {
        Calendar date = Calendar.getInstance();
        date.setTime(base);
        date.add(Calendar.DATE, ((int) (Math.random() * 30)) + 1);    //set random day of month

        return date.getTime();
    }

    public Date getInitialDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(calendar.get(Calendar.YEAR), Calendar.FEBRUARY, calendar.get(Calendar.DATE), 0, 0, 0);

        return calendar.getTime();
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
                        
                        for (Meeting m : user.getMeetingCollection()) {
                            System.out.print(m.getDescription());
                            Meeting updatedM = meetingFacade.getMeetingById(m.getId());
                            
                            if (updatedM.getFinaltime() != null && updatedM.getFinaltime().length() > 0) {
                                System.out.print("Final Start Time: " + updatedM.getFinaltime());
                                ArrayList<Date> dateObj = meetingFacade.deserialize(updatedM.getFinaltime());
                                System.out.print("AFter deserialized: " + dateObj.get(0).toString());
                                Date endTime = addHour(dateObj.get(0));
                                addEvent(new DefaultScheduleEvent(updatedM.getTopic(), dateObj.get(0), endTime));
                                System.out.print("Shouldve Added");
                            }
                        }
                    }

                }
            };
        }
        return lazyEventModel;
    }


    private Date addHour(Date date) {
        
        Calendar cl = Calendar.getInstance(); 
        cl.setTime(date);
        cl.add(Calendar.HOUR, 1);
        cl.add(Calendar.DAY_OF_WEEK, -1);
        cl.add(Calendar.DATE, 1);
        //cl.add(Calendar.YEAR, -1900);
        System.out.print(cl.getTime().toString());
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
