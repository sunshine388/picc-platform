package com.snowyyy.picc.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.snowyyy.picc.entity.MessageDetail;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface MessageDetailMapper extends BaseMapper<MessageDetail> {
}