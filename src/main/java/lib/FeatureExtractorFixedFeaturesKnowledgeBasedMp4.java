package lib;//package FeatureExtractors.FeatureExtractorsFixedFeatures.KnowledgeBased;

import com.axiosys.bento4.AtomList;
import com.axiosys.bento4.InvalidFormatException;
import com.drew.imaging.mp4.Mp4MetadataReader;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;
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
import java.util.*;

//import FeatureExtractors.AFeatureExtractor;
//import FeatureExtractors.FeatureExtractorsFixedFeatures.AFeatureExtractorFixedFeatures;
//import Utils._Exceptions.FeatureExtractionException;


public class FeatureExtractorFixedFeaturesKnowledgeBasedMp4 {

    private enum featuresName {
        file_size,
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
        alb_size,
        ART_size,
        cmt_size,
        day_size,
        nam_size,
        too_size,
        wrt_size,
        covr_size,
        ctts_size,
        data_size,
        desc_size,
        dinf_size,
        dref_size,
        edts_size,
        elst_size,
        free_size,
        ftyp_size,
        hdlr_size,
        ilst_size,
        iods_size,
        mdat_size,
        mdhd_size,
        mdia_size,
        meta_size,
        minf_size,
        moov_size,
        mvhd_size,
        nled_size,
        nmhd_size,
        sbgp_size,
        sdtp_size,
        sgpd_size,
        smhd_size,
        stbl_size,
        stco_size,
        stsc_size,
        stsd_size,
        stss_size,
        stsz_size,
        stts_size,
        tkhd_size,
        trak_size,
        udta_size,
        vmhd_size,
    } // list of the feature names

    public FeatureExtractorFixedFeaturesKnowledgeBasedMp4() {
    } // Empty constructor

    // Extract features from an MPEG-4 file according to the above Enum and returns a LinkedHashMap<String,String>
    public static LinkedHashMap<String, String> extractFeaturesFromSingleElement(String elementFilePath) throws IOException, InvalidFormatException {

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

        features.put("Number_Of_Urls",getURLsCount(elementFilePath));

        AtomList atoms = new AtomList(elementFilePath); // Get file structure as an Atom tree
        // Get a map of atoms names and sizes, duplicate atoms may accrue
        LinkedHashMap<String, Collection<Integer>> atomsAtomList = atoms.getAtomList();

        features.put("alb_size",getAtomSize("ֲ©alb",atomsAtomList));
        features.put("ART_size",getAtomSize("ֲ©ART",atomsAtomList));
        features.put("cmt_size",getAtomSize("ֲ©cmt",atomsAtomList));
        features.put("day_size",getAtomSize("ֲ©day",atomsAtomList));
        features.put("nam_size",getAtomSize("ֲ©nam",atomsAtomList));
        features.put("too_size",getAtomSize("ֲ©too",atomsAtomList));
        features.put("wrt_size",getAtomSize("ֲ©wrt",atomsAtomList));
        features.put("covr_size",getAtomSize("covr",atomsAtomList));
        features.put("ctts_size",getAtomSize("ctts",atomsAtomList));
        features.put("data_size",getAtomSize("data",atomsAtomList));
        features.put("desc_size",getAtomSize("desc",atomsAtomList));
        features.put("dinf_size",getAtomSize("dinf",atomsAtomList));
        features.put("dref_size",getAtomSize("dref",atomsAtomList));
        features.put("edts_size",getAtomSize("edts",atomsAtomList));
        features.put("elst_size",getAtomSize("elst",atomsAtomList));
        features.put("free_size",getAtomSize("free",atomsAtomList));
        features.put("ftyp_size",getAtomSize("ftyp",atomsAtomList));
        features.put("hdlr_size",getAtomSize("hdlr",atomsAtomList));
        features.put("ilst_size",getAtomSize("ilst",atomsAtomList));
        features.put("iods_size",getAtomSize("iods",atomsAtomList));
        features.put("mdat_size",getAtomSize("mdat",atomsAtomList));
        features.put("mdhd_size",getAtomSize("mdhd",atomsAtomList));
        features.put("mdia_size",getAtomSize("mdia",atomsAtomList));
        features.put("meta_size",getAtomSize("meta",atomsAtomList));
        features.put("minf_size",getAtomSize("minf",atomsAtomList));
        features.put("moov_size",getAtomSize("moov",atomsAtomList));
        features.put("mvhd_size",getAtomSize("mvhd",atomsAtomList));
        features.put("nled_size",getAtomSize("nled",atomsAtomList));
        features.put("nmhd_size",getAtomSize("nmhd",atomsAtomList));
        features.put("sbgp_size",getAtomSize("sbgp",atomsAtomList));
        features.put("sdtp_size",getAtomSize("sdtp",atomsAtomList));
        features.put("sgpd_size",getAtomSize("sgpd",atomsAtomList));
        features.put("smhd_size",getAtomSize("smhd",atomsAtomList));
        features.put("stbl_size",getAtomSize("stbl",atomsAtomList));
        features.put("stco_size",getAtomSize("stco",atomsAtomList));
        features.put("stsc_size",getAtomSize("stsc",atomsAtomList));
        features.put("stsd_size",getAtomSize("stsd",atomsAtomList));
        features.put("stss_size",getAtomSize("stss",atomsAtomList));
        features.put("stsz_size",getAtomSize("stsz",atomsAtomList));
        features.put("stts_size",getAtomSize("stts",atomsAtomList));
        features.put("tkhd_size",getAtomSize("tkhd",atomsAtomList));
        features.put("trak_size",getAtomSize("trak",atomsAtomList));
        features.put("udta_size",getAtomSize("udta",atomsAtomList));
        features.put("vmhd_size",getAtomSize("vmhd",atomsAtomList));


        //features.put(featuresName.Duration.toString(), someFunction());
        //Using the Mp4 Parser to extract the relevant features and add them to the Map.
        //Make sure to insert the features to the map at the same order as they are in the enum.
        //Use try-catch to protect the code

        return features;
    } // End of extractFeaturesFromSingleElement method

    //<editor-fold desc="Extractor Methods">
    private static HashMap<String, String> MetaDataExtractor(String elementFilePath) throws IOException {
        InputStream file = new FileInputStream(elementFilePath);
        Metadata metadata = Mp4MetadataReader.readMetadata(file);

        HashMap<String, String> params = new HashMap<>();

        params.put("file_size", getFileSize(elementFilePath));

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

    private static String getFileSize(String path_to_file){
        long size = new File(path_to_file).length();
        return Long.toString(size);
    }

    private static String getURLsCount(String path) throws IOException {

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
        return Integer.toString(linkArray.size());
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

    private static String getAtomSize(String key, LinkedHashMap<String, Collection<Integer>> map){
                if (map.containsKey(key)){
                    Collection<Integer> c = map.get(key);
                    return Collections.max(c).toString(); // In case of duplicate atom names, return max size
                } else return "0";
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
