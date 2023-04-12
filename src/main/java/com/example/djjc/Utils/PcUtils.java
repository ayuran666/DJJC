package com.example.djjc.Utils;

import cn.hutool.system.oshi.OshiUtil;
import com.alibaba.druid.util.StringUtils;
import oshi.hardware.CentralProcessor;
import oshi.hardware.GlobalMemory;
import oshi.hardware.Sensors;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

public class PcUtils {

    private static final BigDecimal GbDw = new BigDecimal(1024 * 1024 * 1024);
    private static final BigDecimal HzDw = new BigDecimal(1000 * 1000);

    private static final GlobalMemory memory = OshiUtil.getMemory();

    public static String getMemUsed(){
        double getTotal = new BigDecimal(memory.getTotal()).divide(GbDw).setScale(2, RoundingMode.HALF_UP).doubleValue();
        double getAvailable = new BigDecimal(memory.getAvailable()).divide(GbDw).setScale(2, RoundingMode.HALF_UP).doubleValue();
        return new DecimalFormat("#.##%").format((getTotal-getAvailable)/getTotal);
    }

    public static String getCpuUsed() throws InterruptedException {
        CentralProcessor processor = OshiUtil.getHardware().getProcessor();
        long[] prevTicks = processor.getSystemCpuLoadTicks();
        // 睡眠1s
        Thread.sleep(800);
        long[] ticks = processor.getSystemCpuLoadTicks();
        long nice = ticks[CentralProcessor.TickType.NICE.getIndex()]
                - prevTicks[CentralProcessor.TickType.NICE.getIndex()];
        long irq = ticks[CentralProcessor.TickType.IRQ.getIndex()]
                - prevTicks[CentralProcessor.TickType.IRQ.getIndex()];
        long softirq = ticks[CentralProcessor.TickType.SOFTIRQ.getIndex()]
                - prevTicks[CentralProcessor.TickType.SOFTIRQ.getIndex()];
        long steal = ticks[CentralProcessor.TickType.STEAL.getIndex()]
                - prevTicks[CentralProcessor.TickType.STEAL.getIndex()];
        long cSys = ticks[CentralProcessor.TickType.SYSTEM.getIndex()]
                - prevTicks[CentralProcessor.TickType.SYSTEM.getIndex()];
        long user = ticks[CentralProcessor.TickType.USER.getIndex()]
                - prevTicks[CentralProcessor.TickType.USER.getIndex()];
        long iowait = ticks[CentralProcessor.TickType.IOWAIT.getIndex()]
                - prevTicks[CentralProcessor.TickType.IOWAIT.getIndex()];
        long idle = ticks[CentralProcessor.TickType.IDLE.getIndex()]
                - prevTicks[CentralProcessor.TickType.IDLE.getIndex()];
        long totalCpu = user + nice + cSys + idle + iowait + irq + softirq + steal;
        return new DecimalFormat("#.##%").format((cSys + user) * 1.0 / totalCpu);
    }

    public static String getCpuTem(){
        Sensors sensors = OshiUtil.getSensors();
        return String.valueOf(sensors.getCpuTemperature());
    }

}
