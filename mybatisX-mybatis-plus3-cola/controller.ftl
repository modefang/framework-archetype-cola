package ${baseInfo.packageName};

import ${serviceInterface.packageName}.${serviceInterface.fileName};
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
* ${tableClass.remark?split('表')[0]}模块
*/
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class ${baseInfo.fileName} {

    private final ${serviceInterface.fileName} ${serviceInterface.fileName?uncap_first?split('I')[0]};

}
