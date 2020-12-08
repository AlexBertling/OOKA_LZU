package hbrs.ooka.log;

public class LoggerFactory {

    public static Logger createLogger(){
        return new ConcreteLogger();
    }

}
