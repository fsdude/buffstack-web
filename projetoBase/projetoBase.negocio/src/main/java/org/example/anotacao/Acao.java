package org.example.anotacao;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE_USE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Acao {

	String[] precedente() default {};

	String nome() default "";
	
	String descricao() default "";
	
	String link() default "";

	boolean menu() default false;

	String icone() default "fa fa-caret-right";

	String menuPai() default "";
	
	int ordem() default 99;

	boolean liberavel() default false;

	boolean acaoPadrao() default false;

}