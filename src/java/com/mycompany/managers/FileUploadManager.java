/*
 * Created by Patrick Gatewood on 2017.04.19  * 
 * Copyright © 2017 Patrick Gatewood. All rights reserved. * 
 */
package com.mycompany.managers;

import com.mycompany.entityClasses.Meeting;
import com.mycompany.entityClasses.MeetingFile;
import com.mycompany.sessionBeans.MeetingFacade;
import com.mycompany.sessionBeans.MeetingFileFacade;

import com.mycompany.jsfClasses.MeetingFileController;
import javax.inject.Inject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import org.primefaces.model.UploadedFile;
import org.primefaces.event.FileUploadEvent;


@Named(value = "fileUploadManager")
@SessionScoped
/**
 *
 * @author Meeting Scheduler
 */
public class FileUploadManager implements Serializable {

    /*
    ===============================
    Instance Variables (Properties)
    ===============================
     */
    private UploadedFile uploadedFile;

    /*
    The instance variable 'meetingFacade' is annotated with the @EJB annotation.
    The @EJB annotation directs the EJB Container (of the GlassFish AS) to inject (store) the object reference
    of the MeetingFacade object, after it is instantiated at runtime, into the instance variable 'meetingFacade'.
     */
    @EJB
    private MeetingFacade meetingFacade;

    /*
    The instance variable 'meetingFileFacade' is annotated with the @EJB annotation.
    The @EJB annotation directs the EJB Container (of the GlassFish AS) to inject (store) the object reference 
    of the MeetingFileFacade object, after it is instantiated at runtime, into the instance variable 'meetingFileFacade'.
     */
    @EJB
    private MeetingFileFacade meetingFileFacade;

    /*
    The instance variable 'meetingFileController' is annotated with the @Inject annotation.
    The @Inject annotation directs the JavaServer Faces (JSF) CDI Container to inject (store) the object reference 
    of the MeetingFileController object, after it is instantiated at runtime, into the instance variable 'meetingFileController'.
    
    We can do this because we annotated the MeetingFileController class with @Named to indicate
    that the CDI container will manage the objects instantiated from the MeetingFileController class.
     */
    @Inject
    private MeetingFileController meetingFileController;

    // Resulting FacesMessage produced
    FacesMessage resultMsg;
    
    // Selected Meeting 
    private Meeting selectedMeeting;

    /*
    =========================
    Getter and Setter Methods
    =========================
     */
    // Returns the uploaded uploadedFile
    public UploadedFile getUploadedFile() {
        return uploadedFile;
    }

    // Obtains the uploaded uploadedFile
    public void setUploadedFile(UploadedFile uploadedFile) {
        this.uploadedFile = uploadedFile;
    }

    public MeetingFacade getMeetingFacade() {
        return meetingFacade;
    }

    public MeetingFileFacade getMeetingFileFacade() {
        return meetingFileFacade;
    }

    public MeetingFileController getMeetingFileController() {
        return meetingFileController;
    }

    public Meeting getSelectedMeeting() {
        return selectedMeeting;
    }

    public void setSelectedMeeting(Meeting selected) {
        this.selectedMeeting = selected;
    }
    
    

    /*
    ================
    Instance Methods
    ================
     */
    public void handleFileUpload(FileUploadEvent event) throws IOException {
        
        System.out.println("Trying file upload");
        
        try {
            Meeting meeting = selectedMeeting;
            
            System.out.println("meeting: " + selectedMeeting.toString());

            /*
            To associate the file to the meeting, record "meetingId_filename" in the database.
            Since each file has its own primary key (unique id), the meeting can upload
            multiple files with the same name.
             */
            String meetingId_filename = meeting.getId() + "_" + event.getFile().getFileName();
            
            System.out.println("filename: " + meetingId_filename);

            /*
            "The try-with-resources statement is a try statement that declares one or more resources. 
            A resource is an object that must be closed after the program is finished with it. 
            The try-with-resources statement ensures that each resource is closed at the end of the
            statement." [Oracle] 
             */
            try (InputStream inputStream = event.getFile().getInputstream();) {

                // The method inputStreamToFile given below writes the uploaded file into the CloudStorage/FileStorage directory.
                inputStreamToFile(inputStream, meetingId_filename);
                inputStream.close();
            }

            /*
            Create a new MeetingFile object with attibutes: (See MeetingFile table definition inputStream DB)
                <> id = auto generated as the unique Primary key for the meeting file object
                <> filename = meetingId_filename
                <> meeting_id = meeting
             */
            MeetingFile newMeetingFile = new MeetingFile(meetingId_filename, meeting);

            /*
            ==============================================================
            If the meetingId_filename was used before, delete the earlier file.
            ==============================================================
             */
            List<MeetingFile> filesFound = getMeetingFileFacade().getMeetingFilesByFilename(meetingId_filename);

            /*
            If the meetingId_filename already exists in the database, 
            the filesFound List will not be empty.
             */
            if (!filesFound.isEmpty()) {

                // Remove the file with the same name from the database
                getMeetingFileFacade().remove(filesFound.get(0));
            }

            //---------------------------------------------------------------
            
            // Create the new MeetingFile entity (row) in the MeetingsDB
            System.out.println("creating meetingFile");
            getMeetingFileFacade().create(newMeetingFile);
            

            // This sets the necessary flag to ensure the messages are preserved.
            FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);

            getMeetingFileController().refreshFileList();

            resultMsg = new FacesMessage("File(s) Uploaded Successfully!");
            FacesContext.getCurrentInstance().addMessage(null, resultMsg);

            // After successful upload, show the MeetingFiles.xhtml facelets page
//            FacesContext.getCurrentInstance().getExternalContext().redirect("MeetingFiles.xhtml");

        } catch (IOException e) {
            resultMsg = new FacesMessage("Something went wrong during file upload! See: " + e.getMessage());
            System.out.println("Error: " + e.getMessage());
            
            FacesContext.getCurrentInstance().addMessage(null, resultMsg);
        }

    }

    // Show the File Upload Page
    public String showFileUploadPage() {

        return "UploadFile?faces-redirect=true";
    }

    /**
     * cancel an upload
     *
     * @return
     */
    public String cancel() {
        //message = "";
        return "Profile?faces-redirect=true";
    }

    private File inputStreamToFile(InputStream inputStream, String file_name)
            throws IOException {

        // Read the series of bytes from the input stream
        byte[] buffer = new byte[inputStream.available()];
        inputStream.read(buffer);

        // Write the series of bytes on uploadedFile.
        File targetFile = new File(Constants.FILES_ABSOLUTE_PATH, file_name);

        OutputStream outStream;
        outStream = new FileOutputStream(targetFile);
        outStream.write(buffer);
        outStream.close();

        return targetFile;
    }

    /**
     * Sets the file location
     *
     * @param data
     */
    public void setFileLocation(MeetingFile data) {

        String fileName = data.getFilename();

        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        ec.getFlash().put("data", data);
    }

    public String getFileLocation() {

        return Constants.FILES_ABSOLUTE_PATH;
    }

    /**
     * Used to return the file extension for a file.
     *
     * @param filename
     * @return
     */
    public static String getExtension(String filename) {

        if (filename == null) {
            return null;
        }
        int extensionPos = filename.lastIndexOf('.');

        int lastUnixPos = filename.lastIndexOf('/');
        int lastWindowsPos = filename.lastIndexOf('\\');
        int lastSeparator = Math.max(lastUnixPos, lastWindowsPos);
        int index = lastSeparator > extensionPos ? -1 : extensionPos;

        if (index == -1) {
            return "";
        } else {
            return filename.substring(index + 1);
        }
    }

}
