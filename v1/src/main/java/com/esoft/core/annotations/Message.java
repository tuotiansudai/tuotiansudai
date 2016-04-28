package com.esoft.core.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME) //运行时编译  
@Target( { ElementType.FIELD })  //修饰的字段  
public @interface Message {

}