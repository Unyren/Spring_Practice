package org.zerock.mreview.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
@Log4j2
@Controller
public class UploadTestController {
    @Value("${org.zerock.upload.path}")//application.properties의 변수
    private String uploadPath;

    @GetMapping("/uploadEx")
    public void uploadEx() {

    }
    @GetMapping("/uploadEx2")
    public void uploadEx2() {

    }
    @PostMapping("/uploadForm")
    public void uploadForm(MultipartFile[] uploadFiles) {
        for (MultipartFile uploadFile : uploadFiles) {

            //이미지 파일만 업로드 가능
            if (uploadFile.getContentType().startsWith("image")==false) {
                log.warn("this file is not image type");
                return;
            }
            //실제 파일 이름 ie나 edge는 전체 경로가 들어오므로
            String OriginalName = uploadFile.getOriginalFilename();//첨부파일명
            String fileName=OriginalName.substring(OriginalName.lastIndexOf("\\")+1);

            log.info(fileName);

            //날짜 폴더 생성
            String folderPath=makeFolder();

            //uuid
            String uuid= UUID.randomUUID().toString();

            //저장할 파일 이름 중간에 _를 이용해서 구분
            String saveName=uploadPath+ File.separator+folderPath+File.separator+uuid+"_"+fileName;
            log.info(saveName);//D:/upload\2025\03\19\5ba0a8bd-b18e-45e7-95e5-f2beb189b413_KakaoTalk_20250317_105548163.jpeg

            Path savePath= Paths.get(saveName);

            try {
                uploadFile.transferTo(savePath);//실제파일저장
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }//포문 끝
    }
    private String makeFolder() {
        String str= LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        String folderPath=str.replace("/",File.separator);
        File uploadPathFolder=new File(uploadPath, folderPath);
        if (uploadPathFolder.exists()==false) {
            uploadPathFolder.mkdirs();
        }
        return folderPath;
    }

}
