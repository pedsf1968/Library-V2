package com.library.libraryapi.model;

/**
 * status of the user
 *
 * FREE : the media can be borrowed
 * BORROWED : the media is borrowed
 * BLOCKED : the media is blocked
 */
public enum MediaStatus {
   FREE,
   BORROWED,
   BLOCKED
}
