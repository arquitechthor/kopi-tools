package com.arquitechthor.kopi.sync;

import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.HeadObjectRequest;
import software.amazon.awssdk.services.s3.model.NoSuchKeyException;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.*;

import java.time.Instant;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class SyncManager {

    private static final String DATA_DIR = "data";
    // private static final String BACKUP_FILE_NAME = "kopi-tools-backup.zip"; //
    // Dynamic now
    private final S3Client s3Client;
    private final String bucketName;

    public SyncManager(String bucketName, String region) {
        this.bucketName = bucketName;
        this.s3Client = S3Client.builder()
                .region(Region.of(region))
                .credentialsProvider(DefaultCredentialsProvider.create())
                .build();
    }

    public boolean hasCredentials() {
        try {
            s3Client.serviceClientConfiguration().credentialsProvider().resolveIdentity().join();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private String getBackupFileName(String userId) {
        return "kopi-tools-backup-" + userId + ".zip";
    }

    public boolean checkForUpdate(String userId) {
        if (!hasCredentials())
            return false;

        String fileName = getBackupFileName(userId);

        try {
            // Check S3 last modified
            var headRequest = HeadObjectRequest.builder()
                    .bucket(bucketName)
                    .key(fileName)
                    .build();
            Instant s3LastModified = s3Client.headObject(headRequest).lastModified();

            // Check local last modified (of the directory or a key file)
            File localDataDir = new File(DATA_DIR);
            if (!localDataDir.exists()) {
                // If local doesn't exist but remote does, we should restore
                return true;
            }

            // Simple check: if S3 is newer than the directory modification time
            // A more robust check might be a specific metadata file, but this suffices for
            // now
            long localLastModifiedMillis = localDataDir.lastModified();
            // If the folder was modified recently, getLastModified might update
            // Let's check the database file specifically if it exists
            File dbFile = new File(DATA_DIR, "kopi.mv.db");
            if (dbFile.exists()) {
                localLastModifiedMillis = dbFile.lastModified();
            }

            return s3LastModified.toEpochMilli() > localLastModifiedMillis;

        } catch (NoSuchKeyException e) {
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void downloadAndRestore(String userId) throws IOException {
        System.out.println("Restoring backup from S3 for user: " + userId);
        String fileName = getBackupFileName(userId);
        Path zipPath = Paths.get(fileName);

        // Download
        s3Client.getObject(b -> b.bucket(bucketName).key(fileName), zipPath);

        // Unzip
        unzip(zipPath.toString(), DATA_DIR);

        // Cleanup zip
        Files.deleteIfExists(zipPath);
        System.out.println("Restore completed.");
    }

    public void uploadBackup(String userId) throws IOException {
        System.out.println("Uploading backup to S3 for user: " + userId);
        String fileName = getBackupFileName(userId);
        Path zipPath = Paths.get(fileName);

        // Zip
        zip(DATA_DIR, zipPath.toString());

        uploadBackupFile(zipPath, userId); // Delegate

        // Cleanup zip
        Files.deleteIfExists(zipPath);
        System.out.println("Upload completed.");
    }

    public void uploadBackupFile(Path zipPath, String userId) throws IOException {
        String fileName = getBackupFileName(userId);
        System.out.println("Uploading backup file to S3: " + zipPath + " as " + fileName);

        PutObjectRequest putOb = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(fileName)
                .build();

        s3Client.putObject(putOb, RequestBody.fromFile(zipPath));
    }

    private void zip(String sourceDirPath, String zipFilePath) throws IOException {
        Path p = Files.createFile(Paths.get(zipFilePath));
        try (ZipOutputStream zs = new ZipOutputStream(Files.newOutputStream(p))) {
            Path pp = Paths.get(sourceDirPath);
            if (!Files.exists(pp))
                return;

            Files.walk(pp)
                    .filter(path -> !Files.isDirectory(path))
                    .forEach(path -> {
                        ZipEntry zipEntry = new ZipEntry(pp.relativize(path).toString());
                        try {
                            zs.putNextEntry(zipEntry);
                            Files.copy(path, zs);
                            zs.closeEntry();
                        } catch (IOException e) {
                            System.err.println(e);
                        }
                    });
        }
    }

    private void unzip(String zipFilePath, String destDir) throws IOException {
        File dir = new File(destDir);
        // create output directory if it doesn't exist
        if (!dir.exists())
            dir.mkdirs();

        FileInputStream fis;
        // buffer for read and write data to file
        byte[] buffer = new byte[1024];
        try {
            fis = new FileInputStream(zipFilePath);
            ZipInputStream zis = new ZipInputStream(fis);
            ZipEntry ze = zis.getNextEntry();
            while (ze != null) {
                String fileName = ze.getName();
                File newFile = new File(destDir + File.separator + fileName);
                // create directories for sub directories in zip
                new File(newFile.getParent()).mkdirs();
                FileOutputStream fos = new FileOutputStream(newFile);
                int len;
                while ((len = zis.read(buffer)) > 0) {
                    fos.write(buffer, 0, len);
                }
                fos.close();
                // close this ZipEntry
                zis.closeEntry();
                ze = zis.getNextEntry();
            }
            // close last ZipEntry
            zis.closeEntry();
            zis.close();
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
