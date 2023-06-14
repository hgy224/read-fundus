package cn.hgy.readfundus.entity;

import lombok.Data;

@Data
public class DoctorRead {
    private Integer id;
    private String doctorName;
    private Integer datasetId;
    private Integer curNum;
    private Integer num;
}
