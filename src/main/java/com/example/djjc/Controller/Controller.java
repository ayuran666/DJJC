package com.example.djjc.Controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.djjc.Common.R;
import com.example.djjc.Dto.Computer;
import com.example.djjc.Entity.Camera;
import com.example.djjc.Service.CameraService;
import com.example.djjc.Utils.CamPullUtils;
import com.example.djjc.Utils.PcUtils;
import lombok.extern.slf4j.Slf4j;
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.FrameRecorder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Queue;

@Slf4j
@RestController
@RequestMapping("/work")
public class Controller {

    @Resource
    private RedisTemplate<String,Queue<String>> redisTemplate;


    @Autowired
    private CameraService cameraService;

    @GetMapping("/getcomputerinfo")
    public R<Computer> getComuterInfo() throws InterruptedException {
        return R.success(new Computer(PcUtils.getCpuUsed(), PcUtils.getMemUsed(), PcUtils.getCpuTem()));
    }

    @GetMapping("/camerapull")
    public R<String> camerapull() {


        return null;

    }

    @PostMapping("/page")
    public R<Page<Camera>> page(@RequestParam("page") int page, @RequestParam("pagesize") int pagesize){
        return R.success(cameraService.page(page,pagesize));
    }

    @GetMapping("/openone")
    public R<String> openone(@RequestParam("id") String id){

        try {
            CamPullUtils.openOne(id,redisTemplate);
        } catch (FrameGrabber.Exception e) {
            R.error("推流系统异常！");
        }


        return R.success("成功");
    }

    @GetMapping("/closeone")
    public R<String> closeone(@RequestParam("id") String id) {
        try {
            CamPullUtils.close(id);
        } catch (FrameGrabber.Exception | FrameRecorder.Exception e) {
            R.error("推流系统异常！");
        }
        return R.success("成功！");
    }
}
