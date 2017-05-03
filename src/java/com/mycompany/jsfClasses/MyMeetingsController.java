/*
 * Created by Patrick Gatewood on 2017.04.09  * 
 * Copyright © 2017 Patrick Gatewood. All rights reserved. * 
 */
package com.mycompany.jsfClasses;

import com.mycompany.entityClasses.Meeting;
import com.mycompany.entityClasses.User;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import org.primefaces.event.SelectEvent;

@Named("myMeetingsController")
@SessionScoped

/**
 * Controller class for MeetingInvitation.xhtml
 *
 * @author PatrickGatewood
 */
public class MyMeetingsController implements Serializable {

    @EJB
    private com.mycompany.sessionBeans.UserFacade userFacade;
    private User loggedInUser;
    private Date newDate;
    private String newDateString;
    
    public MyMeetingsController() {
        newDateString = new String();
    }

    /**
     * Gets the object representing the logged in user.
     * 
     * @return the user currently logged in
     */
    public User getLoggedInUser() {
        loggedInUser = userFacade.findByUsername((String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("username"));
        return loggedInUser;
    }

    public void onDateSelect(SelectEvent event) {
//        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
//        newDateString = format.format(event.getObject());
    }

//    public List<Meeting> getLoggedInUserMeetings() {
//        return (List<Meeting>) loggedInUser.getMeetingCollection();
//    }

    public Date getNewDate() {
        return newDate;
    }

    public void setNewDate(Date newDate) {
        this.newDate = newDate;
    }

    public String getNewDateString() {
        return newDateString;
    }

    public void setNewDateString(String newDateString) {
        this.newDateString = newDateString;
    }
    
    
    
  
    
    
    
    

}
