
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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Method that holds all node generating logic
 *
 */
public class ComplexOperations {

    private static final Logger LOGGER = Logger.getLogger(ComplexOperations.class.getName());

    /**
     * Method generates random GUID and then transfers that value to a String representation.
     *
     * @return Returns java.util.UUID as String
     */
    public static String generateRandomUUIDBase64() {
        UUID uuid = UUID.randomUUID();
        String stringUUID = uuid.toString();
        LOGGER.log(Level.INFO, "Generated random UUID: {0}", stringUUID);

        return stringUUID;
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
            LOGGER.log(Level.SEVERE, "Error parsing dates: {0} - {1}", new Object[]{startDate, endDate});
            throw new IllegalStateException("You need to specify start and and dates correctly! Start: " + startDate + " End: " + endDate);
        }
    }


    /**
     * Method encodes in base64 format a given input value
     * @param messageId given input value
     * @return String value that holds base64 encoded input value
     */
    public static String base64Encode(String messageId) {

        // Custom encoder
        Base64.Encoder customEncoder = Base64.getEncoder().withoutPadding(); // You can customize padding

        // Encoding the string
        byte[] encodedBytes = customEncoder.encode(messageId.getBytes());
        String encodedMessage = new String(encodedBytes);
        LOGGER.log(Level.INFO, "Base64 encoded message: {0}", encodedMessage);

        return encodedMessage;
    }


    /**
     * Method that forms password digest based on rules specified in Input.xml
     * @param nonce first argument - marks a nonce
     * @param created second argument - marks current timestamp as a string value
     * @param clearPassword third argument - marks a clear password value from input.xml
     * @return
     */
    public static  String digestPassword(String nonce,  String created, String clearPassword) {

        try {
            // Step 1: Concatenate nonce, created, and the SHA-1 hash of clearPassword
            String concatenatedString = nonce + created + calculateSHA1(clearPassword);

            // Step 2: Calculate SHA-1 hash of the concatenated string
            String sha1Hash = calculateSHA1(concatenatedString);

            // Step 3: Encode SHA-1 hash using Base64
            String encodedPassword = Base64.getEncoder().encodeToString(sha1Hash.getBytes());
            LOGGER.log(Level.INFO, "Password digested and encoded: {0}", encodedPassword);

            return encodedPassword;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error digesting password: {0}", e.getMessage());
            throw new IllegalStateException("You need to specify all 3 arguments correctly! nonce = " + nonce + ", created = " + created + ", clearPassword = " + clearPassword);
        }
    }


    /**
     * Calculates sha1 value for a given input
     * @param input String value as an input
     * @return sha1 value as String
     */
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

            LOGGER.log(Level.INFO, "Calculated calculateSHA1");
            return stringBuilder.toString();
        } catch (NoSuchAlgorithmException e) {
            LOGGER.log(Level.SEVERE, "Error calculating SHA-1 hash: {0}, {1}", new Object[]{e.getMessage(), e});
            throw new IllegalStateException("sha1 calculation went wrong. Input = " + input);
        }
    }


    /**
     * Converts current date time in some given format to a string
     * @return Formatted string
     */
    public static String getCurrentDateTimeAsString() {

        // Get the current date and time
        LocalDateTime currentDateTime = LocalDateTime.now();

        // Format the current date and time as a string
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");
        String formattedDateTime = currentDateTime.format(formatter);
        LOGGER.log(Level.INFO, "Current date and time formatted as string: {0}", formattedDateTime);
        return currentDateTime.format(formatter);
    }


    /**
     * This method should form username token id - for now it holds only hardcoded value since the true logic is
     * unknown to me so this has a purpose to hold future implementation if needed
     * @return value as a String
     */
    public static String calculateUsernameTokenId() {

        //Since logic how this is formed is not described I have left it here as a hardcoded value,
        // but it can be easily changed since it has its own function now
        String usernameTokenId = "UsernameToken-1";
        LOGGER.log(Level.INFO, "Username token ID generated: {0}", usernameTokenId);
        return usernameTokenId;
    }


    /**
     * Method that parses only first 2 letters of the given string that correspond to the hotel chain code.
     * Assumption is that string has that value at exact position.
     *
     * @param completeCode
     * @return
     */
    public static String getHotelChainCode(String completeCode) {
        String hotelChainCode = completeCode.substring(0, 2);
        LOGGER.log(Level.INFO, "Hotel chain code extracted: {0}", hotelChainCode);
        return hotelChainCode;
    }


    /**
     * Method extracts hotel id from a given string assuming string has that value at exact position.
     * @param value
     * @return
     */
    public static String getHotelId(String value) {
        String hotelId = value.substring(3);
        LOGGER.log(Level.INFO, "Hotel ID extracted: {0}", hotelId);
        return hotelId;
    }


    /**
     * Method that holds the logic on how to extract room data from an input xml into an output xml.
     * For each step look at line comments inside the method.
     *
     * @param inputStream stream that holds a reference to the input xml
     * @return DTO that has all relevant rooms data
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

                //Extract per element data (age code and count value)
                Element entryElement = (Element) entryNodes.item(i);
                String ageQualifyingCode = entryElement.getAttribute("AgeQualifyingCode");
                String count = entryElement.getAttribute("Count");

                //If the value is '8' then we have a child room in question - only increase the counter for child rooms
                if ("8".equals(ageQualifyingCode)) {
                   dto.setNumberOfChildren(dto.getNumberOfChildren() + Integer.parseInt(count));
                }
                //If age code is = '10' we have an adult room in question.
                // Increase appropriate map position for that room size
                else if ("10".equals(ageQualifyingCode)) {
                    if (!allRoomsGrouped.containsKey(count)) {
                        allRoomsGrouped.put(count, 1);
                    }
                    else {
                        allRoomsGrouped.put(count, allRoomsGrouped.get(count) + 1);
                    }
                }
            }

            //Calculate unique room numbers
            dto.setUniqueRoomGroups(allRoomsGrouped.size());
            //Copy over calculated data (to the return DTO)
            dto.setRooms(allRoomsGrouped);

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error in groupRoomsByAgeAndCapacityPerAge");
            throw new IllegalStateException("Error in groupRoomsByAgeAndCapacityPerAge");
        }

        return  dto;
    }

    /**
     * Since we use XSLT 1.0 custom XML creation method was needed to form a fragment of output.xml document that holds
     * rooms data. This method generates XML fragment from scratch using Java and fills in the necessary data so that
     * the result of the construction can be pasted straight into the output.xml .
     * @param map structure that has all adult rooms grouped by capacity
     * @return XML document that is ready to be pasted into the output.xml
     */
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

        String xmlFragment = roomList.toString();
        LOGGER.log(Level.INFO, "Converted map to XML fragment: {0}", xmlFragment);
        return xmlFragment;
    }


    /**
     * Method takes input XML and calls another method that calculates rooms statistics
     * @return DTO that holds all relevant rooms data
     */
    public static BedroomsDto calculateRoomsData() {

        try (FileInputStream inputXmlStream = new FileInputStream("src/main/resources/input.xml")) {

            return ComplexOperations.groupRoomsByAgeAndCapacityPerAge(inputXmlStream);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error calculating rooms data: {0}", e.getMessage());
            throw new IllegalStateException("Error in calculateRoomsData(). Msg = " + e.getMessage());
        }
    }


    /**
     * Method forms URL value based on given host and endpoint values.
     *
     * @param host value for the host
     * @param endPoint value for the endpoint
     * @return String that carries a correct URL value
     */
    public static String formURLForMessageSending(String host, String endPoint) {

        StringBuilder sb = new StringBuilder("https://");
        sb.append(host);
        sb.append("/");
        sb.append(endPoint);

        String url = sb.toString();
        LOGGER.log(Level.INFO, "URL formed for message sending: {0}", url);
        return url;
    }
}