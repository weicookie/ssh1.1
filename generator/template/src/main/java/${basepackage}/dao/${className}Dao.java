<#include "/java_copyright.include">
<#assign className = table.className>   
<#assign classNameLower = className?uncap_first>   
package ${basepackage}.dao;

import org.springframework.stereotype.Repository;
import com.harvey.common.base.*;
import ${basepackage}.domain.*;

@Repository
public class ${className}Dao extends BaseHibernateDao<${className},${table.idColumn.javaType}>{

}
