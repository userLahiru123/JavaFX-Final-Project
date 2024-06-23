package edu.icet.crm.util;

import edu.icet.crm.entity.EmployeeEntity;
import edu.icet.crm.entity.ItemEntity;
import edu.icet.crm.entity.OrderEntity;
import edu.icet.crm.entity.SupplierEntity;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.model.naming.ImplicitNamingStrategyJpaCompliantImpl;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class HibernateUtil {

    private static SessionFactory session = CreateSession();

    private static SessionFactory CreateSession(){
        StandardServiceRegistry build = new StandardServiceRegistryBuilder()
                .configure("hibernate.cfg.xml")
                .build();

        Metadata metaData = new MetadataSources(build)
                .addAnnotatedClass(EmployeeEntity.class)
//                .addAnnotatedClass(SupplierEntity.class)
//                .addAnnotatedClass(ItemEntity.class)
//                .addAnnotatedClass(OrderEntity.class)
                .getMetadataBuilder()
                .applyImplicitNamingStrategy(ImplicitNamingStrategyJpaCompliantImpl.INSTANCE)
                .build();

        return metaData.getSessionFactoryBuilder().build();
    }

    public static Session getSession(){
        return session.openSession();
    }

}
