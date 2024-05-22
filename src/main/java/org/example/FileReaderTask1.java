package org.example;
import java.io.*;
import java.nio.charset.StandardCharsets;

public class FileReaderTask1 implements Runnable {
    private final File file;
    private final BufferedWriter logWriter;
    private final StringBuilder[] parts;

    public FileReaderTask1(File file, BufferedWriter logWriter, StringBuilder[] parts) {
        this.file = file;
        this.logWriter = logWriter;
        this.parts = parts;
    }

    @Override
    public void run() {
        try (DataInputStream dis = new DataInputStream(new FileInputStream(file))) {
            int k = dis.readInt();
            byte[] strBytes = new byte[k];
            dis.readFully(strBytes);
            String s = new String(strBytes, StandardCharsets.UTF_8);
            int d = dis.readInt();
            int p = dis.readInt();

            int characterCount = s.length();
            synchronized (logWriter) {
                logWriter.write(String.format("Прочитали файл %s кол-во байт данных: %d, кол-во считанных символов: %d, контрольное число: %d, номер части: %d\n",
                        file.getName(), k, characterCount, d, p));
                logWriter.flush();
            }

            if (characterCount != d) {
                throw new IllegalArgumentException("Контрольное число не совпадает с количеством символов в строке.");
            }

            parts[p] = new StringBuilder(s);
        } catch (IOException | IllegalArgumentException e) {
            try {
                synchronized (logWriter) {
                    logWriter.write("Ошибка при чтении файла " + file.getName() + ": " + e.getMessage() + "\n");
                    logWriter.flush();
                }
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }
}
