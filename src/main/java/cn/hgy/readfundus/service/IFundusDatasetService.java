package cn.hgy.readfundus.service;

import cn.hgy.readfundus.entity.FundusDataset;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface IFundusDatasetService extends IService<FundusDataset> {
    List<FundusDataset> usedList(int state);
    boolean addDataset(String datasetName, String fileName, int type);
    FundusDataset getByName(String datasetName);
    boolean activateDataset(String datasetName);

    boolean addGpt(String test, String s);

}
