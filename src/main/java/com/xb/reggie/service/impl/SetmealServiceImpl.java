package com.xb.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xb.reggie.dto.SetmealDto;
import com.xb.reggie.entity.Setmeal;
import com.xb.reggie.entity.SetmealDish;
import com.xb.reggie.exception.CustomException;
import com.xb.reggie.exception.DeleteException;
import com.xb.reggie.exception.InsertException;
import com.xb.reggie.exception.UpdateException;
import com.xb.reggie.mapper.SetmealMapper;
import com.xb.reggie.service.SetmealDishService;
import com.xb.reggie.service.SetmealService;
import com.xb.reggie.vo.SetmealVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {
    @Autowired
    private SetmealDishService setmealDishService;
    @Override
    public void addSetmealWithDish(SetmealDto setmealDto) {
        boolean save = this.save(setmealDto);
        if(!save){
            throw new InsertException("添加套餐失败");
        }
        Long id=setmealDto.getId();
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        setmealDishes=setmealDishes.stream().map((item)->{
            item.setSetmealId(id);
            return item;
        }).collect(Collectors.toList());
        setmealDishService.saveBatch(setmealDishes);
    }

    @Override
    public void deleteSetmealWithDish(List<Long> ids) {
       LambdaQueryWrapper<Setmeal> queryWrapper=new LambdaQueryWrapper<>();
       queryWrapper.in(Setmeal::getId,ids).eq(Setmeal::getStatus,1);
        long count = this.count(queryWrapper);
        if(count>0){
            throw new CustomException("套餐处于在售状态，无法删除");
        }
        this.removeByIds(ids);
        LambdaQueryWrapper<SetmealDish> queryWrapper1=new LambdaQueryWrapper<>();
        queryWrapper1.in(SetmealDish::getSetmealId,ids);
        setmealDishService.remove(queryWrapper1);
    }

    @Override
    public SetmealVo getSetmealVoById(Long id) {
        SetmealVo setmealVo=new SetmealVo();
        Setmeal byId = this.getById(id);
        BeanUtils.copyProperties(byId,setmealVo);
        LambdaQueryWrapper<SetmealDish> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(SetmealDish::getSetmealId,id);
        List<SetmealDish> list = setmealDishService.list(queryWrapper);
        setmealVo.setSetmealDishes(list);
        return setmealVo;
    }
    @Override
    public void updateSetmealWithDish(SetmealDto setmealDto) {
        boolean b = this.updateById(setmealDto);
        if(!b){
            throw new UpdateException("修改失败");
        }
        Long id=setmealDto.getId();
        LambdaQueryWrapper<SetmealDish> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(SetmealDish::getSetmealId,id);
        boolean b1 = setmealDishService.remove(queryWrapper);
        if(!b1){
            throw new DeleteException("修改失败!!!");
        }
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        setmealDishes=setmealDishes.stream().map((item)->{
            item.setSetmealId(id);
            return item;
        }).collect(Collectors.toList());
        setmealDishService.saveBatch(setmealDishes);
    }
}
