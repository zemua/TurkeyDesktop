package devs.mrp.turkeydesktop.service.resourcehandler;

public interface ResourceHandler<TYPE,NAME> {
    public TYPE getResource(NAME name);
}
