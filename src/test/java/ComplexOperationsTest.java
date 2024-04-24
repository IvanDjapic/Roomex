import mockit.*;
import org.junit.jupiter.api.Test;
import java.io.*;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;
import mockit.Mock;
import mockit.MockUp;
import java.io.FileInputStream;
import java.io.IOException;

public class ComplexOperationsTest {
    @Test
    void generateRandomUUIDBase64_shouldGenerateUUID() {
        String uuid = ComplexOperations.generateRandomUUIDBase64();
        assertNotNull(uuid);
    }


    @Test
    void calculateNumberOfNights_shouldCalculateCorrectly() throws ParseException {
        String startDate = "2024-04-20";
        String endDate = "2024-04-25";
        int numberOfNights = ComplexOperations.calculateNumberOfNights(startDate, endDate);
        assertEquals(5, numberOfNights);
    }
    @Test
    void calculateNumberOfNights_withInvalidDates_shouldThrowException() {
        assertThrows(IllegalStateException.class, () -> {
            ComplexOperations.calculateNumberOfNights("2024-04-20", "invalidDate");
        });
    }

    @Test
    void base64Encode_shouldEncodeMessage() {
        String message = "Hello, world!";
        String encodedMessage = ComplexOperations.base64Encode(message);
        assertNotNull(encodedMessage);
    }
    @Test
    void base64Encode_withNullInput_shouldThrowException() {
        assertThrows(IllegalStateException.class, () -> {
            ComplexOperations.base64Encode(null);
        });
    }

    @Test
    void digestPassword_shouldDigestPassword() {
        String nonce = "nonce";
        String created = "2024-04-23T12:00:00Z";
        String clearPassword = "password123";
        String encodedPassword = ComplexOperations.digestPassword(nonce, created, clearPassword);
        assertNotNull(encodedPassword);
    }
    @Test
    void digestPassword_withInvalidArguments_shouldThrowException() {
        assertThrows(IllegalStateException.class, () -> {
            ComplexOperations.digestPassword("nonce", "created", null);
        });
    }

    @Test
    void calculateSHA1_shouldCalculateHash() {
        String input = "Hello, world!";
        String hash = ComplexOperations.calculateSHA1(input);
        assertNotNull(hash);
    }
    @Test
    void calculateSHA1_withNullInput_shouldThrowException() {
        assertThrows(IllegalStateException.class, () -> {
            ComplexOperations.calculateSHA1(null);
        });
    }

    @Test
    void getCurrentDateTimeAsString_shouldReturnFormattedString() {
        String dateTime = ComplexOperations.getCurrentDateTimeAsString();
        assertNotNull(dateTime);
    }

    @Test
    void calculateUsernameTokenId_shouldReturnUsernameTokenId() {
        String tokenId = ComplexOperations.calculateUsernameTokenId();
        assertEquals("UsernameToken-1", tokenId);
    }

    @Test
    void getHotelChainCode_shouldReturnFirstTwoLetters() {
        String completeCode = "AB123";
        String chainCode = ComplexOperations.getHotelChainCode(completeCode);
        assertEquals("AB", chainCode);
    }
    @Test
    void getHotelChainCode_withInvalidInput_shouldThrowException() {
        assertThrows(IndexOutOfBoundsException.class, () -> {
            ComplexOperations.getHotelChainCode("A");
        });
    }

    @Test
    void getHotelId_shouldExtractHotelId() {
        String completeCode = "AB123";
        String hotelId = ComplexOperations.getHotelId(completeCode);
        assertEquals("23", hotelId);
    }
    @Test
    void getHotelId_withInvalidInput_shouldThrowException() {
        assertThrows(IndexOutOfBoundsException.class, () -> {
            ComplexOperations.getHotelId("AB");
        });
    }

    @Test
    void groupRoomsByAgeAndCapacityPerAge_shouldGroupRooms() {
        String xml = "<root><ota:GuestCount AgeQualifyingCode=\"8\" Count=\"2\"/><ota:GuestCount AgeQualifyingCode=\"10\" Count=\"3\"/></root>";
        InputStream inputStream = new ByteArrayInputStream(xml.getBytes());
        BedroomsDto dto = ComplexOperations.groupRoomsByAgeAndCapacityPerAge(inputStream);
        assertNotNull(dto);
        assertEquals(2, dto.getNumberOfChildren());
        assertEquals(1, dto.getUniqueRoomGroups());
        Map<String, Integer> expectedRooms = new HashMap<>();
        expectedRooms.put("3", 1);
        assertEquals(expectedRooms, dto.getRooms());
    }

    @Test
    void convertMapToXml_shouldConvertMapToXmlFragment() {
        Map<String, Integer> map = new HashMap<>();
        map.put("2", 3);
        String xmlFragment = ComplexOperations.convertMapToXml(map);
        assertNotNull(xmlFragment);
    }
    @Test
    void convertMapToXml_withNullInput_shouldThrowException() {
        assertThrows(IllegalStateException.class, () -> {
            ComplexOperations.convertMapToXml(null);
        });
    }


    @Test
    void calculateRoomsData_shouldCalculateRoomsData() {
        BedroomsDto dto = ComplexOperations.calculateRoomsData();
        assertNotNull(dto);
    }


    @Test
    void formURLForMessageSending_shouldFormURL() {
        String host = "example.com";
        String endPoint = "api/v1/messages";
        String url = ComplexOperations.formURLForMessageSending(host, endPoint);
        assertNotNull(url);
        assertEquals("https://example.com/api/v1/messages", url);
    }

}
