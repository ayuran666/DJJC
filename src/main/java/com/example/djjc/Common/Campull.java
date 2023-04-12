package com.example.djjc.Common;

import com.example.djjc.Service.CameraPullService;
import com.example.djjc.Service.Serviceimpl.CameraPullServiceImpl;
import com.example.djjc.Utils.CamPullUtils;
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.FrameRecorder;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Queue;


public class Campull extends Thread{

    CameraPullService cameraPullService = new CameraPullServiceImpl();

    FrameGrabber grabber = null;

    FrameRecorder recorder = null;

    private String id;

    private Boolean runing = true;

    private RedisTemplate<String,Queue<String>> redisTemplate = null;



    public Campull(String id, RedisTemplate<String, Queue<String>> redisTemplate) throws FrameGrabber.Exception {
        this.id = id;
        this.redisTemplate = redisTemplate;
    }
    @Override
    public void run(){
        try {
                cameraPullService.recordCamera(id,30,grabber,recorder,this.redisTemplate,this);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
    public void close() throws FrameGrabber.Exception, FrameRecorder.Exception {
        this.runing = false;
        if(recorder != null){
            recorder.stop();
            recorder.release();
        }
        if(grabber != null){
            grabber.close();
        }
    }

    public Boolean getRuning(){
        return this.runing;
    }

}
