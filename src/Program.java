import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;
import lib.FeatureExtractorFixedFeaturesKnowledgeBasedMp4;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class Program {
    public static void main(String[] args) throws IOException, ImageProcessingException {
        String path = "C:\\Users\\ttzaf\\Desktop\\Research\\benign\\Plant - 24105.mp4";
        FeatureExtractorFixedFeaturesKnowledgeBasedMp4 test = new FeatureExtractorFixedFeaturesKnowledgeBasedMp4();
        FeatureExtractorFixedFeaturesKnowledgeBasedMp4.extractFeaturesFromSingleElement(path);
        printElements(path);
    }

    private static void printElements(String elementFilePath) throws IOException, ImageProcessingException {
        InputStream file = new FileInputStream(elementFilePath);
        Metadata metadata = ImageMetadataReader.readMetadata(file);

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
}
