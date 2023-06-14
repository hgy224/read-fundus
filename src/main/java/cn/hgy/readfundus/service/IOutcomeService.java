package cn.hgy.readfundus.service;

import cn.hgy.readfundus.dto.InfoDTO;
import cn.hgy.readfundus.entity.DoctorRead;
import cn.hgy.readfundus.entity.Outcome;
import cn.hgy.readfundus.entity.Patient;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface IOutcomeService extends IService<Outcome> {
    boolean bindPatients(List<Patient> patients, Integer readId);

    InfoDTO getPatient(DoctorRead read);

    boolean updateChoose(Outcome outcome);

    boolean updateOpinion(Outcome outcome);

    boolean remove(Integer readId);
}
