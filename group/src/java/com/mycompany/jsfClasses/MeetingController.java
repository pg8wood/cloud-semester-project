package com.mycompany.jsfClasses;

import com.mycompany.entityClasses.Meeting;
import com.mycompany.entityClasses.MeetingUsers;
import com.mycompany.entityClasses.User;
import com.mycompany.jsfClasses.util.JsfUtil;
import com.mycompany.jsfClasses.util.JsfUtil.PersistAction;
import com.mycompany.sessionBeans.MeetingFacade;
import com.mycompany.sessionBeans.MeetingUsersFacade;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@Named("meetingController")
@SessionScoped
public class MeetingController implements Serializable {

    @EJB
    private com.mycompany.sessionBeans.MeetingFacade meetingFacade;
    
    @EJB
    private com.mycompany.sessionBeans.MeetingUsersFacade meetingUsersFacade;
    
    private List<Meeting> items = null;
    private Meeting selected;

    public MeetingController() {
    }

    public Meeting getSelected() {
        return selected;
    }

    public void setSelected(Meeting selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private MeetingFacade getMeetingFacade() {
        return meetingFacade;
    }
    
    private MeetingUsersFacade getMeetingUsersFacade() {
        return meetingUsersFacade;
    }

    public Meeting prepareCreate() {
        selected = new Meeting();
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("MeetingCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("MeetingUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("MeetingDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<Meeting> getItems() {
        if (items == null) {
            items = getMeetingFacade().findAll();
        }
        return items;
    }

    private void persist(PersistAction persistAction, String successMessage) {
        if (selected != null) {
            setEmbeddableKeys();
            try {
                if (persistAction != PersistAction.DELETE) {
                    getMeetingFacade().edit(selected);
                } else {
                    getMeetingFacade().remove(selected);
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

    public Meeting getMeeting(java.lang.Integer id) {
        return getMeetingFacade().find(id);
    }

    public List<Meeting> getItemsAvailableSelectMany() {
        return getMeetingFacade().findAll();
    }

    public List<Meeting> getItemsAvailableSelectOne() {
        return getMeetingFacade().findAll();
    }
    
    /**
     * Gets all unaccepted meeting invitations for a user
     * 
     * @param user the logged in user
     * @return the list of meetings
     */
    public List<Meeting> getMeetingInvitations(User user) {
        List<MeetingUsers> meetingUsers = getMeetingUsersFacade().getMeetingInvitations(user);
        List<Meeting> meetingInvitations = new ArrayList();
        
        // Add the users to the list
        for (MeetingUsers invitation: meetingUsers) {
            meetingInvitations.add(getMeetingFacade().getMeetingById(invitation.getMeetingUsersPK().getMeetingId()));
        }
        
        return meetingInvitations;
    }
    
    /**
     * Gets all meetings that the user has responded to
     * 
     * @param user the logged in user
     * @return the list of meetings
     */
    public List<Meeting> getUpcomingMeetings(User user) {
        List<MeetingUsers> meetingUsers = getMeetingUsersFacade().getUpcomingMeetings(user);
        List<Meeting> meetingInvitations = new ArrayList();
        
        // Add the users to the list
        for (MeetingUsers invitation: meetingUsers) {
            meetingInvitations.add(getMeetingFacade().getMeetingById(invitation.getMeetingUsersPK().getMeetingId()));
        }
        
        return meetingInvitations;
    }
    
    @FacesConverter(forClass = Meeting.class)
    public static class MeetingControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            MeetingController controller = (MeetingController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "meetingController");
            return controller.getMeeting(getKey(value));
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
            if (object instanceof Meeting) {
                Meeting o = (Meeting) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Meeting.class.getName()});
                return null;
            }
        }

    }

}
