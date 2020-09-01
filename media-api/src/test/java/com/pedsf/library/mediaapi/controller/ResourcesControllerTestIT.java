package com.pedsf.library.mediaapi.controller;

import com.pedsf.library.mediaapi.model.FileType;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.io.FileInputStream;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = {ResourcesController.class})
class ResourcesControllerTestIT extends MediaControllerConfiguration {
   private static final String FILE_NAME = "background.png";
   private static final String BOOK_IMAGE = "978-2070413119";
   private static final String MUSIC_IMAGE = "4988064585816";
   private static final String GAME_IMAGE = "0805529340299";
   private static final String VIDEO_IMAGE = "3475001058980";

   @Autowired
   private MockMvc mockMvc;

   @Test
   @Tag("getImage")
   @DisplayName("Verify that we get image from root /images directory")
   void getImage_returnBackgroudImage_ofBackgroundFileName() throws Exception {
      FileInputStream fis = new FileInputStream( imagesRepository + FILE_NAME);
      byte[] expected  = IOUtils.toByteArray(fis);

      // WHEN
      final MvcResult result = mockMvc.perform(
            MockMvcRequestBuilders.get("/images/" + FILE_NAME))
            .andReturn();

      // THEN
      byte[] found = result.getResponse().getContentAsByteArray();
      assertThat(found).isEqualTo(expected);
   }

   @Test
   @Tag("getImage")
   @DisplayName("Verify that we get empty byte array from root /images directory if file don't exist")
   void getImage_returnEmptyByteArray_ofUnknownFileName() throws Exception {

      // WHEN
      final MvcResult result = mockMvc.perform(
            MockMvcRequestBuilders.get("/images/unknown"))
            .andReturn();

      // THEN
      byte[] found = result.getResponse().getContentAsByteArray();
      assertThat(found).isEqualTo(new byte[0]);
   }

   @Test
   @Tag("getBusinessImage")
   @DisplayName("Verify that we get image from /images/book directory")
   void getBusinessImage_returnBOOKImage_ofBOOKFileName() throws Exception {
      FileInputStream fis = new FileInputStream( bookImagesRepository + "/"+ BOOK_IMAGE + ".jpg");
      byte[] expected  = IOUtils.toByteArray(fis);

      // WHEN
      final MvcResult result = mockMvc.perform(
            MockMvcRequestBuilders.get("/images/"+ FileType.BOOK.name() + "/" + BOOK_IMAGE))
            .andReturn();

      // THEN
      byte[] found = result.getResponse().getContentAsByteArray();
      assertThat(found).isEqualTo(expected);
   }

   @Test
   @Tag("getBusinessImage")
   @DisplayName("Verify that we get image from /images/game directory")
   void getBusinessImage_returnGAMEImage_ofGAMEFileName() throws Exception {
      FileInputStream fis = new FileInputStream( gameImagesRepository + "/"+ GAME_IMAGE + ".jpg");
      byte[] expected  = IOUtils.toByteArray(fis);

      // WHEN
      final MvcResult result = mockMvc.perform(
            MockMvcRequestBuilders.get("/images/"+ FileType.GAME.name() + "/" + GAME_IMAGE))
            .andReturn();

      // THEN
      byte[] found = result.getResponse().getContentAsByteArray();
      assertThat(found).isEqualTo(expected);
   }

   @Test
   @Tag("getBusinessImage")
   @DisplayName("Verify that we get image from /images/music directory")
   void getBusinessImage_returnMUSICImage_ofMUSICFileName() throws Exception {
      FileInputStream fis = new FileInputStream( musicImagesRepository + "/"+ MUSIC_IMAGE + ".jpg");
      byte[] expected  = IOUtils.toByteArray(fis);

      // WHEN
      final MvcResult result = mockMvc.perform(
            MockMvcRequestBuilders.get("/images/"+ FileType.MUSIC.name() + "/" + MUSIC_IMAGE))
            .andReturn();

      // THEN
      byte[] found = result.getResponse().getContentAsByteArray();
      assertThat(found).isEqualTo(expected);
   }

   @Test
   @Tag("getBusinessImage")
   @DisplayName("Verify that we get image from /images/video directory")
   void getBusinessImage_returnVIDEOImage_ofVIDEOFileName() throws Exception {
      FileInputStream fis = new FileInputStream( videoImagesRepository + "/"+ VIDEO_IMAGE + ".jpg");
      byte[] expected  = IOUtils.toByteArray(fis);

      // WHEN
      final MvcResult result = mockMvc.perform(
            MockMvcRequestBuilders.get("/images/"+ FileType.VIDEO.name() + "/" + VIDEO_IMAGE))
            .andReturn();

      // THEN
      byte[] found = result.getResponse().getContentAsByteArray();
      assertThat(found).isEqualTo(expected);
   }

   @Test
   @Tag("getBusinessImage")
   @DisplayName("Verify that we get empty byte array from /image directory if VIDEO file don't exist")
   void getBusinessImage_returnEmptyByteArray_ofUnknownFileName() throws Exception {

      // WHEN
      final MvcResult result = mockMvc.perform(
            MockMvcRequestBuilders.get("/images/"+ FileType.VIDEO.name() + "/unknown"))
            .andReturn();


      // THEN
      byte[] found = result.getResponse().getContentAsByteArray();

      assertThat(found).isEqualTo(new byte[0]);
   }

}