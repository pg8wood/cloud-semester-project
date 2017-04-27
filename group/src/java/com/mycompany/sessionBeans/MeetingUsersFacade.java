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
     *
     * @param user the logged in user
     * @return List the list of meetings
     */
    public List<MeetingUsers> getMeetingInvitations(User user) {
        return getEntityManager().createNamedQuery("MeetingUsers.findByUserIdAndResponse")
                .setParameter("userId", user.getId())
                .setParameter("response", false)
                .getResultList();
    }

    /**
     * Gets a list of all the meetings the user has responded to
     *
     * @param user the logged in user
     * @return List the list of meetings
     */
    public List<MeetingUsers> getUpcomingMeetings(User user) {
//        return getEntityManager().createNamedQuery("MeetingUsers.findByUserIdAndResponse").setParameter("userId", user.getId()).setParameter("response", true).getResultList();
        return getEntityManager().createQuery("SELECT m FROM MeetingUsers m WHERE m.meetingUsersPK.userId = :userId AND m.response = :response AND m.availableTimes != :emptyString")
                .setParameter("userId", user.getId())
                .setParameter("response", true)
                .setParameter("emptyString", "")
                .getResultList();
    }

    /**
     * Gets a MeetingUsers object by a user and a meeting
     *
     * @param user
     * @param meeting
     * @return
     */
    public MeetingUsers getMeetingUser(User user, Meeting meeting) {
        return (MeetingUsers) (getEntityManager().createQuery("SELECT m FROM MeetingUsers m WHERE m.meetingUsersPK.userId = :user AND m.meetingUsersPK.meetingId  = :meeting")
                .setParameter("user", user.getId())
                .setParameter("meeting", meeting.getId())
                .getSingleResult());
    }

}
