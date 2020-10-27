package ar.edu.itba.pod.g3.client.util;


import ar.edu.itba.pod.g3.api.models.Tuple;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ResultWriter {

    public static void writeTime(BufferedWriter outputFileWriter, String message) throws IOException {
        long time = new Date().getTime();
        Timestamp currentTime = new Timestamp(time);
        outputFileWriter.write(currentTime + "\tINFO [main] Client -" + "\t" + message + "\n"); //TODO: include class and line like assigment example shows
    }

    public static void writeQuery2Result(String resultFilePath, Map<String, Tuple<String, Integer>> resultMap, String city) throws IOException {
        FileWriter result2File = new FileWriter(resultFilePath);
        BufferedWriter result2Writer = new BufferedWriter(result2File);
        result2Writer.write("BARRIO;CALLE_CON_MAS_ARBOLES;ARBOLES\n");
        resultMap.entrySet().stream().sorted(new Comparator<Map.Entry<String, Tuple<String, Integer>>>() {
            @Override
            public int compare(Map.Entry<String, Tuple<String, Integer>> o1, Map.Entry<String, Tuple<String, Integer>> o2) {
                if(city.equals("BUE"))
                    return Integer.valueOf(o1.getKey()).compareTo(Integer.valueOf(o2.getKey()));
                return o1.getKey().compareTo(o2.getKey());
            }
        }).forEach((entry) -> {
            try {
                result2Writer.write(entry.getKey() + ";" + entry.getValue().getFirst() + ";" + entry.getValue().getSecond() + "\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        result2Writer.close();
    }

    public static void writeQuery4Result(Map<String, Integer> result) throws IOException{
        FileWriter result2File = new FileWriter("/tmp/data/hello.csv");
        BufferedWriter result2Writer = new BufferedWriter(result2File);
        result2Writer.write("BARIO A; BARRIO B\n");
        int size = result.size();

        List<Map.Entry<String, Integer>> sorted_results = result.entrySet().stream().sorted(new Comparator<Map.Entry<String, Integer>>() {
            @Override
            public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                return o1.getKey().compareTo(o2.getKey());
            }
        }).collect(Collectors.toList());

        IntStream.range(0, size).forEach((index -> {
            IntStream.range(index + 1 , size).forEach(index2 -> {
                try {
                    result2Writer.write(String.format("%s;%s\n",sorted_results.get(index).getKey(), sorted_results.get(index2)));
                }catch (IOException ex){
                    ex.printStackTrace();
                }
            });
        }));
        result2Writer.close();
    }
}
