
package tiendaTech.tienda;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import java.io.FileInputStream;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StorageConfig {

    // TODO: Cambiar por las credenciales de mi propio
    // proyecto Firebase cuando corresponda.

    @Value("${firebase.credentials.path:}")
    private String credentialsPath;

    @Value("${firebase.bucket.name:}")
    private String bucketName;

    @Bean
    public Storage storage() throws IOException {

        // Mientras Firebase no este configurado,
        // utilizamos las credenciales predeterminadas
        // del entorno para construir el cliente.

        if (credentialsPath == null || credentialsPath.isBlank()) {
            return StorageOptions.getDefaultInstance().getService();
        }

        GoogleCredentials credentials;

        try (FileInputStream serviceAccount =
                new FileInputStream(credentialsPath)) {

            credentials = GoogleCredentials.fromStream(serviceAccount);
        }

        if (FirebaseApp.getApps().isEmpty()) {

            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(credentials)
                    .setStorageBucket(bucketName)
                    .build();

            FirebaseApp.initializeApp(options);
        }

        return StorageOptions.newBuilder()
                .setCredentials(credentials)
                .build()
                .getService();
    }
}
