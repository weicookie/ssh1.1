
<#assign className = table.className>
<#assign classNameLower = className?uncap_first>
package ${basepackage}.service;

import com.harvey.common.base.IBaseEntityManager;
import ${basepackage}.domain.${className};

public interface I${className}Service extends IBaseEntityManager<${className}, ${table.idColumn.javaType}>{

}
