/*
 * Created by  Meeting Scheduler on 2017.01.01
 * Copyright © 2017  Meeting Scheduler. All rights reserved.
 */
package com.mycompany.cdibeans;

import java.io.Serializable;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;

/**
 *
 * @author Meeting Scheduler
 */

/* 
The @Named class annotation designates the bean object created in this class 
as a Contexts and Dependency Injection (CDI) named bean. The value attribute 
defines the logical name of the bean that can be used in JSF facelets pages. 
 */
@Named(value = "person")

/* 
The @SessionScoped annotation indicates that this CDI-managed bean will be
maintained (i.e., its property values will be kept) across multiple HTTP requests 
as long as the user's established HTTP session is active.
 */
@SessionScoped

public class Person implements Serializable {

    /*
    ==================
    Instance Variables
    ==================
     */
    // The values of these instance variables are supplied by the user 
    private Double heightInFeet;    // Person's height in Feet
    private Double heightInInches;  // Person's height in Inches
    private Double weightInPounds;  // Person's weight in Pounds

    // The values of these instance variables are computed in the calculateBMI method
    private Double bmiValue;
    private String bmiCategory;

    /*
    Using the @Inject annotation, the compiler is directed to store the object reference
    of the EditorView CDI-named bean into the instance variable editorView at runtime.
    With this injection, the instance variables and instance methods of the EditorView
    class can be accessed in this CDI-named bean.
     */
    @Inject
    private EditorView editorView;

    // Create a new instance of Person
    public Person() {
    }

    /*
    =========================
    Getter and Setter Methods
    =========================
     */
    public Double getHeightInFeet() {
        return heightInFeet;
    }

    public void setHeightInFeet(Double heightInFeet) {
        this.heightInFeet = heightInFeet;
    }

    public Double getHeightInInches() {
        return heightInInches;
    }

    public void setHeightInInches(Double heightInInches) {
        this.heightInInches = heightInInches;
    }

    public Double getWeightInPounds() {
        return weightInPounds;
    }

    public void setWeightInPounds(Double weightInPounds) {
        this.weightInPounds = weightInPounds;
    }

    public Double getBmiValue() {
        return bmiValue;
    }

    public void setBmiValue(Double bmiValue) {
        this.bmiValue = bmiValue;
    }

    public String getBmiCategory() {
        return bmiCategory;
    }

    public void setBmiCategory(String bmiCategory) {
        this.bmiCategory = bmiCategory;
    }

    /*
    ================
    Instance Methods
    ================
     */
    /**
     * Compute bmiValue and bmiCategory
     *
     * @return Display the Results.xhtml page
     */
    public String calculateBMI() {

        //below is in compliance with OOP (communicating to message passer)
        //advantage of this is...maintainability and reusability 
        //double totalHeightInInches = getHeightInInches() + (12.0 * getHeightInFeet());
        //he chose direct access to make the code readable 
        double totalHeightInInches = heightInInches + (12.0 * heightInFeet);
        double bmi = (weightInPounds * 703.0) / (totalHeightInInches * totalHeightInInches);

        /* 
        Round the calculated BMI value to 2 decimal places.
        100d --> 2 decimal places; 1000d --> 3 decimal places; 10000d --> 4 decimal places; etc.
        */
        bmiValue = (double) Math.round(bmi * 100d) / 100d;

        double calculatedBMI = bmiValue;

        if (calculatedBMI < 18.5) {
            bmiCategory = "Underweight";
        } else if (calculatedBMI <= 24.9) {
            bmiCategory = "Normal Weight";
        } else if (calculatedBMI < 30) {
            bmiCategory = "Overweight";
        } else if (calculatedBMI >= 30) {
            bmiCategory = "Obese";
        }

        // Redirect to show the Results.xhtml page
        return "Results.xhtml?faces-redirect=true";
    }

    /**
     * Set user-entered values to null
     *
     * @return Display the CalculateBMI.xhtml page
     */
    public String clear() {

        heightInFeet = null;
        heightInInches = null;
        weightInPounds = null;

        // Redirect to show the CalculateBMI.xhtml page
        return "CalculateBMI.xhtml?faces-redirect=true";
    }

    /**
     * Composes the initial content of the Email message.
     *
     * @return SendMail.xhtml
     */
    public String prepareEmailBody() {

        String imageUrl = "";

        switch (bmiCategory) {
            case "Underweight":
                // To enter a double quote " in a string literal, use the backslash \ escape character as \"
                imageUrl = "<img src=\"http://manta.cs.vt.edu/csd/underweight.png\" style=\"width:200px;height:200px;\">";
                break;
            case "Normal Weight":
                imageUrl = "<img src=\"http://manta.cs.vt.edu/csd/normalWeight.png\" style=\"width:200px;height:200px;\">";
                break;
            case "Overweight":
                imageUrl = "<img src=\"http://manta.cs.vt.edu/csd/overweight.png\" style=\"width:200px;height:200px;\">";
                break;
            case "Obese":
                imageUrl = "<img src=\"http://manta.cs.vt.edu/csd/obese.png\" style=\"width:200px;height:200px;\">";
                break;
            default:
                break;
        }

        // Compose the email content in HTML format
        String emailBodyText = "<div align=\"center\">" + imageUrl + "<br /><br /><b>Body Mass Index (BMI)</b><br /><br />Computed BMI value is "
                + bmiValue + "<br /><br />The person is " + bmiCategory + "!<p>&nbsp;</p></div>";

        // Set the HTML content to be the body of the email message
        editorView.setText(emailBodyText);

        // Redirect to show the SendMail.xhtml page
        return "SendEmail.xhtml?faces-redirect=true";
    }

}
