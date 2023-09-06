package com.bcsd.project.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bcsd.project.domain.SmsSendRecord;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 短信发送记录 Mapper 接口
 *
 * @author liuliang
 * @since 2023-05-29
 */
@Repository
public interface SmsSendRecordMapper extends BaseMapper<SmsSendRecord> {

    List<SmsSendRecord> listPage(SmsSendRecord params);

}
