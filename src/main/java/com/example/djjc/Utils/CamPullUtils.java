package com.example.djjc.Utils;

import com.example.djjc.Common.Campull;
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.FrameRecorder;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Queue;

@Component
public class CamPullUtils {

    private static final HashMap<String,Campull> hashMap = new HashMap<>();

    private static Boolean isopen = true;

    public static void openOne(String id,RedisTemplate<String,Queue<String>> redisTemplate) throws FrameGrabber.Exception {
        Campull campull = new Campull(id,redisTemplate);
        campull.start();
        hashMap.put(id,campull);
    }

    public static void openList(){

    }

    public static void close(String id) throws FrameGrabber.Exception, FrameRecorder.Exception {
        Campull campull = hashMap.get(id);
        if(campull != null){
            campull.close();

        }
    }

    public static Boolean getIsopen(){
        return isopen;
    }

    public static void setIsopen(Boolean isopen){
        CamPullUtils.isopen = isopen;
    }



}
