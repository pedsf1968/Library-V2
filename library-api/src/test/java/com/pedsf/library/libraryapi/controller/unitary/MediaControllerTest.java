package com.pedsf.library.libraryapi.controller.unitary;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pedsf.library.dto.business.BookDTO;
import com.pedsf.library.dto.business.MediaDTO;
import com.pedsf.library.dto.business.PersonDTO;
import com.pedsf.library.exception.ResourceNotFoundException;
import com.pedsf.library.libraryapi.controller.MediaController;
import com.pedsf.library.libraryapi.service.MediaService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import javax.inject.Inject;
import java.nio.charset.StandardCharsets;
import java.sql.Date;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@WebMvcTest(controllers = {MediaController.class})
@ExtendWith(SpringExtension.class)
@ExtendWith(MockitoExtension.class)
@RunWith(MockitoJUnitRunner.class)
class MediaControllerTest {

 @Inject
 private MockMvc mockMvc;

 @MockBean
 private MediaService mediaService;
 private static ObjectMapper mapper = new ObjectMapper();
 private static MediaDTO newMediaDTO;

 @BeforeAll
 static void beforeAll() {
  BookDTO newBookDTO = new BookDTO("954-8789797","The green tomato",1,1,"9548789797",
        new PersonDTO(3, "Victor", "HUGO", Date.valueOf("1802-02-26")),
        new PersonDTO(10, "LGF", "Librairie Générale Française", null));
  newBookDTO.setPages(125);
  newBookDTO.setFormat("COMICS");
  newBookDTO.setType("HUMOR");
  newBookDTO.setHeight(11);
  newBookDTO.setLength(11);
  newBookDTO.setWidth(11);
  newBookDTO.setWeight(220);
  newBookDTO.setSummary("Contrary to popular belief, Lorem Ipsum is not simply random text. It has roots in a piece of " +
        "classical Latin literature from 45 BC, making it over 2000 years old. Richard McClintock, a Latin " +
        "professor at Hampden-Sydney College in Virginia, looked up one of the more obscure Latin words, consectetur," +
        " from a Lorem Ipsum passage, and going through the cites of the word in classical literature, discovered " +
        "the undoubtable source. Lorem Ipsum comes from sections 1.10.32 and 1.10.33 of de Finibus Bonorum et " +
        "Malorum (The Extremes of Good and Evil) by Cicero, written in 45 BC. This book is a treatise on the theory" +
        " of ethics, very popular during the Renaissance. The first line of Lorem Ipsum, Lorem ipsum dolor sit amet.." +
        " comes from a line in section 1.10.32.");

  newMediaDTO = new MediaDTO(newBookDTO);
 }

 @Test
 @Tag("findAllMedias")
 @DisplayName("Verify that we get NotFound if there are no Medias")
 void findAllMedias_returnNotFound_ofResourceNotFoundException() throws Exception {
  // GIVEN
  when(mediaService.findAll()).thenThrow(ResourceNotFoundException.class);

  // WHEN
  mockMvc.perform(
        MockMvcRequestBuilders.get("/medias"))
        .andExpect(MockMvcResultMatchers.status().isNotFound())
        .andReturn();
 }

 @Test
 @Tag("findAllFiltered")
 @DisplayName("Verify that we get NotFound if there are no Medias")
 void findAllFiltered_returnNotFound_ofResourceNotFoundException() throws Exception {
  // GIVEN
  when(mediaService.findAllFiltered(newMediaDTO)).thenThrow(ResourceNotFoundException.class);

  String json = mapper.writeValueAsString(newMediaDTO);

  // WHEN
  mockMvc.perform(
        MockMvcRequestBuilders.post("/medias/searches")
              .contentType(MediaType.APPLICATION_JSON)
              .characterEncoding(String.valueOf(StandardCharsets.UTF_8))
              .content(json))
        .andExpect(MockMvcResultMatchers.status().isNotFound())
        .andReturn();
 }

 @Test
 @Tag("findById")
 @DisplayName("Verify that we get NotFound if with wrong Id")
 void findById_returnNotFound_ofWrongMediaId() throws Exception {
  // GIVEN
  when(mediaService.findById(anyInt())).thenThrow(ResourceNotFoundException.class);

  // WHEN
  mockMvc.perform(
        MockMvcRequestBuilders.get("/medias/" + "98798"))
        .andExpect(MockMvcResultMatchers.status().isNotFound())
        .andReturn();
 }




}
