package com.ssk.bidwise.dal.mysql.system;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ssk.bidwise.dal.dataobject.system.UserDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户 Mapper
 */
@Mapper
public interface UserMapper extends BaseMapper<UserDO> {
}
