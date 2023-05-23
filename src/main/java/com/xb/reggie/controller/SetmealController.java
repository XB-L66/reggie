package com.xb.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xb.reggie.common.R;
import com.xb.reggie.dto.SetmealDto;
import com.xb.reggie.entity.Category;
import com.xb.reggie.entity.Dish;
import com.xb.reggie.entity.Setmeal;
import com.xb.reggie.entity.SetmealDish;
import com.xb.reggie.exception.UpdateException;
import com.xb.reggie.service.CategoryService;
import com.xb.reggie.service.DishService;
import com.xb.reggie.service.SetmealDishService;
import com.xb.reggie.service.SetmealService;
import com.xb.reggie.vo.SetmealDishVo;
import com.xb.reggie.vo.SetmealVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.logging.LogManager;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/setmeal")
public class SetmealController {
    @Autowired
    private SetmealService setmealService;
    @Autowired
    private SetmealDishService setmealDishService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private DishService dishService;
    @PostMapping
    public R<String> addSetmealWithDish(@RequestBody SetmealDto setmealDto){
        setmealService.addSetmealWithDish(setmealDto);
        return R.success("添加套餐成功");
    }
    @GetMapping("/page")
    public R<Page> getSetmealAllByPage(Integer page,Integer pageSize,String name){
        Page<Setmeal> pageInfo=new Page<>(page,pageSize);
        Page<SetmealVo> page1=new Page<>();
        LambdaQueryWrapper<Setmeal> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.like(name!=null,Setmeal::getName,name);
        setmealService.page(pageInfo,queryWrapper);
        BeanUtils.copyProperties(pageInfo,page1,"records");
        List<Setmeal> records = pageInfo.getRecords();
       List<SetmealVo> list= records.stream().map((item)->{
            SetmealVo setmealVo=new SetmealVo();
            BeanUtils.copyProperties(item,setmealVo);
            Long categoryId = item.getCategoryId();
            Category byId = categoryService.getById(categoryId);
            setmealVo.setCategoryName(byId.getName());
            return setmealVo;
        }).collect(Collectors.toList());
       page1.setRecords(list);
        return R.success(page1);
    }
    @DeleteMapping
    public R<String> deleteSetmealByIds(@RequestParam List<Long> ids){
        setmealService.deleteSetmealWithDish(ids);
        return R.success("删除成功");
    }
    @PostMapping("/status/{status}")
    public R<String> updateStatus(@RequestParam List<Long> ids,@PathVariable Integer status){
        for(Long id :ids){
            LambdaUpdateWrapper<Setmeal> updateWrapper=new LambdaUpdateWrapper<>();
            updateWrapper.set(Setmeal::getStatus,status).eq(Setmeal::getId,id);
            boolean update = setmealService.update(updateWrapper);
            if(!update){
                throw new UpdateException("状态修改失败");
            }
        }
        return R.success("状态修改成功");
    }
    @GetMapping("/{id}")
    public R<SetmealVo> getSetmealVoById(@PathVariable Long id){
        SetmealVo setmealVoById = setmealService.getSetmealVoById(id);
        return R.success(setmealVoById);
    }
    @PutMapping
    public R<String> updateSetmealWithDish(@RequestBody SetmealDto setmealDto){
        setmealService.updateSetmealWithDish(setmealDto);
        return R.success("修改成功");
    }
    @GetMapping("/list")
    public R<List<SetmealVo>> getSetmeal(Long categoryId,Integer status){
        LambdaQueryWrapper<Setmeal> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(Setmeal::getCategoryId,categoryId).eq(Setmeal::getStatus,status);
        List<Setmeal> list = setmealService.list(queryWrapper);
        List<SetmealVo> setmealVos=list.stream().map((item)->{
            SetmealVo setmealVo=new SetmealVo();
            BeanUtils.copyProperties(item,setmealVo);
            LambdaQueryWrapper<SetmealDish> queryWrapper1=new LambdaQueryWrapper<>();
            queryWrapper1.eq(SetmealDish::getSetmealId,item.getId());
            List<SetmealDish> list1 = setmealDishService.list(queryWrapper1);
            setmealVo.setSetmealDishes(list1);
            return setmealVo;
        }).collect(Collectors.toList());
        return R.success(setmealVos);
    }
    @GetMapping("/dish/{id}")
    public R<List<SetmealDishVo>> getSetmealDish(@PathVariable Long id){
        LambdaQueryWrapper<SetmealDish> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(SetmealDish::getSetmealId,id);
        List<SetmealDish> list = setmealDishService.list(queryWrapper);
        List<SetmealDishVo> list1=list.stream().map((item)->{
            SetmealDishVo setmealDishVo=new SetmealDishVo();
            BeanUtils.copyProperties(item,setmealDishVo);
            Dish byId = dishService.getById(item.getDishId());
            setmealDishVo.setImage(byId.getImage());
            return setmealDishVo;
        }).collect(Collectors.toList());
        return R.success(list1);
    }
}
