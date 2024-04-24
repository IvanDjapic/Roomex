public class TransformationContext {

    private TransformationStrategy strategy;

    public void setStrategy(TransformationStrategy strategy) {
        this.strategy = strategy;
    }

    public void executeTransformation(String inputXmlPath, String outputXmlPath) {
        if (strategy == null) {
            throw new IllegalStateException("Transformation strategy is not set.");
        }
        strategy.transform(inputXmlPath, outputXmlPath);
    }

}
