package dk.sdu.seb05.semesterprojekt.PersistenceLayer;

public interface ICredit {

    int getId();

    IPerson getPerson();
    void setPerson(IPerson newPerson);

    FunctionType getFunctionType();
    void setFunctionType(FunctionType newFunction);

}
