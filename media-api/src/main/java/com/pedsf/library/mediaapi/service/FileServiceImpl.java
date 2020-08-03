package com.pedsf.library.mediaapi.service;

import com.pedsf.library.exception.FileStorageException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
@Service("FileService")
public class FileServiceImpl implements FileService {

   public void uploadFile(MultipartFile multipartFile, String directoryOutput, String finename) {

      StringBuilder sb = new StringBuilder();
      sb.append(directoryOutput);
      sb.append(finename);

      if(multipartFile!=null) {
         try {
            log.info("\n\nINFO output : {}", sb);
            byte[] bytes = multipartFile.getBytes();
            Path path = Paths.get(sb.toString());
            Files.write(path, bytes);
         } catch (Exception exception) {
            log.error(exception.getMessage());
            throw new FileStorageException("Could not store file " + multipartFile.getOriginalFilename()
                  + ". Please try again!");
         }
      }
   }
}
