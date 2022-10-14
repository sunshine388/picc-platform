package com.snowyyy.picc.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.snowyyy.picc.entity.Department;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface DepartmentMapper extends BaseMapper<Department> {
}