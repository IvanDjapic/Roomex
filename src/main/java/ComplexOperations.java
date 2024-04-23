
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import java.util.Map;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.StringWriter;


public class ComplexOperations {

    /**
     * Method generates random GUID and then transfers it to a String representation.
     *
     * @return Returns java.util.UUID as String
     */
    public static String generateRandomUUIDBase64() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }

    /**
     * Method calculates number of nights as integer value per given start and end date
     *
     * @param startDate start date as a string value
     * @param endDate   end date as a string value
     * @return Number of nights between two dates as an integer
     */
    public static int calculateNumberOfNights(String startDate, String endDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date start = dateFormat.parse(startDate);
            Date end = dateFormat.parse(endDate);
            long difference = end.getTime() - start.getTime();
            // Convert milliseconds to days
            return (int) (difference / (1000 * 60 * 60 * 24));
        } catch (ParseException e) {
            // Handle parsing exception
            e.printStackTrace();
            return -1; // Return -1 in case of error
        }
    }


    public static String base64Encode(String messageId) {

        // Custom encoder
        Base64.Encoder customEncoder = Base64.getEncoder().withoutPadding(); // You can customize padding

        // Encoding the string
        byte[] encodedBytes = customEncoder.encode(messageId.getBytes());
        return new String(encodedBytes);
    }

    public static <DateTime> String digestPassword(String nonce, DateTime created, String clearPassword) {

        try {
            // Step 1: Concatenate nonce, created, and the SHA-1 hash of clearPassword
            String concatenatedString = nonce + created.toString() + calculateSHA1(clearPassword);

            // Step 2: Calculate SHA-1 hash of the concatenated string
            String sha1Hash = calculateSHA1(concatenatedString);

            // Step 3: Encode SHA-1 hash using Base64
            return Base64.getEncoder().encodeToString(sha1Hash.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    private static String calculateSHA1(String input) {
        try {
            // Create MessageDigest instance for SHA-1
            MessageDigest sha1Digest = MessageDigest.getInstance("SHA-1");

            // Get the byte array from the input string
            byte[] inputData = input.getBytes();

            // Update digest with input byte array
            sha1Digest.update(inputData);

            // Calculate the SHA-1 digest
            byte[] sha1HashBytes = sha1Digest.digest();

            // Convert byte array to hexadecimal string
            StringBuilder stringBuilder = new StringBuilder();
            for (byte b : sha1HashBytes) {
                stringBuilder.append(String.format("%02x", b));
            }

            return stringBuilder.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getCurrentDateTimeAsString() {

        // Get the current date and time
        LocalDateTime currentDateTime = LocalDateTime.now();

        // Format the current date and time as a string
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");
        String formattedDateTime = currentDateTime.format(formatter);

        return formattedDateTime;
    }

    /**
     * @return
     */
    public static String calculateUsernameTokenId() {

        //Since logic how this is formed is not described I have left it here as a hardcoded value,
        // but it can be easily changed since it has its own function now
        return "UsernameToken-1";
    }

    /**
     * Method that parses only first 2 letters of the given string that corespond to the hotel chain code
     *
     * @param completeCode
     * @return
     */
    public static String getHotelChainCode(String completeCode) {
        return completeCode.substring(0, 2);
    }

    public static String getHotelId(String value) {
        return value.substring(3);
    }

    /**
     *
     * @param xmlSource
     * @return
     */
    public static BedroomsDto groupRoomsByAgeAndCapacityPerAge(InputStream inputStream) {

        BedroomsDto dto = new BedroomsDto();

        try {

            // Parse the input XML stream and create a Document object
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document inputDoc = builder.parse(inputStream);

            // Initialize a map to store grouped rooms for adults
            Map<String, Integer> allRoomsGrouped = new HashMap<>();

            //List of all nodes that hold rooms data for guest ("GuestCount")
            NodeList entryNodes = inputDoc.getElementsByTagName("ota:GuestCount");
            for (int i = 0; i < entryNodes.getLength(); i++) {

                Element entryElement = (Element) entryNodes.item(i);
                String ageQualifyingCode = entryElement.getAttribute("AgeQualifyingCode");
                String count = entryElement.getAttribute("Count");

                if ("8".equals(ageQualifyingCode)) {
                   dto.setNumberOfChildren(dto.getNumberOfChildren() + Integer.parseInt(count));
                } else if ("10".equals(ageQualifyingCode)) {
                    if (!allRoomsGrouped.containsKey(count)) {
                        allRoomsGrouped.put(count, 1);
                    }
                    else {
                        allRoomsGrouped.put(count, allRoomsGrouped.get(count) + 1);
                    }
                }
            }

            //Calculate unique rooms number
            dto.setUniqueRoomGroups(allRoomsGrouped.size());
            //Copy over calculated data to the DTO
            dto.setRooms(allRoomsGrouped);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return  dto;
    }

    public  static String convertMapToXml(Map<String, Integer> map) {

        StringBuilder roomList = new StringBuilder();
        roomList.append("\n");
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            roomList.append("<room numberOfRooms=\"")
                    .append(entry.getValue())
                    .append("\" numberOfAdults=\"")
                    .append(entry.getKey())
                    .append("\"/>\n");
        }

        return roomList.toString();
    }

    public static BedroomsDto calculateRoomsData() throws IOException {

        // First load input XML data to calculate room output section
        FileInputStream inputXmlStream = new FileInputStream("src/main/resources/input.xml");
        // Do the calculation
        BedroomsDto dto = ComplexOperations.groupRoomsByAgeAndCapacityPerAge(inputXmlStream);
        // Close the input stream
        inputXmlStream.close();

        return dto;
    }


    public static String convertToXml(Map<String, Integer> map, String rootElementName) {
        StringBuilder xml = new StringBuilder();
        xml.append("<").append(rootElementName).append(">");
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            xml.append("<").append(key).append(">").append(value).append("</").append(key).append(">");
        }
        xml.append("</").append(rootElementName).append(">");
        return xml.toString();
    }


    /**
     * Method forms URL value based on given host and endpoint values.
     *
     * @param host
     * @param endPoint
     * @return
     */
    public static String formURLForMessageSending(String host, String endPoint) {

        StringBuilder sb = new StringBuilder("https://");
        sb.append(host);
        sb.append("/");
        sb.append(endPoint);
        return sb.toString();
    }
}