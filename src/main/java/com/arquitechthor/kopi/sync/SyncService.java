package com.arquitechthor.kopi.sync;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import jakarta.annotation.PostConstruct;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class SyncService {

    private SyncManager syncManager;
    private final JdbcTemplate jdbcTemplate;

    @Value("${S3_BUCKET_NAME:}")
    private String bucketName;

    @Value("${AWS_REGION:eu-west-1}")
    private String region;

    public SyncService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @PostConstruct
    public void init() {
        if (bucketName != null && !bucketName.isEmpty()) {
            this.syncManager = new SyncManager(bucketName, region);
        }
    }

    public boolean isConfigured() {
        return syncManager != null && syncManager.hasCredentials();
    }

    public String getBucketName() {
        return bucketName;
    }

    public void upload(String userId) throws IOException {
        if (!isConfigured()) {
            throw new IllegalStateException("AWS Credentials not configured");
        }

        String backupFileName = "kopi-tools-backup.zip";
        Path backupPath = Paths.get(backupFileName);

        try {
            jdbcTemplate.execute("BACKUP TO '" + backupFileName + "'");
            syncManager.uploadBackupFile(backupPath, userId);
        } finally {
            Files.deleteIfExists(backupPath);
        }
    }

    public void restore(String userId) throws IOException {
        if (!isConfigured()) {
            throw new IllegalStateException("AWS Credentials not configured");
        }
        syncManager.downloadAndRestore(userId);
    }
}
