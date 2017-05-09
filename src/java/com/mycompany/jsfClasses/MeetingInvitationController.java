/*
 * Created by Patrick Gatewood on 2017.04.15  * 
 * Copyright © 2017 Patrick Gatewood. All rights reserved. * 
 */
package com.mycompany.jsfClasses;

import java.util.Date;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

/**
 *
 * @author PatrickGatewood
 */
@Named("meetingInvitationController")
@RequestScoped
public class MeetingInvitationController {
    
    @EJB
    private com.mycompany.sessionBeans.MeetingFacade meetingFacade;
    
    
    /**
     * Sets the selected Date. 
     * 
     * @param selectedDate the Date to set 
     */
    public void setSelectedDate(Date selectedDate) {
         meetingFacade.setSelectedDate(selectedDate);
    }
}
