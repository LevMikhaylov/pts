package com.example.Logging;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.charset.StandardCharsets;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.StandardOpenOption;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class PostRateLogger {
    private static final String LOG_FILE_PATH = "C:/Users/Student/Downloads/demo (3)/src/main/java/com/example/orders/Logging/logs.txt";

    @Pointcut("execution (* com.example.demo.Controllers.RateController.addRate(..)) && args(rate,..)")
    public void callAddRate() {}

    @Before("callAddRate()")
    public void makeFile() throws Exception {
        Path path = Paths.get(LOG_FILE_PATH);
        if (!Files.exists(path)) {
            Files.createFile(path);
        }
    }

    @After("callAddRate()")
    public void saveLogToFile() throws Exception {
        String logMessage = "Rate added successfully!\n";
        byte[] bytes = logMessage.getBytes(StandardCharsets.UTF_8);
        
        try (FileChannel fileChannel = FileChannel.open(Paths.get(LOG_FILE_PATH), StandardOpenOption.WRITE, StandardOpenOption.APPEND)) {
            ByteBuffer buffer = ByteBuffer.wrap(bytes);
            fileChannel.write(buffer);
        }
    }
}
