public class TransformationFactory {

    public static TransformationStrategy getTransformationStrategyForClient(String clientName) {

        if ("ClientA".equals(clientName)) {
            return new ClientATransformationStrategy();
        } else if ("ClientB".equals(clientName)) {
            return new ClientBTransformationStrategy();
        } else if ("ClientC".equals(clientName)) {
            return new ClientCTransformationStrategy();
        }

        throw new IllegalStateException("No transformation strategy found!");
    }
}
