package com.xzzz.A1_java.base.A_基础.注解.自定义注解.重复注解;


@Tag(age=5)
@Tag(name="小杨" , age=9)
//@Tags({@Tag(age=5),
//	@Tag(name="疯狂Java" , age=9)})
public class TagTest
{
    public static void main(String[] args)
    {
        Class<TagTest> clazz = TagTest.class;
		/*
		使用Java 8新增的getDeclaredAnnotationsByType()方法获取
		修饰FkTagTest类的多个@FkTag注解
		*/
        Tag[] tags = clazz.getDeclaredAnnotationsByType(Tag.class);
        // 遍历修饰FkTagTest类的多个@FkTag注解
        for(Tag tag : tags)
        {
            System.out.println(tag.name() + "-->" + tag.age());
        }
		/*
		使用传统的getDeclaredAnnotation()方法获取
		修饰FkTagTest类的@FkTags注解
		*/
        Tags container = clazz.getDeclaredAnnotation(Tags.class);
        System.out.println(container);
    }
}
