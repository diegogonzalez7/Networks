package es.udc.redes.tutorial.info;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.SimpleDateFormat;

public class Info {
    public static void main(String[] args) {
        String relativePath = args[0];
        File file = new File(relativePath);
        Path path = file.toPath();
        try {
            BasicFileAttributes attr = Files.readAttributes(path, BasicFileAttributes.class);
            System.out.println("Size: " + attr.size() + " bytes");
            System.out.println("Last Modified: " + new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(attr.lastModifiedTime().toMillis()));
            System.out.println("Name: " + file.getName());
            System.out.println("Extension: " + getFileExtension(file));
            System.out.println("Type: " + getFileType(file));
            System.out.println("Absolute Path: " + file.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String getFileExtension(File file) {
        String name = file.getName();
        int index = name.lastIndexOf(".");
        if (index == -1) {
            return "";
        }
        return name.substring(index + 1);
    }

    private static String getFileType(File file) {
        if (file.isDirectory()) {
            return "Directory";
        }
        String extension = getFileExtension(file);
        return switch (extension) {
            case "txt", "java", "py" -> "Text";
            case "png", "jpg", "jpeg", "gif" -> "Image";
            default -> "Unknown";
        };
    }
}
