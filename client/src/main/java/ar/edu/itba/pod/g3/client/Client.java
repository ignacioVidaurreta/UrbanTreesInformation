package ar.edu.itba.pod.g3.client;

import ar.edu.itba.pod.g3.api.models.NeighbourhoodData;
import ar.edu.itba.pod.g3.api.models.TreeData;
import ar.edu.itba.pod.g3.api.models.Tuple;
import ar.edu.itba.pod.g3.api.query2.Query2Collator;
import ar.edu.itba.pod.g3.api.query2.Query2Mapper;
import ar.edu.itba.pod.g3.api.query2.Query2ReducerFactory;
import ar.edu.itba.pod.g3.client.csv.BUETreeCSVReader;
import ar.edu.itba.pod.g3.client.exceptions.InvalidPropertyException;
import ar.edu.itba.pod.g3.client.exceptions.MalformedCSVException;
import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.client.config.ClientNetworkConfig;
import com.hazelcast.config.GroupConfig;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.ICompletableFuture;
import com.hazelcast.core.IList;
import com.hazelcast.mapreduce.Job;
import com.hazelcast.mapreduce.JobTracker;
import com.hazelcast.mapreduce.KeyValueSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import java.util.*;
import java.util.concurrent.ExecutionException;

import static ar.edu.itba.pod.g3.client.util.PropertyParser.*;

public class Client {
    private static Logger logger = LoggerFactory.getLogger(Client.class);
    private final List<String> ipAddresses;
    private final String city;
    private final String inputDirectory;
    private final String outputDirectory;
    private final int query;

    public Client(String city, List<String> ipAddresses, String inputDirectory, String outputDirectory, int query) throws InvalidPropertyException {
        this.city = validateCity(city);
        this.ipAddresses = ipAddresses;
        this.inputDirectory = validateDirectory(inputDirectory, "inPath");
        this.outputDirectory = validateDirectory(outputDirectory, "outPath");
        this.query = query;
    }

    public static void main(String[] args) throws IOException, MalformedCSVException, ExecutionException, InterruptedException {
        logger.info("UrbanTreesInformation Client Starting ...");
        final Properties arguments = System.getProperties();

        final Optional<Client> maybeClient = parseArguments(arguments);
        if (!maybeClient.isPresent())
            return;

        final Client client = maybeClient.get();

        logger.info(String.format("Created client with City: %s and IP Addresses: %s\n Input File: %s, Output File: %s. Query=%d",
                client.getCity(),
                client.getIpAddresses().toString(),
                client.getInputDirectory(),
                client.getOutputDirectory(),
                client.getQuery()
                ));

        // Hazelcast config
        final ClientConfig clientConfig = initializeConfig(client);
        final HazelcastInstance hazelcastClient = HazelcastClient.newHazelcastClient(clientConfig);

        streetWithMoreTreesByNeighborhood(hazelcastClient, client);
    }

    private static void streetWithMoreTreesByNeighborhood(HazelcastInstance hazelcastClient, Client client) throws IOException, MalformedCSVException, ExecutionException, InterruptedException {
        final IList<TreeData> treesList = hazelcastClient.getList("g3-trees");
        final KeyValueSource<String, TreeData> source = KeyValueSource.fromList(treesList);
        final JobTracker jobTracker = hazelcastClient.getJobTracker("query-2");
        BUETreeCSVReader.readCsv(treesList::add, buildTreesCSVPath(client));

        Job<String, TreeData> job = jobTracker.newJob(source);
        Map<String, Tuple<String, Integer>> result = null;

        ICompletableFuture<Map<String, Tuple<String, Integer>>> future = job
                .mapper(new Query2Mapper())
                .reducer(new Query2ReducerFactory())
                .submit(new Query2Collator());
        result = future.get();

        System.out.println(result);
//        ResultWriter.writeResult2(resultPath, result);
    }

    private static ClientConfig initializeConfig(final Client client) {
        final ClientConfig clientConfig = new ClientConfig();
        clientConfig.setGroupConfig(new GroupConfig("g3", "n4ch170c4p0"));
        final ClientNetworkConfig networkConfig = clientConfig.getNetworkConfig();
        client.getIpAddresses().forEach(networkConfig::addAddress);
        return clientConfig;
    }

    /* package */ static String buildNeighbourhoodCSVPath(Client client) {
        return String.format( "%s/barrios%s.csv",client.getInputDirectory(), client.getCity());
    }

    /* package */ static String buildTreesCSVPath(Client client){
        return String.format( "%s/arboles%s.csv", client.getInputDirectory(), client.getCity());
    }


    public String getInputDirectory() {
        return inputDirectory;
    }

    public String getOutputDirectory() {
        return outputDirectory;
    }

    public List<String> getIpAddresses() {
        return ipAddresses;
    }

    public String getCity() {
        return city;
    }

    public int getQuery() {
        return query;
    }
}
