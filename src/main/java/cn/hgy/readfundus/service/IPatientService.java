package cn.hgy.readfundus.service;

import cn.hgy.readfundus.aggregates.PatientRich;
import cn.hgy.readfundus.entity.Patient;
import cn.hutool.core.text.csv.CsvRow;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface IPatientService extends IService<Patient> {
    List<Patient> list(Integer datasetId);
    PatientRich getPatientInfo(CsvRow row);

    PatientRich getPatient(CsvRow row);
}
