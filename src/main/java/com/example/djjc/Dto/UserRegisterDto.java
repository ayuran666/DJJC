package com.example.djjc.Dto;

import com.example.djjc.Entity.User;
import lombok.Data;

@Data
public class UserRegisterDto extends User {
    private String code;

}
