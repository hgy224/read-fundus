package cn.hgy.readfundus.controller;

import cn.hgy.readfundus.common.Result;
import cn.hgy.readfundus.dto.FundusDatasetDTO;
import cn.hgy.readfundus.entity.FundusDataset;
import cn.hgy.readfundus.service.impl.FundusDatasetService;
import cn.hutool.core.bean.BeanUtil;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/dataset")
public class FundusDatasetController {

    @Resource
    private FundusDatasetService fundusDatasetService;

    @GetMapping("/use")
    public Result<List<FundusDatasetDTO>> usedDataset(){
        List<FundusDataset> datasets = fundusDatasetService.usedList();
        List<FundusDatasetDTO> datasetDTOS = datasets.stream()
                .map(dataset ->
                        BeanUtil.copyProperties(dataset, FundusDatasetDTO.class))
                .collect(Collectors.toList());
        return Result.success(datasetDTOS);
    }

    @PutMapping("/add/{type}")
    public Result<String> addDataset(@PathVariable String type){
        boolean flag = false;
        if (type.equals("test")){
            flag = fundusDatasetService.addDataset("test", "test.csv", 2);
        }else if (type.equals("data")){
            flag = fundusDatasetService.addDataset("糖尿病数据集", "500人理化数据.csv", 2);
        }
        if (flag) return Result.success("添加数据集成功!");
        return Result.error("添加数据集失败!");
    }

    @GetMapping("/activate")
    public Result<String> activate(String name){
        boolean flag = fundusDatasetService.activateDataset(name);
        if (flag) return Result.success("激活数据集成功!");
        return Result.error("激活数据集失败!");
    }
}
