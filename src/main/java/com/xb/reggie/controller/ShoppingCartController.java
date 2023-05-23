package com.xb.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xb.reggie.common.BaseContext;
import com.xb.reggie.common.R;
import com.xb.reggie.entity.ShoppingCart;
import com.xb.reggie.exception.DeleteException;
import com.xb.reggie.exception.InsertException;
import com.xb.reggie.exception.UpdateException;
import com.xb.reggie.service.ShoppingCartService;
import org.apache.ibatis.annotations.Insert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RequestMapping("/shoppingCart")
@RestController
public class ShoppingCartController {
    @Autowired
    private ShoppingCartService shoppingCartService;
    @PostMapping("/add")
    public R<String> add(@RequestBody ShoppingCart shoppingCart){
        shoppingCart.setUserId(BaseContext.getCurrentId());
        Long dishId=shoppingCart.getDishId();
        if(dishId!=null){
            LambdaQueryWrapper<ShoppingCart> queryWrapper=new LambdaQueryWrapper<>();
            queryWrapper.eq(ShoppingCart::getDishId,dishId);
            ShoppingCart one = shoppingCartService.getOne(queryWrapper);
            if(one!=null){
                one.setNumber(1 + one.getNumber());
                boolean update = shoppingCartService.updateById(one);
                if(!update){
                    throw new UpdateException("添加购物车失败");
                }
            }else{
                shoppingCart.setCreateTime(LocalDateTime.now());
                boolean save = shoppingCartService.save(shoppingCart);
                if(!save){
                    throw new InsertException("添加购物车失败");
                }
            }
        }else{
            LambdaQueryWrapper<ShoppingCart> queryWrapper=new LambdaQueryWrapper<>();
            queryWrapper.eq(ShoppingCart::getSetmealId,shoppingCart.getSetmealId());
            ShoppingCart one = shoppingCartService.getOne(queryWrapper);
            if(one!=null){
                one.setNumber(1 + one.getNumber());
                boolean update = shoppingCartService.updateById(one);
                if(!update){
                    throw new UpdateException("添加购物车失败");
                }
            }else{
                shoppingCart.setCreateTime(LocalDateTime.now());
                boolean save = shoppingCartService.save(shoppingCart);
                if(!save){
                    throw new InsertException("添加购物车失败");
                }
            }
        }
        return R.success("添加成功");
    }
    @GetMapping("/list")
    public R<List<ShoppingCart>> getCartList(){
        Long currentId = BaseContext.getCurrentId();
        LambdaQueryWrapper<ShoppingCart> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId,currentId);
        List<ShoppingCart> list = shoppingCartService.list(queryWrapper);
        return R.success(list);
    }
    @DeleteMapping("/clean")
    public R<String> cleanCartList(){
        Long currentId = BaseContext.getCurrentId();
        LambdaQueryWrapper<ShoppingCart> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId,currentId);
        boolean remove = shoppingCartService.remove(queryWrapper);
        if(!remove){
            throw new DeleteException("清空购物车失败");
        }
        return R.success("清空购物车成功");
    }
    @PostMapping("/sub")
    public R<String> subCartList(@RequestBody ShoppingCart shoppingCart){
        Long dishId=shoppingCart.getDishId();
        if(dishId!=null){
            LambdaQueryWrapper<ShoppingCart> queryWrapper=new LambdaQueryWrapper<>();
            queryWrapper.eq(ShoppingCart::getDishId,dishId);
            ShoppingCart one = shoppingCartService.getOne(queryWrapper);
            if(one.getNumber()==1){
                boolean remove = shoppingCartService.remove(queryWrapper);
                if(!remove){
                    throw new DeleteException("菜品减少失败");
                }
            }else{
                one.setNumber(one.getNumber()-1);
                shoppingCartService.updateById(one);
            }
        }else{
            LambdaQueryWrapper<ShoppingCart> queryWrapper=new LambdaQueryWrapper<>();
            queryWrapper.eq(ShoppingCart::getSetmealId,shoppingCart.getSetmealId());
            ShoppingCart one = shoppingCartService.getOne(queryWrapper);
            if(one.getNumber()==1){
                boolean remove = shoppingCartService.remove(queryWrapper);
                if(!remove){
                    throw new DeleteException("套餐减少失败");
                }
            }else{
                one.setNumber(one.getNumber()-1);
                shoppingCartService.updateById(one);
            }
        }
        return R.success("商品减少成功");
    }
}
