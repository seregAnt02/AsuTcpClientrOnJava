package duma.asu.presents;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.FileInputStream;

public class XmlParser {

    private File file;

    public XmlParser(File file) {
        this.file = file;
    }


    public File getFile() {
        return file;
    }


    public int number_file_to_delete() {
        if (getFile().getName() == Client.PACKED_VIDEO_FILES.toString() + "/dash.mpd")
            try (FileInputStream fileIS = new FileInputStream(Client.PACKED_VIDEO_FILES.toString() + "/dash.mpd")) {
                DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();//newSecureDocumentBuilderFactory();
                DocumentBuilder builder = builderFactory.newDocumentBuilder();
                Document xmlDocument = builder.parse(fileIS);
                int number = Integer.parseInt(segmentTimelineToDeleteFiles(xmlDocument));
                //System.out.println("Файл на удаление: " + number);
                builderFactory = null;
                builder = null;
                xmlDocument = null;
                return number;

            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        return 0;
    }

    private String segmentTimelineToDeleteFiles(Document doc) {
        Node first = doc.getElementsByTagName("SegmentTimeline").item(0);
        NodeList nodeList = first.getChildNodes();
        int n = nodeList.getLength();
        Node current, attr = null;
        for (int x = 0; x < n; x++) {
            current = nodeList.item(x);
            if(current.getNodeType() == Node.ELEMENT_NODE) {

                for (int j = 0; j < current.getAttributes().getLength(); j++){
                    attr = current.getAttributes().item(j);
                    if(attr.getNodeType() == Node.ATTRIBUTE_NODE && attr.getNodeName().equals("r")){
                        /*System.out.println(
                                attr.getNodeName() + ": " + attr.getTextContent());*/
                        return attr.getTextContent();
                    }
                }
            }
        }
        first = null;
        nodeList = null;
        current = null;
        attr = null;
        return null;
    }
}
