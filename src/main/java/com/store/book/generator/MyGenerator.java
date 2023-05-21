//package com.store.book.generator;
//
//import org.hibernate.HibernateException;
//import org.hibernate.engine.spi.SharedSessionContractImplementor;
//import org.apache.commons.lang3.StringUtils;
//import org.hibernate.id.IdentifierGenerator;
//
//import java.io.Serializable;
//import java.sql.Connection;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.sql.Statement;
//
//public class MyGenerator implements IdentifierGenerator {
//
//    @Override
//    public Serializable generate(
//            SharedSessionContractImplementor session, Object obj)
//            throws HibernateException {
//
//        String prefix = "KH";
//        Connection connection = session.connection();
//
//        try {
//            Statement statement=connection.createStatement();
//
//            ResultSet rs=statement.executeQuery("select count(ID_CLIENT) as Id from client");
//
//            if(rs.next())
//            {
//                int id=rs.getInt(1)+1;
//                String generatedId = prefix + StringUtils.leftPad(String.valueOf(id), 5, "0");
//                return generatedId;
//            }
//        } catch (SQLException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//
//        return null;
//    }
//}