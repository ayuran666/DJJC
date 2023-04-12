package com.example.djjc.Utils;

import com.example.djjc.Common.Campull;
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.FrameRecorder;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Queue;

@Component
public class CamPullUtils {

    private static final HashMap<Long,Campull> hashMap = new HashMap<>();

    private static Boolean isopen = true;

    public static void openOne(Long id,RedisTemplate<String,Queue<String>> redisTemplate) throws FrameGrabber.Exception {
        Campull campull = new Campull(id,redisTemplate);
        campull.start();
        hashMap.put(id,campull);
    }

    public static void openList(List<Long> ids,RedisTemplate<String,Queue<String>> redisTemplate) throws FrameGrabber.Exception {
        for (Long id : ids) {
            openOne(id,redisTemplate);
        }
    }

    public static void closeOne(Long id) throws FrameGrabber.Exception, FrameRecorder.Exception {
        Campull campull = hashMap.get(id);
        if(campull != null){
            campull.close();

        }
    }

    public static void closelist(List<Long> ids) throws FrameGrabber.Exception, FrameRecorder.Exception {
        for (Long id : ids) {
            closeOne(id);
        }
    }



    public static Boolean getIsopen(){
        return isopen;
    }

    public static void setIsopen(Boolean isopen){
        CamPullUtils.isopen = isopen;
    }



}
