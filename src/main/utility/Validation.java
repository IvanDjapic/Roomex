public class Validation {


    private static void checkValue(String arg){
        if(arg == null || arg.isEmpty() || arg.isBlank())
            throw new IllegalArgumentException("Argument has illegal value!");
    }

    private static void checkFileSuffix(String fileName){
        if (!fileName.endsWith(".xml")) {
            throw new IllegalStateException("Not a valid xml name!");
        }
    }

    public static void validateArgsValue(String... args){

        if (args == null || args.length < 2) {
            throw new IllegalStateException("Not enough input arguments!");
        }
        for(String a : args){
            checkValue(a);
            checkFileSuffix(a);
        }
    }

}
