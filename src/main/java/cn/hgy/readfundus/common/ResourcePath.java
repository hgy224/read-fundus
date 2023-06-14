package cn.hgy.readfundus.common;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "path")
public class ResourcePath {
    // 图片所在路径
    private String img;

    // 信息文件所在位置
    private String info;

    private String outcome;
}
