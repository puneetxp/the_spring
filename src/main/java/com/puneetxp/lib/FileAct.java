package com.puneetxp.lib;

import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.nio.file.*;

public class FileAct {
    private String dir;
    private String publicPath;
    private String baseUrl;

    public FileAct(String dir) {
        this.dir = dir;
        new File(dir).mkdirs();
    }

    public static FileAct init(String prefix) {
        return new FileAct(prefix);
    }

    public FileAct publicDir(String publicDir, String prefix) {
        this.baseUrl = prefix;
        this.publicPath = Paths.get(prefix, publicDir).toString();
        this.dir = Paths.get(this.dir, "public", publicDir).toString();
        new File(this.dir).mkdirs();
        return this;
    }

    public Object upload(MultipartFile file, String name) {
        if (file.isEmpty())
            return "Can't Upload";

        String originalName = file.getOriginalFilename();
        String extension = originalName.substring(originalName.lastIndexOf("."));
        String fileName = (name == null || name.isEmpty()) ? originalName : name + extension;

        Path targetPath = Paths.get(this.dir, fileName);

        try {
            Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);
            Map<String, String> res = new HashMap<>();
            res.put("name", originalName);
            res.put("path", targetPath.toString());
            res.put("dir", this.dir);
            res.put("public", Paths.get(this.baseUrl, fileName).toString());
            return res;
        } catch (IOException e) {
            e.printStackTrace();
            return "Upload Failed";
        }
    }
}
