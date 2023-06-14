package cn.hgy.readfundus.entity;

import lombok.Data;

@Data
public class Patient {
    private Integer id;
    // 数据集编号
    private Integer datasetId;
    // 图片名
    private String imageName;
    // 图片数量
    private Integer imageNum;
    // meta信息id
    private Integer infoId;
}
