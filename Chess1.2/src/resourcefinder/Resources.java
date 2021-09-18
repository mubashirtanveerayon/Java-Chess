package resourcefinder;

import game.Main;
import util.Constants;

import java.io.*;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.zip.*;

public class Resources {

    public static File files = new File("src");

    public static void extractArchive(String archiveFilePath, String destinationPath) throws IOException {
        Path archiveFile = FileSystems.getDefault().getPath(archiveFilePath);
        Path destPath = FileSystems.getDefault().getPath(destinationPath);

        Files.createDirectories(destPath); // create dest path folder(s)

        try (ZipFile archive = new ZipFile(archiveFile.toFile())) {

            // sort entries by name to always create folders first
            List<? extends ZipEntry> entries = archive.stream()
                    .sorted(Comparator.comparing(ZipEntry::getName))
                    .collect(Collectors.toList());

            // copy each entry in the dest path
            for (ZipEntry entry : entries) {
                Path entryDest = destPath.resolve(entry.getName());

                if (entry.isDirectory()) {
                    Files.createDirectory(entryDest);
                    continue;
                }

                Files.copy(archive.getInputStream(entry), entryDest);
            }

        }
    }

    public static boolean extractResources() throws Exception {
        String osName = System.getProperty("os.name").toLowerCase();
        String self = Main.class
                    .getProtectionDomain()
                    .getCodeSource()
                    .getLocation()
                    .toURI()
                    .getPath();
        if (osName.contains("windows")) {
            self = self.substring(1);
        }
        String dst = "src";
        if(files.exists()){
            deleteDirectory(files);
        }
        extractArchive(self, dst);
        return files.exists();
    }

    public static void deleteDirectory(File file)
    {
        // store all the paths of files and folders present
        // inside directory
        for (File subfile : file.listFiles()) {

            // if it is a subfolder,e.g Rohan and Ritik,
            // recursiley call function to empty subfolder
            if (subfile.isDirectory()) {
                deleteDirectory(subfile);
            }

            // delete files and empty subfolders
            subfile.delete();
        }
    }

    public static boolean acquirePermission(String filePath){
        String osName = System.getProperty("os.name").toLowerCase();
        if(osName.contains("linux")){
            try{
                Runtime.getRuntime().exec("chmod +x " + filePath);
            }catch(Exception ex){
                System.out.println("in permission " +ex);
                return false;
            }
        }
        return true;
    }

    public static String getEnginePath(){
        String osName = System.getProperty("os.name").toLowerCase();
        String path = "src" + File.separator + "engine" + File.separator;
        if (osName.contains("windows")) {
            path += Constants.STOCKFISH_WINDOWS;
        } else if (osName.contains("linux")) {
            path += Constants.STOCKFISH_LINUX;
        }
        return path;
    }
}
