package cn.hgy.readfundus.aggregates;

import cn.hgy.readfundus.entity.Patient;
import cn.hgy.readfundus.entity.PatientInfo;
import lombok.Data;

@Data
public class PatientRich {

    private Patient patient;

    private PatientInfo patientInfo;
}
