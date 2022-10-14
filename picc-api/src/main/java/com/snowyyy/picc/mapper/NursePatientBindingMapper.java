package com.snowyyy.picc.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.snowyyy.picc.entity.NursePatientBinding;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface NursePatientBindingMapper extends BaseMapper<NursePatientBinding> {
}