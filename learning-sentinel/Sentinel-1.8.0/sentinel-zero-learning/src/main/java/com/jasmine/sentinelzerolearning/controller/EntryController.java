package com.jasmine.sentinelzerolearning.controller;

import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.context.ContextUtil;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sun.misc.Unsafe;

import java.lang.reflect.Field;

/**
 * @author wangyf
 * @since 2.0.0
 */
@RestController
@RequestMapping("/test/entry")
public class EntryController {

    /**
     * 一个上下文中多个 Entry
     */
    @GetMapping("/more")
    public void moreEntry() {

        ContextUtil.enter("上下文_测试条目");

        Entry e1 = null;
        try {
            e1 = SphU.entry("资源1");
            System.out.println("资源1 > 执行");
            entry2();
        } catch (BlockException e) {
            e.printStackTrace();
        } finally {
            if (e1 != null) {
                e1.exit();
                printAddresses(ContextUtil.getContext());
                System.out.println(ContextUtil.getContext().getName());
            }
            ContextUtil.exit();
        }
    }

    private void entry2() {
        Entry e2 = null;
        try {
            e2 = SphU.entry("资源2");
            System.out.println("资源2 > 执行");
        } catch (BlockException e) {
            e.printStackTrace();
        } finally {
            if (e2 != null) {
                e2.exit();
            }
        }
    }


    static final Unsafe unsafe = getUnsafe();
    static final boolean is64bit = true;

    public static void printAddresses(Object... objects) {
        System.out.print("0x");
        long last = 0;
        int offset = unsafe.arrayBaseOffset(objects.getClass());
        int scale = unsafe.arrayIndexScale(objects.getClass());
        switch (scale) {
            case 4:
                long factor = is64bit ? 8 : 1;
                final long i1 = (unsafe.getInt(objects, offset) & 0xFFFFFFFFL) * factor;
                System.out.print(Long.toHexString(i1));
                last = i1;
                for (int i = 1; i < objects.length; i++) {
                    final long i2 = (unsafe.getInt(objects, offset + i * 4) & 0xFFFFFFFFL) * factor;
                    if (i2 > last)
                        System.out.print(", +" + Long.toHexString(i2 - last));
                    else
                        System.out.print(", -" + Long.toHexString( last - i2));
                    last = i2;
                }
                break;
            case 8:
                throw new AssertionError("Not supported");
        }
        System.out.println();
    }

    private static Unsafe getUnsafe() {
        try {
            Field theUnsafe = Unsafe.class.getDeclaredField("theUnsafe");
            theUnsafe.setAccessible(true);
            return (Unsafe) theUnsafe.get(null);
        } catch (Exception e) {
            throw new AssertionError(e);
        }
    }

}
