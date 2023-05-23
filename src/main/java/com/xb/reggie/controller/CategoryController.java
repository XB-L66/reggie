package com.xb.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xb.reggie.common.R;
import com.xb.reggie.entity.Category;
import com.xb.reggie.exception.CategoryDuplicatedException;
import com.xb.reggie.service.CategoryService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/category")
@RestController
public class CategoryController extends BaseController{
    @Autowired
    private CategoryService categoryService;
    @PostMapping
    public R<String> addCategory(@RequestBody Category category){
        LambdaQueryWrapper<Category> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(Category::getName,category.getName());
        Category one = categoryService.getOne(queryWrapper);
        if(one!=null){
            throw new CategoryDuplicatedException("分类名称已占用!");
        }
        categoryService.save(category);
        return R.success("分类添加成功");
    }
    @GetMapping("/page")
    public R<Page<Category>> getCategoryByPage(Integer page,Integer pageSize){
        Page<Category> pageInfo=new Page<>(page,pageSize);
        LambdaQueryWrapper<Category> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.orderByAsc(Category::getSort);
        categoryService.page(pageInfo,queryWrapper);
        return R.success(pageInfo);
    }
    @DeleteMapping
    public R<String> deleteCategoryById(Long ids){
        categoryService.remove(ids);
        return R.success("删除成功");
    }
    @PutMapping
    public R<String> updateCategory(@RequestBody Category category){
        categoryService.updateById(category);
        return R.success("修改成功!");
    }
    @GetMapping("/list")
    public R<List<Category>> getCategoryAllByType(Category category){
        LambdaQueryWrapper<Category> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(category.getType()!=null,Category::getType,category.getType());
        queryWrapper.orderByAsc(Category::getSort).orderByDesc(Category::getUpdateTime);
        List<Category> list = categoryService.list(queryWrapper);
        return R.success(list);
    }
}
