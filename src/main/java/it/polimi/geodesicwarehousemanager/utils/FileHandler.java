package it.polimi.geodesicwarehousemanager.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

public class FileHandler {
    public static String addFile(String fileName, byte[] fileContent, String relativePath) throws IOException {

        String userHome = System.getProperty("user.home");

        String uploadFolder = userHome + File.separator + "Geodesic WareHouse" + File.separator + relativePath;
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
        return System.getProperty("user.home") + File.separator + "Geodesic WareHouse" + File.separator + relativePath;
    }

    public static File getFile(String path) {
        return new File(getFullPath(path));
    }
}
