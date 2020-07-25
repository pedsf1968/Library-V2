package com.pedsf.library.userapi.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class UserDTO implements Serializable {
    static final int FIRSTNAME_MIN = 2;
    static final int FIRSTNAME_MAX = 50;
    static final int LASTNAME_MIN = 2;
    static final int LASTNAME_MAX = 50;
    static final int PASSWORD_MIN = 4;
    static final int PASSWORD_MAX = 255;
    static final int EMAIL_MIN = 4;
    static final int EMAIL_MAX = 255;
    static final String EMAIL_REGEXP = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";
    static final String EMAIL_MESSAGE = "\"Not a valid email address !\"";
    static final int PHONE_MAX = 14;
    static final String PHONE_REGEXP = "^(?:(?:\\+|00)33[\\s.-]{0,3}(?:\\(0\\)[\\s.-]{0,3})?|0)[1-9](?:(?:[\\s.-]?\\d{2}){4}|\\d{2}(?:[\\s.-]?\\d{3}){2}|)$";
    static final String PHONE_MESSAGE = "Not a valid phone number !";
    static final int PHOTO_MAX = 255;
    private static final int STREET_MAX = 50;
    private static final int ZIP_MIN = 5;
    private static final int ZIP_MAX = 6;
    private static final int CITY_MAX = 50;
    private static final int COUNTRY_MAX = 50;
    static final String ERROR_MESSAGE_BETWEEN = "Length should be between : ";
    static final String ERROR_MESSAGE_LESS = "Length should less than : ";


    private Integer id;

    @NotNull
    @Size(min = FIRSTNAME_MIN, max = FIRSTNAME_MAX, message = ERROR_MESSAGE_BETWEEN + FIRSTNAME_MIN + " AND " + FIRSTNAME_MAX + " !")
    private String firstName;

    @NotNull
    @Size(min = LASTNAME_MIN, max = LASTNAME_MAX, message = ERROR_MESSAGE_BETWEEN + LASTNAME_MIN + " AND " + LASTNAME_MAX + " !")
    private String lastName;

    @NotNull
    @Size(min = PASSWORD_MIN, max = PASSWORD_MAX, message = ERROR_MESSAGE_BETWEEN + PASSWORD_MIN + " AND " + PASSWORD_MAX + " !")
    private String password;

    @NotNull
    @Size(min = PASSWORD_MIN, max = PASSWORD_MAX, message = ERROR_MESSAGE_BETWEEN + PASSWORD_MIN + " AND " + PASSWORD_MAX + " !")
    private String matchingPassword;

    @NotNull
    @Size(min = EMAIL_MIN, max = EMAIL_MAX, message = ERROR_MESSAGE_BETWEEN + EMAIL_MIN + " AND " + EMAIL_MAX + " !")
    @Pattern(regexp = EMAIL_REGEXP, message = EMAIL_MESSAGE)
    private String email;

    @Size(max = PHONE_MAX)
    @Pattern(regexp = PHONE_REGEXP, message = PHONE_MESSAGE)
    private String phone;

    @Size(max = PHOTO_MAX)
    private String photoLink;

    private String status;
    private Integer counter;
    private List<String> roles;

    @NotNull
    @Size(max = STREET_MAX, message = ERROR_MESSAGE_LESS + STREET_MAX + " !")
    private String street1;

    @Size(max = STREET_MAX, message = ERROR_MESSAGE_LESS + STREET_MAX + " !")
    private String street2;

    @Size(max = STREET_MAX, message = ERROR_MESSAGE_LESS + STREET_MAX + " !")
    private String street3;

    @NotNull
    @Size(min = ZIP_MIN, max = ZIP_MAX, message = ERROR_MESSAGE_BETWEEN + ZIP_MIN + " AND " + ZIP_MAX + " !")
    private String zipCode;

    @NotNull
    @Size(max = CITY_MAX, message = ERROR_MESSAGE_LESS + CITY_MAX + " !")
    private String city;

    @Size(max = COUNTRY_MAX, message = ERROR_MESSAGE_LESS + COUNTRY_MAX + " !")
    private String country;

    public void initRole(){
        this.roles = new ArrayList<>();
    }

    public void addRole(String role){
        if(this.roles==null){
            this.roles = new ArrayList<>();
        }

        this.roles.add(role);
    }

    public void removeRole(String role){
        this.roles.remove(role);
    }
}
