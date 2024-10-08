package org.devkursat.springbootfilecopytopathways.service;

import org.apache.commons.io.FileUtils;
import org.devkursat.springbootfilecopytopathways.model.CopyingWay;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class FileService {

    public String copyFile(MultipartFile multipartFile, CopyingWay copyingWay) throws IOException {
        return switch (copyingWay) {
            case IO_API -> saveFile_IoAPI(multipartFile);
            case NIO_API -> saveFile_NioAPI(multipartFile);
            case COMMONS_IO -> saveFile_CommonsIO(multipartFile);
            case GUAVA -> saveFile_Guava(multipartFile);
            case NIO_CHANNEL -> saveFile_NioChannel(multipartFile);
        };
    }

    private String saveFile_IoAPI(MultipartFile multipartFile) throws IOException {
        File copied = new File("src/main/resources/files/" + multipartFile.getOriginalFilename());
        try (
                InputStream in = new BufferedInputStream(multipartFile.getInputStream());
                OutputStream out = new BufferedOutputStream(new FileOutputStream(copied))
        ) {
            byte[] buffer = new byte[1024];
            int lengthRead;
            while ((lengthRead = in.read(buffer)) > 0) {
                out.write(buffer, 0, lengthRead);
                out.flush();
            }
        }

        return "IO ile kopyalanan dosya adı: " + copied.getName();
    }

    private String saveFile_NioAPI(MultipartFile multipartFile) throws IOException {
        Path copied = Paths.get("src/main/resources/files/" + multipartFile.getOriginalFilename());

        Path originalPath = Files.createTempFile("temp", multipartFile.getOriginalFilename());
        multipartFile.transferTo(originalPath.toFile());

        Files.copy(originalPath, copied);

        return "NIO ile kopyalanan dosya adı: " + copied.getFileName();
    }

    private String saveFile_NioChannel(MultipartFile multipartFile) throws IOException {
        FileChannel sourceChannel = null;
        FileChannel destChannel = null;
        File copied;
        try {
            File source = File.createTempFile("temp", null);
            multipartFile.transferTo(source);

            String copiedPath = "src/main/resources/files/" + multipartFile.getOriginalFilename();
            copied = new File(copiedPath);

            sourceChannel = new FileInputStream(source).getChannel();
            destChannel = new FileOutputStream(copied).getChannel();
            destChannel.transferFrom(sourceChannel, 0, sourceChannel.size());
        } finally {
            if (sourceChannel != null) sourceChannel.close();
            if (destChannel != null) destChannel.close();
        }

        return "NIO-Channel ile kopyalanan dosya adı: " + copied.getName();
    }

    private String saveFile_CommonsIO(MultipartFile multipartFile) throws IOException {
        File copied = new File("src/main/resources/files/" + multipartFile.getOriginalFilename());

        File original = File.createTempFile("temp", multipartFile.getOriginalFilename());
        multipartFile.transferTo(original);

        FileUtils.copyFile(original, copied);

        return "CommonsIO ile kopyalanan dosya adı: " + copied.getName();
    }

    private String saveFile_Guava(MultipartFile multipartFile) throws IOException {
        File copied = new File("src/main/resources/files/" + multipartFile.getOriginalFilename());

        File original = File.createTempFile("temp", multipartFile.getOriginalFilename());
        multipartFile.transferTo(original);

        Files.copy(original.toPath(), copied.toPath());

        return "Guava ile kopyalanan dosya adı: " + copied.getName();
    }
}
