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
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;
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
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;

@Named("meetingController")
@SessionScoped
public class MeetingController implements Serializable {

    @EJB
    private com.mycompany.sessionBeans.MeetingFacade meetingFacade;

    @EJB
    private com.mycompany.sessionBeans.UserFacade userFacade;

    @EJB
    private com.mycompany.sessionBeans.MeetingUsersFacade meetingUsersFacade;

    @EJB
    private com.mycompany.sessionBeans.MeetingFileFacade meetingFileFacade;

    // Instance fields
    private List<Meeting> items;
    private List<Meeting> userSpecificItems;
    private ArrayList<Date> timesForDay;
    private Meeting selected;
    private Meeting lastMeetingCreated;
    private Date selectedDate;
    private MeetingUsers mu;
    private boolean isResponding;
    private Date startTime;
    private String time;
    private Date endTime;
    private List<String> tsArray;
    private Date finalDateSelect;

    public List<Meeting> getUpcomingMeetingsAfterToday(User user) throws ParseException {
        if (user != null) {
            List<MeetingUsers> meetingusers = getMeetingUsersFacade().getUpcomingMeetings(user);
            List<Meeting> response = new ArrayList<>();
            Date today = new Date();
            for (MeetingUsers mu : meetingusers) {
                Meeting m = mu.getMeeting();
                if (m != null && m.isFinalized()) {
                    DateFormat formatter = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy");
                    try {

                        Date meetingDate = formatter.parse(m.getFinaltime());
                        if (meetingDate.after(today)) {
                            response.add(m);
                        }
                    } catch (ParseException e) {
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
        System.out.println("Final time set to: " + finalDateSelect);
        this.finalDateSelect = finalDateSelect;
    }

    public void onDateSelect(SelectEvent event) {
        setFinalDateSelect((Date) event.getObject());
        System.out.print("date selected");
        System.out.print(event.getObject().toString());
        System.out.print(finalDateSelect.toString());
        //SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");

    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public MeetingController() {
        items = null;
        userSpecificItems = null;
        timesForDay = null;
        selected = null;
        lastMeetingCreated = null;
        selectedDate = null;
        isResponding = false;
        mu = null;
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
        User user = userFacade.findByUsername((String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("username"));
        selected.setOwnerId(user);
        tsArray = new ArrayList<>();
        return selected;
    }

    public Meeting prepareView(Meeting meeting) {
        selected = meeting;
        return selected;
    }

    public void create() {
        lastMeetingCreated = selected;
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("MeetingCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
            userSpecificItems = null;
            mu = null;
            tsArray = new ArrayList<String>();
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("MeetingUpdated"));
    }

    public void destroy(User user, List<String> emails) throws Exception {
        user.prepareEmail(4, emails);
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("MeetingDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
            userSpecificItems = null;
            mu = null;
            tsArray = new ArrayList<String>();
        }
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("MeetingDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
            userSpecificItems = null;
            mu = null;
            tsArray = new ArrayList<String>();
        }
    }

    /**
     * Destroys the most recently-created meeting.
     */
    public void destroyLast() {
        System.out.println("destroying last");
        selected = getMeetingFacade().getMeetingById(getMeetingFacade().getMeetingMaxId());
        System.out.println(selected.toString());
        destroy();
    }

    public List<Meeting> getUserSpecificItems() {
        if (userSpecificItems == null) {
            //userSpecificItems = getMeetingFacade().findUserSpecificMeetings(userFacade.findByUsername((String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("username")));
            userSpecificItems = getMeetingFacade().getMeetingsByOwnerId(userFacade.findByUsername((String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("username")));
        }
        return userSpecificItems;
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
            User user = userFacade.findByUsername((String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("username"));
            try {
                if (persistAction != PersistAction.DELETE) {
                    if (tsArray != null) {
                        selected.setTimeslots(makeTimeslotsString());
                    }
                    getMeetingFacade().edit(selected);
                    int id = getMeetingFacade().getMeetingMaxId();
                    List<Integer> invitees = getInviteesId();

                    // Set up MeetingUsers objects for invitees
                    for (int i : invitees) {
                        mu = new MeetingUsers(i, id);
                        mu.setAvailableTimes("");
                        mu.setResponse(false);
                        getMeetingUsersFacade().edit(mu);
                    }

                    // Set the MeetingUsers object for the owner 
                    mu = new MeetingUsers(user.getId(), id);
                    mu.setAvailableTimes(selected.getTimeslots());
                    mu.setResponse(true);
                    getMeetingUsersFacade().edit(mu);
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
                    // For debugging constraint violations 
//                    System.out.println("FOUND CONSTRAINT VIOLATION");
//                    for (ConstraintViolation v : ((ConstraintViolationException) cause).getConstraintViolations()) {
//                        System.out.println(v.getConstraintDescriptor());
//                    }

                    JsfUtil.addErrorMessage(ex, "You have added too many meeting times. Please try again with fewer times!");
                } else {
                    JsfUtil.addErrorMessage(ex, "You have added too many meeting times. Please try again with fewer times!");
                }
            } catch (Exception ex) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
                JsfUtil.addErrorMessage(ex, "You have added too many meeting times. Please try again with fewer times!");
            }
        }
    }

    /**
     * Autocomplete finding users.
     *
     * @param query the query being searched
     * @return
     */
    public List<String> completeText(String query) {
        List<User> results = userFacade.findAll();
        List<String> usernames = new ArrayList();

        for (User user : results) {
            usernames.add(user.getUsername());
        }

        return usernames;
    }

    public List<Integer> getInviteesId() {
        String invitees = selected.getInvitees();
        invitees = invitees.replace(" ", "");
        String[] inviteeArray = invitees.split(",");

        int userId;
        List<Integer> userIds = new ArrayList<Integer>();
        for (String i : inviteeArray) {
            if (userFacade.findByUsername(i) != null) {
                userId = userFacade.findByUsername(i).getId();
                userIds.add(userId);
            }
        }
        return userIds;
    }

    public List<String> getInviteesEmails() {
        String invitees = selected.getInvitees();
        invitees = invitees.replace(" ", "");
        String[] inviteeArray = invitees.split(",");

        String userEmail;
        List<String> userEmails = new ArrayList<String>();
        for (String i : inviteeArray) {
            if (userFacade.findByUsername(i) != null) {
                userEmail = userFacade.findByUsername(i).getEmail();
                userEmails.add(userEmail);
            }
        }
        return userEmails;
    }

    public String makeTimeslotsString() {
        String timeslot = "";
        for (int i = 0; i < tsArray.size() - 1; i++) {
            timeslot += tsArray.get(i) + ", ";
        }
        if (tsArray.size() > 0) {
            timeslot += tsArray.get(tsArray.size() - 1);
        }
        return timeslot;
    }

    /**
     * Adds a new time to the meeting's list of potential times
     */
    public void updateTime() throws ParseException {
        FacesMessage resultMessage = new FacesMessage("Filler");
        if (startTime == null || time == null) {
            resultMessage = new FacesMessage("Please select a date and time first!");
            resultMessage.setSeverity(FacesMessage.SEVERITY_ERROR);
        } else if (!tsArray.contains(startTime.toString())) {
            String[] dateComponents = startTime.toString().split(" ");
            //Update the time section to be the time field input value
            dateComponents[3] = time;
            String pattern = "E MMM dd HH:mm Z yyyy";
            StringBuilder builder = new StringBuilder();

            for (String n : dateComponents) {
                builder.append(n);
                builder.append(" ");
            }
            builder.deleteCharAt(builder.length() - 1);
            SimpleDateFormat format = new SimpleDateFormat(pattern);
            try {
                System.out.println(builder.toString());
                startTime = format.parse(builder.toString());
                if (tsArray.contains(startTime.toString())) {
                    resultMessage = new FacesMessage("You have already added this time!");
                    resultMessage.setSeverity(FacesMessage.SEVERITY_ERROR);
                } else {
                    tsArray.add(startTime.toString());

                    resultMessage = new FacesMessage("Added new time.");
                }

            } catch (Exception e) {
                resultMessage = new FacesMessage("Error With Time, Please Follow Format HH:MM");
            }
        }

        FacesContext.getCurrentInstance().addMessage(null, resultMessage);
    }

    /**
     * Removes all selected times
     */
    public void clearTimes() {
        tsArray = new ArrayList<>();
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
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

    public List<String> getTsArray() {
        return tsArray;
    }

    public void setTsArray(List<String> tsArray) {
        this.tsArray = tsArray;
    }

    public Meeting getLastMeetingCreated() {
        return lastMeetingCreated;
    }

    public void setLastMeetingCreated(Meeting lastMeetingCreated) {
        this.lastMeetingCreated = lastMeetingCreated;
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
            if (u == null || u.getUser() == null || u.getUser().getId() == null) {
                break;
            }
            out += u.getAvailableTimes() + ",";
        }

        String in = out.length() > 0 ? out.substring(0, out.length() - 1) : "";
        System.out.print("FIND ME -------------------------------------------");
        System.out.print(in);

        String mmm = "";
        ArrayList<Date> d = getMeetingFacade().deserializeResponseTimes(in);

        Collections.sort(d);
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
        
        System.out.println("CHECKING NUM YES for " + date.toString());

        for (Date toCheck : d) {
            if (toCheck.toString().length() < 10 || date.toString().length() < 5 ) {
                break;
            }
            String compareString = toCheck.toString().substring(4, toCheck.toString().length() - 5);
            String newCompareString = date.toString().substring(4, date.toString().length() - 5);
            
            System.out.println("comparing '" + compareString + "' and '" + newCompareString + "'");
            
            
            if (compareString.equals(newCompareString)) {
                System.out.println("yes++");
                yes++;
            }
        }

        // Subtract one to account for the meeting owner
        return yes - 1;
    }

    public void updateFinalTime(int id, String time) {
        Meeting s = getMeetingFacade().getMeetingById(id);
        setSelected(s);
        System.out.print("This is selected: " + selected.toString());
        selected.setFinaltime(time);
        update();
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
