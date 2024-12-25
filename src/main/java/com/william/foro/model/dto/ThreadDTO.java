package com.william.foro.model.dto;

import lombok.Data;

@Data
public class ThreadDTO {


    private String title;
    private String message;
    private Long userId;
    private Long publicationId;
}
