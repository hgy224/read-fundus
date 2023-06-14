package cn.hgy.readfundus.service.impl;

import cn.hgy.readfundus.entity.PatientInfo;
import cn.hgy.readfundus.mapper.PatientInfoMapper;
import cn.hgy.readfundus.mapper.PatientMapper;
import cn.hgy.readfundus.service.IPatientInfoService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class PatientInfoService extends ServiceImpl<PatientInfoMapper, PatientInfo> implements IPatientInfoService {

    /**
     * 根据dataset_id和patient_id来获取info_id
     */
    @Override
    public Integer getIdByDatasetPatient(Integer datasetId, String patientId){
        LambdaQueryWrapper<PatientInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PatientInfo::getDatasetId, datasetId);
        queryWrapper.eq(PatientInfo::getPatientId, patientId);
        return getOne(queryWrapper).getId();
    }

    /**
     * 根据dataset_id和patient_id来更新gpt
     */
    @Override
    public boolean update(String gpt, Integer datasetId, String patientId) {
        LambdaUpdateWrapper<PatientInfo> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(PatientInfo::getGpt, gpt);
        updateWrapper.eq(PatientInfo::getDatasetId, datasetId);
        updateWrapper.eq(PatientInfo::getPatientId, patientId);
        return update(updateWrapper);
    }
}
