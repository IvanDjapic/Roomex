

import javax.xml.transform.*;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {

    private static final Logger LOGGER = Logger.getLogger(Main.class.getName());


    public static void main(String[] args) {

        try {

            validateInputArguments(args);

            // Load input XML
            Source inputXml = new StreamSource(new File(Constants.ResourceLocation + args[0]));
            // Load XSLT stylesheet
            Source xslt = new StreamSource(new File(Constants.ResourceLocation + "transform.xsl"));

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
            transformer.transform(inputXml, new StreamResult(new File(Constants.ResourceLocation + args[1])));
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error creating XML: {0}", e.getMessage());
        }
    }

    private static void validateInputArguments(String... args){

        if (args == null || args.length < 2) {
            LOGGER.log(Level.SEVERE, "Could not parse input arguments!");
            throw new IllegalStateException("Not enough input arguments!");
        }
        for (String arg : args) {
            if (!arg.endsWith(".xml")) {
                LOGGER.log(Level.SEVERE, "Not a valid xml name!");
                throw new IllegalStateException("Not a valid xml name!");
            }
        }

    }
}
