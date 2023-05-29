package com.xb.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xb.reggie.common.R;
import com.xb.reggie.entity.OrderDetail;
import com.xb.reggie.entity.Orders;
import com.xb.reggie.exception.UpdateException;
import com.xb.reggie.service.OrderDetailService;
import com.xb.reggie.service.OrderService;
import com.xb.reggie.vo.OrdersVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 订单
 */
@Slf4j
@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderDetailService orderDetailService;

    /**
     * 用户下单
     * @param orders
     * @return
     */
    @PostMapping("/submit")
    public R<String> submit(@RequestBody Orders orders){
        log.info("订单数据：{}",orders);
        orderService.submit(orders);
        return R.success("下单成功");
    }
    @GetMapping("/page")
    public R<Page> getOrdersByPage(Integer page, Integer pageSize, String number, LocalDateTime beginTime,LocalDateTime endTime){
        Page<Orders> pageInfo=new Page<>(page,pageSize);
        LambdaQueryWrapper<Orders> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.isNotBlank(number),Orders::getNumber,number);
        queryWrapper.between(beginTime!=null&&endTime!=null,Orders::getOrderTime,beginTime,endTime);
        queryWrapper.orderByDesc(Orders::getCheckoutTime).orderByDesc(Orders::getOrderTime);
        orderService.page(pageInfo,queryWrapper);
        return R.success(pageInfo);
    }
    @PutMapping
    public R<String> updateOrdersStatus(@RequestBody Orders orders){
        LambdaUpdateWrapper<Orders> updateWrapper=new LambdaUpdateWrapper<>();
        updateWrapper.eq(Orders::getId,orders.getId());
        updateWrapper.set(Orders::getStatus,orders.getStatus());
        boolean update = orderService.update(updateWrapper);
        if(!update){
            throw new UpdateException("状态修改失败");
        }
        return R.success("状态修改成功");
    }

    @GetMapping("/userPage")
    public R<Page> getUserOrdersByPage(Integer page, Integer pageSize){
        Page<Orders> pageInfo=new Page<>(page,pageSize);
        Page<OrdersVo> page1=new Page<>();
        BeanUtils.copyProperties(pageInfo,page1,"records");
        LambdaQueryWrapper<Orders> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(Orders::getCheckoutTime).orderByDesc(Orders::getOrderTime);
        orderService.page(pageInfo,queryWrapper);
        List<Orders> records = pageInfo.getRecords();
        List<OrdersVo> ordersVoList=records.stream().map((item)->{
            OrdersVo ordersVo=new OrdersVo();
            BeanUtils.copyProperties(item,ordersVo);
            Long id = item.getId();
            LambdaQueryWrapper<OrderDetail> queryWrapper1=new LambdaQueryWrapper<>();
            queryWrapper1.eq(OrderDetail::getOrderId,id);
            List<OrderDetail> list = orderDetailService.list(queryWrapper1);
            ordersVo.setOrderDetails(list);
            return ordersVo;
        }).collect(Collectors.toList());
        page1.setRecords(ordersVoList);
        return R.success(page1);
    }

}