package com.library.libraryapi.model;

/**
 * status of the user
 *
 * FREE : the media can be borrowed (can be booked or borrowed)
 * BORROWED : the media is borrowed
 * BOOKED : the media is booked (during 48h waiting for user pickup)
 * BLOCKED : the media is blocked (in the borrowing procedure)
 */
public enum MediaStatus {
   FREE,
   BORROWED,
   BOOKED,
   BLOCKED
}
