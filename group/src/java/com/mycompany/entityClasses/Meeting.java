/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.entityClasses;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@Entity
@Table(name = "Meeting")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Meeting.findAll", query = "SELECT m FROM Meeting m")
    , @NamedQuery(name = "Meeting.findById", query = "SELECT m FROM Meeting m WHERE m.id = :id")
    , @NamedQuery(name = "Meeting.findByAddress1", query = "SELECT m FROM Meeting m WHERE m.address1 = :address1")
    , @NamedQuery(name = "Meeting.findByAddress2", query = "SELECT m FROM Meeting m WHERE m.address2 = :address2")
    , @NamedQuery(name = "Meeting.findByCity", query = "SELECT m FROM Meeting m WHERE m.city = :city")
    , @NamedQuery(name = "Meeting.findByState", query = "SELECT m FROM Meeting m WHERE m.state = :state")
    , @NamedQuery(name = "Meeting.findByZipcode", query = "SELECT m FROM Meeting m WHERE m.zipcode = :zipcode")
    , @NamedQuery(name = "Meeting.findByTopic", query = "SELECT m FROM Meeting m WHERE m.topic = :topic")
    , @NamedQuery(name = "Meeting.findByDescription", query = "SELECT m FROM Meeting m WHERE m.description = :description")

})
public class Meeting implements Serializable {

    @Size(max = 256)
    @Column(name = "timeslots")
    private String timeslots;
    
    @Size(max = 256)
    @Column(name = "finaltime")
    private String finaltime;

    

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "meeting")
    private List<MeetingUsers> meetingUsersList;

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 128)
    @Column(name = "address1")
    private String address1;
    @Size(max = 128)
    @Column(name = "address2")
    private String address2;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 64)
    @Column(name = "city")
    private String city;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2)
    @Column(name = "state")
    private String state; 
    @Basic(optional = false)
    @NotNull
    @Column(name = "zipcode")
    private short zipcode;
    @Size(max = 64)
    @Column(name = "topic")
    private String topic;
    @Size(max = 256)
    @Column(name = "description")
    private String description;
    @ManyToMany(mappedBy = "meetingCollection")
    private Collection<User> userCollection;
    @JoinColumn(name = "owner_id", referencedColumnName = "id")
    @ManyToOne
    private User ownerId;

//    // Instance fields
//    private HashMap<Calendar, List<String>> timesByDateMap;

    public Meeting() {
//        timesByDateMap = new HashMap<>();
    }

    public Meeting(Integer id) {
        this.id = id;
    }

    public Meeting(Integer id, String address1, String city, String state, short zipcode) {
        this.id = id;
        this.address1 = address1;
        this.city = city;
        this.state = state;
        this.zipcode = zipcode;
//        this.timesByDateMap = null;
    }

    // ----------------
    // Instance methods 
    // ----------------
    
    /**
     * Gets the meeting's timeslots and de-serializes them
     */
//    public void deserializeTimeslots() {
//        timesByDateMap = new HashMap<>();
//        Scanner dateScanner = new Scanner(getTimeslots());
//        dateScanner.useDelimiter(";");
//
//        // Iterate over timeslot String getting dates
//        while (dateScanner.hasNext()) {
//            List<String> timesList = new ArrayList();
//            Scanner timeScanner = new Scanner(dateScanner.next());
//            timeScanner.useDelimiter(",");
//
//            // Create a Calendar object to serve as the HashMap Key
//            String dateString = timeScanner.next();
//            Scanner dateScan = new Scanner(dateString);
//            dateScan.useDelimiter("-");
//            Calendar calendarDate = Calendar.getInstance();
//            
//            // dateString format is YYYY-MM-DD
//            calendarDate.set(dateScan.nextInt(), dateScan.nextInt(), dateScan.nextInt());
//            
//            // Store the times
//            while (timeScanner.hasNext()) {
//                timesList.add(timeScanner.next());
//            }
//
//            // Map the calendarDate to the list of times
//            timesByDateMap.put(calendarDate, timesList);
//        }
//    }
    
//    /**
//     * Gets a Calendar object's string representation in the format YYYY-MM-DD
//     * 
//     * @param calendarDate the Calendar object to interpret
//     * @return String the string representation of the Calendar object
//     */
//    public String getCalendarString(Calendar calendarDate) {
//        StringBuilder sb = new StringBuilder();
//        sb.append(Integer.toString(calendarDate.YEAR));
//        sb.append("-");
//        sb.append(Integer.toString(calendarDate.MONTH));
//        sb.append("-");
//        sb.append(Integer.toString(calendarDate.DAY_OF_MONTH));
//        
//        return sb.toString();
//    }

    // -------------------
    // Setters and getters
    // -------------------
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public short getZipcode() {
        return zipcode;
    }

    public void setZipcode(short zipcode) {
        this.zipcode = zipcode;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @XmlTransient
    public Collection<User> getUserCollection() {
        return userCollection;
    }

    public void setUserCollection(Collection<User> userCollection) {
        this.userCollection = userCollection;
    }

    public User getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(User ownerId) {
        this.ownerId = ownerId;
    }
    
    public int getRealOwnerId(){
        return ownerId.getId();
    }
    
    public boolean isOwner(int id){
        return ownerId.getId() == id;
    }
    
    public String getFinaltime() {
        return finaltime;
    }

    public void setFinaltime(String finaltime) {
        this.finaltime = finaltime;
    }

//    public HashMap<Calendar, List<String>> getTimesByDateMap() {
//        if (timesByDateMap == null) {
//            deserializeTimeslots();
//        }
//        
//        return timesByDateMap;
//    }
//
//    public void setTimesByDateMap(HashMap<Calendar, List<String>> timesByDateMap) {
//        this.timesByDateMap = timesByDateMap;
//    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Meeting)) {
            return false;
        }
        Meeting other = (Meeting) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.entityClasses.Meeting[ id=" + id + " ]";
    }

    @XmlTransient
    public List<MeetingUsers> getMeetingUsersList() {
        return meetingUsersList;
    }

    public void setMeetingUsersList(List<MeetingUsers> meetingUsersList) {
        this.meetingUsersList = meetingUsersList;
    }

    public String getTimeslots() {
        return timeslots;
    }

    public void setTimeslots(String timeslots) {
        this.timeslots = timeslots;
    }

}
