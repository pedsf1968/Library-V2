package com.library.mediaapi.service;

import com.library.mediaapi.exception.FileStorageException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Paths;

@Slf4j
@Service("FileService")
public class FileServiceImpl implements FileService {

   public void uploadFile(MultipartFile file, String directory) {

      if(file!=null) {
         try {
            log.info("\n\nINFO directory : {}", directory);
            Files.write(Paths.get(directory), file.getBytes());
         } catch (Exception e) {
            e.printStackTrace();
            throw new FileStorageException("Could not store file " + file.getOriginalFilename()
                  + ". Please try again!");
         }
      }
   }
}
