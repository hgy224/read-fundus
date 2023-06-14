package cn.hgy.readfundus.service;

import cn.hgy.readfundus.entity.PatientInfo;
import com.baomidou.mybatisplus.extension.service.IService;

public interface IPatientInfoService extends IService<PatientInfo> {
    Integer getIdByDatasetPatient(Integer datasetId, String patientId);
}
