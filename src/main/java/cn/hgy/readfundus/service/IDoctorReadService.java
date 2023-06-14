package cn.hgy.readfundus.service;

import cn.hgy.readfundus.entity.DoctorRead;
import com.baomidou.mybatisplus.extension.service.IService;

public interface IDoctorReadService extends IService<DoctorRead> {
    boolean addCurNum(Integer readId);
}
