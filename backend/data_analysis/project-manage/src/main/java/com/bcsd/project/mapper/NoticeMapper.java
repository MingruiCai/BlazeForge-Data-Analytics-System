package com.bcsd.project.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bcsd.project.domain.Notice;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 通知公告表 Mapper 接口
 *
 * @author liuliang
 * @since 2023-05-29
 */
@Repository
public interface NoticeMapper extends BaseMapper<Notice> {

    List<Notice> sendList(Notice params);

    List<Notice> receiveList(Notice params);

    Notice receiveInfo(Notice params);
}
