package com.xb.reggie.vo;

import com.xb.reggie.entity.Setmeal;
import com.xb.reggie.entity.SetmealDish;
import lombok.Data;

import java.util.List;

@Data
public class SetmealVo extends Setmeal {
    private String categoryName;
    private List<SetmealDish> setmealDishes;
}
