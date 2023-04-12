package com.example.djjc.Service;

import com.example.djjc.Common.Campull;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.FrameRecorder;
import org.springframework.data.redis.core.RedisTemplate;

import java.awt.*;
import java.io.IOException;
import java.util.Queue;

public interface CameraPullService {
    void recordCamera(Long id, double frameRate, FrameGrabber grabber, FrameRecorder recorder, RedisTemplate<String, Queue<String>> redisTemplate, Campull campull) throws IOException, InterruptedException;

    void jietu(Frame frame, Queue<String> queue) throws IOException;
}
