package cn.hgy.readfundus.service.impl;

import cn.hgy.readfundus.entity.DoctorRead;
import cn.hgy.readfundus.entity.Patient;
import cn.hgy.readfundus.mapper.DoctorReadMapper;
import cn.hgy.readfundus.service.IDoctorReadService;
import cn.hgy.readfundus.service.IFundusDatasetService;
import cn.hgy.readfundus.service.IOutcomeService;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.lang.UUID;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import org.springframework.data.redis.core.StringRedisTemplate;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;


@Service
public class DoctorReadService extends ServiceImpl<DoctorReadMapper, DoctorRead> implements IDoctorReadService {

    @Resource
    private IFundusDatasetService fundusDatasetService;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 检查该名字是否重名, 重名返回read_id, 否则返回null
     */
    public Integer checkName(String doctorName, Integer datasetId){
        LambdaQueryWrapper<DoctorRead> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DoctorRead::getDoctorName, doctorName);
        queryWrapper.eq(DoctorRead::getDatasetId, datasetId);
        DoctorRead read = getOne(queryWrapper);
        return read == null ? null : read.getId();
    }

    /**
     * 根据数据集id查找所有的doctor
     */
    public List<DoctorRead> list(Integer datasetId) {
        LambdaQueryWrapper<DoctorRead> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DoctorRead::getDatasetId, datasetId);
        return list(queryWrapper);
    }

    /**
     * 根据前端提交的医生名字和数据集id创建label
     * 在outcome创建结果信息，方便记录结果和进度
     */
    public boolean createLabel(DoctorRead doctorRead) {
        // 补齐信息
        if (doctorRead.getNum() == null){
            doctorRead.setNum(fundusDatasetService.getById(doctorRead.getDatasetId()).getNum());
        }

        if (!save(doctorRead)) return false;
        // 获取刚刚插入数据的id
        Integer readId = checkName(doctorRead.getDoctorName(), doctorRead.getDatasetId());
        doctorRead.setId(readId);
        return true;
    }

    @Override
    public boolean checkPassword(Integer id, String password) {
        DoctorRead doctor = this.getById(id);
        return password.equals(doctor.getPassword());
    }

    @Override
    public String login(Integer id) {
        //保存用户信息到redis，表示已经登录
        // 随机生成token，作为登录令牌
        String token = UUID.randomUUID().toString(true);
        String tokenKey = "login:token:"+token;
        stringRedisTemplate.opsForValue().set(tokenKey, id.toString(), 30L, TimeUnit.MINUTES);
        //设置token有效期
        return token;
    }

    /**
     * 将对应的read记录的读片数+1
     */
    @Override
    public boolean addCurNum(Integer readId) {
        return update().setSql("cur_num=cur_num+1").eq("id", readId).update();
    }
}
