package com.pedsf.library.mediaapi.controller;


import com.pedsf.library.mediaapi.model.FileType;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

@Controller
public class ResourcesController {


   @Value("${media-api.images.repository}")
   private String imagesRepository;
   @Value("${media-api.book.images.repository}")
   private String bookImagesRepository;
   @Value("${media-api.music.images.repository}")
   private  String musicImagesRepository;
   @Value("${media-api.video.images.repository}")
   private  String videoImagesRepository;
   @Value("${media-api.game.images.repository}")
   private  String gameImagesRepository;
   @Value("${media-api.user.images.repository}")
   private  String userImagesRepository;

   @GetMapping("/images/{imageName}")
   public @ResponseBody byte[] getImage(@PathVariable("imageName") String imageName) throws IOException {
      InputStream  in = new FileInputStream(imagesRepository + imageName);

      return IOUtils.toByteArray(in);
   }

   @GetMapping("/images/{type}/{imageName}")
   public @ResponseBody  byte[] getBusinessImage(@PathVariable(value = "type", required = false) String type, @PathVariable("imageName") String imageName) throws IOException {
      InputStream in;

      if (type.equals(FileType.BOOK.name())){
         in = new FileInputStream(bookImagesRepository + imageName + ".jpg");
      } else if (type.equals(FileType.MUSIC.name())){
         in = new FileInputStream(musicImagesRepository + imageName + ".jpg");
      } else if (type.equals(FileType.VIDEO.name())){
         in = new FileInputStream(videoImagesRepository + imageName + ".jpg");
      } else if (type.equals(FileType.GAME.name())){
         in = new FileInputStream(gameImagesRepository + imageName + ".jpg");
      } else if (type.equals(FileType.USER.name())){
         in = new FileInputStream(userImagesRepository + imageName + ".jpg");
      } else {
         in = new FileInputStream(imagesRepository + imageName + ".jpg");
      }
      return IOUtils.toByteArray(in);
   }

}
