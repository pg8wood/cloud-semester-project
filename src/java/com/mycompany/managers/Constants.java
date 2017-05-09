/*
 * Created by Patrick Gatewood on 2017.04.20  * 
 * Copyright © 2017 Patrick Gatewood. All rights reserved. * 
 */
package com.mycompany.managers;

/**
 *
 * @author Patrick Gatewood
 */
public final class Constants {

    /* =========== Our Design Decision ===========
        We decided to use directories external to our application 
        for the storage and retrieval of user's files.
    
        We do not want to use a database for the following reasons: 
            (a) Database storage and retrieval of large files as 
                BLOB (binary large object) degrades performance.
            (b) BLOBs increase the database complexity.
    
        Therefore, we use the following two external directories 
        for the storage and retrieval of user's files.
    
     */
    public static final String FILES_ABSOLUTE_PATH = "/Users/PatrickGatewood/Documents/cloud/CloudStorage/FileStorageLocation-Team4/FileStorage";
    public static final String PHOTOS_ABSOLUTE_PATH = "/Users/PatrickGatewood/Documents/cloud/CloudStorage/FileStorageLocation-Team4/UserPhotoStorage/";

    /*
    URL to the sign in page
     */
    public static final String MYMEETINGS_URL = "http://localhost:8080/group/MyMeetings.xhtml";
    /*
    URL to the image in the email
     */
    public static final String EMAIL_IMAGE = "<img src=\"http://venus.cs.vt.edu/group/resources/images/schedule_logo.jpg\" style=\"width:230px;height:64px;\">";
    public static final String FILES_RELATIVE_PATH = "FileStorageLocation-Team4/FileStorage/";
    public static final String PHOTOS_RELATIVE_PATH = "FileStorageLocation-Team4/FileStorage/UserPhotoStorage/";
    public static final String DEFAULT_PHOTO_RELATIVE_PATH = "/Users/PatrickGatewood/Documents/cloud/CloudStorage/FileStorageLocation-Team4/FileStorage/UserPhotoStorage/defaultUserPhoto.png";


    /* Temporary filename */
    public static final String TEMP_FILE = "tmp_file";

    /* =========== Our Design Decision ===========
        We decided to scale down the user's uploaded photo to 200x200 px,
        which we call the Thumbnail photo, and use it.
    
        We do not want to use the uploaded photo as is, which may be
        very large in size degrading performance.
     */
    public static final Integer THUMBNAIL_SIZE = 200;

    /* United States postal state abbreviations */
    public static final String[] STATES = {"AK", "AL", "AR", "AZ", "CA", "CO", "CT",
        "DC", "DE", "FL", "GA", "GU", "HI", "IA", "ID", "IL", "IN", "KS", "KY", "LA", "MA",
        "MD", "ME", "MH", "MI", "MN", "MO", "MS", "MT", "NC", "ND", "NE", "NH", "NJ", "NM",
        "NV", "NY", "OH", "OK", "OR", "PA", "PR", "PW", "RI", "SC", "SD", "TN", "TX", "UT",
        "VA", "VI", "VT", "WA", "WI", "WV", "WY"};

    /* Security questions to reset password  */
    public static final String[] QUESTIONS = {
        "In what city were you born?",
        "What is your mother's maiden name?",
        "What elementary school did you attend?",
        "What was the make of your first car?",
        "What is your father's middle name?",
        "What is the name of your most favorite pet?",
        "What street did you grow up on?"
    };

}
