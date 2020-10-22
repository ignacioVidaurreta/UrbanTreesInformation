package ar.edu.itba.pod.g3.client;

import ar.edu.itba.pod.g3.client.csv.BUETreeCSVReader;
import ar.edu.itba.pod.g3.client.csv.NeighbourhoodCSVReader;
import ar.edu.itba.pod.g3.client.csv.VANTreeCSVReader;
import ar.edu.itba.pod.g3.client.exceptions.InvalidPropertyException;
import ar.edu.itba.pod.g3.client.exceptions.MalformedCSVException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import static ar.edu.itba.pod.g3.client.Client.buildNeighbourhoodCSVPath;
import static ar.edu.itba.pod.g3.client.Client.buildTreesCSVPath;

public class CSVParserTest {
    Client clientBUE;
    Client clientVAN;
    @Before
    public void setUp(){
        try {
            clientBUE = new Client("BUE", Collections.singletonList("127.0.0.1"), "testData/", "testData/", 1);
            clientVAN = new Client("VAN", Collections.singletonList("127.0.0.1"), "testData/", "testData/", 1);
        }catch (InvalidPropertyException ex){
            ex.printStackTrace();
            Assert.fail();
        }
    }
    @Test
    public void parsesNeighbourhoodCorrectlyTest() throws InvalidPropertyException, IOException, MalformedCSVException {

        List<NeighbourhoodData> neighbourhoodData = new LinkedList<>();
        NeighbourhoodCSVReader.readCsv(neighbourhoodData::add, buildNeighbourhoodCSVPath(clientBUE));
        Assert.assertFalse(neighbourhoodData.isEmpty());

        neighbourhoodData = new LinkedList<>();
        NeighbourhoodCSVReader.readCsv(neighbourhoodData::add, buildNeighbourhoodCSVPath(clientVAN));
        Assert.assertFalse(neighbourhoodData.isEmpty());
    }

    @Test
    public void parsesTreeDataCorrectlyTest() throws InvalidPropertyException, IOException, MalformedCSVException {

        List<TreeData> treeDataList = new LinkedList<>();
        BUETreeCSVReader.readCsv(treeDataList::add, buildTreesCSVPath(clientBUE));
        Assert.assertFalse(treeDataList.isEmpty());

        treeDataList = new LinkedList<>();
        VANTreeCSVReader.readCsv(treeDataList::add, buildTreesCSVPath(clientVAN));
        Assert.assertFalse(treeDataList.isEmpty());
    }
}
