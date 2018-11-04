package com.pxl.teamy;

public class User {


    public String bio,dateOfBirth,gender,image,name,username;



    public User() {

    }

    public User(String bio, String dateOfBirth, String gender, String image, String name, String username) {
        this.bio = bio;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.image = image;
        this.name = name;
        this.username = username;
    }




    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }








}
