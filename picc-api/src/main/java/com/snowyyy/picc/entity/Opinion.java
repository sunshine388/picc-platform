package com.snowyyy.picc.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("picc_opinion")
public class Opinion {
    private static final long serialVersionUID = 1L;
    @TableId(value = "id",type = IdType.AUTO)
    private String id;
    @TableField("user_id")
    private String userId;
    @TableField("role")
    private String role;
    @TableField("username")
    private String username;
    @TableField("problem")
    private String problem;
    @TableField("ctime")
    private String ctime;
    @TableField("whether_to_solve")
    private String whetherToSolve;
    @TableField("solution")
    private String solution;
}
