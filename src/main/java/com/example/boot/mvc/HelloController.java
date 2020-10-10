package com.example.boot.mvc;

import com.example.boot.annotation.RedisLimiter;
import com.example.boot.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
@RequestMapping("/test")
public class HelloController {


    @RedisLimiter(value = 60.0D)
    @RequestMapping("/hello")
    public Result hello(){
        Result result = new Result();
        result.setData(Thread.currentThread().getName()+ " ----- " +"处理正常的业务逻辑");
        return result;
    }

    public static void main(String[] args) throws ParseException {
        SimpleDateFormat sim  = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat s= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long time = sim.parse("2020-10-05 21:39:11").getTime();
        long time1 = s.parse("2020-10-05 21:39:12").getTime();


        System.out.println(time1 - time);
    }
}
