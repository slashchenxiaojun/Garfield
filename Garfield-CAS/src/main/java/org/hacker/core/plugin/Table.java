package org.hacker.core.plugin;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Model注解
 * 说明：标注Model对应的数据源、表、主键
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
public @interface Table {
	
	/**
	 * Model对应的数据库
	 * @return
	 */
    String dataSourceName() default "main";
    
    /**
     * Model对应的表
     * @return
     */
    String tableName();

    /**
     * Model的主键列名称
     * @return
     */
    String pkName() default "id";
    
}
