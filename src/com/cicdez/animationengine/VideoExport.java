package com.cicdez.animationengine;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.awt.image.VolatileImage;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Objects;

public final class VideoExport {
    public static void export(VideoIterator video, ExportSettings settings) throws Exception {
        settings.proper();
        settings.clear();
        renderFrames(video, settings);
        createVideo(settings.framesDestinationFolder.toString(), settings.videoFileOutput, settings.fps,
                settings.digitFrameIndexWrapper);
//        if (settings.removesFramesDestination) clearDirectory(settings.framesDestinationFolder);
        // TODO: 14.10.2023 Delete frames after finishing FFmpeg process (Process.waitFor() - DOESN'T WORK)
    }
    
    private static void renderFrames(VideoIterator video, ExportSettings settings) throws IOException {
        int index = -1;
        System.gc();
        
        video.setFrames(settings.frames);
        while (video.hasNext()) {
            index++;
            Frame frame = video.next();
            
            Path framePath = Paths.get(settings.framesDestinationFolder.toString(),
                    settings.formatDigitIndex(index) + ".png");
            BufferedImage image = new BufferedImage(settings.width, settings.height, BufferedImage.TYPE_INT_ARGB);
            BufferedOutputStream stream = new BufferedOutputStream(Files.newOutputStream(framePath));
            
            frame.draw(image.createGraphics());
            ImageIO.write(image, "png", stream);
            stream.close();
            
            printProgress(index, settings.frames);
        }
    }
    
    private static void printProgress(int completed, int total) {
        int length = 16;
        int space = completed * length / total + 1;
        char esc = '\u001b';
        StringBuilder spaces = new StringBuilder(), left = new StringBuilder();
        for (int index = 0; index < space; index++) {
            spaces.append("=");
        }
        for (int index = 0; index < length - space; index++) {
            left.append("-");
        }
        System.out.println("<" + esc + "[32;1m" + spaces + esc + "[0m" + left + "> (" + completed + "/" + total + ")");
    }
    
    private static void createVideo(String framesDestination, Path video, int framerate, int digits) throws IOException {
        System.out.println("Combining Frames to video...");
        String command = "ffmpeg -framerate " + framerate + " -i \"" + framesDestination + "/%0" + digits +
                "d.png\" -c:v libx264 -pix_fmt yuv420p \"" + video + "\"";
        Process process = Runtime.getRuntime().exec("cmd.exe /c " + command);
    }
    
    private static void clearDirectory(Path dir) throws IOException {
        System.out.println("Deleting Files from '" + dir + "'");
        Files.walkFileTree(dir, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                Files.delete(file);
                return FileVisitResult.CONTINUE;
            }
        });
    }
    
    public static final class ExportSettings {
        private final int frames;
        private final int width, height;
        private final int fps;
        private final Path framesDestinationFolder;
        private final Path videoFileOutput;
        private final int digitFrameIndexWrapper;
        private final boolean removesFramesDestination;
        
        public ExportSettings(int width, int height, int fps, int frames, String framesDestinationFolder,
                              String videoFileOutput, boolean removesFramesDestination) {
            this.width = width;
            this.height = height;
            this.fps = fps;
            this.frames = frames;
            this.digitFrameIndexWrapper = String.valueOf(frames).length();
            this.framesDestinationFolder = Paths.get(framesDestinationFolder);
            this.videoFileOutput = Paths.get(videoFileOutput);
            this.removesFramesDestination = removesFramesDestination;
        }
        
        private String formatDigitIndex(int index) {
            return String.format("%0" + digitFrameIndexWrapper + "d", index);
        }
        
        private void proper() {
            if (!System.getenv("Path").contains("ffmpeg"))
                throw new RuntimeException("Unable to find ffmpeg on your computer");
            
            if (frames < 1) throw new RuntimeException("Frames < 1");
            Objects.requireNonNull(framesDestinationFolder, "framesDestinationFolder is null!");
            Objects.requireNonNull(videoFileOutput, "videoFileOutput is null!");
            if (fps < 0) throw new RuntimeException("FPS < 0");
            if (width < 0) throw new RuntimeException("Width < 0");
            if (height < 0) throw new RuntimeException("Height < 0");
        }
        
        
        private void clear() throws IOException {
            System.out.println("Clearing...");
            clearDirectory(framesDestinationFolder);
            Files.deleteIfExists(videoFileOutput);
        }
    }
}
