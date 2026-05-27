import javax.sound.sampled.*;
import java.io.File;
import java.net.URL;

public class SoundPlayer {
    public void play(String path) {
        try {
            // prima prova dal classpath
            URL url = getClass().getResource(path);

            if (url != null) {
                AudioInputStream audio = AudioSystem.getAudioInputStream(url);
                Clip clip = AudioSystem.getClip();
                clip.open(audio);
                clip.start();
                return;
            }

            // fallback: cerca in src/sounds/ (per VS Code)
            String filePath = path.startsWith("/") ? path.substring(1) : path;
            File file = new File("src/" + filePath);

            if (!file.exists()) {
                System.err.println("[SoundPlayer] File non trovato: " + file.getPath());
                return;
            }

            AudioInputStream audio = AudioSystem.getAudioInputStream(file);
            Clip clip = AudioSystem.getClip();
            clip.open(audio);
            clip.start();

        } catch (Exception e) {
            System.err.println("[SoundPlayer] Errore: " + path);
            e.printStackTrace();
        }
    }
}
