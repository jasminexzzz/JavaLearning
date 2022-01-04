package com.jasmine.sentinelzerolearning.controller;

import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.context.ContextUtil;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.jasmine.sentinelzerolearning.util.MemoryAddrUtil;
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
                MemoryAddrUtil.printAddresses(ContextUtil.getContext());
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


}
