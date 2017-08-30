<#include "/java_copyright.include">
<#assign className = table.className>   
<#assign classNameLower = className?uncap_first> 
package ${basepackage}.service;
import ${servicepackage}.${moduleName}.I${className}Service;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

<#include "/java_imports.include">
@Service
public class ${className}Service extends BaseEntityManager<${className},${table.idColumn.javaType}> implements I${className}Service{

	private ${className}Dao ${classNameLower}Dao;

	/**增加setXXXX()方法,spring就可以通过autowire自动设置对象属性,请注意大小写*/
    @Autowired
	public void set${className}Dao(${className}Dao dao) {
		this.${classNameLower}Dao = dao;
	}

	public EntityDao<${className},${table.idColumn.javaType}> getEntityDao() {
		return this.${classNameLower}Dao;
	}

	
<#list table.columns as column>
	<#if column.unique && !column.pk>
	@Transactional(readOnly=true)
	public ${className} getBy${column.columnName}(${column.javaType} v) {
		return ${classNameLower}Dao.getBy${column.columnName}(v);
	}	
	
	</#if>
</#list>
}
