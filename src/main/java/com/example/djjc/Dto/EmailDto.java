package com.example.djjc.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmailDto implements Serializable {

    /**
     * 发送邮箱的列表
     */
    private List<String> tos;

    //主题
    private String theme;

    //内容
    private String content;
}
