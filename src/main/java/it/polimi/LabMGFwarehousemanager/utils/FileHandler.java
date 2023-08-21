package it.polimi.LabMGFwarehousemanager.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class FileHandler {
    public static String addFile(String fileName, byte[] fileContent, String relativePath) throws IOException {

        String userHome = System.getProperty("user.home");

        String uploadFolder = getFullPath(relativePath);
        File uploadDir = new File(uploadFolder);
        if (!uploadDir.exists()) uploadDir.mkdirs();
        Path filePath = Paths.get(uploadFolder, fileName);
        String newFileName = fileName;

        if (Files.exists(filePath)) {
            boolean isFileEqual = areFilesEqual(filePath.toString(), fileContent);
            int i = 1;
            while (Files.exists(filePath) && !isFileEqual) {
                newFileName = fileName.replace(".", "_" + i + ".");
                filePath = Paths.get(uploadFolder, newFileName);
                if (Files.exists(filePath)) {
                    isFileEqual = areFilesEqual(filePath.toString(), fileContent);
                }
                i++;
            }
        }
        Files.write(filePath, fileContent);
        return relativePath + File.separator + newFileName;
    }

    private static boolean areFilesEqual(String filePath1, byte[] fileContent) throws IOException {
        byte[] file1Content = Files.readAllBytes(Paths.get(filePath1));
        return Arrays.equals(file1Content, fileContent);
    }

    public static String getFullPath(String relativePath){
        return System.getProperty("user.home") + File.separator + "Lab MGF Warehouse" + File.separator + relativePath;
    }

    public static void deleteFile(String path) {
        File file = new File(getFullPath(path));
        if (file.exists()) {
            file.delete();
        }
    }

    public static File getFile(String path) {
        if(Files.exists(Paths.get(getFullPath(path)))){
            return new File(getFullPath(path));
        } else {
            return null;
        }
    }

    public static File createZipFile(String zipFileName, ArrayList<String> documentsPaths) throws IOException {
        String zipFilePath = getFullPath("Temp"+ File.separator + zipFileName);
        File zipFile = new File(zipFilePath);

        File parentDir = zipFile.getParentFile();
        if (!parentDir.exists()) parentDir.mkdirs();

        try (FileOutputStream fos = new FileOutputStream(zipFile);
             ZipOutputStream zos = new ZipOutputStream(fos)) {

            byte[] buffer = new byte[4096];

            for (String documentPath : documentsPaths) {
                File file = new File(getFullPath(documentPath));
                if (file.exists()) {
                    try (FileInputStream fis = new FileInputStream(file)) {
                        zos.putNextEntry(new ZipEntry(file.getName()));

                        int bytesRead;
                        while ((bytesRead = fis.read(buffer)) != -1) {
                            zos.write(buffer, 0, bytesRead);
                        }

                        zos.closeEntry();
                    }
                }
            }
        }

        return zipFile;
    }
}
