package com.pedsf.library.dto.global;

import com.pedsf.library.Parameters;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class UserDTO implements Serializable {
    private static final String USER_COUNTRY = "FRANCE";
    private static final Integer USER_COUNTER = 0;

    private Integer id;

    @Size(min = Parameters.FIRSTNAME_MIN, max = Parameters.FIRSTNAME_MAX, message = Parameters.ERROR_FORMAT_BETWEEN + Parameters.FIRSTNAME_MIN + " AND " + Parameters.FIRSTNAME_MAX + " !")
    private String firstName;

    @Size(min = Parameters.LASTNAME_MIN, max = Parameters.LASTNAME_MAX, message = Parameters.ERROR_FORMAT_BETWEEN + Parameters.LASTNAME_MIN + " AND " + Parameters.LASTNAME_MAX + " !")
    private String lastName;

    @Size(min = Parameters.PASSWORD_MIN, max = Parameters.PASSWORD_MAX, message = Parameters.ERROR_FORMAT_BETWEEN + Parameters.PASSWORD_MIN + " AND " + Parameters.PASSWORD_MAX + " !")
    private String password;

    @Size(min = Parameters.PASSWORD_MIN, max = Parameters.PASSWORD_MAX, message = Parameters.ERROR_FORMAT_BETWEEN + Parameters.PASSWORD_MIN + " AND " + Parameters.PASSWORD_MAX + " !")
    private String matchingPassword;

    @Size(min = Parameters.EMAIL_MIN, max = Parameters.EMAIL_MAX, message = Parameters.ERROR_FORMAT_BETWEEN + Parameters.EMAIL_MIN + " AND " + Parameters.EMAIL_MAX + " !")
    @Pattern(regexp = Parameters.EMAIL_REGEXP, message = Parameters.ERROR_EMAIL_FORMAT)
    private String email;

    @Size(max = Parameters.PHONE_MAX, message = Parameters.ERROR_FORMAT_LESS + Parameters.PHONE_MAX)
    @Pattern(regexp = Parameters.PHONE_REGEXP, message = Parameters.ERROR_PHONE_FORMAT)
    private String phone;

    @Size(max = Parameters.PHOTO_MAX, message = Parameters.ERROR_FORMAT_LESS + Parameters.PHOTO_MAX)
    private String photoLink;

    @Size(max = Parameters.STATUS_MAX, message = Parameters.ERROR_FORMAT_LESS + Parameters.STATUS_MAX)
    private String status;
    private Integer counter;
    private List<String> roles;

    @Size(max = Parameters.STREET_MAX, message = Parameters.ERROR_FORMAT_LESS + Parameters.STREET_MAX + " !")
    private String street1;

    @Size(max = Parameters.STREET_MAX, message = Parameters.ERROR_FORMAT_LESS + Parameters.STREET_MAX + " !")
    private String street2;

    @Size(max = Parameters.STREET_MAX, message = Parameters.ERROR_FORMAT_LESS + Parameters.STREET_MAX + " !")
    private String street3;

    @Size(min = Parameters.ZIP_MIN, max = Parameters.ZIP_MAX, message = Parameters.ERROR_FORMAT_BETWEEN + Parameters.ZIP_MIN + " AND " + Parameters.ZIP_MAX + " !")
    private String zipCode;

    @Size(max = Parameters.CITY_MAX, message = Parameters.ERROR_FORMAT_LESS + Parameters.CITY_MAX + " !")
    private String city;

    @Size(max = Parameters.COUNTRY_MAX, message = Parameters.ERROR_FORMAT_LESS + Parameters.COUNTRY_MAX + " !")
    private String country;

    public UserDTO(Integer id,
                @NotNull @Size(min = Parameters.FIRSTNAME_MIN, max = Parameters.FIRSTNAME_MAX, message = Parameters.ERROR_FORMAT_BETWEEN + Parameters.FIRSTNAME_MIN + " AND " + Parameters.FIRSTNAME_MAX + " !") String firstName,
                @NotNull @Size(min = Parameters.LASTNAME_MIN, max = Parameters.LASTNAME_MAX, message = Parameters.ERROR_FORMAT_BETWEEN + Parameters.LASTNAME_MIN + " AND " + Parameters.LASTNAME_MAX + " !") String lastName,
                @NotNull @Size(min = Parameters.EMAIL_MIN, max = Parameters.EMAIL_MAX, message = Parameters.ERROR_FORMAT_BETWEEN + Parameters.EMAIL_MIN + " AND " + Parameters.EMAIL_MAX + " !") @Pattern(regexp = Parameters.EMAIL_REGEXP, message = Parameters.ERROR_EMAIL_FORMAT) String email,
                @NotNull @Size(min = Parameters.PASSWORD_MIN, max = Parameters.PASSWORD_MAX, message = Parameters.ERROR_FORMAT_BETWEEN + Parameters.PASSWORD_MIN + " AND " + Parameters.PASSWORD_MAX + " !") String password,
                @NotNull @Size(max = Parameters.STREET_MAX, message = Parameters.ERROR_FORMAT_LESS + Parameters.STREET_MAX + " !") String street1,
                @NotNull @Size(max = Parameters.ZIP_MAX, message = Parameters.ERROR_FORMAT_LESS + Parameters.ZIP_MAX + " !") String zipCode,
                @NotNull @Size(max = Parameters.CITY_MAX, message = Parameters.ERROR_FORMAT_LESS + Parameters.CITY_MAX + " !") String city) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.street1 = street1;
        this.street2 = "";
        this.street3 = "";
        this.zipCode = zipCode;
        this.city = city;
        this.country = USER_COUNTRY;
        this.counter = USER_COUNTER;
    }

    public UserDTO() {
        // nothing to do
    }

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
