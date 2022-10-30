package ${baseInfo.packageName};

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import ${dataobject.packageName}.${dataobject.fileName};
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ${mapperInterface.fileName} extends BaseMapper<${dataobject.fileName}> {

}
