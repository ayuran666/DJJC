package com.example.djjc.Service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.djjc.Entity.Camera;

public interface CameraService extends IService<Camera> {

    Page<Camera> page(int page, int pagesize);
}
