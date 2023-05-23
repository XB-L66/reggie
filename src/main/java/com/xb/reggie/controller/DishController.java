package com.xb.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xb.reggie.common.R;
import com.xb.reggie.dto.DishDto;
import com.xb.reggie.entity.Category;
import com.xb.reggie.entity.Dish;
import com.xb.reggie.entity.DishFlavor;
import com.xb.reggie.exception.UpdateException;
import com.xb.reggie.service.CategoryService;
import com.xb.reggie.service.DishFlavorService;
import com.xb.reggie.service.DishService;
import com.xb.reggie.vo.DishVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/dish")
public class DishController extends BaseController{
    @Autowired
    private DishService dishService;
    @Autowired
    private DishFlavorService dishFlavorService;
    @Autowired
    private CategoryService categoryService;
    @PostMapping
    public R<String> addDish(@RequestBody DishDto dishDto){
            dishService.saveWithFlavor(dishDto);
            return R.success("添加成功");
    }
    @GetMapping("/page")
    public R<Page> getDishAllByPage(Integer page, Integer pageSize,String name){
        Page<Dish> pageInfo=new Page<>(page,pageSize);
        Page<DishVo> page1=new Page<>();
        LambdaQueryWrapper<Dish> queryWrapper=new LambdaQueryWrapper();
        queryWrapper.like(name!=null,Dish::getName,name);
        queryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);
        dishService.page(pageInfo,queryWrapper);
        BeanUtils.copyProperties(pageInfo,page1,"records");
        List<Dish> records = pageInfo.getRecords();
        List<DishVo> list=null;
        list=records.stream().map((item)->{
            DishVo dishVo=new DishVo();
            BeanUtils.copyProperties(item,dishVo);
            Long categoryId = item.getCategoryId();
            Category byId = categoryService.getById(categoryId);
            if(byId!=null){
                String name1 = byId.getName();
                dishVo.setCategoryName(name1);
            }
            return dishVo;
        }).collect(Collectors.toList());
        page1.setRecords(list);
        return R.success(page1);
    }
    @GetMapping("/{id}")
    public R<DishVo> getDishWithFlavor(@PathVariable Long id){
        DishVo withFlavor = dishService.getWithFlavor(id);
        return R.success(withFlavor);
    }
    @PutMapping
    public R<String> updateDishWithFlavor(@RequestBody DishDto dishDto){
            dishService.updateWithFlavor(dishDto);
            return R.success("菜品信息修改成功");
    }
    @DeleteMapping
    public R<String> deleteDishWithFlavor(@RequestParam List<Long> ids){
        dishService.deleteWithFlavor(ids);
        return R.success("删除成功");
    }
    @PostMapping("/status/{status}")
    public R<String> updateStatus(@RequestParam List<Long> ids,@PathVariable Integer status){
       for(Long id :ids){
           LambdaUpdateWrapper<Dish> updateWrapper=new LambdaUpdateWrapper<>();
           updateWrapper.set(Dish::getStatus,status).eq(Dish::getId,id);
           boolean update = dishService.update(updateWrapper);
           if(!update){
               throw new UpdateException("状态修改失败");
           }
       }
       return R.success("状态修改成功");
    }
    @GetMapping("/list")
    public R<List<DishVo>> getDishAllByCategoryId(Long categoryId){
        LambdaQueryWrapper<Dish> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(Dish::getCategoryId,categoryId);
        List<Dish> list = dishService.list(queryWrapper);
        List<DishVo> dishVoList=null;
         dishVoList=list.stream().map((item)->{
            DishVo dishVo=new DishVo();
            BeanUtils.copyProperties(item,dishVo);
            LambdaQueryWrapper<DishFlavor> queryWrapper1=new LambdaQueryWrapper<>();
            queryWrapper1.eq(DishFlavor::getDishId,item.getId());
            List<DishFlavor> list1 = dishFlavorService.list(queryWrapper1);
            dishVo.setFlavors(list1);
            return dishVo;
        }).collect(Collectors.toList());
        return R.success(dishVoList);
    }
}
