package com.example.boot.mvc;

import com.example.boot.annotation.RedisLimiter;
import com.example.boot.utils.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/limit")
public class LimitingController {
    public static final Logger LOG = LoggerFactory.getLogger(LimitingController.class);


    @RedisLimiter(
            value = 10.0D
    )
    @RequestMapping("/limitTest")
    public Result limit() {
        Result result = new Result();
        result.setData(123);
        return result;
    }

}
