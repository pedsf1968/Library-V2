package com.pedsf.library.mediaapi.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

//@Configuration
public class MediaControllerConfiguration {

   @Value("${media-api.images.repository}")
   public String imagesRepository;

   @Value("${media-api.book.images.repository}")
   public String bookImagesRepository;

   @Value("${media-api.music.images.repository}")
   public String musicImagesRepository;

   @Value("${media-api.video.images.repository}")
   public String videoImagesRepository;

   @Value("${media-api.game.images.repository}")
   public String gameImagesRepository;

   @Value("${media-api.user.images.repository}")
   public String userImagesRepository;
}
