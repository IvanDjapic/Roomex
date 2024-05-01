public enum Constants {

    EncodingType ("http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-soap-message-security-1.0#Base64Binary"),
    Type("http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-username-token-profile-1.0#PasswordDigest"),

    ResourceLocation("src/main/resources/"),
    TransformFileName("transform.xsl");

    private final String value;

    Constants(final String value){
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
