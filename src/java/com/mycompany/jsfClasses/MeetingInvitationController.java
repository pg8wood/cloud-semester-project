/*
 * Created by Patrick Gatewood on 2017.04.15  * 
 * Copyright © 2017 Patrick Gatewood. All rights reserved. * 
 */
package com.mycompany.jsfClasses;

import java.util.Date;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import org.primefaces.context.RequestContext;

/**
 *
 * @author PatrickGatewood
 */
@Named("meetingInvitationController")
@RequestScoped
public class MeetingInvitationController {
    
    @EJB
    private com.mycompany.sessionBeans.MeetingFacade meetingFacade;
    
    
    public void setSelectedDate(Date selectedDate, String toUpdate) {
         meetingFacade.setSelectedDate(selectedDate);
        
//        System.out.println("updating: " + toUpdate);
//        
//        RequestContext context = RequestContext.getCurrentInstance();
//        context.update(toUpdate + ":timePanel");

        if (selectedDate != null) {
            System.out.printf("Date set to %s", selectedDate.toString());
        } else {
            System.out.println("Date set to NULL!!!");
        }
    }
}
