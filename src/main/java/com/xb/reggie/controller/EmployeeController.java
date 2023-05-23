package com.xb.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xb.reggie.common.R;
import com.xb.reggie.entity.Employee;
import com.xb.reggie.exception.*;
import com.xb.reggie.service.EmployeeService;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController extends BaseController {
    @Autowired
    private EmployeeService employeeService;
    @PostMapping("/login")
    public R<Employee> login(HttpSession session, @RequestBody Employee employee){
            String password=employee.getPassword();
        String md5Password = DigestUtils.md5DigestAsHex(password.getBytes());
        LambdaQueryWrapper<Employee> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getUsername,employee.getUsername());
        Employee result = employeeService.getOne(queryWrapper);
        if(result==null){
           throw new UsernameNotFoundException("用户不存在！");
        }
        if(!md5Password.equals(result.getPassword())){
            throw new PassWordNotMatchException("密码不正确");
        }
        if(result.getStatus()==0){
            throw new UsernameAlreadyStopException("用户已被禁用");
        }
        session.setAttribute("id",result.getId());
        return R.success(result);
    }

    @PostMapping("/logout")
    public R<String> logout(HttpSession session){
        session.removeAttribute("uid");
        return R.success("退出成功！");
    }
    @PostMapping()
    public R<String> addEmployee(@RequestBody Employee employee,HttpSession session){
        String username=employee.getUsername();
        LambdaQueryWrapper<Employee> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getUsername,username);
        Employee one = employeeService.getOne(queryWrapper);
        if(one!=null){
            throw new UserNameDuplicatedException("用户名已被占用!");
        }
        Long id=getUidFromSession(session);
        String password = DigestUtils.md5DigestAsHex("123456".getBytes());
        employee.setPassword(password);
//        employee.setCreateUser(id);
//        employee.setUpdateUser(id);
//        employee.setUpdateTime(LocalDateTime.now());
//        employee.setCreateTime(LocalDateTime.now());
        boolean save = employeeService.save(employee);
        if(!save){
            throw new InsertException("员工添加失败");
        }
        return R.success("添加员工成功");
    }
    @GetMapping("/page")
    public R<Page<Employee>> getEmployeeByPage(Integer page, Integer pageSize, String name){
        Page<Employee> pageInfo=new Page<>(page,pageSize);
        LambdaQueryWrapper<Employee> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.isNotBlank(name),Employee::getName,name).orderByDesc(Employee::getUpdateTime);
        employeeService.page(pageInfo,queryWrapper);
        return R.success(pageInfo);
    }
    @PutMapping()
    public R<String> updateStatus(@RequestBody Employee employee,HttpSession session){
        boolean b = employeeService.updateById(employee);
        if(!b){
            throw new UpdateException("状态修改失败");
        }
        return R.success("员工信息修改成功！");
    }
    @GetMapping("/{id}")
    public R<Employee> selectEmployeeById(@PathVariable("id") Long id){
        LambdaQueryWrapper<Employee> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getId,id);
        Employee one = employeeService.getOne(queryWrapper);
        if(one==null){
            throw new UsernameNotFoundException("用户不存在！");
        }
        return R.success(one);
    }
}
