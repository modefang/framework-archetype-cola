package ${baseInfo.packageName};

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@ToString
@NoArgsConstructor
@SuperBuilder
@TableName("${tableClass.tableName}")
public class ${tableClass.shortClassName}DO extends BaseDO {

<#list tableClass.baseBlobFields as field>
    <#if field.fieldName!="deleted" && field.fieldName!="updateTime" && field.fieldName!="createTime">
    /**
     * ${field.remark!}
     */
    private ${field.shortTypeName} ${field.fieldName};

    </#if>
</#list>
}
