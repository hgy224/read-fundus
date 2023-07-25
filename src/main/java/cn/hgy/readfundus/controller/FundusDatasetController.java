package cn.hgy.readfundus.controller;

import cn.hgy.readfundus.common.Result;
import cn.hgy.readfundus.dto.FundusDatasetDTO;
import cn.hgy.readfundus.entity.FundusDataset;
import cn.hgy.readfundus.service.IFundusDatasetService;
import cn.hutool.core.bean.BeanUtil;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/dataset")
public class FundusDatasetController {

    @Resource
    private IFundusDatasetService fundusDatasetService;

    @GetMapping("/use/{state}")
    public Result<List<FundusDatasetDTO>> usedDataset(@PathVariable Integer state){
        List<FundusDataset> datasets = fundusDatasetService.usedList(state);
        List<FundusDatasetDTO> datasetDTOS = datasets.stream()
                .map(dataset ->
                        BeanUtil.copyProperties(dataset, FundusDatasetDTO.class))
                .collect(Collectors.toList());
        return Result.success(datasetDTOS);
    }

    @GetMapping("/type/{datasetId}")
    public Result<Integer> getType(@PathVariable Integer datasetId){
        FundusDataset dataset = fundusDatasetService.getById(datasetId);
        if (dataset==null) return Result.error("数据集错误!");
        return Result.success(dataset.getType());
    }

    @GetMapping("/password/{datasetId}")
    public Result<Integer> getPassword(@PathVariable Integer datasetId){
        FundusDataset dataset = fundusDatasetService.getById(datasetId);
        if (dataset==null) return Result.error("数据集错误!");
        return Result.success(dataset.getPassword());
    }

    @PutMapping("/add/{type}")
    public Result<String> addDataset(@PathVariable String type){
        boolean flag = false;
        if (type.equals("test")){
            flag = fundusDatasetService.addDataset("test", "test.csv", 2);
        }else if (type.equals("data")){
            flag = fundusDatasetService.addDataset("糖尿病数据集", "500人理化数据.csv", 2);
        }else if (type.equals("data1")){
            flag = fundusDatasetService.addDataset("新增的550人", "550人理化数据.csv", 2);
        }else if (type.equals("portable")){
            flag = fundusDatasetService.addDataset("Portable", "portable.csv", 1);
        }else if (type.equals("nonportable")){
            flag = fundusDatasetService.addDataset("Non-portable", "nonportable.csv", 1);
        }else if (type.equals("seed")){
            flag = fundusDatasetService.addDataset("seed", "seed.csv", 1);
        }
        if (flag) return Result.success("添加数据集成功!");
        return Result.error("添加数据集失败!");
    }

    @PutMapping("/gpt/{type}")
    public Result<String> addGpt(@PathVariable String type){
        boolean flag = false;
        if (type.equals("test")){
            flag = fundusDatasetService.addGpt("test", "gpt建议.csv");
        }else if (type.equals("data")){
            flag = fundusDatasetService.addGpt("糖尿病数据集", "gpt建议.csv");
        }else if (type.equals("data1")){
            flag = fundusDatasetService.addGpt("新增的550人", "550人gpt建议.csv");
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
