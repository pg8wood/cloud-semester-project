/*
 * Created by Patrick Gatewood on 2017.04.19  * 
 * Copyright © 2017 Patrick Gatewood. All rights reserved. * 
 */
package com.mycompany.sessionBeans;

import com.mycompany.entityClasses.Meeting;
import com.mycompany.entityClasses.MeetingFile;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class MeetingFileFacade extends AbstractFacade<MeetingFile> {

    @PersistenceContext(unitName = "groupPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public MeetingFileFacade() {
        super(MeetingFile.class);
    }

    @Override
    public void create(MeetingFile m) {
        System.out.print("meeting: " + m.toString());
        super.create(m);
    }

    // ----------------
    // Instance methods 
    // ----------------
    
    /**
     * Gets a specified meeting that a user is a part of by Meeting
     *
     * @param meeting the meeting to query with
     * @return List<MeetingFile> the attachments for the given meeting
     */
    public List<MeetingFile> getMeetingFilesByMeeting(Meeting meeting) {
        if (meeting != null) {
            List resultList = ((List<MeetingFile>) getEntityManager()
                    .createQuery("SELECT m FROM MeetingFile m WHERE m.meetingId.id = :meeting")
                    .setParameter("meeting", meeting.getId()).getResultList());

            return resultList;
        }

        return new ArrayList();
    }

    /**
     * Gets a specified meeting that a user is a part of by Id
     *
     * @param fileId the ID (primary key) of the MeetingFile
     * @return MeetingFile the attachments for the given meeting
     */
    public MeetingFile getMeetingFileById(int fileId) {
        return (MeetingFile) getEntityManager()
                .createNamedQuery("MeetingFile.findById").setParameter("id", fileId)
                .getSingleResult();
    }

    
    /**
     * Gets a specified meeting that a user is a part of by filename
     *
     * @param filename the name of the file to query with
     * @return List<MeetingFile> the attachments for the given meeting
     */
    public List<MeetingFile> getMeetingFilesByFilename(String filename) {
        return getEntityManager().createNamedQuery("MeetingFile.findByFilename")
                .setParameter("filename", filename).getResultList();
    }

}
