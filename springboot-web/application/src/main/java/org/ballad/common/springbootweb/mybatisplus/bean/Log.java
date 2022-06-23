package org.ballad.common.springbootweb.mybatisplus.bean;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;

@Data
@Builder
@ToString
public class Log {

    private String userId;  // 用户id

    private String userNickname; //用户昵称

    private String operationInfo; //操作信息

    private String interfaceName; // 调用的接口方法名

    private String applicationName; // 调用的服务名

    private LocalDateTime createTime; //操作时间
}


