import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.File;

import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientATransformationStrategy implements TransformationStrategy {


    private static final Logger LOGGER = Logger.getLogger(ClientATransformationStrategy.class.getName());

    @Override
    public void transform(String inputXmlPath, String outputXmlPath) {

        try {

            // Load input XML
            Source inputXml = new StreamSource(new File(Constants.ResourceLocation + inputXmlPath));
            // Load XSLT stylesheet
            Source xslt = new StreamSource(new File(Constants.ResourceLocation.toString() + Constants.TransformFileName.toString()));

            // Create a transformer
            TransformerFactory factory = TransformerFactory.newInstance();
            Transformer transformer = factory.newTransformer(xslt);

            //Calculate bedrooms data
            BedroomsDto dto = ComplexOperations.calculateRoomsData();
            //Form XML fragment for bedrooms data - since we use XSLT 1.0
            String xmlString = ComplexOperations.convertMapToXml(dto.getRooms());

            // Set the parameter values
            transformer.setParameter("userNameTokenId", ComplexOperations.calculateUsernameTokenId());
            transformer.setParameter("encodingType", Constants.EncodingType);
            transformer.setParameter("type", Constants.Type);
            transformer.setParameter("uniqueRoomGroups", dto.getUniqueRoomGroups());
            transformer.setParameter("numberOfChildren", dto.getNumberOfChildren());
            transformer.setParameter("roomsMap", xmlString);

            // Perform the transformation
            transformer.transform(inputXml, new StreamResult(new File(Constants.ResourceLocation + outputXmlPath)));
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error creating XML: {0}", e.getMessage());
        }
    }
}
