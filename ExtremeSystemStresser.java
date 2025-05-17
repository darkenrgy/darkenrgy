import java.io.*;
import java.net.*;
import java.nio.file.*;
import java.util.*;

public class ExtremeSystemStresser {
    private static final String STOP_KEYWORD = "darkenergy";
    private static final String FILE_NAME = "stress_test_file.txt";
    private static final String DOWNLOAD_URL = "https://speed.hetzner.de/100MB.bin"; 
    private static final String DOWNLOAD_FILE = "downloaded_file.bin";
    private static final int SHUTDOWN_TIME = 5 * 60 * 1000; 

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        new Thread(ExtremeSystemStresser::stressCPU).start();
        new Thread(ExtremeSystemStresser::stressRAM).start();
        new Thread(ExtremeSystemStresser::stressDisk).start();
        new Thread(ExtremeSystemStresser::downloadFiles).start();
        new Thread(ExtremeSystemStresser::shutdownLoop).start();

               while (true) {
            String input = scanner.nextLine();
            if (STOP_KEYWORD.equalsIgnoreCase(input.trim())) {
                System.out.println("Stopping stress test...");
                System.exit(0);
            }
        }
    }

        private static void stressCPU() {
        while (true) {
            double value = Math.random();
            for (int i = 0; i < Integer.MAX_VALUE / 100; i++) {
                value = Math.pow(value, 2) % Math.random() + Math.sin(value);
            }
        }
    }

        private static void stressRAM() {
        List<byte[]> memoryEater = new ArrayList<>();
        try {
            while (true) {
                memoryEater.add(new byte[50 * 1024 * 1024]); 
                Thread.sleep(100);
            }
        } catch (OutOfMemoryError | InterruptedException e) {
            System.out.println("RAM stress encountered an error, but continuing...");
        }
    }

        private static void stressDisk() {
        try {
            while (true) {
                Files.write(Paths.get(FILE_NAME), new byte[100 * 1024 * 1024], StandardOpenOption.CREATE, StandardOpenOption.APPEND);
                Thread.sleep(200);
            }
        } catch (IOException | InterruptedException e) {
            System.out.println("Disk stress encountered an error.");
        }
    }

        private static void downloadFiles() {
        try {
            while (true) {
                System.out.println("Downloading file...");
                URL url = new URL(DOWNLOAD_URL);
                InputStream in = url.openStream();
                Files.copy(in, Paths.get(DOWNLOAD_FILE), StandardCopyOption.REPLACE_EXISTING);
                in.close();
                System.out.println("Download completed. Restarting...");
            }
        } catch (Exception e) {
            System.out.println("Download error: No internet or file not found.");
        }
    }

        private static void shutdownLoop() {
        try {
            while (true) {
                Thread.sleep(SHUTDOWN_TIME);
                System.out.println("Shutting down system...");
                if (System.getProperty("os.name").toLowerCase().contains("win")) {
                    Runtime.getRuntime().exec("shutdown -s -t 0");
                } else {
                    Runtime.getRuntime().exec("shutdown -h now");
                }
            }
        } catch (Exception e) {
            System.out.println("Shutdown error: " + e.getMessage());
        }
    }
}