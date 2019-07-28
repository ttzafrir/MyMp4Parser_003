import com.drew.imaging.mp4.Mp4MetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;
import lib.FeatureExtractorFixedFeaturesKnowledgeBasedMp4;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Program {
    public static void main(String[] args) throws IOException, ImageProcessingException {
        //String path = "C:\\Users\\ttzaf\\Desktop\\Research\\benign\\Plant - 24105.mp4";
        String folderDir = "C:\\Users\\ttzaf\\Desktop\\Jupiter N Projects\\Research\\benign samples"; // Source folder
        List<String> listOfDir = getFilesDir(folderDir);

        for(String path : listOfDir) {
            FeatureExtractorFixedFeaturesKnowledgeBasedMp4 test = new FeatureExtractorFixedFeaturesKnowledgeBasedMp4();
            FeatureExtractorFixedFeaturesKnowledgeBasedMp4.extractFeaturesFromSingleElement(path);
        }

        //printElements(path);

        /*        Map<String, Integer> map = FeatureCountToMap(listOfDir);
        System.out.println(map);
        SaveMapToCsv(map,"C:\\Users\\ttzaf\\Desktop\\FeaturesCount.csv");*/
    }

    private static Map<String, Integer>  FeatureCountToMap(List<String> listOfDir) throws IOException, ImageProcessingException {
        Map<String, Integer> map = new HashMap<String, Integer>();

        assert listOfDir != null;
        for(String path : listOfDir) {
            List<String> FeatureList = getFeaturesNames(path);

            for (String k : FeatureList) {
                if (map.containsKey(k)) {
                    int v = map.get(k);
                    map.put(k, v + 1);
                } else {
                    map.put(k, 1);
                }
            }
        }
        return map;
    }

    private static void SaveMapToCsv(Map<String, Integer> map, String filename) {
        String eol = System.getProperty("line.separator");

        try (
                Writer writer = new FileWriter(filename)) {
            for (HashMap.Entry<String, Integer> entry : map.entrySet()) {
                writer.append(entry.getKey())
                        .append(',')
                        .append(entry.getValue().toString())
                        .append(eol);
            }
        } catch (IOException ex) {
            ex.printStackTrace(System.err);
        }
    }

    private static void printElements(String elementFilePath) throws IOException, ImageProcessingException {
        InputStream file = new FileInputStream(elementFilePath);
        Metadata metadata = Mp4MetadataReader.readMetadata(file);

        for (Directory directory : metadata.getDirectories()) {
            for (Tag tag : directory.getTags()) {
                System.out.format("[%s] - %s = %s",
                        directory.getName(), tag.getTagName(), tag.getDescription());
                System.out.println();
            }
            if (directory.hasErrors()) {
                for (String error : directory.getErrors()) {
                    System.err.format("ERROR: %s", error);
                }
            }
        }
    }

    private static List<String> getFeaturesNames(String elementFilePath) throws IOException, ImageProcessingException {
        List<String> FeatureList = new ArrayList<>();
        InputStream file = new FileInputStream(elementFilePath);
        Metadata metadata = Mp4MetadataReader.readMetadata(file);

        for (Directory directory : metadata.getDirectories()) {
            for (Tag tag : directory.getTags()) {
                FeatureList.add(directory.getName()+"_"+tag.getTagName());
            }
/*            if (directory.hasErrors()) {
                for (String error : directory.getErrors()) {
                    System.err.format("ERROR: %s", error);
                }
            }*/
        }
        return FeatureList;
    }

    private static List<String> getFilesDir(String path) {
        try (Stream<Path> walk = Files.walk(Paths.get(path))) {

            List<String> result = walk.filter(Files::isRegularFile)
                    .map(x -> x.toString()).collect(Collectors.toList());

            //result.forEach(System.out::println);
            return result;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }



}
