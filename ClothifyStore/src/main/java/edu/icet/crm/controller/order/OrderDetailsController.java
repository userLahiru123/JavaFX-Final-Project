package edu.icet.crm.controller.order;

import edu.icet.crm.dto.OrderList;
import edu.icet.crm.util.CrudUtil;

import java.sql.SQLException;
import java.util.List;

public class OrderDetailsController {

    private OrderDetailsController(){}

    private static OrderDetailsController instance;

    public static OrderDetailsController getInstance(){
        if(instance == null){
            return instance = new OrderDetailsController();
        }
        return instance;
    }

    public boolean addOrderDetail(List<OrderList> orderDetailList){
        boolean isAdd=false;
        for (OrderList orderDetail:orderDetailList){
            isAdd = addOrderDetail(orderDetail);
        }
        if (isAdd){
            return true;
        }
        return false;
    }
    public boolean addOrderDetail(OrderList orderDetail){

        try {
            Object isAdd = CrudUtil.execute(
                    "INSERT INTO orderlist VALUES('"+orderDetail.getOrderId()+"','"+orderDetail.getItemCode()+"','"+orderDetail.getQuantity()+"',"+Double.valueOf(orderDetail.getTotal())+" )"
            );
            System.out.println(isAdd);
            return (Boolean) isAdd;
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
