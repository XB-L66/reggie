package com.xb.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xb.reggie.dto.DishDto;
import com.xb.reggie.dto.SetmealDto;
import com.xb.reggie.entity.Setmeal;
import com.xb.reggie.entity.SetmealDish;
import com.xb.reggie.vo.SetmealVo;

import java.util.List;

public interface SetmealService extends IService<Setmeal> {
    void addSetmealWithDish(SetmealDto setmealDto);
    void deleteSetmealWithDish(List<Long> ids);
    SetmealVo getSetmealVoById(Long id);
    void updateSetmealWithDish(SetmealDto setmealDto);
}
