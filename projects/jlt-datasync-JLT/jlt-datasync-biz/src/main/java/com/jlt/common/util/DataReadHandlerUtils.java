package com.jlt.common.util;

import com.jlt.common.annotation.DataReadHandler;
import org.reflections.Reflections;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Billy.Zhang
 * @date 2018/10/26
 */
public class DataReadHandlerUtils {
    public static Map<String, String> map = new ConcurrentHashMap<String, String>();

    static {
        //反射工具包，指明扫描路径
        Reflections reflections = new Reflections("com.jlt.datasync.exthandler.impl");
        //获取带Handler注解的类
        Set<Class<?>> classList = reflections.getTypesAnnotatedWith(DataReadHandler.class);
        for (Class classes : classList) {
            DataReadHandler t = (DataReadHandler) classes.getAnnotation(DataReadHandler.class);
            //注解值为key，类名为value
            map.put(t.value(), CommonUtil.toLowerCaseFirstWord(classes.getSimpleName()));
        }
    }

    //通过jlt_job_detail.tableName，也就是注解的值获取相应处理Handler的类名
    public static String getBeanName(String value) {
        return map.get(value);
    }
}


