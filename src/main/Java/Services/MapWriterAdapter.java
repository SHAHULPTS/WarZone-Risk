package Services;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import Models.GameState;
import Models.Map;

public class MapWriterAdapter extends MapFileWriter{
    private ConquestMapFileWriter l_conquestMapFileWriter;
    public MapWriterAdapter(ConquestMapFileWriter p_conquestMapFileWriter) {
        this.l_conquestMapFileWriter = p_conquestMapFileWriter;
    }
    public void parseMapToFile(GameState p_gameState, FileWriter l_writer, String l_mapFormat) throws IOException {
        l_conquestMapFileWriter.parseMapToFile(p_gameState, l_writer, l_mapFormat);
    }
}
