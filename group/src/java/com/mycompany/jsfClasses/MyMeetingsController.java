/*
 * Created by Patrick Gatewood on 2017.04.09  * 
 * Copyright © 2017 Patrick Gatewood. All rights reserved. * 
 */
package com.mycompany.jsfClasses;

import com.mycompany.entityClasses.Meeting;
import com.mycompany.entityClasses.User;
import java.io.Serializable;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;

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

    /**
     * Gets the object representing the logged in user.
     *
     * @param username the username to search
     * @return
     */
    public User getLoggedInUser() {
        loggedInUser = userFacade.findByUsername((String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("username"));
        return loggedInUser;
    }

    public List<Meeting> getLoggedInUserMeetings() {
        return (List<Meeting>) loggedInUser.getMeetingCollection();
    }
}
