package cn.hgy.readfundus.entity;

import lombok.Data;

@Data
public class DoctorRead {
    private Integer id;
    private String doctorName;
    private Integer datasetId;
    private Integer curNum;
    private Integer num;
    private String password;
    public DoctorRead() {
    }

    public DoctorRead(String doctorName, Integer datasetId, Integer num) {
        this.doctorName = doctorName;
        this.datasetId = datasetId;
        this.num = num;
    }


}
