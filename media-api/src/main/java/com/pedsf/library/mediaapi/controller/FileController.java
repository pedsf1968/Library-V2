package com.pedsf.library.mediaapi.controller;

import com.pedsf.library.mediaapi.exception.ConflictException;
import com.pedsf.library.mediaapi.model.FileType;
import com.pedsf.library.mediaapi.service.FileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
public class FileController {
   private final FileService fileService;

   @Value("${media-api.images.repository}")
   private String imagesRepository;
   @Value("${media-api.book.images.repository}")
   private String bookImagesRepository;
   @Value("${media-api.music.images.repository}")
   private String musicImagesRepository;
   @Value("${media-api.video.images.repository}")
   private String videoImagesRepository;
   @Value("${media-api.game.images.repository}")
   private String gameImagesRepository;
   @Value("${media-api.user.images.repository}")
   private String userImagesRepository;

   public FileController(FileService fileService) {
      this.fileService = fileService;
   }

   @GetMapping("/uploadFile")
   public String uploadFile(Model model){
      return "fileUpload";
   }

   @PostMapping("/uploadFile")
   public ResponseEntity<Void> uploadFile(@RequestParam("file") MultipartFile file,
                                    @RequestParam("fileType") FileType fileType,
                                    @RequestParam("fileName") String fileName) {

      if (!fileName.matches("[a-zA-Z0-9]")) {
         throw new ConflictException("Invalid fileName "+ fileName);
      }

      if (fileType.equals(FileType.BOOK)) {
         fileService.uploadFile(file, bookImagesRepository, fileName);
      } else if (fileType.equals(FileType.MUSIC)) {
         fileService.uploadFile(file, musicImagesRepository, fileName);
      } else if (fileType.equals(FileType.VIDEO)) {
         fileService.uploadFile(file, videoImagesRepository, fileName);
      } else if (fileType.equals(FileType.GAME)) {
         fileService.uploadFile(file, gameImagesRepository, fileName);
      } else if (fileType.equals(FileType.USER)) {
         fileService.uploadFile(file, userImagesRepository,fileName);
      } else {
         fileService.uploadFile(file, imagesRepository, fileName);
      }

      return ResponseEntity.ok().build();
   }

}
