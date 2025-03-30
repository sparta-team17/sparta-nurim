package com.example.nurim.domain.program.generator;

import com.example.nurim.domain.common.exception.CustomException;
import com.example.nurim.domain.common.exception.ErrorCode;
import com.example.nurim.domain.program.enums.ProgramStatus;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.StringJoiner;

@Component
public class ProgramListKeyGenerator implements KeyGenerator {

    @Override
    public Object generate(Object target, Method method, Object... params) {

        if (params.length == 0 || params[0] == null) {
            return "defaultKey";
        }

        Object request = params[0];

        try {
            Class<?> clazz = request.getClass();

            String title = (String) clazz.getMethod("getTitle").invoke(request);
            String location = (String) clazz.getMethod("getLocation").invoke(request);
            Object statusObj = clazz.getMethod("getStatus").invoke(request);
            String status = statusObj != null ? statusObj.toString() : "null";
            Integer page = (Integer) clazz.getMethod("getPage").invoke(request);
            Integer size = (Integer) clazz.getMethod("getSize").invoke(request);

            return new StringJoiner("-")
                .add(title != null ? title : "null")
                .add(location != null ? location : "null")
                .add(status != null ? status : "null")
                .add(String.valueOf(page))
                .add(String.valueOf(size))
                .toString();

        } catch (Exception e) {
            throw new CustomException(ErrorCode.UNKNOWN);
        }
    }
}
