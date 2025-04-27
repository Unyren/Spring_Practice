package org.zerock.mreview.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@Data
@AllArgsConstructor
public class UploadResultDTO implements Serializable {
    private String fileName;
    private String uuid;
    private String folderPath;

    //첨부파일의 이미지 경로
    public String getImageURL() {
        try {
            return URLEncoder.encode(folderPath +"/" +uuid +"_"+fileName,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public String getThumbnailURL(){
        try {
            return URLEncoder.encode(folderPath +"/thumb_" +uuid +"_"+fileName,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
}
