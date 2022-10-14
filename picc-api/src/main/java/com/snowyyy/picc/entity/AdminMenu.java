package com.snowyyy.picc.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("picc_admin_menu")
public class AdminMenu {
    @TableId(value = "id",type = IdType.INPUT)
    private String id;
    @TableField("menu_name")
    private String menuName;
    @TableField("menu_level")
    public String menuLevel;
    @TableField("menu_path")
    private String menuPath;
    @TableField("menu_order")
    public String menuOrder;
}
