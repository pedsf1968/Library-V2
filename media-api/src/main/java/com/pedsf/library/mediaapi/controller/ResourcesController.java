package com.pedsf.library.mediaapi.controller;


import com.pedsf.library.mediaapi.model.FileType;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.*;

@Slf4j
@Controller
public class ResourcesController extends MediaControllerConfiguration {

   @GetMapping("/images/{imageName}")
   public @ResponseBody byte[] getImage(@PathVariable("imageName") String imageName) throws IOException {
      File fileUnsafe = new File(imagesRepository+imageName);

      try {
         if (fileUnsafe.exists()) {
            InputStream in = new FileInputStream(imagesRepository + imageName);
            return IOUtils.toByteArray(in);
         }
      }catch (FileNotFoundException exception) {
         log.error(exception.getMessage());
      }
      return new byte[0];
   }

   @GetMapping("/images/{type}/{imageName}")
   public @ResponseBody  byte[] getBusinessImage(@PathVariable(value = "type", required = false) String type, @PathVariable("imageName") String imageName) throws IOException {
      StringBuilder sb = new StringBuilder();
      InputStream in ;

      try {
         if (type.equals(FileType.BOOK.name())){
            sb.append(bookImagesRepository);
         } else if (type.equals(FileType.MUSIC.name())){
            sb.append(musicImagesRepository);
         } else if (type.equals(FileType.VIDEO.name())){
            sb.append(videoImagesRepository);
         } else if (type.equals(FileType.GAME.name())){
            sb.append(gameImagesRepository);
         } else {
            sb.append(imagesRepository);
         }
         sb.append(imageName).append(".jpg");

         File fileUnsafe = new File(sb.toString());;

         if (fileUnsafe.exists()) {
            in = new FileInputStream(sb.toString());
            return IOUtils.toByteArray(in);
         }
      }catch (FileNotFoundException exception) {
         log.error(exception.getMessage());
      }
      return new byte[0];
   }

}
