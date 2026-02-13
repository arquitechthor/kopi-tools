package com.arquitechthor.kopi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class KopiToolsApplication {

	public static void main(String[] args) {
		// Pre-boot Synchronization Check
		String bucket = System.getenv("S3_BUCKET_NAME");
		String region = System.getenv("AWS_REGION");
		if (region == null || region.isEmpty()) {
			region = "eu-west-1"; // Default region
		}

		if (bucket != null && !bucket.isEmpty()) {
			try {
				com.arquitechthor.kopi.sync.SyncManager syncManager = new com.arquitechthor.kopi.sync.SyncManager(
						bucket, region);
				if (syncManager.checkForUpdate()) {
					System.out.println("Newer backup found in S3. Restoring...");
					syncManager.downloadAndRestore();
				}
			} catch (Exception e) {
				System.err.println("Failed to sync from S3 during startup: " + e.getMessage());
				// Continue startup
			}
		}

		SpringApplication.run(KopiToolsApplication.class, args);
	}

}
