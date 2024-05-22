package org.example;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;

public class Main {
    private static final int filesCount = 7;
    private static final String logFIleName = "OutputLogFile.log";
    private static final String outputFileName = "OutputTxtFile.txt"; // Измените номер варианта на нужный
    private static final String fileDirect = "files"; // Директория с файлами

    public static void main(String[] args) {
        try (BufferedWriter logWriter = new BufferedWriter(new FileWriter(logFIleName))) {
            StringBuilder[] parts = new StringBuilder[filesCount];
            Thread[] threads = new Thread[filesCount];

            File dir = new File(fileDirect);

            File[] files = dir.listFiles();

            logWriter.write("Файлы в директории:\n");

            if (files.length != filesCount) {
                logWriter.write("Ошибка: Найдено неверное количество файлов. Ожидалось: " + filesCount + ", Найдено: " + files.length + "\n");
                return;
            }

            Arrays.sort(files, Comparator.comparing(File::getName));

            threads[0] = new Thread(new FileReaderTask1(files[0], logWriter, parts));
            threads[1] = new Thread(new FileReaderTask2(files[1], logWriter, parts));
            threads[2] = new Thread(new FileReaderTask3(files[2], logWriter, parts));
            threads[3] = new Thread(new FileReaderTask4(files[3], logWriter, parts));
            threads[4] = new Thread(new FileReaderTask5(files[4], logWriter, parts));
            threads[5] = new Thread(new FileReaderTask6(files[5], logWriter, parts));
            threads[6] = new Thread(new FileReaderTask7(files[6], logWriter, parts));

            for (Thread thread : threads) {
                thread.start();
            }

            for (Thread thread : threads) {
                thread.join();
            }

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFileName))) {
                for (StringBuilder part : parts) {
                    if (part != null) {
                        writer.write(part.toString());
                    }
                }
            }

            logWriter.write("Все файлы успешно прочитаны и объединены.\n");
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
