package cn.hgy.readfundus.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FundusDataset {

    private Integer id;

    // 眼底数据集名字
    private String datasetName;

    // 信息文件的名字
    private String infoFile;

    // 眼底数据集以什么为单位 0: 图片  1: 眼睛  2: 人
    private Integer type;

    // 眼底数据集的大小
    private Integer num;

    // 数据集的状态  0: 不能用  1: 可用
    private Integer state;

    public FundusDataset(String datasetName, String infoFile, Integer type, Integer num) {
        this.datasetName = datasetName;
        this.infoFile = infoFile;
        this.type = type;
        this.num = num;
    }
}
