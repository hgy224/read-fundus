package cn.hgy.readfundus.dto;

import lombok.Data;

@Data
public class FundusDatasetDTO {
    private Integer id;

    // 眼底数据集名字
    private String datasetName;

    // 眼底数据集的大小
    private Integer num;

    // 眼底数据集以什么为单位 0: 图片  1: 眼睛  2: 人
    private Integer type;
}
