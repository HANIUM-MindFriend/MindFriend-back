package com.example.mindfriend.dto.request;

import com.example.mindfriend.domain.User;
import lombok.Data;

@Data
public class postDiary {
    private String title;
    private String content;
    private String image;

}
