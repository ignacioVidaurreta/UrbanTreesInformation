package ar.edu.itba.pod.g3.client.util;


import ar.edu.itba.pod.g3.api.models.Tuple;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Map;

public class ResultWriter {

    public static void writeTime(BufferedWriter outputFileWriter, String message) throws IOException {
        long time = new Date().getTime();
        Timestamp currentTime = new Timestamp(time);
        outputFileWriter.write(currentTime + "\tINFO [main] Client -" + "\t" + message + "\n"); //TODO: include class and line like assigment example shows
    }

    public static void writeQuery2Result(String resultFilePath, Map<String, Tuple<String, Integer>> resultMap) throws IOException {
        FileWriter result2File = new FileWriter(resultFilePath);
        BufferedWriter result2Writer = new BufferedWriter(result2File);
        result2Writer.write("BARRIO;CALLE_CON_MAS_ARBOLES;ARBOLES\n");
        resultMap.forEach((key, value) -> {
            try {
                result2Writer.write(key + ";" + value.getFirst() + ";" + value.getSecond() + "\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        result2Writer.close();
    }
}
