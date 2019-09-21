//package com.jasmine.Java高级.工具类.雪花算法;
//
//import java.util.Timer;
//import java.util.TimerTask;
//import java.util.concurrent.TimeUnit;
//
////@Service("machineIdUtil")
//public class MachineIdUtil {
//
//    /**
//     *redis 实例
//     */
//    @Autowired
//    private Cluster jimClient;
//    /**
//     * 机器id
//     */
//    public static Integer machine_id;
//    /**
//     * 本地ip地址
//     */
//    private static String localIp;
//    private static TimeUnit timeUnit = TimeUnit.DAYS;
//
//    /**
//     * hash机器IP初始化一个机器ID
//     */
//    @PostConstruct
//    public void initMachineId() throws Exception {
//        localIp = IpUtil.getInet4Address();
//
//        Long ip_ = Long.parseLong(localIp.replaceAll("\\.", ""));
//        //这里取128,为后续机器Ip调整做准备。
//        machine_id = ip_.hashCode()% 128;
//        //创建一个机器ID
//        createMachineId();
//        logger.info("初始化 machine_id :{}", machine_id);
//        SnowFlakeGenerator.initMachineId(machine_id);
//    }
//
//    /**
//     * 容器销毁前清除注册记录
//     */
//    @PreDestroy
//    public void destroyMachineId() {
//        jimClient.del(RedisConstant.OPLOG_MACHINE_ID_kEY + machine_id);
//    }
//
//
//    /**
//     * 主方法：获取一个机器id
//     *
//     * @return
//     */
//    public Integer createMachineId() {
//        try {
//            //向redis注册，并设置超时时间
//            Boolean aBoolean = registMachine(machine_id);
//            //注册成功
//            if (aBoolean) {
//                //启动一个线程更新超时时间
//                updateExpTimeThread();
//                //返回机器Id
//                return machine_id;
//            }
//            //检查是否被注册满了.不能注册，就直接返回
//            if (!checkIfCanRegist()) {
//                //注册满了，加一个报警
//                Profiler.businessAlarm("medicine-oplog-createMachineId", System.currentTimeMillis(), "128个机器码已经注册满！");
//                return machine_id;
//            }
//            logger.info("createMachineId->ip:{},machineId:{}, time:{}", localIp, machine_id, DateUtil.getDate());
//
//            //递归调用
//            createMachineId();
//        } catch (Exception e) {
//            getRandomMachineId();
//            return machine_id;
//        }
//        getRandomMachineId();
//        return machine_id;
//    }
//
//    /**
//     * 检查是否被注册满了
//     *
//     * @return
//     */
//    private Boolean checkIfCanRegist() {
//        Boolean flag = true;
//        //判断0~127这个区间段的机器IP是否被占满
//        for (int i = 0; i <= 127; i++) {
//            flag = jimClient.exists(RedisConstant.OPLOG_MACHINE_ID_kEY + i);
//            //如果不存在。说明还可以继续注册。直接返回i
//            if (!flag) {
//                machine_id = i;
//                break;
//            }
//        }
//        return !flag;
//    }
//
//    /**
//     * 1.更新超時時間
//     * 注意，更新前检查是否存在机器ip占用情况
//     */
//    private void updateExpTimeThread() {
//        //开启一个线程执行定时任务:
//        //1.每23小时更新一次超时时间
//        new Timer(localIp).schedule(new TimerTask() {
//            @Override
//            public void run() {
//                //检查缓存中的ip与本机ip是否一致，一致則更新時間，不一致則重新取一個机器ID
//                Boolean b = checkIsLocalIp(String.valueOf(machine_id));
//                if (b) {
//                    logger.info("更新超时时间 ip:{},machineId:{}, time:{}", localIp, machine_id, DateUtil.getDate("yyyy-MM-dd HH:mm:ss"));
//                    jimClient.expire(RedisConstant.OPLOG_MACHINE_ID_kEY + machine_id, 1, timeUnit);
//                } else {
//                    logger.info("重新生成机器ID ip:{},machineId:{}, time:{}", localIp, machine_id, DateUtil.getDate("yyyy-MM-dd HH:mm:ss"));
//                    //重新生成机器ID，并且更改雪花中的机器ID
//                    getRandomMachineId();
//                    //重新生成并注册机器id
//                    createMachineId();
//                    //更改雪花中的机器ID
//                    SnowFlakeGenerator.initMachineId(machine_id);
//                    //結束當前任務
//                    logger.info("Timer->thread->name:{}", Thread.currentThread().getName());
//                    this.cancel();
//                }
//            }
//        }, 10 * 1000, 1000 * 60 * 60 * 23);
//    }
//
//    /**
//     * 获取1~127随机数
//     */
//    public void getRandomMachineId() {
//        machine_id = (int) (Math.random() * 127);
//    }
//
//    /**
//     * 机器ID顺序获取
//     */
//    public void incMachineId() {
//        if (machine_id >= 127) {
//            machine_id = 0;
//        } else {
//            machine_id += 1;
//        }
//    }
//
//    /**
//     * @param mechineId
//     * @return
//     */
//    private Boolean checkIsLocalIp(String mechineId) {
//        String ip = jimClient.get(RedisConstant.OPLOG_MACHINE_ID_kEY + mechineId);
//        logger.info("checkIsLocalIp->ip:{}", ip);
//        return localIp.equals(ip);
//    }
//
//    /**
//     * 1.注册机器
//     * 2.设置超时时间
//     *
//     * @param mechineId 取值为0~127
//     * @return
//     */
//    private Boolean registMachine(Integer mechineId) throws Exception {
//        return jimClient.set(RedisConstant.OPLOG_MACHINE_ID_kEY + mechineId, localIp, 1, TimeUnit.DAYS, false);
//    }
//
//
//}
//
//
