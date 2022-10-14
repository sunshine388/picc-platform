package com.snowyyy.picc.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
import com.snowyyy.picc.entity.Opinion;

@Mapper
@Repository
public interface OpinionMapper  extends BaseMapper<Opinion> {
}
