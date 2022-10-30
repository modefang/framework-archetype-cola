package ${baseInfo.packageName};

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import ${mapperInterface.packageName}.${mapperInterface.fileName};
import ${dataobject.packageName}.${dataobject.fileName};
import org.springframework.stereotype.Repository;

@Repository
public class ${baseInfo.fileName} extends ServiceImpl<${mapperInterface.fileName}, ${dataobject.fileName}> {

}
