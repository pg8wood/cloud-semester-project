/*
 * Created by Patrick Gatewood on 2017.04.09  * 
 * Copyright © 2017 Patrick Gatewood. All rights reserved. * 
 */
package com.mycompany.jsfClasses;

import java.io.Serializable;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;

@Named("meetingInvitationController")
@SessionScoped

/**
 * Controller class for MeetingInvitation.xhtml
 *
 * @author PatrickGatewood
 */
public class MeetingInvitationController implements Serializable {

    // Instance veriables
    private boolean responding = false;

    public boolean isResponding() {
        return responding;
    }

    public void setResponding(boolean isResponding) {
        this.responding = isResponding;
    }
    
    
}
