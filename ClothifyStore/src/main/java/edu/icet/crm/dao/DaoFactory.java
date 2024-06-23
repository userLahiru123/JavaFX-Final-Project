package edu.icet.crm.dao;

import edu.icet.crm.dao.custom.impl.EmployeeDaoImpl;
import edu.icet.crm.dao.custom.impl.ItemDaoImpl;
import edu.icet.crm.dao.custom.impl.SupplierDaoImpl;
import edu.icet.crm.util.DaoType;

public class DaoFactory {

    private static DaoFactory instance;

    private DaoFactory(){}

    public static DaoFactory getInstance(){
        return instance != null ? instance : (instance = new DaoFactory());
    }

    public <T extends SuperDao>T getDao(DaoType type){
        switch (type){
            case EMPLOYEE: return (T)new EmployeeDaoImpl();
            case SUPPLIER: return (T)new SupplierDaoImpl();
            case ITEM: return (T)new ItemDaoImpl();
        }
        return null;
    }

}
