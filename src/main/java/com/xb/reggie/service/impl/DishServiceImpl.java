package com.xb.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xb.reggie.dto.DishDto;
import com.xb.reggie.entity.Dish;
import com.xb.reggie.entity.DishFlavor;
import com.xb.reggie.exception.*;
import com.xb.reggie.mapper.DishMapper;
import com.xb.reggie.service.DishFlavorService;
import com.xb.reggie.service.DishService;
import com.xb.reggie.vo.DishVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {
    @Autowired
    private DishFlavorService dishFlavorService;
    @Override
    public void saveWithFlavor(DishDto dishDto) {
        String name=dishDto.getName();
        LambdaQueryWrapper<Dish> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(Dish::getName,name);
        Dish one = this.getOne(queryWrapper);
        if(one!=null){
            throw new DishDuplicatedException("菜品名称已存在!");
        }
        boolean save = this.save(dishDto);
        if(!save){
            throw new InsertException("菜品添加失败");
        }
        Long dishId=dishDto.getId();
        List<DishFlavor> flavors = dishDto.getFlavors();
        flavors=flavors.stream().map((item)->{
            item.setDishId(dishId);
            return item;
        }).collect(Collectors.toList());
        boolean b = dishFlavorService.saveBatch(flavors);
        if(!b){
            throw new InsertException("菜品口味添加失败");
        }
    }

    @Override
    public DishVo getWithFlavor(Long id) {
        DishVo dishVo=new DishVo();
        Dish dish = this.getById(id);
        if(dish==null){
            throw new DishNotFoundException("菜品不存在！");
        }
        BeanUtils.copyProperties(dish,dishVo);
        LambdaQueryWrapper<DishFlavor> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId,id);
        List<DishFlavor> list = dishFlavorService.list(queryWrapper);
        dishVo.setFlavors(list);
        return dishVo;
    }

    @Override
    public void updateWithFlavor(DishDto dishDto) {
        boolean b = this.updateById(dishDto);
        if(!b){
            throw new UpdateException("菜品信息修改错误");
        }
        LambdaQueryWrapper<DishFlavor> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId,dishDto.getId());
        boolean remove = dishFlavorService.remove(queryWrapper);
        if(!remove){
            throw new DeleteException("菜品信息修改错误");
        }
        Long id=dishDto.getId();
        List<DishFlavor> flavors = dishDto.getFlavors();
        flavors=flavors.stream().map((item)->{
            item.setDishId(id);
            return item;
        }).collect(Collectors.toList());
        boolean b1 = dishFlavorService.saveBatch(flavors);
        if(!b1){
            throw new InsertException("菜品信息修改错误");
        }

    }

    @Override
    public void deleteWithFlavor(List<Long> ids) {
//        for(Long id:ids){
//            LambdaQueryWrapper<DishFlavor> queryWrapper=new LambdaQueryWrapper<>();
//            queryWrapper.eq(DishFlavor::getDishId,id);
//            boolean remove = dishFlavorService.remove(queryWrapper);
//            if(!remove){
//                throw new DeleteException("删除失败");
//            }
//        }
//        List<Long> longs = Arrays.asList(ids);
//        boolean b = this.removeBatchByIds(longs);
//        if(!b){
//            throw new DeleteException("删除失败！");
//        }
        LambdaQueryWrapper<Dish> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.in(Dish::getId,ids).eq(Dish::getStatus,1);
        long count = this.count(queryWrapper);
        if(count>0){
            throw new CustomException("该菜品除于在售状态，不能删除");
        }
        this.removeByIds(ids);
        LambdaQueryWrapper<DishFlavor> queryWrapper1=new LambdaQueryWrapper<>();
        queryWrapper1.in(DishFlavor::getDishId,ids);
        dishFlavorService.remove(queryWrapper1);
    }
}
