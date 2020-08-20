package com.pedsf.library;

public final class Parameters {
   public static final int AUDIO_FORMAT_MAX = 255;
   public static final int CITY_MAX = 50;
   public static final int COUNTRY_MAX = 50;
   public static final int EAN_MAX = 20;
   public static final int EMAIL_MAX = 255;
   public static final int EMAIL_MIN = 4;
   public static final String EMAIL_REGEXP = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";
   public static final int FIRSTNAME_MAX = 50;
   public static final int FIRSTNAME_MIN = 2;
   public static final int FORMAT_MAX = 20;
   public static final int IMAGE_FORMAT_MAX = 255;
   public static final int ISBN_MAX = 20;
   public static final int LASTNAME_MAX = 50;
   public static final int LASTNAME_MIN = 2;
   public static final int MEDIA_STATUS_MAX = 10;
   public static final int MEDIA_TYPE_MAX = 10;
   public static final int PASSWORD_MAX = 255;
   public static final int PASSWORD_MIN = 4;
   public static final int PHONE_MAX = 14;
   public static final String PHONE_REGEXP = "^(?:(?:\\+|00)33[\\s.-]{0,3}(?:\\(0\\)[\\s.-]{0,3})?|0)[1-9](?:(?:[\\s.-]?\\d{2}){4}|\\d{2}(?:[\\s.-]?\\d{3}){2}|)$";
   public static final int PHOTO_MAX = 255;
   public static final int PEGI_MAX = 4;
   public static final int PUBLIC_TYPE_MAX = 20;
   public static final int ROLE_MAX = 10;
   public static final int STATUS_MAX = 10;
   public static final int STREET_MAX = 50;
   public static final int SUMMARY_MAX = 2048;
   public static final int TITLE_MAX = 50;
   public static final int TITLE_MIN = 1;
   public static final int TYPE_MAX = 20;
   public static final int URL_MAX = 255; // default length
   public static final int ZIP_MAX = 6;
   public static final int ZIP_MIN = 5;

   public static final String ERROR_FORMAT_BETWEEN = "Length should be between : ";
   public static final String ERROR_FORMAT_LESS = "Length should less than : ";
   public static final String ERROR_EMAIL_FORMAT = "Not a valid email address !";
   public static final String ERROR_PHONE_FORMAT = "Not a valid phone number !";




   private Parameters() {
      throw new IllegalStateException("Utility class");
   }
}
