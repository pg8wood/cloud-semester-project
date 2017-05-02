/*
 * Created by Erin Kocis on 2017.04.17  * 
 * Copyright © 2017 Erin Kocis. All rights reserved. * 
 */
package com.mycompany.entityClasses;

import com.mycompany.cdibeans.EmailSender;
import com.mycompany.managers.Constants;
import com.mycompany.sessionBeans.UserPhotoFacade;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.mail.MessagingException;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Erin
 */
@Entity
@Table(name = "User")
@XmlRootElement

@NamedQueries({
    @NamedQuery(name = "User.findAll", query = "SELECT u FROM User u")
    , @NamedQuery(name = "User.findById", query = "SELECT u FROM User u WHERE u.id = :id")
    , @NamedQuery(name = "User.findByUsername", query = "SELECT u FROM User u WHERE u.username = :username")
    , @NamedQuery(name = "User.findByPassword", query = "SELECT u FROM User u WHERE u.password = :password")
    , @NamedQuery(name = "User.findByFirstName", query = "SELECT u FROM User u WHERE u.firstName = :firstName")
    , @NamedQuery(name = "User.findByMiddleName", query = "SELECT u FROM User u WHERE u.middleName = :middleName")
    , @NamedQuery(name = "User.findByLastName", query = "SELECT u FROM User u WHERE u.lastName = :lastName")
    , @NamedQuery(name = "User.findByAddress1", query = "SELECT u FROM User u WHERE u.address1 = :address1")
    , @NamedQuery(name = "User.findByAddress2", query = "SELECT u FROM User u WHERE u.address2 = :address2")
    , @NamedQuery(name = "User.findByCity", query = "SELECT u FROM User u WHERE u.city = :city")
    , @NamedQuery(name = "User.findByState", query = "SELECT u FROM User u WHERE u.state = :state")
    , @NamedQuery(name = "User.findByZipcode", query = "SELECT u FROM User u WHERE u.zipcode = :zipcode")
    , @NamedQuery(name = "User.findBySecurityQuestion", query = "SELECT u FROM User u WHERE u.securityQuestion = :securityQuestion")
    , @NamedQuery(name = "User.findBySecurityAnswer", query = "SELECT u FROM User u WHERE u.securityAnswer = :securityAnswer")
    , @NamedQuery(name = "User.findByEmail", query = "SELECT u FROM User u WHERE u.email = :email")
})

public class User implements Serializable {

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private Collection<MeetingUsers> meetingUsersCollection;

    @JoinTable(name = "Meeting_Users", joinColumns = {
        @JoinColumn(name = "user_id", referencedColumnName = "id")}, inverseJoinColumns = {
        @JoinColumn(name = "meeting_id", referencedColumnName = "id")})
    @ManyToMany
    private Collection<Meeting> meetingCollection;
    @OneToMany(mappedBy = "ownerId")
    private Collection<Meeting> meetingCollection1;

    // User was a reserved keyword in SQL in 1999, but not any more.

    /*
    ========================================================
    Instance variables representing the attributes (columns)
    of the User table in the CloudDriveDB database.
    ========================================================
     */
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 32)
    @Column(name = "username")
    private String username;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 32)
    @Column(name = "password")
    private String password;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 32)
    @Column(name = "first_name")
    private String firstName;

    @Size(max = 32)
    @Column(name = "middle_name")
    private String middleName;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 32)
    @Column(name = "last_name")
    private String lastName;

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
    // state was a reserved keyword in SQL in 1999, but not any more.

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "zipcode")
    private String zipcode;

    @Basic(optional = false)
    @NotNull
    @Column(name = "security_question")
    private int securityQuestion;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 128)
    @Column(name = "security_answer")
    private String securityAnswer;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 128)
    @Column(name = "email")
    private String email;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "userPhoto")
    private String userPhoto;

    @OneToMany(mappedBy = "userId")
    private Collection<UserPhoto> userPhotoCollection;

    @OneToMany(mappedBy = "userId")
    private Collection<UserFile> userFileCollection;

    /*
    ===============================================================
    Class constructors for instantiating a User entity object to
    represent a row in the User table in the CloudDriveDB database.
    ===============================================================
     */
    public User() {
        this.userPhoto = "defaultUserPhoto.png";
    }

    public User(Integer id) {
        this.id = id;
        this.userPhoto = "defaultUserPhoto.png";
    }

    public User(Integer id, String username, String password, String firstName, String lastName, String address1, String city, String state, String zipcode, int securityQuestion, String securityAnswer, String email) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address1 = address1;
        this.city = city;
        this.state = state;
        this.zipcode = zipcode;
        this.securityQuestion = securityQuestion;
        this.securityAnswer = securityAnswer;
        this.email = email;
        this.userPhoto = "defaultUserPhoto.png";
    }

    /*
    ======================================================
    Getter and Setter methods for the attributes (columns)
    of the User table in the CloudDriveDB database.
    ======================================================
     */
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
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

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public int getSecurityQuestion() {
        return securityQuestion;
    }

    public void setSecurityQuestion(int securityQuestion) {
        this.securityQuestion = securityQuestion;
    }

    public String getSecurityAnswer() {
        return securityAnswer;
    }

    public void setSecurityAnswer(String securityAnswer) {
        this.securityAnswer = securityAnswer;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setUserPhoto(String extension) {
        this.userPhoto = getId() + "." + extension;
    }

    public void setDefaultUserPhoto() {
        this.userPhoto = "defaultUserPhoto.png";
    }

    public String getExtension() {
        String ext = this.userPhoto;
        ext = ext.substring(getId().toString().length() + 1);
        return ext;
    }

    /*
    User's photo image file is named as "userId.fileExtension", e.g., 5.jpg for user with id 5.
    Since the user can have only one photo, this makes sense.
     */
    public String getUserPhoto() {
        return this.userPhoto;
    }

    /*
    public void setUserPhoto(String userPhoto) {
        this.userPhoto = userPhoto;
    }
<<<<<<< HEAD

    public String getUserPhotoFilePath() {
        return Constants.PHOTOS_RELATIVE_PATH + this.userPhoto;
=======
     */
    public String getUserPhotoFilePath() {
        return Constants.PHOTOS_RELATIVE_PATH + getUserPhoto();
    }

    /*
    public void setThumbnailFileName(String thumbnailFileName) {
        this.thumbnailFileName = thumbnailFileName;
    }
     */

 /*
    The thumbnail photo image size is set to 200x200px in Constants.java as follows:
    public static final Integer THUMBNAIL_SIZE = 200;
    
    If the user uploads a large photo file, we will scale it down to THUMBNAIL_SIZE
    and use it so that the size is reasonable for performance reasons.
    
    The photo image scaling is properly done by using the imgscalr-lib-4.2.jar file.
    
    The thumbnail file is named as "userId_thumbnail.fileExtension", 
    e.g., 5_thumbnail.jpg for user with id 5.
     */
    public String getThumbnailFileName() {
        //getExtension()
        return "thumbnail_" + this.userPhoto;
    }

    public String getRelativeThumbnailFilePath() {
        return Constants.PHOTOS_RELATIVE_PATH + getThumbnailFileName();
    }

    public String getPhotoFilePath() {
        //getPhotoFilename
        return Constants.PHOTOS_ABSOLUTE_PATH + getUserPhoto();
    }

    public String getAbsoluteThumbnailFilePath() {
        return Constants.PHOTOS_ABSOLUTE_PATH + getThumbnailFileName();
    }

    public String getTemporaryFilePath() {
        return Constants.PHOTOS_ABSOLUTE_PATH + "tmp_file";
    }

    // The @XmlTransient annotation is used to resolve potential name collisions
    // between a JavaBean property name and a field name.
    @XmlTransient
    public Collection<UserPhoto> getUserPhotoCollection() {
        return userPhotoCollection;
    }

    public void setUserPhotoCollection(Collection<UserPhoto> userPhotoCollection) {
        this.userPhotoCollection = userPhotoCollection;
    }

    @XmlTransient
    public Collection<UserFile> getUserFileCollection() {
        return userFileCollection;
    }

    public void setUserFileCollection(Collection<UserFile> userFileCollection) {
        this.userFileCollection = userFileCollection;
    }

    /*
    ================
    Instance Methods
    ================
     */
    /**
     * @return Generates and returns a hash code value for the object with id
     */
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    /**
     * Checks if the User object identified by 'object' is the same as the User
     * object identified by 'id'
     *
     * @param object The User object identified by 'object'
     * @return True if the User 'object' and 'id' are the same; otherwise,
     * return False
     */
    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof User)) {
            return false;
        }
        User other = (User) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    public boolean equalID(User u) {
        System.out.print("checking");
        if (id == u.id) {
            System.out.print("true");
        }
        return (id == u.id);
    }

    /**
     * @return the String representation of a User id
     */
    @Override
    public String toString() {

        // Returned String is the one shown in the p:dataTable under the User Id column in UserFiles.xhtml.
        return id.toString();
    }

    /**
     * Sets the necessary properties to send the email
     *
     * @param option
     * @param emails
     * @throws java.lang.Exception
     */
    public void prepareEmail(int option, List<String> emails) throws Exception {
        for (String email : emails) {
            EmailSender emailSender = new EmailSender();
            emailSender.setEmailTo(email);
            emailSender.setEmailSubject(prepareEmailSubject(option));
            emailSender.setEmailBody(prepareEmailBody(option));
            // Send the email on another thread so the user doesn't have 
            // to wait for execution to complete
            ThreadPoolExecutor executor = new ThreadPoolExecutor(10, 10, 1, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        emailSender.sendEmail();
                    } catch (MessagingException ex) {
                        Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            });

        }

    }

    /**
     * Sets the necessary properties to send the email
     *
     * @param option
     * @param email
     * @throws java.lang.Exception
     */
    public void prepareOwnerEmail(int option, String email) throws Exception {
        EmailSender emailSender = new EmailSender();
        emailSender.setEmailTo(email);
        emailSender.setEmailSubject(prepareEmailSubject(option));
        emailSender.setEmailBody(prepareEmailBody(option));

        // Send the email on another thread so the user doesn't have 
        // to wait for execution to complete
        ThreadPoolExecutor executor = new ThreadPoolExecutor(10, 10, 1, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    emailSender.sendEmail();
                } catch (MessagingException ex) {
                    Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    /**
     * Prepares the email title
     *
     * @param option specifies the context of the email
     * @return emailTitle
     */
    public String prepareEmailSubject(int option) {
        String emailSubject = "";

        switch (option) {
            case 0:
                //sends to list of invitees
                emailSubject = "New Meeting Invite!";
                break;
            case 1:
                //sends to owner
                emailSubject = "New Meeting Response!";
                break;
            case 2:
                //sends to list of invitees
                emailSubject = "Upcoming Meeting Updated!";
                break;
            case 3:
                //sends to list of invitees
                emailSubject = "Upcoming Meeting Has Been Finalized!!";
                break;
            case 4:
                //sends to list of invitees
                emailSubject = "Meeting Cancelled!";
                break;
            default:
                break;
        }
        return emailSubject;
    }

    /**
     * Composes the initial content of the Email message.
     *
     * @param option specifies the context of the email
     * @return emailBodyText
     */
    public String prepareEmailBody(int option) {

        // Compose the email content in HTML format
        String emailBodyText = "";

        // The image for the email
        String imageUrl = Constants.EMAIL_IMAGE;

        switch (option) {
            //email participant when invited to a meeting 
            case 0:
                // To enter a double quote " in a string literal, use the backslash \ escape character as \"
                emailBodyText = "<div align=\"center\">" + imageUrl + "<br /><br /><b>You Have Been Invited To A Meeting!</b><br /><br />"
                        + "<a href=" + Constants.MYMEETINGS_URL + ">Click here</a> to view your current meeting invitations!</div>";
                break;
            //email owner when participants respond
            case 1:
                emailBodyText = "<div align=\"center\">" + imageUrl + "<br /><br /><b>Participants Have Responded To Your Meeting!</b><br /><br />"
                        + "<a href=" + Constants.MYMEETINGS_URL + ">Click here</a> to view the status of your current meetings!</div>";
                break;
            //email participants if owner updates meeting
            case 2:
                emailBodyText = "<div align=\"center\">" + imageUrl + "<br /><br /><b>One of Your Upcoming Meetings Has Been Updated!</b><br /><br />"
                        + "<a href=" + Constants.MYMEETINGS_URL + ">Click here</a> to view the status of your upcoming meetings!</div>";
                break;
            //email participants when owner selects final meeting time
            case 3:
                emailBodyText = "<div align=\"center\">" + imageUrl + "<br /><br /><b>An Upcoming Meeting Time Has Been Finalized!</b><br /><br />"
                        + "<a href=" + Constants.MYMEETINGS_URL + ">Click here</a> to view your upcoming meetings!</div>";
                break;
            //email participants when owner selects final meeting time
            case 4:
                emailBodyText = "<div align=\"center\">" + imageUrl + "<br /><br /><b>One of Your Upcoming Has Been Cancelled!</b><br /><br />"
                        + "<a href=" + Constants.MYMEETINGS_URL + ">Click here</a> to view the status of your upcoming meetings!</div>";
                break;
            default:
                break;
        }

        // Set the HTML content to be the body of the email message
        //editorView.setText(emailBodyText);
        // Redirect to show the SendMail.xhtml page
        //return "SendEmail.xhtml?faces-redirect=true";
        return emailBodyText;
    }

    @XmlTransient
    public Collection<Meeting> getMeetingCollection() {
        return meetingCollection;
    }

    public void setMeetingCollection(Collection<Meeting> meetingCollection) {
        this.meetingCollection = meetingCollection;
    }

    @XmlTransient
    public Collection<Meeting> getMeetingCollection1() {
        return meetingCollection1;
    }

    public void setMeetingCollection1(Collection<Meeting> meetingCollection1) {
        this.meetingCollection1 = meetingCollection1;
    }

    @XmlTransient
    public Collection<MeetingUsers> getMeetingUsersCollection() {
        return meetingUsersCollection;
    }

    public void setMeetingUsersCollection(Collection<MeetingUsers> meetingUsersCollection) {
        this.meetingUsersCollection = meetingUsersCollection;
    }

}
