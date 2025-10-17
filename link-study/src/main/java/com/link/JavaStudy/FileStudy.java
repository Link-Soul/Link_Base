package com.link.JavaStudy;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * @Author Link
 * @Date 2025/3/15 12:59
 */
public class FileStudy {

    public static void main(String[] args) throws IOException, UnsupportedAudioFileException {
        String filePath = "D:\\workplace\\filePath\\标准录音 13.mp3";
        try {
            File file = new File(filePath);
            FileInputStream fileInputStream = new FileInputStream(file);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(fileInputStream);
            long durationInSeconds = (long) (audioStream.getFrameLength() / audioStream.getFormat().getFrameRate());
            System.out.println("Duration: " + durationInSeconds + " seconds");
            audioStream.close();
        } catch (UnsupportedAudioFileException | IOException e) {
            e.printStackTrace();
        }
    }

}
