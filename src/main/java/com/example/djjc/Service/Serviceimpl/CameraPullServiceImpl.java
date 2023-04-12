package com.example.djjc.Service.Serviceimpl;

import com.example.djjc.Common.Campull;
import com.example.djjc.Service.CameraPullService;
import com.example.djjc.Utils.CamPullUtils;
import com.sun.xml.internal.messaging.saaj.util.ByteOutputStream;
import lombok.RequiredArgsConstructor;
import org.bytedeco.javacpp.Loader;
import org.bytedeco.javacpp.avcodec;
import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_objdetect;
import org.bytedeco.javacv.*;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class CameraPullServiceImpl implements CameraPullService {

    private static final String prx = "rtmp://localhost:1940/live/";



    @Override
    public void recordCamera(Long id, double frameRate, FrameGrabber grabber, FrameRecorder recorder, RedisTemplate<String,Queue<String>> redisTemplate, Campull campull) throws IOException, InterruptedException {
        Loader.load(opencv_objdetect.class);
        grabber = FrameGrabber.createDefault(0);//本机摄像头默认0，这里使用javacv的抓取器，至于使用的是ffmpeg还是opencv，请自行查看源码
        grabber.start();//开启抓取器

        OpenCVFrameConverter.ToIplImage converter = new OpenCVFrameConverter.ToIplImage();//转换器
        opencv_core.IplImage grabbedImage = converter.convert(grabber.grab());//抓取一帧视频并将其转换为图像，至于用这个图像用来做什么？加水印，人脸识别等等自行添加
        int width = grabbedImage.width();
        int height = grabbedImage.height();

        recorder = FrameRecorder.createDefault(prx+id, width, height);
        recorder.setVideoCodec(avcodec.AV_CODEC_ID_H264); // avcodec.AV_CODEC_ID_H264，编码
        recorder.setFormat("flv");//封装格式，如果是推送到rtmp就必须是flv封装格式
        recorder.setFrameRate(frameRate);

        recorder.start();//开启录制器
        long startTime=0;
        long videoTS=0;
        Queue<String> queue = new LinkedList<>();
//        CanvasFrame frame = new CanvasFrame("camera", CanvasFrame.getDefaultGamma() / grabber.getGamma());
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frame.setAlwaysOnTop(true);

        Frame rotatedFrame=converter.convert(grabbedImage);//不知道为什么这里不做转换就不能推到rtmp
        while (/*frame.isVisible() && */campull.getRuning() && (grabbedImage = converter.convert(grabber.grab())) != null) {
            rotatedFrame = converter.convert(grabbedImage);
            //frame.showImage(rotatedFrame);
            jietu(rotatedFrame,queue);

            redisTemplate.opsForValue().set(String.valueOf(id),queue,30, TimeUnit.SECONDS);
            if (startTime == 0) {
                startTime = System.currentTimeMillis();
            }
            videoTS = 1000 * (System.currentTimeMillis() - startTime);
            recorder.setTimestamp(videoTS);
            recorder.record(rotatedFrame);
            Thread.sleep(40);
        }
        //frame.dispose();
        recorder.stop();
        recorder.release();
        grabber.stop();
    }

    @Override
    public void jietu(Frame frame, Queue<String> queue) throws IOException {
        Java2DFrameConverter java2DFrameConverter = new Java2DFrameConverter();
        BufferedImage image = java2DFrameConverter.convert(frame);
        ByteOutputStream byteOutputStream = new ByteOutputStream();
        ImageIO.write(image,"jpg",byteOutputStream);
        String s = Base64Utils.encodeToString(byteOutputStream.getBytes());
        queue.add("data:image/jpg;base64,"+s);
        if (queue.size() == 31){
            queue.poll();
        }
    }


}
