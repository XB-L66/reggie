package com.xb.reggie.vo;

import com.xb.reggie.entity.Dish;
import com.xb.reggie.entity.DishFlavor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class DishVo extends Dish {
    private List<DishFlavor> flavors = new ArrayList<>();

    private String categoryName;
}
