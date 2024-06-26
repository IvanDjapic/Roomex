import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {

    private static final Logger LOGGER = Logger.getLogger(Main.class.getName());


    public static void main(String[] args) {

        try {

            //Check did we get the correct input arguments
            Validation.validateArgsValue(args);

            //Pick a transformation - in out case it is Client A, but this can be,
            // for example CLIENT - SERVICE architecture  where we extract client type from the request
            // Parameter decides the instance which Factory produces
            TransformationStrategy strategy = TransformationFactory.getTransformationStrategyForClient("ClientA");

            //Set a context so that we can call the correct
            TransformationContext context = new TransformationContext();
            context.setStrategy(strategy);
            context.executeTransformation(args[0], args[1]);

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error creating XML: {0}", e.getMessage());
        }
    }
}
