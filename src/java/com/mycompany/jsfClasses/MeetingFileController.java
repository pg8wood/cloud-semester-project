/*
 * Created by Patrick Gatewood on 2017.04.19  * 
 * Copyright © 2017 Patrick Gatewood. All rights reserved. * 
 */
package com.mycompany.jsfClasses;

import com.mycompany.entityClasses.User;
import com.mycompany.entityClasses.MeetingFile;
import com.mycompany.jsfClasses.util.JsfUtil;
import com.mycompany.jsfClasses.util.JsfUtil.PersistAction;
import com.mycompany.managers.Constants;
import com.mycompany.sessionBeans.UserFacade;
import com.mycompany.sessionBeans.MeetingFileFacade;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import org.primefaces.component.graphicimage.GraphicImage;

@Named("meetingFileController")
@SessionScoped

public class MeetingFileController implements Serializable {

    /*
    ===============================
    Instance Variables (Properties)
    ===============================
    The instance variable 'userFacade' is annotated with the @EJB annotation.
    The @EJB annotation directs the EJB Container (of the GlassFish AS) to inject (store) the object reference
    of the UserFacade object, after it is instantiated at runtime, into the instance variable 'userFacade'.
     */
    @EJB
    private UserFacade userFacade;

    /*
    The instance variable 'meetingFileFacade' is annotated with the @EJB annotation.
    The @EJB annotation directs the EJB Container (of the GlassFish AS) to inject (store) the object reference 
    of the MeetingFileFacade object, after it is instantiated at runtime, into the instance variable 'meetingFileFacade'.
     */
    @EJB
    private MeetingFileFacade meetingFileFacade;

    // selected = Selected MeetingFile object
    private MeetingFile selected;

    // items = list of MeetingFile objects
    private List<MeetingFile> items = null;

    /*
    cleanedFileNameHashMap<KEY, VALUE>
        KEY   = Integer fileId
        VALUE = String cleanedFileNameForSelected
     */
    HashMap<Integer, String> cleanedFileNameHashMap = null;

    // Message to show when file type cannot be processed
    private String fileTypeMessage = "";

    // Selected row number in p:dataTable in MeetingFiles.xhtml
    private String selectedRowNumber = "0";

    /*
    ==================
    Constructor Method
    ==================
     */
    public MeetingFileController() {
    }

    /*
    =========================
    Getter and Setter Methods
    =========================
     */
    public UserFacade getUserFacade() {
        return userFacade;
    }

    public MeetingFileFacade getMeetingFileFacade() {
        return meetingFileFacade;
    }

    public MeetingFile getSelected() {
        return selected;
    }

    public void setSelected(MeetingFile selected) {
        this.selected = selected;
    }

    public String getSelectedRowNumber() {
        return selectedRowNumber;
    }

    public void setSelectedRowNumber(String selectedRowNumber) {
        this.selectedRowNumber = selectedRowNumber;
    }

    public String getFileTypeMessage() {
        return fileTypeMessage;
    }

    public void setFileTypeMessage(String fileTypeMessage) {
        this.fileTypeMessage = fileTypeMessage;
    }

    protected void setEmbeddableKeys() {
    }

    public MeetingFile getMeetingFile(java.lang.Integer id) {
        return getMeetingFileFacade().find(id);
    }

    public List<MeetingFile> getItemsAvailableSelectMany() {
        return getMeetingFileFacade().findAll();
    }

    public List<MeetingFile> getItemsAvailableSelectOne() {
        return getMeetingFileFacade().findAll();
    }

    /*
    ================
    Instance Methods
    ================
     */
    public MeetingFile prepareCreate() {
        selected = new MeetingFile();
        initializeEmbeddableKey();
        return selected;
    }

    protected void initializeEmbeddableKey() {
    }

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("MeetingFileCreated"));

        /*
        JsfUtil.isValidationFailed() returns TRUE if the validationFailed() method has been called
        for the current request. Return of FALSE means that the create operation was successful and 
        we can reset items to null so that it will be recreated with the newly created user file.
         */
        if (!JsfUtil.isValidationFailed()) {
            items = null;
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("MeetingFileUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("MeetingFileDeleted"));

        /*
        JsfUtil.isValidationFailed() returns TRUE if the validationFailed() method has been called
        for the current request. Return of FALSE means that the destroy operation was successful and 
        we can reset items to null so that it will be recreated without the destroyed user file.
         */
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;
        }
    }

    private void persist(PersistAction persistAction, String successMessage) {

        if (selected != null) {
            setEmbeddableKeys();
            try {
                if (persistAction != PersistAction.DELETE) {
                    getMeetingFileFacade().edit(selected);
                } else {
                    getMeetingFileFacade().remove(selected);
                }
                JsfUtil.addSuccessMessage(successMessage);

            } catch (EJBException ex) {

                String msg = "";
                Throwable cause = ex.getCause();
                if (cause != null) {
                    msg = cause.getLocalizedMessage();
                }
                if (msg.length() > 0) {
                    JsfUtil.addErrorMessage(msg);
                } else {
                    JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
                }
            } catch (Exception ex) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
                JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            }
        }
    }

    @FacesConverter(forClass = MeetingFile.class)
    public static class MeetingFileControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            MeetingFileController controller = (MeetingFileController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "meetingFileController");
            return controller.getMeetingFile(getKey(value));
        }

        java.lang.Integer getKey(String value) {
            java.lang.Integer key;
            key = Integer.valueOf(value);
            return key;
        }

        String getStringKey(java.lang.Integer value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value);
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof MeetingFile) {
                MeetingFile o = (MeetingFile) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), MeetingFile.class.getName()});
                return null;
            }
        }

    }

    /*
    =========================
    Delete Selected User File
    =========================
     */
    public String deleteSelectedMeetingFile() {

        MeetingFile meetingFileToDelete = selected;

        FacesMessage resultMsg;

        // This sets the necessary flag to ensure the messages are preserved.
        FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);

        if (meetingFileToDelete == null) {
            resultMsg = new FacesMessage("You do not have a file to delete!");
        } else {
            try {
                // Delete the file from CloudStorage/FileStorage
                Files.deleteIfExists(Paths.get(meetingFileToDelete.getFilePath()));

                // Delete the user file record from the CloudDriveDB database
                getMeetingFileFacade().remove(meetingFileToDelete);
                // MeetingFileFacade inherits the remove() method from AbstractFacade

                resultMsg = new FacesMessage("Selected file is successfully deleted!");

                // See method below
                refreshFileList();

            } catch (IOException e) {
                resultMsg = new FacesMessage("Something went wrong while deleting the user file! See: " + e.getMessage());
                FacesContext.getCurrentInstance().addMessage(null, resultMsg);
            }
        }

        FacesContext.getCurrentInstance().addMessage(null, resultMsg);

        return "MeetingFiles?faces-redirect=true";
    }

    /*
    ========================
    Refresh User's File List
    ========================
     */
    public void refreshFileList() {
        /*
        By setting the items to null, we force the getItems
        method above to retrieve all of the user's files again.
         */
        items = null;
    }

    /*
    ===============================
    Return Image File given File Id
    ===============================
     */
    /**
     *
     * @param fileId database primary key value for a user file
     * @return the file if it is an image file; otherwise return a blank image
     */
    public String imageFile(Integer fileId) {

        // Obtain the object reference of the MeetingFile whose primary key = fileId
        MeetingFile meetingFile = getMeetingFileFacade().getMeetingFileById(fileId);

        // Obtain the meetingFile's filename as it is stored in the CloudDrive DB database
        String meetingFileName = meetingFile.getFilename();

        // Extract the file extension from the filename
        String fileExtension = meetingFileName.substring(meetingFileName.lastIndexOf(".") + 1);

        // Convert file extension to uppercase
        String fileExtensionInCaps = fileExtension.toUpperCase();

        switch (fileExtensionInCaps) {
            case "JPG":
            case "JPEG":
            case "PNG":
            case "GIF":
                // File is an acceptable image type. Return the image file's relative path.
                return Constants.FILES_RELATIVE_PATH + meetingFileName;
            default:
                /*
                The file is not an image file. Return noPreviewImage.png, which is a
                blank transparent image of size 36x36 px, from the resources folder.
                 */
                return "resources/images/noPreviewImage.png";
        }
    }

    /*
    =====================================
    Return Cleaned Filename given File Id
    =====================================
     */
    public String cleanedFilenameForFileId(Integer fileId) {
        /*
        cleanedFileNameHashMap<KEY, VALUE>
            KEY   = Integer fileId
            VALUE = String cleanedFileNameForSelected
         */

        // Obtain the cleaned filename for the given fileId
        String cleanedFileName = cleanedFileNameHashMap.get(fileId);

        return cleanedFileName;
    }

    /*
    =========================================
    Return Cleaned Filename for Selected File
    =========================================
     */
    // This method is called from MeetingFiles.xhtml by passing the fileId as a parameter.
    public String cleanedFileNameForSelected() {

        Integer fileId = selected.getId();
        /*
        cleanedFileNameHashMap<KEY, VALUE>
            KEY   = Integer fileId
            VALUE = String cleanedFileNameForSelected
         */

        // Obtain the cleaned filename for the given fileId
        String cleanedFileName = cleanedFileNameHashMap.get(fileId);

        return cleanedFileName;
    }

    /*
    ====================================
    Return Selected File's Relative Path
    ====================================
     */
    public String getFileRelativePath(MeetingFile attachment) {
        if (attachment != null) {
            return Constants.FILES_RELATIVE_PATH + attachment.getFilename();
        }
        return "";
    }

    /*
    ====================================
    Return icon name for file extension
    ====================================
     */
    public String getFaIconClass(MeetingFile file) {
        selected = file;
        
        if (selected == null) {
            return "";
        }

        switch (extensionOfSelectedFileInCaps()) {
            case "JPG":
            case "JPEG":
            case "PNG":
            case "GIF":
                // Selected file is an image file
                return "fa fa-file-image-o";
            case "PDF":
                return "fa fa-file-pdf-o";
            case "MP4":
            case "MOV":
            case "M4V":
                return "fa fa-file-movie-o";
            case "ZIP":
                return "fa fa-file-zip-o";
            default:
                return "fa fa-file-o";
        }

    }
    
      /*
    ===================================
    Return true if file is a video file
    ===================================
     */
    public boolean isVideo() {

        if (selected == null) {
            return false;
        }

        switch (extensionOfSelectedFileInCaps()) {
            case "MP4":
            case "MOV":
            case "M4V":
                return true;
            default:
                return false;
        }

    }
    
    

    /*
    =============================================
    Return True if Selected File is an Image File
    =============================================
     */
    public boolean isImage() {
        
        if (selected == null) {
            return false;
        }

        fileTypeMessage = "";

        switch (extensionOfSelectedFileInCaps()) {
            case "JPG":
            case "JPEG":
            case "PNG":
            case "GIF":
                // Selected file is an image file
                return true;
            default:
                return false;
        }
    }

    /*
    ========================================
    Return True if Selected File is Viewable
    ========================================
     */
    public boolean isViewable() {
        if (selected == null) {
            return false;
        }

        switch (extensionOfSelectedFileInCaps()) {
            case "CSS":
            case "CSV":
            case "HTML":
            case "JAVA":
            case "PDF":
            case "SQL":
            case "TXT":
                // Selected file is viewable
                fileTypeMessage = "";
                return true;
            default:
                fileTypeMessage = "Unable to display the selected file!";
                return false;
        }
    }

    /*
    =================================================
    Return True if Selected File is an MP4 Video File
    =================================================
     */
    public boolean isMP4Video() {
        if (selected == null) {
            return false;
        }

        return extensionOfSelectedFileInCaps().equals("MP4");
    }
    
    /*
    =====================================================================
    Return True if Selected File is another type of file not handled here
    =====================================================================
     */
    public boolean isOtherFileType() {
        if (selected == null) {
            return false;
        }
        
        return !isVideo() && !isViewable() &&! isImage();
    }

    /*
    ========================================================
    Return Extension of the Selected File in Capital Letters
    ========================================================
     */
    public String extensionOfSelectedFileInCaps() {
        
        if (selected == null) {
            return "";
        }

        // Obtain the selected filename
        String meetingFileName = selected.getFilename();

        // Extract the file extension from the filename
        String fileExtension = meetingFileName.substring(meetingFileName.lastIndexOf(".") + 1);

        // Convert file extension to be in capital letters
        String fileExtensionInCaps = fileExtension.toUpperCase();

        return fileExtensionInCaps;
    }
    
    /*
    ==========================================================
    Return Extension of the Selected File in Lowercase Letters
    ==========================================================
     */
    public String extensionOfSelectedFileInLower() {
        
        if (selected == null) {
            return "";
        }

        // Obtain the selected filename
        String meetingFileName = selected.getFilename();

        // Extract the file extension from the filename
        String fileExtension = meetingFileName.substring(meetingFileName.lastIndexOf(".") + 1);

        // Convert file extension to be in capital letters
        String fileExtensionInCaps = fileExtension.toLowerCase();

        return fileExtensionInCaps;
    }

}
