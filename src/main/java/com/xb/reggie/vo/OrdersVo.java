package com.xb.reggie.vo;


import com.xb.reggie.entity.OrderDetail;
import com.xb.reggie.entity.Orders;
import lombok.Data;

import java.util.List;

@Data
public class OrdersVo extends Orders {


    private List<OrderDetail> orderDetails;
	
}
