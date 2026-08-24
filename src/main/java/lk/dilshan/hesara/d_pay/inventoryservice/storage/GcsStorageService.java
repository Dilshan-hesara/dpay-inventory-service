// package lk.dilshan.hesara.d_pay.inventoryservice.storage;

// import com.google.auth.oauth2.GoogleCredentials;
// import com.google.cloud.storage.*;
// import lombok.extern.slf4j.Slf4j;
// import org.springframework.beans.factory.annotation.Value;
// import org.springframework.stereotype.Service;
// import org.springframework.web.multipart.MultipartFile;

// import java.io.FileInputStream;
// import java.io.IOException;
// import java.util.UUID;

// /**
//  * Service responsible for uploading product images to Google Cloud Storage.
//  * <p>
//  * For local development, set the following placeholders in inventory-service.yaml
//  * and the service will automatically bypass GCS and return a dummy image URL
//  * without making any network calls:
//  *   gcp.storage.bucket-name: YOUR_GCS_BUCKET_NAME
//  *   gcp.storage.credentials-path: YOUR_GCS_CREDENTIALS_JSON_PATH
//  */
// @Service
// @Slf4j
// public class GcsStorageService {

//     @Value("${gcp.storage.bucket-name}")
//     private String bucketName;

//     @Value("${gcp.storage.credentials-path}")
//     private String credentialsPath;

//     /**
//      * Uploads a multipart image file to GCS and returns the public URL.
//      * If no image is provided, or GCS credentials are not configured (local dev),
//      * returns a dummy placeholder URL immediately without making any network calls.
//      *
//      * @param file the image file to upload (may be null or empty)
//      * @return public GCS URL, or a dummy placeholder URL for local development
//      */
//     public String uploadProductImage(MultipartFile file) {
//         // No image provided — return a generic placeholder immediately
//         if (file == null || file.isEmpty()) {
//             log.warn("No image file provided. Returning placeholder URL.");
//             return "https://dummyimage.com/600x400/1a1a2e/ffffff&text=No+Image";
//         }

//         // GCS not configured (local dev) — bypass immediately, no network calls made
//         if (credentialsPath == null || credentialsPath.isBlank() || credentialsPath.contains("YOUR_GCS")) {
//             log.warn("GCS credentials not configured. Bypassing upload for local dev. File: {}", file.getOriginalFilename());
//             return "https://dummyimage.com/600x400/16213e/ffffff&text=" + file.getOriginalFilename();
//         }

//         try {
//             GoogleCredentials credentials = GoogleCredentials
//                     .fromStream(new FileInputStream(credentialsPath));

//             Storage storage = StorageOptions.newBuilder()
//                     .setCredentials(credentials)
//                     .build()
//                     .getService();

//             String objectName = "products/" + UUID.randomUUID() + "-" + file.getOriginalFilename();

//             BlobId blobId = BlobId.of(bucketName, objectName);
//             BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
//                     .setContentType(file.getContentType())
//                     .build();

//             storage.create(blobInfo, file.getBytes());

//             String publicUrl = String.format(
//                     "https://storage.googleapis.com/%s/%s", bucketName, objectName);
//             log.info("Uploaded product image to GCS: {}", publicUrl);
//             return publicUrl;

//         } catch (IOException e) {
//             log.error("GCS upload failed", e);
//             throw new RuntimeException("Failed to upload image to Google Cloud Storage", e);
//         }
//     }

//     /**
//      * Deletes an object from GCS given its full public URL.
//      * Skips deletion for placeholder/dummy URLs or when GCS is not configured.
//      */
//     public void deleteProductImage(String imageUrl) {
//         if (imageUrl == null || imageUrl.isBlank()) {
//             return;
//         }
//         if (credentialsPath == null || credentialsPath.isBlank() || credentialsPath.contains("YOUR_GCS")) {
//             log.warn("Bypassing GCS delete — credentials not configured. URL: {}", imageUrl);
//             return;
//         }
//         if (imageUrl.contains("dummyimage.com")) {
//             log.debug("Skipping GCS delete for dummy/placeholder URL: {}", imageUrl);
//             return;
//         }

//         try {
//             String prefix = String.format("https://storage.googleapis.com/%s/", bucketName);
//             if (imageUrl.startsWith(prefix)) {
//                 String objectName = imageUrl.substring(prefix.length());
//                 GoogleCredentials credentials = GoogleCredentials
//                         .fromStream(new FileInputStream(credentialsPath));
//                 Storage storage = StorageOptions.newBuilder()
//                         .setCredentials(credentials)
//                         .build()
//                         .getService();
//                 storage.delete(BlobId.of(bucketName, objectName));
//                 log.info("Deleted GCS object: {}", objectName);
//             }
//         } catch (IOException e) {
//             log.warn("GCS delete failed for URL: {}", imageUrl, e);
//         }
//     }
// }

package lk.dilshan.hesara.d_pay.inventoryservice.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@Slf4j
public class GcsStorageService {

    public String uploadProductImage(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            log.warn("No image file provided.");
            return "https://dummyimage.com/600x400/000/fff&text=No+Image";
        }
        
        log.info("Bypassing actual GCS upload. Returning dummy URL for local testing.");
        return "https://dummyimage.com/600x400/000/fff&text=" + file.getOriginalFilename();
    }

    public void deleteProductImage(String imageUrl) {
        log.info("Bypassing actual GCS delete for dummy URL: {}", imageUrl);
    }
}