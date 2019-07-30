package lib;//package FeatureExtractors.FeatureExtractorsFixedFeatures.KnowledgeBased;

import com.drew.imaging.ImageProcessingException;
import com.drew.imaging.mp4.Mp4Handler;
import com.drew.imaging.mp4.Mp4MetadataReader;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;
import com.drew.metadata.mp4.boxes.HandlerBox;
import com.google.common.base.Charsets;
import com.google.common.io.ByteSource;
import org.nibor.autolink.LinkExtractor;
import org.nibor.autolink.LinkSpan;
import org.nibor.autolink.LinkType;
import org.nibor.autolink.Span;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.LinkedHashMap;

//import FeatureExtractors.AFeatureExtractor;
//import FeatureExtractors.FeatureExtractorsFixedFeatures.AFeatureExtractorFixedFeatures;
//import Utils._Exceptions.FeatureExtractionException;


public class FeatureExtractorFixedFeaturesKnowledgeBasedMp4 {

    private enum featuresName {
        MP4_Transformation_Matrix,
        MP4_Rotation,
        MP4_Preferred_Volume,
        MP4_Preferred_Rate,
        MP4_Next_Track_ID,
        MP4_Modification_Time,
        MP4_Minor_Version,
        MP4_Media_Time_Scale,
        MP4_Major_Brand,
        MP4_Duration_in_Seconds,
        MP4_Duration,
        MP4_Creation_Time,
        MP4_Compatible_Brands,
        MP4_Video_Width,
        MP4_Video_Vertical_Resolution,
        MP4_Video_Opcolor,
        MP4_Video_Modification_Time,
        MP4_Video_ISO_639_2_Language_Code,
        MP4_Video_Horizontal_Resolution,
        MP4_Video_Height,
        MP4_Video_Graphics_Mode,
        MP4_Video_Frame_Rate,
        MP4_Video_Depth,
        MP4_Video_Creation_Time,
        MP4_Video_Compressor_Name,
        MP4_Video_Compression_Type,
        MP4_Sound_Sample_Size,
        MP4_Sound_Sample_Rate,
        MP4_Sound_Number_of_Channels,
        MP4_Sound_Modification_Time,
        MP4_Sound_ISO_639_2_Language_Code,
        MP4_Sound_Format,
        MP4_Sound_Creation_Time,
        MP4_Sound_Balance,
        Number_Of_Urls,
    } // list of the feature names

    public FeatureExtractorFixedFeaturesKnowledgeBasedMp4() {
    } // Empty constructor


    public static LinkedHashMap<String, String> extractFeaturesFromSingleElement(String elementFilePath) throws IOException {

        LinkedHashMap<String, String> features = new LinkedHashMap<>();
        HashMap<String, String> featuresHashMap = MetaDataExtractor(elementFilePath);
        //Set<String> hashKeySet = featuresHashMap.keySet();
        for (featuresName featureName : featuresName.values()) {
            //boolean containsKey = hashKeySet.contains(featureName.toString());
            boolean containsKey = featuresHashMap.containsKey(featureName.toString());
            if (containsKey) {
                String value = featuresHashMap.get(featureName.toString());
                features.put(featureName.toString(),value);
            } else {
                //System.out.println(featureName);
                features.put(featureName.toString(),null);
            }

        }

        ArrayList<String> linkArray = getURLs(elementFilePath);
        features.put("Number_Of_Urls",Integer.toString(linkArray.size()));

        //System.out.println(features.toString());




        //features.put(featuresName.Duration.toString(), someFunction());
        //Using the Mp4 Parser to extract the relevant features and add them to the Map.
        //Make sure to insert the features to the map at the same order as they are in the enum.
        //Use try-catch to protect the code

        return features;
    }

    //<editor-fold desc="Extractor Methods">
    private static HashMap<String, String> MetaDataExtractor(String elementFilePath) throws IOException {
        InputStream file = new FileInputStream(elementFilePath);
        Metadata metadata = Mp4MetadataReader.readMetadata(file);

        HashMap<String, String> params = new HashMap<>();

        for (Directory directory : metadata.getDirectories()) {
            for (Tag tag : directory.getTags()) {
                String name = (directory.getName()+"_"+tag.getTagName()).replace(" ","_").replace("-","_"); // temporary solution
                params.put(name, tag.getDescription());
            }
/*            if (directory.hasErrors()) {
                for (String error : directory.getErrors()) {
                    System.err.format("ERROR: %s", error);
                }
            }*/
        }
        return params;
    }

    private static ArrayList<String> getURLs(String path) throws IOException {

        String input = convertFileTosTRING(path);
        LinkExtractor linkExtractor = LinkExtractor.builder()
                .linkTypes(EnumSet.of(LinkType.URL, LinkType.WWW))
                .build();
        Iterable<LinkSpan> links = linkExtractor.extractLinks(input);

        ArrayList<String> linkArray = new ArrayList<>();
        for(Span link :links) {
            int start = link.getBeginIndex();
            int end = link.getEndIndex();
            String url = input.substring(start, end);
            linkArray.add(url);
        }

        return linkArray;
    }


    private static String convertFileTosTRING(String path) throws IOException {
        InputStream inputStream = new FileInputStream(new File(path));

        ByteSource byteSource = new ByteSource() {
            @Override
            public InputStream openStream() throws IOException {
                return inputStream;
            }
        };

        String text = byteSource.asCharSource(Charsets.ISO_8859_1).read();

        //System.out.println(text);
        return text;
    }


    //</editor-fold>
    //@Override
        public ArrayList<String> getFeaturesHeaders() {
        ArrayList<String> featuresHeaders = new ArrayList<>();
        for (featuresName featureName : featuresName.values()) {
            featuresHeaders.add(featureName.toString());
        }
        return featuresHeaders;
    } // Feature Headers are the features names

    //@Override
    public String getID() {
        return null;
    }

    //@Override
    public String getName() {
        return "Mp4 Knowledge-Based";
    }

    //@Override
    public String getDescription() {
        return "Extracts set of X knowledge-based features from a Mp4 image.";
    }
    /**
    @Override
    public AFeatureExtractor clone() {
        return new lib.FeatureExtractorFixedFeaturesKnowledgeBasedMp4();
    }*/
}
