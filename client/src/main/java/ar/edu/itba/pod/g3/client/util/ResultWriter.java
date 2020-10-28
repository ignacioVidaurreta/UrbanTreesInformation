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

    public static void writeQuery2Result(String resultFilePath, Map<String, Tuple<String, Integer>> resultMap) throws IOException {
        FileWriter result2File = new FileWriter(resultFilePath);
        BufferedWriter result2Writer = new BufferedWriter(result2File);
        result2Writer.write("BARRIO;CALLE_CON_MAS_ARBOLES;ARBOLES\n");
        resultMap.entrySet().stream().sorted(new Comparator<Map.Entry<String, Tuple<String, Integer>>>() {
            @Override
            public int compare(Map.Entry<String, Tuple<String, Integer>> o1, Map.Entry<String, Tuple<String, Integer>> o2) {
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

    public static void writeQuery3Result(String resultFilePath, List<Map.Entry<String, Double>> resultList) throws IOException {
        FileWriter result2File = new FileWriter(resultFilePath);
        BufferedWriter result2Writer = new BufferedWriter(result2File);
        result2Writer.write("NOMBRE_CIENTIFICO;PROMEDIO_DIAMETRO\n");
        resultList.forEach(entry -> {
            try {
                result2Writer.write(entry.getKey() + ";" + String.format("%.2f", entry.getValue()) + "\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        result2Writer.close();
    }

    public static void writeQuery4Result(String resultFilePath, Map<String, Integer> result) throws IOException {
        FileWriter result2File = new FileWriter(resultFilePath);
        BufferedWriter result2Writer = new BufferedWriter(result2File);
        result2Writer.write("Barrio A;Barrio B\n");
        int size = result.size();

        List<Map.Entry<String, Integer>> sorted_results = result.entrySet().stream().sorted(new Comparator<Map.Entry<String, Integer>>() {
            @Override
            public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                return o1.getKey().compareTo(o2.getKey());
            }
        }).collect(Collectors.toList());

        IntStream.range(0, size).forEach((index -> {
            IntStream.range(index + 1, size).forEach(index2 -> {
                try {
                    result2Writer.write(String.format("%s;%s\n", sorted_results.get(index).getKey(), sorted_results.get(index2).getKey()));
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            });
        }));
        result2Writer.close();
    }

    public static void writeQuery5Result(String resultFilePath, Map<Integer, List<String>> resultMap) throws IOException {
        FileWriter result5File = new FileWriter(resultFilePath);
        BufferedWriter result5Writer = new BufferedWriter(result5File);
        result5Writer.write("Grupo;Barrio A;Barrio B\n");
        resultMap.entrySet().stream().sorted((entry1, entry2) -> entry2.getKey() - entry1.getKey()).forEach((entry) -> {
            try {
                List<String> neighborhoods = entry.getValue();
                if (neighborhoods.size() > 1) {
                    neighborhoods.sort(String::compareTo);
                    for (int i = 0; i < neighborhoods.size() - 1; i++) {
                        for (int j = i + 1; j < neighborhoods.size(); j++) {
                            result5Writer.write((entry.getKey() * 1000) + ";" + neighborhoods.get(i) + ";" + neighborhoods.get(j) + "\n");
                        }
                    }
                }
                result5Writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public static void writeQuery1Result(String resultFilePath, Map<String, Double> resultMap) throws IOException {
        System.out.println("writing result");
        FileWriter result1File = new FileWriter(resultFilePath);
        BufferedWriter result1Writer = new BufferedWriter(result1File);
        result1Writer.write("BARRIO;ARBOLES_POR_HABITANTE\n");
        resultMap.entrySet().stream().sorted(new Comparator<Map.Entry<String, Double>>() {
            @Override
            public int compare(Map.Entry<String, Double> e1, Map.Entry<String, Double> e2) {
                Double e1Val =  Math.floor(e1.getValue() * 100) / 100;
                Double e2Val =  Math.floor(e2.getValue() * 100) / 100;
                int cmp =  e1Val.compareTo(e2Val);
                if (cmp != 0) {
                    return -cmp;
                } else {
                    return e1.getKey().compareTo(e2.getKey());
                }
            }
        }).forEach(entry -> {
            try {
                result1Writer.write(entry.getKey() + ";" + String.format("%.2f", entry.getValue()) + "\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        result1Writer.close();
    }
}
