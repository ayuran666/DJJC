package com.example.djjc.Service.Serviceimpl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.djjc.Entity.Camera;
import com.example.djjc.Mapper.CameraMapper;
import com.example.djjc.Service.CameraService;
import org.springframework.stereotype.Service;

@Service
public class CameraServiceImpl extends ServiceImpl<CameraMapper, Camera> implements CameraService {
    @Override
    public Page<Camera> page(int page, int pagesize) {
        Page<Camera> cameraPage = new Page<>();
        LambdaQueryWrapper<Camera> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByAsc(Camera::getCreateTime);
        this.page(cameraPage,wrapper);
        return cameraPage;
    }
}
