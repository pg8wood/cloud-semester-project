/*
 * Created by Patrick Gatewood on 2017.05.02  * 
 * Copyright © 2017 Patrick Gatewood. All rights reserved. * 
 */
package com.mycompany.entityClasses;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author PatrickGatewood
 */
@Entity
@Table(name = "Meeting_Users")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "MeetingUsers.findAll", query = "SELECT m FROM MeetingUsers m")
    , @NamedQuery(name = "MeetingUsers.findByUserId", query = "SELECT m FROM MeetingUsers m WHERE m.meetingUsersPK.userId = :userId")
    , @NamedQuery(name = "MeetingUsers.findByMeetingId", query = "SELECT m FROM MeetingUsers m WHERE m.meetingUsersPK.meetingId = :meetingId")
    , @NamedQuery(name = "MeetingUsers.findByResponse", query = "SELECT m FROM MeetingUsers m WHERE m.response = :response")
    , @NamedQuery(name = "MeetingUsers.findByAvailableTimes", query = "SELECT m FROM MeetingUsers m WHERE m.availableTimes = :availableTimes")
    , @NamedQuery(name = "MeetingUsers.findByMeetingIdAndResponse", query = "SELECT m FROM MeetingUsers m WHERE m.response = :response AND m.meetingUsersPK.meetingId = :meetingId")
    , @NamedQuery(name = "MeetingUsers.findByUserIdAndResponse", query = "SELECT m FROM MeetingUsers m WHERE m.meetingUsersPK.userId = :userId AND m.response = :response")
})
public class MeetingUsers implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected MeetingUsersPK meetingUsersPK;
    @Column(name = "response")
    private Boolean response;
    @Size(max = 4000)
    @Column(name = "available_times")
    private String availableTimes;
    @JoinColumn(name = "user_id", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private User user;
    @JoinColumn(name = "meeting_id", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Meeting meeting;

    public MeetingUsers() {
    }

    public MeetingUsers(MeetingUsersPK meetingUsersPK) {
        this.meetingUsersPK = meetingUsersPK;
    }

    public MeetingUsers(int userId, int meetingId) {
        this.meetingUsersPK = new MeetingUsersPK(userId, meetingId);
    }

    public MeetingUsersPK getMeetingUsersPK() {
        return meetingUsersPK;
    }

    public void setMeetingUsersPK(MeetingUsersPK meetingUsersPK) {
        this.meetingUsersPK = meetingUsersPK;
    }

    public Boolean getResponse() {
        return response;
    }

    public void setResponse(Boolean response) {
        this.response = response;
    }

    public String getAvailableTimes() {
        return availableTimes;
    }

    public void setAvailableTimes(String availableTimes) {
        this.availableTimes = availableTimes;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Meeting getMeeting() {
        return meeting;
    }

    public void setMeeting(Meeting meeting) {
        this.meeting = meeting;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (meetingUsersPK != null ? meetingUsersPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MeetingUsers)) {
            return false;
        }
        MeetingUsers other = (MeetingUsers) object;
        if ((this.meetingUsersPK == null && other.meetingUsersPK != null) || (this.meetingUsersPK != null && !this.meetingUsersPK.equals(other.meetingUsersPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.entityClasses.MeetingUsers[ meetingUsersPK=" + meetingUsersPK + " response: " + response + " ]";
    }

}
