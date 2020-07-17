package com.library.web.dto;

/**
 * status of the user
 *
 * MEMBER : he suscribe but haven't yet borrowed a media
 * BORROWER : he has borrowed a media for 4 weeks
 * DELAY : he has delayed a media return between 4 and 8 weeks
 * FORBIDDEN : he hasn't pay the subscription
 * BAN : delayed return over the limit of 8 weeks
 */
public enum UserStatus {
   MEMBER,
   BORROWER,
   DELAY,
   FORBIDDEN,
   BAN
}
