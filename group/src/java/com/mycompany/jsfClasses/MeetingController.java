package com.mycompany.jsfClasses;

import com.mycompany.entityClasses.Meeting;
import com.mycompany.entityClasses.MeetingUsers;
import com.mycompany.entityClasses.User;
import com.mycompany.jsfClasses.util.JsfUtil;
import com.mycompany.jsfClasses.util.JsfUtil.PersistAction;
import com.mycompany.sessionBeans.MeetingFacade;
import com.mycompany.sessionBeans.MeetingFileFacade;
import com.mycompany.sessionBeans.MeetingUsersFacade;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;

@Named("meetingController")
@SessionScoped
public class MeetingController implements Serializable {

    @EJB
    private com.mycompany.sessionBeans.MeetingFacade meetingFacade;

    @EJB
    private com.mycompany.sessionBeans.MeetingUsersFacade meetingUsersFacade;

    @EJB
    private com.mycompany.sessionBeans.MeetingFileFacade meetingFileFacade;

    // Instance fields
    private List<Meeting> items;
    private ArrayList<Date> timesForDay;
    private Meeting selected;
    private Date selectedDate;
    private boolean isResponding;
    private Date finalDateSelect;

    public List<Meeting> getUpcomingMeetingsAfterToday(User user) throws ParseException{
        if(user != null){
            List<MeetingUsers> meetingusers = getMeetingUsersFacade().getUpcomingMeetings(user);
            List<Meeting> response = new ArrayList<>();
            Date today = new Date();
            for(MeetingUsers mu : meetingusers){
                Meeting m = mu.getMeeting();
                if(m.isFinalized()){
                    DateFormat formatter = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy");
                    try{
                        
                        Date meetingDate = formatter.parse(m.getFinaltime());
                        if(meetingDate.after(today)){
                            response.add(m);
                        }
                    } catch (ParseException e){
                        System.out.println("ERROR PARSING DATE");
                    }

                }
            }
            return response;
        }
        return null;
    }
    
    public Date getFinalDateSelect() {
        return finalDateSelect;
    }

    public void setFinalDateSelect(Date finalDateSelect) {
        this.finalDateSelect = finalDateSelect;
    }

    public void onDateSelect(SelectEvent event) {
        setFinalDateSelect((Date) event.getObject());
        System.out.print("date selected");
        System.out.print(event.getObject().toString());
        System.out.print(finalDateSelect.toString());
        //SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");

    }

    public MeetingController() {
        items = null;
        timesForDay = null;
        selected = null;
        selectedDate = null;
        isResponding = false;
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

    public MeetingFacade getMeetingFacade() {
        return meetingFacade;
    }

    private MeetingUsersFacade getMeetingUsersFacade() {
        return meetingUsersFacade;
    }

    public MeetingFileFacade getMeetingFileFacade() {
        return meetingFileFacade;
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

    public Date getSelectedDate() {
        return selectedDate;
    }

    public ArrayList<Date> getTimesForDay() {
        return timesForDay;
    }

    public void setTimesForDay(ArrayList<Date> timesForDay) {
        this.timesForDay = timesForDay;
    }

    public boolean isIsResponding() {
        return isResponding;
    }

    public void setIsResponding(boolean isResponding) {
        this.isResponding = isResponding;
    }

    public boolean shouldHideTimeForMeeting(Meeting meeting) {
        return meeting.equals(this.selected);
    }

    public void setSelectedDate(Date selectedDate, String toUpdate, Meeting selected) {
        this.selectedDate = selectedDate;
        this.isResponding = true;
        this.selected = selected;
    }

    /**
     * Get the selected date as a readable String
     *
     * @return String the date as a string
     */
    public String getSelectedDateAsString() {
        if (selectedDate == null) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        sb.append(getDayOfWeek(selectedDate.getDay()))
                .append(", ")
                .append(getMonthName(selectedDate.getMonth()))
                .append(" ")
                .append(selectedDate.getDate());

        return sb.toString();
    }

    public boolean shouldRenderRepeat() {
        return selectedDate != null;
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
        for (MeetingUsers invitation : meetingUsers) {
            meetingInvitations.add(getMeetingFacade().getMeetingById(invitation.getMeetingUsersPK().getMeetingId()));
        }
        //System.out.print("Invites: " + meetingInvitations.size());

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
        for (MeetingUsers invitation : meetingUsers) {
            meetingInvitations.add(getMeetingFacade().getMeetingById(invitation.getMeetingUsersPK().getMeetingId()));
        }

        System.out.print(meetingInvitations.size());
        return meetingInvitations;
    }

    public List<MeetingUsers> getNotResponded(Meeting m) {
        return getMeetingUsersFacade().getNotResponded(m);
    }

    /*
    For the selected meeting m, return all the times people responded they could attend
     */
    public ArrayList<Date> getAllAvailableTimes(Meeting m) {
        List<MeetingUsers> users = getMeetingUsersFacade().getResponded(m);
        String out = "";

        for (MeetingUsers u : users) {
            //out += "User id: " + u.getUser().getUsername() + "  Times: " + u.getAvailableTimes() + "   ";
            out += u.getAvailableTimes() + ",";
        }

        String in = out.substring(0, out.length() - 1);
        System.out.print("FIND ME -------------------------------------------");
        System.out.print(in);

        String mmm = "";
        ArrayList<Date> d = getMeetingFacade().deserializeResponseTimes(in);

        return d;
    }

    public int findNumYes(Date date, Meeting m) {
        int yes = 0;
        List<MeetingUsers> users = getMeetingUsersFacade().getResponded(m);

        String input = "";
        for (MeetingUsers u : users) {
            input += u.getAvailableTimes() + ",";
        }

        input = input.substring(0, input.length() - 1);
        ArrayList<Date> d = getMeetingFacade().deserialize(input);

        for (Date toCheck : d) {
            if (toCheck.equals(date)) {
                yes++;
            }
        }
        return yes;
    }

    public void updateFinalTime(int id, String time) {
        Meeting s = getMeetingFacade().getMeetingById(id);
        setSelected(s);
        System.out.print("This is selected: " + selected.toString());
        selected.setFinaltime(time);
        update();

        System.out.print("updating final time");
    }

    /**
     * Gets the times associated with a particular date
     *
     * @param times the list of times to examine
     * @param day
     * @return
     */
    public ArrayList<Date> getTimesForDay(List<Date> times, Date day) {
        int dateStartIndex = times.indexOf(day);
        ArrayList newTimesForDay = new ArrayList();

        if (day != null && dateStartIndex >= 0) {
            for (int i = dateStartIndex; i < times.size(); i++) {
                if (times.get(i).getYear() == day.getYear()
                        && times.get(i).getMonth() == day.getMonth()
                        && times.get(i).getDate() == day.getDate()) {
                    newTimesForDay.add(times.get(i));
                } else {
                    break;
                }
            }
        }

        if (day == null) {
            System.out.println("\n\n\nWARNING: DAY IS NULL");
        } else {

            if (newTimesForDay.size() > 0) {
                timesForDay = newTimesForDay;

            }
        }

        return timesForDay;
    }

    /**
     * Gets a list of unique days in dateList
     *
     * @prereq: dateList must be sorted in chronological order
     * @param meeting the meeting to examine
     * @return ArraList<Date> a list containing unique days only
     */
    public ArrayList<Date> getUniqueDateListForMeeting(Meeting meeting) {
        ArrayList<Date> uniqueDays = new ArrayList<>();

        for (Date d : getMeetingFacade().getTimeslotsForMeeting(meeting)) {
            if (uniqueDays.isEmpty()) {
                uniqueDays.add(d);
            } else if (uniqueDays.get(uniqueDays.size() - 1).getDate() != d.getDate()) {
                uniqueDays.add(d);
            }
        }

        return uniqueDays;
    }

    public String getDayOfWeek(int day) {
        switch (day) {
            case 0:
                return "Sun";
            case 1:
                return "Mon";
            case 2:
                return "Tue";
            case 3:
                return "Wed";
            case 4:
                return "Thu";
            case 5:
                return "Fri";
            case 6:
                return "Sat";
            default:
                return "";
        }
    }

    public String getMonthName(int month) {
        switch (month) {
            case 0:
                return "January";
            case 1:
                return "February";
            case 2:
                return "March";
            case 3:
                return "April";
            case 4:
                return "May";
            case 5:
                return "June";
            case 6:
                return "July";
            case 7:
                return "August";
            case 8:
                return "September";
            case 9:
                return "October";
            case 10:
                return "November";
            case 11:
                return "December";
            default:
                return "";
        }
    }

    /**
     * Gets the appropriate String for the ConfirmDialog based on how the user
     * has responded to an invitation.
     *
     * @param availableTimes the list of times the user has indicated that they
     * are available
     * @return String the message to show the user
     */
    public String getConfirmationMessage(ArrayList<String> availableTimes) {
        if (availableTimes.isEmpty()) {
            return "You haven't selected any times. If you continue, you are declining this invitation. Are you sure you would like to continue?";
        } else {
            return "Are you sure you would like to submit all times and days selected?";
        }
    }

    /**
     * Gets the day of the week associated with a date.
     */
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
