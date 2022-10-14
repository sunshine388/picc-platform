package com.snowyyy.picc.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.snowyyy.picc.entity.WxUser;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;


@Mapper
@Repository
public interface WxUserMapper extends BaseMapper<WxUser> {
}
