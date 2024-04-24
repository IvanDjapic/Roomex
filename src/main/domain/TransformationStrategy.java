import javax.xml.transform.TransformerConfigurationException;

public interface TransformationStrategy{

    void transform(String inputXmlPath, String outputXmlPath);

}
