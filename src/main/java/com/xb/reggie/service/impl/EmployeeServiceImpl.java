package com.xb.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xb.reggie.entity.Employee;
import com.xb.reggie.mapper.EmployeeMapper;
import com.xb.reggie.service.EmployeeService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {
}
