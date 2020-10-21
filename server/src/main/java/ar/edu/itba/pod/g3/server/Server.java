package ar.edu.itba.pod.g3.server;

import com.hazelcast.config.Config;
import com.hazelcast.config.XmlConfigBuilder;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;

public class Server {
    private static Logger logger = LoggerFactory.getLogger(Server.class);

    public static void main(String[] args) {
        logger.info("UrbanTreesInformation Server Starting ...");
        try {
            startServer("hazelcast.xml");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void startServer(String configPath) throws FileNotFoundException {
        Config config = new XmlConfigBuilder(configPath).build();
        HazelcastInstance h = Hazelcast.newHazelcastInstance(config);
    }
}
