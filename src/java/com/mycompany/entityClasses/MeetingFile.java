/*
 * Created by Patrick Gatewood on 2017.04.19  * 
 * Copyright © 2017 Patrick Gatewood. All rights reserved. * 
 */
package com.mycompany.entityClasses;

import com.mycompany.managers.Constants;
import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author PatrickGatewood
 */
@Entity
@Table(name = "MeetingFile")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "MeetingFile.findAll", query = "SELECT m FROM MeetingFile m")
    , @NamedQuery(name = "MeetingFile.findById", query = "SELECT m FROM MeetingFile m WHERE m.id = :id")
    , @NamedQuery(name = "MeetingFile.findByFilename", query = "SELECT m FROM MeetingFile m WHERE m.filename = :filename")})
public class MeetingFile implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 256)
    @Column(name = "filename")
    private String filename;
    @JoinColumn(name = "meeting_id", referencedColumnName = "id")
    @ManyToOne
    private Meeting meetingId;

    public MeetingFile() {
    }

    public MeetingFile(Integer id) {
        this.id = id;
    }

    public MeetingFile(Integer id, String filename) {
        this.id = id;
        this.filename = filename;
    }

    public MeetingFile(String filename, Meeting id) {
        this.filename = filename;
        meetingId = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public Meeting getMeetingId() {
        return meetingId;
    }

    public void setMeetingId(Meeting meetingId) {
        this.meetingId = meetingId;
    }

    public String getFilePath() {
        return Constants.FILES_ABSOLUTE_PATH + getFilename();
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MeetingFile)) {
            return false;
        }
        MeetingFile other = (MeetingFile) object;
        
        return !((this.id == null && other.id != null) 
                || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return "com.mycompany.entityClasses.MeetingFile[ id=" + id + "  meetingId=" + meetingId + "]";
    }

}
