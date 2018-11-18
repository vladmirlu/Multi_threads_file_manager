package com.multithreads.management.workers;

import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import java.util.List;
import java.util.concurrent.Future;

/**
 * Split commands.
 */
public class FileSplitter {

    public FileSplitter() {

    }

    /**
     * Splits file.
     *
     * @return list of files
     * @throws IOException if an I/O error occurs
     */
    public List<Future<File>> split(String filePath, String partFileSize, FileService fileService) throws IOException {

        File file = fileService.getOriginalFile(filePath);

        long splitFileLength = file.length() <= Long.parseLong(partFileSize) ? file.length() : Long.parseLong(partFileSize);
        long bytesLeftAmount = file.length() % splitFileLength;
        long partsQuantity = (file.length() - bytesLeftAmount) / splitFileLength;
        List<Future<File>> futures = new ArrayList<>();

        Files.createDirectory(Paths.get(file.getParent() + "/split"));
        for (long i = 0; i < partsQuantity; i++) {
            File partFile = new File(file.getParent() + "/split/" + i + "." + FilenameUtils.getExtension(file.getName()));
            Future<File> f = fileService.getWorkerFuture(file, splitFileLength, i * splitFileLength, 0, partFile);
            futures.add(f);
        }
        if (bytesLeftAmount > 0) {
            File partFile = new File(file.getParent() + "/split/" + partsQuantity + "." + FilenameUtils.getExtension(file.getName()));
            Future<File> f = fileService.getWorkerFuture(file, bytesLeftAmount, file.length() - bytesLeftAmount, 0, partFile);
            futures.add(f);
        }
        return futures;
    }
}
