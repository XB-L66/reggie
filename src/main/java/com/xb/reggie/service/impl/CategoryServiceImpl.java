package com.xb.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xb.reggie.entity.Category;
import com.xb.reggie.entity.Dish;
import com.xb.reggie.entity.Setmeal;
import com.xb.reggie.exception.CategoryRelateDishException;
import com.xb.reggie.exception.CategoryRelateSetmealException;
import com.xb.reggie.exception.DeleteException;
import com.xb.reggie.mapper.CategoryMapper;
import com.xb.reggie.service.CategoryService;
import com.xb.reggie.service.DishService;
import com.xb.reggie.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
    @Autowired
    private DishService dishService;
    @Autowired
    private SetmealService setmealService;
    @Override
    public void remove(Long id) {
        LambdaQueryWrapper<Dish> queryWrapper=new LambdaQueryWrapper();
        queryWrapper.eq(Dish::getCategoryId,id);
        long count = dishService.count(queryWrapper);
        if(count>0){
            throw new CategoryRelateDishException("该分类下包含菜品，不能删除！");
        }
        LambdaQueryWrapper<Setmeal> queryWrapper1=new LambdaQueryWrapper<>();
        queryWrapper1.eq(Setmeal::getCategoryId,id);
        long count1 = setmealService.count(queryWrapper1);
        if(count1>0){
            throw new CategoryRelateSetmealException("该分类下包含套餐，不能删除！");
        }
        boolean b = super.removeById(id);
        if(!b){
            throw new DeleteException("分类删除失败！");
        }
    }
}
