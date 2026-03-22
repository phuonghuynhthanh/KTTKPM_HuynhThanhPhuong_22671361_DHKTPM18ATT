public class AdapterApp {
    public static void main(String[] args) {

        JsonProcessor processor = new XmlToJsonAdapter(new XmlProcessor());

        String json = "{name: 'Phuong', age: 22}";
        processor.processJson(json);
    }
}

//////////////////////////////////////////////////////////

interface JsonProcessor {
    void processJson(String jsonData);
}

//////////////////////////////////////////////////////////

class XmlProcessor {
    public void processXml(String xmlData) {
        System.out.println("Processing XML: " + xmlData);
    }
}

//////////////////////////////////////////////////////////

class XmlToJsonAdapter implements JsonProcessor {

    private XmlProcessor xmlProcessor;

    public XmlToJsonAdapter(XmlProcessor xmlProcessor) {
        this.xmlProcessor = xmlProcessor;
    }

    public void processJson(String jsonData) {
        String xml = convertJsonToXml(jsonData);
        xmlProcessor.processXml(xml);
    }

    private String convertJsonToXml(String json) {
        return "<data>" + json + "</data>";
    }

    private String convertXmlToJson(String xml) {
        return xml.replace("<data>", "").replace("</data>", "");
    }
}