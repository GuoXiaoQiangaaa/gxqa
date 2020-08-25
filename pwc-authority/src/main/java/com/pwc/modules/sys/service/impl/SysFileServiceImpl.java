

package com.pwc.modules.sys.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pwc.modules.sys.dao.SysFileDao;
import com.pwc.modules.sys.entity.SysFileEntity;
import com.pwc.modules.sys.service.SysFileService;
import org.springframework.stereotype.Service;


/**
 *
 * @author zk
 */
@Service("sysFileService")
public class SysFileServiceImpl extends ServiceImpl<SysFileDao, SysFileEntity> implements SysFileService {

}
