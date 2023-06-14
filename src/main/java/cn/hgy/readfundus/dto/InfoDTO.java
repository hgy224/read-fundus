package cn.hgy.readfundus.dto;

import lombok.Data;

@Data
public class InfoDTO {
    private ImageDTO image;

    private DoctorReadDTO doctorInfo;
    private PatientInfoDTO patientInfo;
}
