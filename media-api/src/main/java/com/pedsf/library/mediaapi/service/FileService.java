package com.pedsf.library.mediaapi.service;

import org.springframework.web.multipart.MultipartFile;


public interface FileService {
   void uploadFile(MultipartFile file, String directory);
}
