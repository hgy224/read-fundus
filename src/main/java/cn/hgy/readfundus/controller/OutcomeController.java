package cn.hgy.readfundus.controller;

import cn.hgy.readfundus.common.ResourcePath;
import cn.hgy.readfundus.common.Result;
import cn.hgy.readfundus.dto.DoctorReadDTO;
import cn.hgy.readfundus.dto.InfoDTO;
import cn.hgy.readfundus.dto.InfoLabelDTO;
import cn.hgy.readfundus.dto.OutcomeDTO;
import cn.hgy.readfundus.entity.DoctorRead;
import cn.hgy.readfundus.entity.FundusDataset;
import cn.hgy.readfundus.entity.Outcome;
import cn.hgy.readfundus.service.IDoctorReadService;
import cn.hgy.readfundus.service.IFundusDatasetService;
import cn.hgy.readfundus.service.IOutcomeService;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.text.csv.CsvUtil;
import cn.hutool.core.text.csv.CsvWriter;
import cn.hutool.core.util.CharsetUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import jakarta.annotation.Resource;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/outcome")
public class OutcomeController {
    @Resource
    private ResourcePath resourcePath;

    @Resource
    private IDoctorReadService doctorReadService;

    @Resource
    private IOutcomeService outcomeService;

    @Resource
    private IFundusDatasetService fundusDatasetService;

    @GetMapping("/info/{readId}")
    public Result<InfoDTO> read(@PathVariable Integer readId){
        DoctorRead read = doctorReadService.getById(readId);
        if (read.getCurNum().equals(read.getNum())) return Result.error("任务已完成!");
        // 获取此次应该读片的病人的所有信息
        InfoDTO dto = outcomeService.getPatient(read);
        dto.setDoctorInfo(BeanUtil.copyProperties(read, DoctorReadDTO.class));
        return Result.success(dto);
    }

    @GetMapping("/infoLabel/{readId}")
    public Result<InfoLabelDTO> readLabel(@PathVariable Integer readId){
        DoctorRead read = doctorReadService.getById(readId);
        if (read.getCurNum().equals(read.getNum())) return Result.error("任务已完成!");
        // 获取此次应该读片的病人的所有信息
        InfoLabelDTO dto = outcomeService.getPatientLabel(read);
        dto.setDoctorInfo(BeanUtil.copyProperties(read, DoctorReadDTO.class));
        return Result.success(dto);
    }

    @GetMapping("/download/{name}")
    public void download(HttpServletResponse response, @PathVariable String name){
        //输入流，读取文件内容
        try {
            String imageName = Paths.get(resourcePath.getImg(), name).toString();
            if (imageName.contains("-_-")){
                imageName = imageName.replaceAll("-_-", "/");

            }
            FileInputStream fileInputStream = new FileInputStream(imageName);
            //输出流，通过输出流将文件写回浏览器
            ServletOutputStream outputStream = response.getOutputStream();
            if (imageName.endsWith("png")){
                response.setContentType("image/png");
            }else {
                response.setContentType("image/jepg");
            }

            int len = 0;
            byte[] bytes = new byte[10240];
            while ((len = fileInputStream.read(bytes))!=-1){
                outputStream.write(bytes, 0, len);
            }
            outputStream.close();
            fileInputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @PostMapping("/chooseOnly")
    public Result<String> putChooseOutcomeOnly(@RequestBody Outcome outcome){
        DoctorRead read = doctorReadService.getById(outcome.getReadId());
        outcome.setI(read.getCurNum());
        if (outcomeService.updateChoose(outcome) && doctorReadService.addCurNum(outcome.getReadId())) return Result.success("提交成功!");
        return Result.error("提交失败!");
    }

    @PostMapping("/choose")
    public Result<String> putChooseOutcome(@RequestBody Outcome outcome){
        DoctorRead read = doctorReadService.getById(outcome.getReadId());
        outcome.setI(read.getCurNum());
        if (outcomeService.updateChoose(outcome)) return Result.success("提交成功!");
        return Result.error("提交失败!");
    }

    @PostMapping("/opinion")
    public Result<String> putOpinionOutcome(@RequestBody Outcome outcome){
        DoctorRead read = doctorReadService.getById(outcome.getReadId());
        outcome.setI(read.getCurNum());
        if (outcomeService.updateOpinion(outcome)) return Result.success("提交成功!");
        return Result.error("提交失败!");
    }

    @GetMapping("/get/{readId}")
    public Result<String> getOutcome(@PathVariable Integer readId){
        // 获取医生名字
        DoctorRead read = doctorReadService.getById(readId);
        // 获取数据集名字
        FundusDataset dataset = fundusDatasetService.getById(read.getDatasetId());
        // 生成保存结果的文件名字
        String outcomeFileName = dataset.getDatasetName() + "_" + read.getDoctorName() + ".csv";
        String path = Paths.get(resourcePath.getOutcome(), outcomeFileName).toString();
        File file = new File(path);

        // 查询结果
        LambdaQueryWrapper<Outcome> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Outcome::getReadId, readId);
        List<Outcome> list = outcomeService.list(queryWrapper);
        // 保存要写入文件的结果
        List<OutcomeDTO> outcomes = list.stream()
                .map(outcome ->
                        BeanUtil.copyProperties(outcome, OutcomeDTO.class))
                .collect(Collectors.toList());
        if (file.getParentFile().exists() || file.getParentFile().mkdirs()) {
            CsvWriter writer = CsvUtil.getWriter(file, CharsetUtil.CHARSET_UTF_8);
            writer.writeBeans(outcomes);
            writer.close();
            return Result.success("保存到了"+path);
        }
        return Result.error("保存结果失败!");
    }
}
