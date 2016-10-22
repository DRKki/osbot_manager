package file_manager;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Optional;
import java.util.Properties;

public class PropertiesFileManager {

    public static Optional<Properties> getOSBotProperties() {
        try {
            Properties osbotAccountProperties = new Properties();
            FileInputStream propertiesInputStream = new FileInputStream("osbotAccountProperties");
            osbotAccountProperties.load(propertiesInputStream);
            propertiesInputStream.close();
            return Optional.of(osbotAccountProperties);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public static void setOSBotProperties(final String osbotPath, final String username, final String password) {
        try {
            FileOutputStream out = new FileOutputStream("osbotAccountProperties");
            Properties osbotAccountProperties = new Properties();
            osbotAccountProperties.setProperty("path", osbotPath);
            osbotAccountProperties.setProperty("username", username);
            osbotAccountProperties.setProperty("password", password);
            osbotAccountProperties.store(out, "");
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
