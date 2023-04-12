package com.example.djjc.Entity;


import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class Camera implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String name;

    private String ipAddress;

    private Integer port;

    private Integer isUse;

    @TableField(value = "create_time",fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(value = "update_time",fill = FieldFill.UPDATE)
    private LocalDateTime updateTime;

}
