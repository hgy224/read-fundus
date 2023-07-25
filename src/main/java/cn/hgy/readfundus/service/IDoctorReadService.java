package cn.hgy.readfundus.service;

import cn.hgy.readfundus.entity.DoctorRead;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface IDoctorReadService extends IService<DoctorRead> {
    boolean addCurNum(Integer readId);
    Integer checkName(String doctorName, Integer datasetId);
    List<DoctorRead> list(Integer datasetId);
    boolean createLabel(DoctorRead doctorRead);

    boolean checkPassword(Integer id, String password);

    String login(Integer id);
}
