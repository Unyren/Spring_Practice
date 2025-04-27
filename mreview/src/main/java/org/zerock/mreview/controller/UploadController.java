package org.zerock.mreview.controller;

import lombok.extern.log4j.Log4j2;
import net.coobird.thumbnailator.Thumbnailator;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.zerock.mreview.dto.UploadResultDTO;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@Log4j2
public class UploadController {

    @Value("${org.zerock.upload.path}")//application.properties의 변수
    private String uploadPath;

    @PostMapping("/uploadAjax")
    public ResponseEntity<List<UploadResultDTO>> uploadFile(MultipartFile[] uploadFiles) {

        List<UploadResultDTO> resultDTOList = new ArrayList<>();

        for (MultipartFile uploadFile : uploadFiles) {

            //이미지 파일만 업로드 가능
            if (uploadFile.getContentType().startsWith("image")==false) {
                log.warn("this file is not image type");
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);//403 error
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
            String saveName=uploadPath+File.separator+folderPath+File.separator+uuid+"_"+fileName;
            log.info(saveName);//D:/upload\2025\03\19\5ba0a8bd-b18e-45e7-95e5-f2beb189b413_KakaoTalk_20250317_105548163.jpeg

            Path savePath=Paths.get(saveName);

            try {
                uploadFile.transferTo(savePath);//실제파일저장

                //썸네일 이미지 만들기
                if(uploadFile.getContentType().startsWith("image")==true) {

                    String thumbnailSaveName=uploadPath+File.separator+folderPath+File.separator+"thumb_"+uuid+"_"+fileName;

                    File thumbnailFile=new File(thumbnailSaveName);

                    //섬네일 크기 지정
                    Thumbnailator.createThumbnail(savePath.toFile(),thumbnailFile,100,100);
                }

                resultDTOList.add(new UploadResultDTO(fileName,uuid,folderPath));//resultDTOList에 첨부파일 정보저장
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }//포문 끝
        return new ResponseEntity<>(resultDTOList, HttpStatus.OK);
    }//메소드

    private String makeFolder() {
        String str= LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        String folderPath=str.replace("/",File.separator);
        File uploadPathFolder=new File(uploadPath, folderPath);
        if (uploadPathFolder.exists()==false) {
            uploadPathFolder.mkdirs();
        }
        return folderPath;
    }//메이크폴더 메소드

    @GetMapping("/display")
    public ResponseEntity<byte[]> getFile(String fileName) {
        ResponseEntity<byte[]> result = null;
        try {
            String srcFileName= URLDecoder.decode(fileName,"UTF-8");
            log.info(srcFileName);
            File file=new File(uploadPath+File.separator+srcFileName);
            log.info("file: "+file);

            //mine타입처리
            HttpHeaders header = new HttpHeaders();
            header.add("Content-Type", Files.probeContentType(file.toPath()));
            result=new ResponseEntity<>(FileCopyUtils.copyToByteArray(file),header,HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

        }
        return result;
    }
    @PostMapping("/removeFile")
    public ResponseEntity<Boolean> removeFile(String fileName) {
        String srcFileName=null;
        try {
            srcFileName=URLDecoder.decode(fileName,"UTF-8");

            File file=new File(uploadPath+File.separator+srcFileName);
            boolean result=file.delete();//파일삭제

            //파일에 mime 값 가져오기
            Path source=Paths.get(uploadPath+File.separator+srcFileName);
            String mimeType=Files.probeContentType(source);
            //이미지 파일경우만 썸네일파일 삭제
            if (mimeType.startsWith("image")==true) {
                File thumbnail=new File(file.getParent(),"thumb_"+file.getName());
                result=thumbnail.delete();//썸네일 파일삭제
            }
            return new ResponseEntity<>(result,HttpStatus.OK);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}//class
