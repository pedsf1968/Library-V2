package com.pedsf.library.userapi.model;

import com.pedsf.library.Parameters;

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

   @Id
   @Column(name = "id")
   @GeneratedValue(strategy =  GenerationType.IDENTITY)
   private Integer id;

   @NotNull
   @Size(min = Parameters.FIRSTNAME_MIN, max = Parameters.FIRSTNAME_MAX, message = Parameters.ERROR_FORMAT_BETWEEN + Parameters.FIRSTNAME_MIN + " AND " + Parameters.FIRSTNAME_MAX + " !")
   @Column(name = "firstname", length = Parameters.FIRSTNAME_MAX)
   private String firstName;

   @NotNull
   @Size(min = Parameters.LASTNAME_MIN, max = Parameters.LASTNAME_MAX, message = Parameters.ERROR_FORMAT_BETWEEN + Parameters.LASTNAME_MIN + " AND " + Parameters.LASTNAME_MAX + " !")
   @Column(name = "lastname", length = Parameters.LASTNAME_MAX)
   private String lastName;

   @NotNull
   @Size(min = Parameters.PASSWORD_MIN, max = Parameters.PASSWORD_MAX, message = Parameters.ERROR_FORMAT_BETWEEN + Parameters.PASSWORD_MIN + " AND " + Parameters.PASSWORD_MAX + " !")
   @Column(name = "password", length = Parameters.PASSWORD_MAX)
   private String password;

   @NotNull
   @Size(min = Parameters.EMAIL_MIN, max = Parameters.EMAIL_MAX, message = Parameters.ERROR_FORMAT_BETWEEN + Parameters.EMAIL_MIN + " AND " + Parameters.EMAIL_MAX + " !")
   @Pattern(regexp = Parameters.EMAIL_REGEXP, message = Parameters.ERROR_EMAIL_FORMAT)
   @Column(name = "email", unique = true, length = Parameters.EMAIL_MAX)
   private String email;

   @Size(max = Parameters.PHONE_MAX, message = Parameters.ERROR_FORMAT_LESS + Parameters.PHONE_MAX)
   @Pattern(regexp = Parameters.PHONE_REGEXP, message = Parameters.ERROR_PHONE_FORMAT)
   @Column(name = "phone", length = Parameters.EMAIL_MAX)
   private String phone;

   @Size(max = Parameters.PHOTO_MAX, message = Parameters.ERROR_FORMAT_LESS + Parameters.PHOTO_MAX)
   private String photoLink;

   @Size(max = Parameters.STATUS_MAX, message = Parameters.ERROR_FORMAT_LESS + Parameters.STATUS_MAX)
   @Column(name = "status", length = Parameters.STATUS_MAX)
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
   @Size(max = Parameters.STREET_MAX, message = Parameters.ERROR_FORMAT_LESS + Parameters.STREET_MAX + " !")
   @Column(name = "street1", length = Parameters.STREET_MAX)
   private String street1;

   @Size(max = Parameters.STREET_MAX, message = Parameters.ERROR_FORMAT_LESS + Parameters.STREET_MAX + " !")
   @Column(name = "street2", length = Parameters.STREET_MAX)
   private String street2;

   @Size(max = Parameters.STREET_MAX, message = Parameters.ERROR_FORMAT_LESS + Parameters.STREET_MAX + " !")
   @Column(name = "street3", length = Parameters.STREET_MAX)
   private String street3;

   @NotNull
   @Size(max = Parameters.ZIP_MAX, message = Parameters.ERROR_FORMAT_LESS + Parameters.ZIP_MAX + " !")
   @Column(name = "zip_code", length = Parameters.ZIP_MAX)
   private String zipCode;

   @NotNull
   @Size(max = Parameters.CITY_MAX, message = Parameters.ERROR_FORMAT_LESS + Parameters.CITY_MAX + " !")
   @Column(name = "city", length = Parameters.CITY_MAX)
   private String city;

   @Size(max = Parameters.COUNTRY_MAX, message = Parameters.ERROR_FORMAT_LESS + Parameters.COUNTRY_MAX + " !")
   @Column(name = "country", length = Parameters.COUNTRY_MAX)
   private String country = "FRANCE";

   public User() {
      // empty constructor for creating empty user and add attribute after
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
