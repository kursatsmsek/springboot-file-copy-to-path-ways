package org.devkursat.springbootfilecopytopathways.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

@Service
public class FileService {

    public String saveFile_IoAPI(MultipartFile multipartFile) throws IOException {
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

        return "Yüklenen ve kopyalanan dosya adı: " + copied.getName();
    }
}
