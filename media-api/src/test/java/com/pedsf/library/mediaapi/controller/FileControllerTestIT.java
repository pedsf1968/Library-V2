package com.pedsf.library.mediaapi.controller;

import com.pedsf.library.dto.business.BookDTO;
import com.pedsf.library.mediaapi.model.FileType;
import com.pedsf.library.mediaapi.service.FileService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.multipart.MultipartFile;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(FileController.class)
@RunWith(SpringRunner.class)
class FileControllerTestIT extends MediaControllerConfiguration {
   private final static String FILE_NAME = "filename";
   private MockMvc mockMvc;

   @Autowired
   private WebApplicationContext webApplicationContext;

   @MockBean
   private FileService fileService;
   private MockMultipartFile mockMultipartFile;

   @BeforeEach
   void beforeEach() {
      mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
      mockMultipartFile = new MockMultipartFile("data", FILE_NAME, "text/plain", "some txt".getBytes());
      doNothing().when(fileService).uploadFile(any(MultipartFile.class),anyString(),anyString());
   }

   @Test
   @Tag("uploadFile")
   @DisplayName("Verify that save BOOK image type file in BOOK image directory")
   void uploadFile_saveFileInBOOKDirectory_of_BOOKFileType() throws Exception {

      // GIVEN
      ArgumentCaptor<MultipartFile> multipartFileCaptor = ArgumentCaptor.forClass(MultipartFile.class);
      ArgumentCaptor<String> directoryCaptor = ArgumentCaptor.forClass(String.class);
      ArgumentCaptor<String> fileNameCaptor = ArgumentCaptor.forClass(String.class);

      // WHEN
      mockMvc.perform(MockMvcRequestBuilders.multipart("/uploadFile")
                  .file("file", mockMultipartFile.getBytes())
                  .param("fileType", FileType.BOOK.name())
                  .param("fileName", FILE_NAME))
            .andExpect(status().is2xxSuccessful())
            .andReturn();

      // THEN
      verify(fileService).uploadFile(multipartFileCaptor.capture(),directoryCaptor.capture(),fileNameCaptor.capture());
      String directoryFound = directoryCaptor.getAllValues().get(0);
      String fileNameFound = fileNameCaptor.getAllValues().get(0);
      assertThat(directoryFound).isEqualTo(bookImagesRepository);
      assertThat(fileNameFound).isEqualTo(FILE_NAME);
   }

   @Test
   @Tag("uploadFile")
   @DisplayName("Verify that save GAME image type file in GAME image directory")
   void uploadFile_saveFileInGAMEDirectory_of_GAMEFileType() throws Exception {

      // GIVEN
      ArgumentCaptor<MultipartFile> multipartFileCaptor = ArgumentCaptor.forClass(MultipartFile.class);
      ArgumentCaptor<String> directoryCaptor = ArgumentCaptor.forClass(String.class);
      ArgumentCaptor<String> fileNameCaptor = ArgumentCaptor.forClass(String.class);

      // WHEN
      mockMvc.perform(MockMvcRequestBuilders.multipart("/uploadFile")
            .file("file", mockMultipartFile.getBytes())
            .param("fileType", FileType.GAME.name())
            .param("fileName", FILE_NAME))
            .andExpect(status().is2xxSuccessful())
            .andReturn();

      // THEN
      verify(fileService).uploadFile(multipartFileCaptor.capture(),directoryCaptor.capture(),fileNameCaptor.capture());
      String directoryFound = directoryCaptor.getAllValues().get(0);
      String fileNameFound = fileNameCaptor.getAllValues().get(0);
      assertThat(directoryFound).isEqualTo(gameImagesRepository);
      assertThat(fileNameFound).isEqualTo(FILE_NAME);
   }

   @Test
   @Tag("uploadFile")
   @DisplayName("Verify that save MUSIC image type file in MUSIC image directory")
   void uploadFile_saveFileInMUSICDirectory_of_MUSICFileType() throws Exception {

      // GIVEN
      ArgumentCaptor<MultipartFile> multipartFileCaptor = ArgumentCaptor.forClass(MultipartFile.class);
      ArgumentCaptor<String> directoryCaptor = ArgumentCaptor.forClass(String.class);
      ArgumentCaptor<String> fileNameCaptor = ArgumentCaptor.forClass(String.class);

      // WHEN
      mockMvc.perform(MockMvcRequestBuilders.multipart("/uploadFile")
            .file("file", mockMultipartFile.getBytes())
            .param("fileType", FileType.MUSIC.name())
            .param("fileName", FILE_NAME))
            .andExpect(status().is2xxSuccessful())
            .andReturn();

      // THEN
      verify(fileService).uploadFile(multipartFileCaptor.capture(),directoryCaptor.capture(),fileNameCaptor.capture());
      String directoryFound = directoryCaptor.getAllValues().get(0);
      String fileNameFound = fileNameCaptor.getAllValues().get(0);
      assertThat(directoryFound).isEqualTo(musicImagesRepository);
      assertThat(fileNameFound).isEqualTo(FILE_NAME);
   }

   @Test
   @Tag("uploadFile")
   @DisplayName("Verify that save VIDEO image type file in VIDEO image directory")
   void uploadFile_saveFileInVIDEODirectory_of_VIDEOFileType() throws Exception {

      // GIVEN
      ArgumentCaptor<MultipartFile> multipartFileCaptor = ArgumentCaptor.forClass(MultipartFile.class);
      ArgumentCaptor<String> directoryCaptor = ArgumentCaptor.forClass(String.class);
      ArgumentCaptor<String> fileNameCaptor = ArgumentCaptor.forClass(String.class);

      // WHEN
      mockMvc.perform(MockMvcRequestBuilders.multipart("/uploadFile")
            .file("file", mockMultipartFile.getBytes())
            .param("fileType", FileType.VIDEO.name())
            .param("fileName", FILE_NAME))
            .andExpect(status().is2xxSuccessful())
            .andReturn();

      // THEN
      verify(fileService).uploadFile(multipartFileCaptor.capture(),directoryCaptor.capture(),fileNameCaptor.capture());
      String directoryFound = directoryCaptor.getAllValues().get(0);
      String fileNameFound = fileNameCaptor.getAllValues().get(0);
      assertThat(directoryFound).isEqualTo(videoImagesRepository);
      assertThat(fileNameFound).isEqualTo(FILE_NAME);
   }

   @Test
   @Tag("uploadFile")
   @DisplayName("Verify that save other image type file in root image directory")
   void uploadFile_saveFileInRootDirectory_of_OtherFileType() throws Exception {

      // GIVEN
      ArgumentCaptor<MultipartFile> multipartFileCaptor = ArgumentCaptor.forClass(MultipartFile.class);
      ArgumentCaptor<String> directoryCaptor = ArgumentCaptor.forClass(String.class);
      ArgumentCaptor<String> fileNameCaptor = ArgumentCaptor.forClass(String.class);

      // WHEN
      mockMvc.perform(MockMvcRequestBuilders.multipart("/uploadFile")
            .file("file", mockMultipartFile.getBytes())
            .param("fileType", FileType.OTHER.name())
            .param("fileName", FILE_NAME))
            .andExpect(status().is2xxSuccessful())
            .andReturn();

      // THEN
      verify(fileService).uploadFile(multipartFileCaptor.capture(),directoryCaptor.capture(),fileNameCaptor.capture());
      String directoryFound = directoryCaptor.getAllValues().get(0);
      String fileNameFound = fileNameCaptor.getAllValues().get(0);
      assertThat(directoryFound).isEqualTo(imagesRepository);
      assertThat(fileNameFound).isEqualTo(FILE_NAME);
   }


}
