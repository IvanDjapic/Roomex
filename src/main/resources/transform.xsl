<!-- stylesheet.xsl -->
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:ota="http://www.opentravel.org/OTA/2003/05"
                xmlns:add="http://www.w3.org/2005/08/addressing"
                xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/"
                xmlns:oas1="http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd"
                xmlns:oas="http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd"
                xmlns:java="http://xml.apache.org/xslt/java"
                exclude-result-prefixes="java">

    <xsl:namespace-alias stylesheet-prefix="java" result-prefix="java" />

    <!-- Variables -->
    <xsl:variable name="username" select="/ota:OTA_HotelAvailRQ/ota:POS/ota:Source/ota:RequestorID/@ID" />
    <xsl:variable name="messageId" select="java:ComplexOperations.generateRandomUUIDBase64()" />
    <xsl:variable name="clearPassword" select="/ota:OTA_HotelAvailRQ/ota:POS/ota:Source/ota:RequestorID/@MessagePassword" />
    <xsl:variable name="currentDateTime" select="java:ComplexOperations.getCurrentDateTimeAsString()" />
    <xsl:variable name="action" select="/ota:OTA_HotelAvailRQ/ota:POS/ota:Source/ota:RequestorOptions/ota:RequestorOption[@Name='action']/@Value" />
    <xsl:variable name="host" select="/ota:OTA_HotelAvailRQ/ota:POS/ota:Source/ota:RequestorOptions/ota:RequestorOption[@Name='targetHost']/@Value" />
    <xsl:variable name="endPoint" select="/ota:OTA_HotelAvailRQ/ota:POS/ota:Source/ota:RequestorOptions/ota:RequestorOption[@Name='endpoint']/@Value" />
    <xsl:variable name="hotelCode" select="/ota:OTA_HotelAvailRQ/ota:AvailRequestSegments/ota:AvailRequestSegment/ota:HotelSearchCriteria/ota:Criterion/ota:HotelRef/@HotelCityCode" />
    <xsl:variable name="hotelCityCode" select="/ota:OTA_HotelAvailRQ/ota:AvailRequestSegments/ota:AvailRequestSegment/ota:HotelSearchCriteria/ota:Criterion/ota:HotelRef/@HotelCode" />

    <xsl:param name="userNameTokenId"/>
    <xsl:param name="encodingType"/>
    <xsl:param name="type"/>
    <xsl:param name="bedroomsXml"/>
    <xsl:param name="map"/>
    <xsl:param name="uniqueRoomGroups"/>
    <xsl:param name="numberOfChildren"/>

    <xsl:param name="roomsMap"/>



    <xsl:template match="ota:OTA_HotelAvailRQ">
        <soapenv:Envelope>
            <soapenv:Header>
                <add:MessageID>
                    <xsl:value-of select="$messageId"/>
                </add:MessageID>
                <add:Action>
                    <xsl:value-of select="$action"/>
                </add:Action>
                <add:To>
                    <xsl:value-of select="java:ComplexOperations.formURLForMessageSending($host, $endPoint)"/>
                </add:To>
                <oas:Security>
                    <oas:UsernameToken oas1:Id="{$userNameTokenId}">
                        <oas:Username>
                            <xsl:value-of select="$username"/>
                        </oas:Username>
                        <oas:Nonce EncodingType="{$encodingType}">
                            <xsl:value-of select="java:ComplexOperations.base64Encode($messageId)"/>
                        </oas:Nonce>
                        <oas:Password Type="{$type}">
                            <xsl:value-of select="java:ComplexOperations.digestPassword($messageId, $currentDateTime, $clearPassword)"/>
                        </oas:Password>
                        <oas1:Created>
                            <xsl:value-of select="$currentDateTime"/>
                        </oas1:Created>
                    </oas:UsernameToken>
                </oas:Security>
            </soapenv:Header>
            <soapenv:Body>
                <availabilityRequest inputCurrency="{@RequestedCurrency}" inputLanguageCode="{@PrimaryLangID}">
                    <globalInputParameters>
                        <hotelInputDetails chainCode="{java:ComplexOperations.getHotelChainCode($hotelCityCode)}" hotelId="{java:ComplexOperations.getHotelId($hotelCityCode)}" locationCode="{$hotelCode}"/>
                        <hotelStayDuration numberOfNights="{java:ComplexOperations.calculateNumberOfNights(substring(ota:AvailRequestSegments/ota:AvailRequestSegment/ota:StayDateRange/@Start, 1, 10), substring(ota:AvailRequestSegments/ota:AvailRequestSegment/ota:StayDateRange/@End, 1, 10))}"
                                           startDate="{substring(ota:AvailRequestSegments/ota:AvailRequestSegment/ota:StayDateRange/@Start, 1, 10)}"/>
                    </globalInputParameters>
                    <roomInputParameters>
                        <rooms uniqueRoomGroups="{$uniqueRoomGroups}" numberOfChildren="{$numberOfChildren}">
                            <xsl:value-of select="$roomsMap" disable-output-escaping="yes"/>
                        </rooms>
                    </roomInputParameters>
                </availabilityRequest>
            </soapenv:Body>
        </soapenv:Envelope>
    </xsl:template>


</xsl:stylesheet>
