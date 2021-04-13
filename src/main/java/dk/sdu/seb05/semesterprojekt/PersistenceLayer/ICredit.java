package dk.sdu.seb05.semesterprojekt.PersistenceLayer;

public interface ICredit {

    IPerson getPerson();
    void setPerson(IPerson newPerson);

    FunctionType getFunctionType();
    void setFunctionType(FunctionType newFunction);

}
