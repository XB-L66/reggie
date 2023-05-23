package com.xb.reggie.handler;

import com.xb.reggie.common.R;
import com.xb.reggie.exception.*;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLIntegrityConstraintViolationException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler({BaseException.class,FileUploadException.class})
    public R<String> exceptionHandler(Throwable e){
            if(e instanceof UserNameDuplicatedException){
                return R.error(e.getMessage());
            }else if(e instanceof UsernameNotFoundException){
                return R.error(e.getMessage());
            }else if(e instanceof CategoryDuplicatedException){
                return R.error(e.getMessage());
            }else if(e instanceof CategoryRelateDishException){
                return R.error(e.getMessage());
            }else if(e instanceof  CategoryRelateSetmealException){
                return R.error(e.getMessage());
            }else if(e instanceof FileUploadIoException){
                return R.error(e.getMessage());
            }else if(e instanceof  DishDuplicatedException){
                return R.error(e.getMessage());
            }else if(e instanceof  DishNotFoundException){
                return R.error(e.getMessage());
            }else if(e instanceof InsertException){
                return R.error(e.getMessage());
            }else if(e instanceof UpdateException){
                return R.error(e.getMessage());
            }else if(e instanceof DeleteException){
                return R.error(e.getMessage());
            }else if(e instanceof FileEmptyException){
                return R.error(e.getMessage());
            }else if(e instanceof  FileSizeException){
                return R.error(e.getMessage());
            }else if(e instanceof FileStateException){
                return R.error(e.getMessage());
            }else if(e instanceof FileTypeException){
                return R.error(e.getMessage());
            }else if(e instanceof PassWordNotMatchException){
                return R.error(e.getMessage());
            }else if(e instanceof UsernameNotFoundException){
                return R.error(e.getMessage());
            }else if(e instanceof UsernameAlreadyStopException){
                return R.error(e.getMessage());
            }else if(e instanceof CustomException){
                return R.error(e.getMessage());
            }
            return null;
    }
}
