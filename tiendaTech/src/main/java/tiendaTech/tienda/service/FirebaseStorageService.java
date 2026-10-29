
package tiendaTech.tienda.service;

import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class FirebaseStorageService {

    // TODO: Cambiar por mi propia configuración de Firebase.
    // Los valores se encuentran en application.properties.

    @Value("${firebase.bucket.name}")
    private String bucketName;

    @Value("${firebase.storage.path}")
    private String storagePath;

    private final Storage storage;

    // Sube una imagen a Firebase Storage.
    public String uploadImage(MultipartFile localFile, String folder, Integer id) throws IOException {

        String originalName = localFile.getOriginalFilename();
        String fileExtension = "";

        if (originalName != null && originalName.contains(".")) {
            fileExtension = originalName.substring(originalName.lastIndexOf("."));
        }

        String fileName = "img" + getFormattedNumber(id) + fileExtension;

        File tempFile = convertToFile(localFile);

        try {
            return uploadToFirebase(tempFile, folder, fileName);

        } catch (IOException e) {
            throw e;

        } catch (RuntimeException e) {
            throw new IOException(
                    "No fue posible almacenar la imagen en Firebase.", e);

        } finally {
            if (tempFile.exists()) {
                tempFile.delete();
            }
        }
    }

    // Convierte MultipartFile a un archivo temporal.
    private File convertToFile(MultipartFile multipartFile) throws IOException {

        File tempFile = File.createTempFile("upload-", ".tmp");

        try (FileOutputStream fos = new FileOutputStream(tempFile)) {
            fos.write(multipartFile.getBytes());
        }

        return tempFile;
    }

    // Almacena la imagen y genera una URL firmada.
    private String uploadToFirebase(File file, String folder, String fileName) throws IOException {

        BlobId blobId = BlobId.of(
                bucketName,
                storagePath + "/" + folder + "/" + fileName
        );

        String mimeType = Files.probeContentType(file.toPath());

        BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
                .setContentType(mimeType != null ? mimeType : "media")
                .build();

        storage.create(
                blobInfo,
                Files.readAllBytes(file.toPath())
        );

        return storage.signUrl(
                blobInfo,
                1825,
                TimeUnit.DAYS
        ).toString();
    }

    // Formatea el identificador a 14 dígitos.
    private String getFormattedNumber(long id) {
        return String.format("%014d", id);
    }
}
