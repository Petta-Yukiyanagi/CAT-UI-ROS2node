import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import processing.data.JSONObject;

public class FaceIPC {

    private final Path rxDir;

    public FaceIPC(String ipcRoot) throws IOException {
        rxDir = Paths.get(ipcRoot, "broadcast");
        Files.createDirectories(rxDir);
    }

    public List<UICommand> poll() throws IOException {

        List<UICommand> result = new ArrayList<>();

        List<Path> files = Files.list(rxDir)
            .filter(p -> p.toString().endsWith(".json"))
            .sorted()
            .toList();

        for (Path p : files) {
            try {
                String text = Files.readString(p);
                Files.delete(p);

                JSONObject json = JSONObject.parse(text);
                result.add(UICommand.from(json));

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }
}
