package edu.icet.crm.bo.custom.impl;

import edu.icet.crm.bo.custom.SupplierBo;
import edu.icet.crm.dao.DaoFactory;
import edu.icet.crm.dao.custom.SupplierDao;
import edu.icet.crm.dto.Supplier;
import edu.icet.crm.entity.SupplierEntity;
import edu.icet.crm.util.DaoType;
import org.modelmapper.ModelMapper;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SupplierBoImpl implements SupplierBo {
    private final SupplierDao supplierDao = DaoFactory.getInstance().getDao(DaoType.SUPPLIER);

    @Override
    public boolean saveSupplier(Supplier dto) {
        return supplierDao.save(new ModelMapper().map(dto, SupplierEntity.class));
    }

    @Override
    public boolean deleteById(String supId) throws SQLException, ClassNotFoundException {
        return supplierDao.deleteById(supId);
    }

    @Override
    public ResultSet searchById(String supId) throws ClassNotFoundException, SQLException {
        return supplierDao.searchById(supId);
    }

    public boolean updateSupplier(String name, String company, String email, String supId) throws SQLException, ClassNotFoundException {
        return supplierDao.updateSupplier(name,company,email,supId);
    }
}
