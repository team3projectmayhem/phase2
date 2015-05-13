package com.example.dad.nsatracker;

/**
 * User class.
 */
public class User {

    private int UUID;
    private String email;
//    private String password;
//    private String secretQ;
//    private String SecretA;

    public User(final int UUID, final String email ) {
        //this.password = password;
        this.UUID = UUID;
        this.email = email;
//        this.secretQ = secretQ;
//        SecretA = secretA;
    }

    public int getUUID() {
        return UUID;
    }

    public String getEmail() {
        return email;
    }

//    public String getPassword() {
//        return password;
//    }

//    public String getSecretQ() {
//        return secretQ;
//    }
//
//    public String getSecretA() {
//        return SecretA;
//    }

    public void save() {

    }

    public void clear() {

    }
}
