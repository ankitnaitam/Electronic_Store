package com.lcwd.electronic.store.service.impl;

import com.lcwd.electronic.store.exceptions.BadApiRequestException;
import com.lcwd.electronic.store.helper.AppConstants;
import com.lcwd.electronic.store.service.FileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

@Service
@Slf4j
public class FileServiceImpl implements FileService {
    @Override
    public String uploadFile(MultipartFile file, String path) throws IOException {
        String originalFilename = file.getOriginalFilename();
        log.info("Filename:{}", originalFilename);
        String filename = UUID.randomUUID().toString();
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String filenameWithExtension = filename + extension;
        String fullPathWithFilename = path + filenameWithExtension;
        if (extension.equalsIgnoreCase(".png") || extension.equalsIgnoreCase(".jpg") || extension.equalsIgnoreCase(".jpeg")) {
            File folder = new File(path);
            if (!folder.exists()) {
                folder.mkdirs();
            }
            Files.copy(file.getInputStream(), Paths.get(fullPathWithFilename));
            return filenameWithExtension;
        } else {
            throw new BadApiRequestException(AppConstants.FILE_NOT_ALLOWED + extension);
        }
    }

    @Override
    public InputStream getResource(String path, String name) throws FileNotFoundException {
        String fullPath = path + File.separator + name;
        FileInputStream inputStream = new FileInputStream(fullPath);
        return inputStream;
    }
}
