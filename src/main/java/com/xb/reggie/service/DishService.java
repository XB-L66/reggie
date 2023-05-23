package com.xb.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xb.reggie.dto.DishDto;
import com.xb.reggie.entity.Dish;
import com.xb.reggie.vo.DishVo;

import java.util.List;

public interface DishService extends IService<Dish> {
    void saveWithFlavor(DishDto dishDto);
    DishVo getWithFlavor(Long id);
    void updateWithFlavor(DishDto dishDto);
    void deleteWithFlavor(List<Long> ids);
}
