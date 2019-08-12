package com.longxw.demo.updater;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(SQLUpdaterConfiguration.class)
@Documented
public @interface EnableAutoUpdateSQL {
}
