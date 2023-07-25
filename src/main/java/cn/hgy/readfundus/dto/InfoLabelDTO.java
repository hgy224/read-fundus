package cn.hgy.readfundus.dto;

import lombok.Data;

@Data
public class InfoLabelDTO {
    private ImageDTO image;

    private DoctorReadDTO doctorInfo;
    private LabelDTO label;
}
