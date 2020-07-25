package com.pedsf.library.userapi.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * class for user informations
 */
@Entity(name = "User")
@Table(name = "users")
public class User implements Serializable {

   static final int FIRSTNAME_MIN = 2;
   static final int FIRSTNAME_MAX = 50;
   static final int LASTNAME_MIN = 2;
   static final int LASTNAME_MAX = 50;
   static final int PASSWORD_MIN = 4;
   static final int PASSWORD_MAX = 255;
   static final int EMAIL_MIN = 4;
   static final int EMAIL_MAX = 50;
   static final String EMAIL_REGEXP = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";
   static final String EMAIL_MESSAGE = "\"Not a valid email address !\"";
   static final int PHONE_MAX = 14;
   static final String PHONE_REGEXP = "^(?:(?:\\+|00)33[\\s.-]{0,3}(?:\\(0\\)[\\s.-]{0,3})?|0)[1-9](?:(?:[\\s.-]?\\d{2}){4}|\\d{2}(?:[\\s.-]?\\d{3}){2}|)$";
   static final String PHONE_MESSAGE = "Not a valid phone number !";
   static final int PHOTO_MAX = 255;
   static final int STATUS_MAX = 10;
   static final int STREET_MAX = 50;
   static final int ZIPCODE_MAX = 6;
   static final int CITY_MAX = 50;
   static final int COUNTRY_MAX = 50;

   @Id
   @Column(name = "id")
   @GeneratedValue(strategy =  GenerationType.IDENTITY)
   private Integer id;

   @NotNull
   @Size(min = FIRSTNAME_MIN, max = FIRSTNAME_MAX)
   @Column(name = "firstname", length = FIRSTNAME_MAX)
   private String firstName;

   @NotNull
   @Size(min = LASTNAME_MIN, max = LASTNAME_MAX)
   @Column(name = "lastname", length = LASTNAME_MAX)
   private String lastName;

   @NotNull
   @Size(min = PASSWORD_MIN, max = PASSWORD_MAX)
   @Column(name = "password", length = PASSWORD_MAX)
   private String password;

   @NotNull
   @Size(min = EMAIL_MIN, max = EMAIL_MAX)
   @Pattern(regexp = EMAIL_REGEXP, message = EMAIL_MESSAGE)
   @Column(name = "email", unique = true, length = EMAIL_MAX)
   private String email;

   @Size(max = PHONE_MAX)
   @Pattern(regexp = PHONE_REGEXP, message = PHONE_MESSAGE)
   @Column(name = "phone", length = EMAIL_MAX)
   private String phone;

   @Column(name = "photo_url", length = PHOTO_MAX)
   private String photoLink;

   @Column(name = "status", length = STATUS_MAX)
   private String status;

   @Column(name = "counter")
   private Integer counter;

   @ManyToMany(cascade=CascadeType.ALL,fetch=FetchType.EAGER)
   @JoinTable(name="users_roles",
         joinColumns = {@JoinColumn(name="user_id", referencedColumnName="id")},
         inverseJoinColumns = {@JoinColumn(name="role_id", referencedColumnName="id")}
   )
   private Set<Role> roles;

   @NotNull
   @Column(name = "street1", length = STREET_MAX)
   private String street1;

   @Column(name = "street2", length = STREET_MAX)
   private String street2;

   @Column(name = "street3", length = STREET_MAX)
   private String street3;

   @NotNull
   @Column(name = "zip_code", length = ZIPCODE_MAX)
   private String zipCode;

   @NotNull
   @Column(name = "city", length = CITY_MAX)
   private String city;

   @Column(name = "country", length = COUNTRY_MAX)
   private String country = "FRANCE";

   public User() {
   }

   public Integer getId() {
      return id;
   }

   public void setId(Integer id) {
      this.id = id;
   }

   public String getFirstName() {
      return firstName;
   }

   public void setFirstName(String firstName) {
      this.firstName = firstName;
   }

   public String getLastName() {
      return lastName;
   }

   public void setLastName(String lastName) {
      this.lastName = lastName;
   }

   public String getPassword() {
      return password;
   }

   public void setPassword(String password) {
      this.password = password;
   }

   public String getEmail() {
      return email;
   }

   public void setEmail(String email) {
      this.email = email;
   }

   public String getPhone() {
      return phone;
   }

   public void setPhone(String phone) {
      this.phone = phone;
   }

   public String getPhotoLink() {
      return photoLink;
   }

   public void setPhotoLink(String photoLink) {
      this.photoLink = photoLink;
   }

   public String getStatus() {
      return status;
   }

   public void setStatus(String status) {
      this.status = status;
   }

   public Integer getCounter() {
      return counter;
   }

   public void setCounter(Integer counter) {
      this.counter = counter;
   }

   public Set<Role> getRoles() {
      return roles;
   }

   public void setRoles(Set<Role> roles) {
      this.roles = roles;
   }

   public String getStreet1() {
      return street1;
   }

   public void setStreet1(String street1) {
      this.street1 = street1;
   }

   public String getStreet2() {
      return street2;
   }

   public void setStreet2(String street2) {
      this.street2 = street2;
   }

   public String getStreet3() {
      return street3;
   }

   public void setStreet3(String street3) {
      this.street3 = street3;
   }

   public String getZipCode() {
      return zipCode;
   }

   public void setZipCode(String zipCode) {
      this.zipCode = zipCode;
   }

   public String getCity() {
      return city;
   }

   public void setCity(String city) {
      this.city = city;
   }

   public String getCountry() {
      return country;
   }

   public void setCountry(String country) {
      this.country = country;
   }

   public void initRole(){
      this.roles = new HashSet<>();
   }

   public void addRole(Role role) {
      if(this.roles==null){
         this.roles = new HashSet<>();
      }
      this.roles.add(role);
      role.getUsers().add(this);
   }

   public void removeRole(Role role){
      if(this.roles!=null){
         this.roles.remove(role);
         role.getUsers().remove(this);
      }
   }

}
