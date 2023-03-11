package com.example.djjc.Entity;


import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    @Length(min = 4, max = 12)
    private String id;

    @Length(min = 8, max = 16)
    private String password;

    @Email()
    private String email;

    @Past
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @Past()
    private LocalDateTime lastOnlineTime;
}
