package com.library.libraryapi.service;

import com.pedsf.library.libraryapi.dto.business.MediaDTO;
import com.pedsf.library.libraryapi.proxy.UserApiProxy;
import com.pedsf.library.libraryapi.repository.BorrowingRepository;
import com.pedsf.library.libraryapi.service.BorrowingService;
import com.pedsf.library.libraryapi.service.MediaService;
import com.pedsf.library.userapi.dto.UserDTO;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

@ExtendWith(MockitoExtension.class)
class BorrowingServiceTest {
   private static List<UserDTO> userDTOS;
   private static List<MediaDTO> mediaDTOS;

   @Mock
   private static BorrowingRepository borrowingRepository;

   @Mock
   private static UserApiProxy userApiProxy;

   @Mock
   private static MediaService mediaService;

   @InjectMocks
   BorrowingService borrowingService;

   @BeforeAll
   private static void beforeAll() {


   }

   public void borrow() {
      //La liste de réservation ne peut comporter qu’un maximum de personnes correspondant à 2x le nombre d’exemplaires de l’ouvrage.
      /* UserDTO userDTO = userDTOS.get(1);
      MediaDTO mediaDTO = mediaDTOS.get(1);

      borrowingService.borrow(userDTO.getId(),mediaDTO.getId()); */
   }
}