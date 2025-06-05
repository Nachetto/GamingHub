package dev.nacho.ghub.domain.service.security;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.Security;
import java.security.cert.CertificateException;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class KeyStoreService {

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    @Value("${security.keyStorePassword}")
    private String keyStorePassword;

    @Value("${security.keyStorePath}")
    private String keyStorePath;

    @Value("${security.key-store-type}")
    private String keyStoreType;

    private static final Logger logger = Logger.getLogger(KeyStoreService.class.getName());

    public void saveToken(String token, String alias, char[] password) {
        try {
            KeyStore keyStore = loadKeyStore();
            SecretKey secretKey = new SecretKeySpec(token.getBytes(StandardCharsets.UTF_8), "RAW");
            keyStore.setEntry(alias, new KeyStore.SecretKeyEntry(secretKey),
                    new KeyStore.PasswordProtection(password));

            try (FileOutputStream fos = new FileOutputStream(keyStorePath)) {
                keyStore.store(fos, keyStorePassword.toCharArray());
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error saving token", e);
            throw new RuntimeException("Failed to save token", e);
        }
    }

    public String getToken(String alias, char[] password) {
        try {
            KeyStore keyStore = loadKeyStore();
            KeyStore.Entry entry = keyStore.getEntry(alias, new KeyStore.PasswordProtection(password));

            if (entry instanceof KeyStore.SecretKeyEntry) {
                return new String(((KeyStore.SecretKeyEntry) entry).getSecretKey().getEncoded(),
                        StandardCharsets.UTF_8);
            }
            throw new SecurityException("Invalid token entry type");
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error retrieving token", e);
            throw new RuntimeException("Failed to retrieve token", e);
        }
    }

    private KeyStore loadKeyStore() throws KeyStoreException, IOException,
            CertificateException, NoSuchAlgorithmException {

        KeyStore keyStore = KeyStore.getInstance(keyStoreType);
        File keystoreFile = new File(keyStorePath);

        if (!keystoreFile.exists()) {
            logger.info("Keystore not found. Creating a new keystore...");
            initializeKeyStore();  // This already saves the keystore
        }

        try (FileInputStream fis = new FileInputStream(keystoreFile)) {
            keyStore.load(fis, keyStorePassword.toCharArray());
        }

        return keyStore;
    }

    public void initializeKeyStore() {
        try {
            KeyStore keyStore = KeyStore.getInstance(keyStoreType);
            keyStore.load(null, keyStorePassword.toCharArray());

            try (FileOutputStream fos = new FileOutputStream(keyStorePath)) {
                keyStore.store(fos, keyStorePassword.toCharArray());
                logger.info("New keystore created at: " + keyStorePath);
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Keystore initialization failed", e);
            throw new RuntimeException("Failed to create keystore", e);
        }
    }
}