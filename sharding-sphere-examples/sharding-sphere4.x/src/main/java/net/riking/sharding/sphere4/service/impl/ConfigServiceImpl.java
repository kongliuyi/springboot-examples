package net.riking.sharding.sphere4.service.impl;

import net.riking.sharding.sphere4.entity.Config;
import net.riking.sharding.sphere4.mapper.ConfigMapper;
import net.riking.sharding.sphere4.service.IConfigService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author kongLiuYi
 * @since 2021-11-23
 */
@Service
public class ConfigServiceImpl extends ServiceImpl<ConfigMapper, Config> implements IConfigService {

}
