package edu.icet.crm.bo;

import edu.icet.crm.bo.custom.impl.EmployeeBoImpl;
import edu.icet.crm.bo.custom.impl.ItemBoImpl;
import edu.icet.crm.bo.custom.impl.SupplierBoImpl;
import edu.icet.crm.util.BoType;

public class BoFactory {

    private static BoFactory instance;

    private BoFactory(){}

    public static BoFactory getInstance(){
        return instance != null ? instance : (instance = new BoFactory());
    }

    public <T extends SuperBo>T getBo(BoType type){
        switch (type){
            case EMPLOYEE: return (T) new EmployeeBoImpl();
            case SUPPLIER: return (T) new SupplierBoImpl();
            case ITEM: return (T) new ItemBoImpl();
        }
        return null;
    }

}
