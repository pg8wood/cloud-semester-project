/*
 * Created by Alex Martin on 2017.04.13  * 
 * Copyright © 2017 Alex Martin. All rights reserved. * 
 */
package com.mycompany.entityClasses;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

/**
 *
 * @author alexmartin
 */
@Embeddable
public class MeetingUsersPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "user_id")
    private int userId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "meeting_id")
    private int meetingId;

    public MeetingUsersPK() {
    }

    public MeetingUsersPK(int userId, int meetingId) {
        this.userId = userId;
        this.meetingId = meetingId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getMeetingId() {
        return meetingId;
    }

    public void setMeetingId(int meetingId) {
        this.meetingId = meetingId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) userId;
        hash += (int) meetingId;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MeetingUsersPK)) {
            return false;
        }
        MeetingUsersPK other = (MeetingUsersPK) object;
        if (this.userId != other.userId) {
            return false;
        }
        if (this.meetingId != other.meetingId) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.entityClasses.MeetingUsersPK[ userId=" + userId + ", meetingId=" + meetingId + " ]";
    }
    
}
