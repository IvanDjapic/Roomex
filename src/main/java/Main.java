

import javax.xml.transform.*;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.File;

public class Main {

    public static void main(String[] args) {

        String outputXmlPath = "src/main/resources/output.xml";

        try {
            // Load input XML
            Source inputXml = new StreamSource(new File("src/main/resources/input.xml"));
            // Load XSLT stylesheet
            Source xslt = new StreamSource(new File("src/main/resources/transform.xsl"));

            // Create a transformer
            TransformerFactory factory = TransformerFactory.newInstance();
            Transformer transformer = factory.newTransformer(xslt);

            BedroomsDto dto = ComplexOperations.calculateRoomsData();
            String xmlString = ComplexOperations.convertMapToXml(dto.getRooms());

            // Set the parameter value
            transformer.setParameter("userNameTokenId", ComplexOperations.calculateUsernameTokenId());
            transformer.setParameter("encodingType", Constants.EncodingType);
            transformer.setParameter("type", Constants.Type);
            transformer.setParameter("uniqueRoomGroups", dto.getUniqueRoomGroups());
            transformer.setParameter("numberOfChildren", dto.getNumberOfChildren());
            transformer.setParameter("roomsMap", xmlString);

            // Perform the transformation
            transformer.transform(inputXml, new StreamResult(new File(outputXmlPath)));
        } catch(Exception e ){
            System.out.print("Fatal error: " + e + ", " + e.getMessage());
        }
    }
}
