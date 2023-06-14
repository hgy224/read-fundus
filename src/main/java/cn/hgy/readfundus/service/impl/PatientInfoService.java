package cn.hgy.readfundus.service.impl;

import cn.hgy.readfundus.entity.PatientInfo;
import cn.hgy.readfundus.mapper.PatientInfoMapper;
import cn.hgy.readfundus.mapper.PatientMapper;
import cn.hgy.readfundus.service.IPatientInfoService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class PatientInfoService extends ServiceImpl<PatientInfoMapper, PatientInfo> implements IPatientInfoService {

    /**
     * 根据dataset_id和patient_id来获取info_id
     */
    public Integer getIdByDatasetPatient(Integer datasetId, String patientId){
        LambdaQueryWrapper<PatientInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PatientInfo::getDatasetId, datasetId);
        queryWrapper.eq(PatientInfo::getPatientId, patientId);
        return getOne(queryWrapper).getId();
    }
}
