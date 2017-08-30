<#include "/macro.include"/>
<#include "/java_copyright.include">
<#assign className = table.className>   
<#assign classNameLower = className?uncap_first> 
package ${basepackage}.domain;

import java.util.*;
import javax.persistence.*;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import com.harvey.common.base.*;

import ${basepackage}.domain.*;

@Entity
@Table(name = "${table.sqlName}")
public class ${className} extends BaseEntity{
	private static final long serialVersionUID = 5454155825314635342L;
	
	//alias
	public static final String TABLE_ALIAS = "${table.tableAlias}";
	<#list table.columns as column>
	public static final String ${column.constantName} = "${column.columnAlias}";
	</#list>
	
	//date formats
	<#list table.columns as column>
	<#if column.isDateTimeColumn>
	public static final String FORMAT_${column.constantName} = DATE_FORMAT;
	</#if>
	</#list>
	
	
	//columns START
	<#list table.columns as column>
	/**
	 * ${column.columnAlias!}	   db_column: ${column.sqlName} 
	 */	
	<#if column.pk>
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	</#if>
	@Column(name = "${column.sqlName}", unique = ${column.unique?string}, nullable = ${column.nullable?string}, insertable = true, updatable = true, length = ${column.size})
	private ${column.javaType} ${column.columnNameLower};
	</#list>
	//columns END

<@generateConstructor className/>
<@generateJavaColumns/>
<@generateJavaOneToMany/>
<@generateJavaManyToOne/>

	public String toString() {
		return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
		<#list table.columns as column>
			.append("${column.constantName}",get${column.columnName}())
		</#list>
			.toString();
	}
	
	public int hashCode() {
		return new HashCodeBuilder()
		<#list table.pkColumns as column>
			.append(get${column.columnName}())
		</#list>
			.toHashCode();
	}
	
	public boolean equals(Object obj) {
		<#list table.columns as column>
		<#if column.pk>
		if(this.get${column.columnName}() == null){
			return false;
		}
		</#if>
		</#list>
		if(!(obj instanceof ${className})){
			return false;
		}
		if(this == obj) {
			return true;
		}
		${className} other = (${className})obj;
		return new EqualsBuilder()
			<#list table.pkColumns as column>
			.append(get${column.columnName}(),other.get${column.columnName}())
			</#list>
			.isEquals();
	}
}

<#macro generateJavaColumns>
	<#list table.columns as column>
		<#if column.isDateTimeColumn>
	public String get${column.columnName}String() {
		return DateConvertUtils.format(get${column.columnName}(), FORMAT_${column.constantName});
	}
	public void set${column.columnName}String(String value) {
		set${column.columnName}(DateConvertUtils.parse(value, FORMAT_${column.constantName},${column.javaType}.class));
	}
	
		</#if>	
	public void set${column.columnName}(${column.javaType} value) {
		this.${column.columnNameLower} = value;
	}
	
	public ${column.javaType} get${column.columnName}() {
		return this.${column.columnNameLower};
	}
	</#list>
</#macro>

<#macro generateJavaOneToMany>
	<#list table.exportedKeys.associatedTables?values as foreignKey>
	<#assign fkSqlTable = foreignKey.sqlTable>
	<#assign fkTable	= fkSqlTable.className>
	<#assign fkPojoClass = fkSqlTable.className>
	<#assign fkPojoClassVar = fkPojoClass?uncap_first>
	
	@OneToMany(cascade = { CascadeType.MERGE }, fetch = FetchType.LAZY, mappedBy = "${classNameLower}")
	private Set<${fkPojoClass}> ${fkPojoClassVar}s = new HashSet<${fkPojoClass}>(0);
	public void set${fkPojoClass}s(Set<${fkPojoClass}> ${fkPojoClassVar}){
		this.${fkPojoClassVar}s = ${fkPojoClassVar};
	}
	
	public Set<${fkPojoClass}> get${fkPojoClass}s() {
		return ${fkPojoClassVar}s;
	}
	</#list>
</#macro>

<#macro generateJavaManyToOne>
	<#list table.importedKeys.associatedTables?values as foreignKey>
	<#assign fkSqlTable = foreignKey.sqlTable>
	<#assign fkTable	= fkSqlTable.className>
	<#assign fkPojoClass = fkSqlTable.className>
	<#assign fkPojoClassVar = fkPojoClass?uncap_first>
	
	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumns({
	<#list foreignKey.parentColumns?values as fkColumn>
		@JoinColumn(name = "${fkColumn}",nullable = false, insertable = false, updatable = false) <#if fkColumn_has_next>,</#if>
	</#list>
	})
	private ${fkPojoClass} ${fkPojoClassVar};
	
	public void set${fkPojoClass}(${fkPojoClass} ${fkPojoClassVar}){
		<#list fkSqlTable.columns as column>
			<#if column.pk>
		this.set${column.columnName}(${fkPojoClassVar} == null ? null : ${fkPojoClassVar}.get${column.columnName}());
			</#if>
		</#list>
		this.${fkPojoClassVar} = ${fkPojoClassVar};
	}
	
	public ${fkPojoClass} get${fkPojoClass}() {
		return ${fkPojoClassVar};
	}
	</#list>
</#macro>
