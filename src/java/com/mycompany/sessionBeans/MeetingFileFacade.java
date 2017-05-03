/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.sessionBeans;

import com.mycompany.entityClasses.Meeting;
import com.mycompany.entityClasses.MeetingFile;
import com.mycompany.entityClasses.MeetingUsers;
import com.mycompany.entityClasses.User;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class MeetingFileFacade extends AbstractFacade<MeetingFile> {

    @PersistenceContext(unitName = "groupPU")
    private EntityManager em;
    private Date selectedDate;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    // Instance fields
    private List<Date> dateList;

    public MeetingFileFacade() {
        super(MeetingFile.class);
        dateList = new ArrayList();
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
     * Gets a specified meeting that a user is a part of
     *
     * @param meeting the meeting to query with
     * @return List<MeetingFile> the attachments for the given meeting
     */
    public List<MeetingFile> getMeetingFilesByMeeting(Meeting meeting) {
        if (meeting != null) {
            System.out.println("searching for: " + meeting.toString());
            System.out.println("\n\n" + getEntityManager().createNamedQuery("MeetingFile.findAll").getResultList().toString());
            List resultList = ((List<MeetingFile>) getEntityManager().createQuery("SELECT m FROM MeetingFile m WHERE m.meetingId.id = :meeting").setParameter("meeting", meeting.getId()).getResultList());
            System.out.println("results: " + resultList.toString());
            
            return resultList;
        }
        
        return new ArrayList();
    }

    public MeetingFile getMeetingFileById(int fileId) {
        return (MeetingFile) getEntityManager().createNamedQuery("MeetingFile.findById").setParameter("id", fileId).getSingleResult();
    }

    public List<MeetingFile> getMeetingFilesByFilename(String filename) {
        return getEntityManager().createNamedQuery("MeetingFile.findByFilename").setParameter("filename", filename).getResultList();
    }

}
