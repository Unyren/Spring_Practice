package org.zerock.mreview.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class MovieImageDTO implements Serializable {
    private String uuid;

    private String imgName;

    private String path;

    //첨부파일의 이미지 경로
    public String getImageURL() {
        try {
            return URLEncoder.encode(path +"/" +uuid +"_"+imgName,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public String getThumbnailURL(){
        try {
            return URLEncoder.encode(path +"/thumb_" +uuid +"_"+imgName,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
}
