package com.project01.reactspring.utils;

import com.project01.reactspring.exception.CustomException.AppException;
import com.project01.reactspring.exception.CustomException.ErrorCode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Component
public class UploadFile {

    @Value("${file.upload-dir}")
    private String uploadDir;

    public String uploadFile(MultipartFile file) {

            String fileName=file.getOriginalFilename().replace(" ", "_");
            try {
                File uploadFile=new File(uploadDir);
                if(!uploadFile.exists()){
                    new File(uploadDir).mkdirs();
                }
                FileCopyUtils.copy(file.getBytes(),new File(uploadDir+fileName));

            }catch (IOException e) {
                e.printStackTrace();
            }
            return fileName;
        }
}
