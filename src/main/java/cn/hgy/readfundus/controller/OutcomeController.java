package cn.hgy.readfundus.controller;

import cn.hgy.readfundus.common.ResourcePath;
import cn.hgy.readfundus.common.Result;
import cn.hgy.readfundus.dto.DoctorReadDTO;
import cn.hgy.readfundus.dto.InfoDTO;
import cn.hgy.readfundus.entity.DoctorRead;
import cn.hgy.readfundus.entity.Outcome;
import cn.hgy.readfundus.service.IDoctorReadService;
import cn.hgy.readfundus.service.IOutcomeService;
import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import jakarta.annotation.Resource;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.*;

import java.io.FileInputStream;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("/outcome")
public class OutcomeController {
    @Resource
    private ResourcePath resourcePath;

    @Resource
    private IDoctorReadService doctorReadService;

    @Resource
    private IOutcomeService outcomeService;

    @GetMapping("/info/{readId}")
    public Result<InfoDTO> read(@PathVariable Integer readId){
        DoctorRead read = doctorReadService.getById(readId);
        if (read.getCurNum().equals(read.getNum())) return Result.error("任务已完成!");
        // 获取此次应该读片的病人的所有信息
        InfoDTO dto = outcomeService.getPatient(read);
        dto.setDoctorInfo(BeanUtil.copyProperties(read, DoctorReadDTO.class));
        return Result.success(dto);
    }

    @GetMapping("/download/{name}")
    public void download(HttpServletResponse response, @PathVariable String name){
        //输入流，读取文件内容
        try {
            String imageName = Paths.get(resourcePath.getImg(), name).toString();
            FileInputStream fileInputStream = new FileInputStream(imageName);
            //输出流，通过输出流将文件写回浏览器
            ServletOutputStream outputStream = response.getOutputStream();
            response.setContentType("image/jepg");
            int len = 0;
            byte[] bytes = new byte[1024];
            while ((len = fileInputStream.read(bytes))!=-1){
                outputStream.write(bytes, 0, len);
            }
            outputStream.close();
            fileInputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
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
    public void getOutcome(@PathVariable Integer readId){
        DoctorRead read = doctorReadService.getById(readId);


        LambdaQueryWrapper<Outcome> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Outcome::getReadId, readId);
        List<Outcome> list = outcomeService.list(queryWrapper);
    }
}
