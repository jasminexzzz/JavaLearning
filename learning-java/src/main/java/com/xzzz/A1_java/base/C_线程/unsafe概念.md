## public final native boolean compareAndSwapObject(Object var1, long var2, Object var4, Object var5);

```java
UNSAFE_ENTRY(
  jboolean,
  Unsafe_CompareAndSwapObject(
    JNIEnv *env,
    jobject unsafe,
    jobject obj,  // 参数1
    jlong offset, // 参数2
    jobject e_h,  // 参数3
    jobject x_h   // 参数4
  )

)
  UnsafeWrapper("Unsafe_CompareAndSwapObject");
  oop x = JNIHandles::resolve(x_h); // 新值
  oop e = JNIHandles::resolve(e_h); // 预期值
  oop p = JNIHandles::resolve(obj);
  // 在内存中的具体位置
  HeapWord* addr = (HeapWord *)index_oop_from_field_offset_long(p, offset);
  // 调用了另一个方法
  oop res = oopDesc::atomic_compare_exchange_oop(x, addr, e, true);
  // 如果返回的res等于e，则判定满足compare条件（说明res应该为内存中的当前值），但实际上会有ABA的问题
  jboolean success  = (res == e);  
  // success为true时，说明此时已经交换成功（调用的是最底层的cmpxchg指令）
  if (success)
    // 每次Reference类型数据写操作时，都会产生一个Write Barrier暂时中断操作，配合垃圾收集器
    update_barrier_set((void*)addr, x);
  return success;
UNSAFE_END
```
