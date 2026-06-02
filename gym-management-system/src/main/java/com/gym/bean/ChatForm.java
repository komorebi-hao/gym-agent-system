package com.gym.bean;

import lombok.Data;
import org.springframework.context.annotation.Bean;

@Data
public class ChatForm {

    private String memoryId;  //对话id
    private int memberAccount; //会员账号
    private String message; //用户问题
}
