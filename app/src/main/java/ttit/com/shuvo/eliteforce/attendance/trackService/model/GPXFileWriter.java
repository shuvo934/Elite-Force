package ttit.com.shuvo.eliteforce.attendance.trackService.model;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class GPXFileWriter {
    private static final String XML_HEADER = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>";
    private static final String TAG_GPX = "<gpx"
            + " xmlns=\"http://www.topografix.com/GPX/1/1\""
            + " version=\"1.1\""
            + " xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\""
            + " xsi:schemaLocation=\"http://www.topografix.com/GPX/1/1 http://www.topografix.com/GPX/1/1/gpx.xsd\">";
    private static final SimpleDateFormat POINT_DATE_FORMATTER = new SimpleDateFormat(
            "yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.ENGLISH);

    public static void writeGpxFile(String trackName,
                                    ArrayList<String> trkpt, File target) throws IOException {
        FileWriter fw = new FileWriter(target);
        try {
            fw.write(XML_HEADER + "\n");
            fw.write(TAG_GPX + "\n");
            for (int i = 0; i < trkpt.size(); i++) {
                fw.write(trkpt.get(i));
            }

            fw.write("</gpx>");
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void upDateGpxFile(String trackName, ArrayList<String> trkpt, File target, String previousData) throws IOException {
        FileWriter fw = new FileWriter(target);
        try {
            fw.write(previousData+"\n");
            for (int i = 0; i < trkpt.size(); i++) {
                fw.write(trkpt.get(i));
            }

            fw.write("</gpx>");
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void writeTrackPoints(String trackName, FileWriter fw,
                                         String ttrrt, String des) throws IOException {
        fw.write("\t" + "<trk>" + "\n");
        fw.write("\t\t" + "<name>" + trackName + "</name>" + "\n");
        fw.write("\t\t" + "<desc>" + "Length: " +des + "</desc>" + "\n");
        fw.write("\t\t" + "<trkseg>" + "\n");
        fw.write(ttrrt);

        fw.write("\t\t" + "</trkseg>" + "\n");
        fw.write("\t" + "</trk>" + "\n");
    }
}
