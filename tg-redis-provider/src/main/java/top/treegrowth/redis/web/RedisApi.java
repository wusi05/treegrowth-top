package top.treegrowth.redis.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.treegrowth.model.redis.PureIdentifyCode;
import top.treegrowth.redis.dao.RedisDao;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
public class RedisApi {

    @Autowired
    private RedisDao redisDao;

    @RequestMapping(value = "api/service/redis/identifyCode", method = POST)
    public void setIdentifyCode(@RequestBody PureIdentifyCode pureIdentifyCode) {
        redisDao.setIdentifyCode(pureIdentifyCode);
    }

    @RequestMapping(value = "api/service/redis/{phoneNum}", method = GET)
    public String getIdentifyCode(@PathVariable(value = "phoneNum") String phoneNum) {
        return redisDao.getIdentifyCode(phoneNum);
    }
}