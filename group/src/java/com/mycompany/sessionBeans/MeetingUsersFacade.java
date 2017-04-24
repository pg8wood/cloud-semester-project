/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.sessionBeans;

import com.mycompany.entityClasses.Meeting;
import com.mycompany.entityClasses.MeetingUsers;
import com.mycompany.entityClasses.User;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Jason
 */
@Stateless
public class MeetingUsersFacade extends AbstractFacade<MeetingUsers> {

    @PersistenceContext(unitName = "groupPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public MeetingUsersFacade() {
        super(MeetingUsers.class);
    }
    
    // Instance methods
    
    /**
     * Gets a list of all the meetings the user has not responded to yet
     * @param user the logged in user
     * @return List the list of meetings
     */
    public List<MeetingUsers> getMeetingInvitations(User user) {
        return getEntityManager().createNamedQuery("MeetingUsers.findByUserIdAndResponse").setParameter("userId", user.getId()).setParameter("response", false).getResultList();
    }
    
    /**
     * Gets a list of all the meetings the user has responded to
     * @param user the logged in user
     * @return List the list of meetings
     */
    public List<MeetingUsers> getUpcomingMeetings(User user) {
        return getEntityManager().createNamedQuery("MeetingUsers.findByUserIdAndResponse").setParameter("userId", user.getId()).setParameter("response", true).getResultList();
    }
    
    public List<MeetingUsers> getNotResponded(Meeting meeting){
        return getEntityManager().createNamedQuery("MeetingUsers.findByMeetingIdAndResponse").setParameter("meetingId", meeting.getId()).setParameter("response", false).getResultList();
    
    }
    
    public List<MeetingUsers> getResponded(Meeting meeting){
        return getEntityManager().createNamedQuery("MeetingUsers.findByMeetingIdAndResponse").setParameter("meetingId", meeting.getId()).setParameter("response", true).getResultList();
    
    }
    
}
