package ${baseInfo.packageName};

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service("${serviceInterface.fileName?uncap_first?split('I')[0]}")
public class ${baseInfo.fileName} implements ${serviceInterface.fileName} {

}
