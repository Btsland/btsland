package info.btsland.app.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BaseThread extends Thread {
    private final static Logger logger = LoggerFactory.getLogger(BaseThread.class);

    /**
     * isDead:是否杀死线程
     */
    private boolean isDead = false;

    /**
     * isStop:是否停止
     */
    private boolean isStop = false;

    /**
     * isRun:是否已开始执行
     */
    private boolean isRun = false;

    /**
     * isWait:是否处于等待
     */
    private boolean isSleep = false;
    private int sleepTime;

    private int time=3;

    public double getTime() {
        return time;
    }

    public BaseThread setTime(int time) {
        this.time = time;
        return this;
    }

    public BaseThread() {
        super();
        this.setDaemon(false);//设置为非守护线程  
        logger.info("线程:[" + this.getId() + "] 被创建");
    }

    public BaseThread(String threadName) {
        super(threadName);
        this.setDaemon(false);//设置为非守护线程  
        logger.info("线程:[" + threadName + "-" + this.getId() + "] 被创建");
    }

    /**
     * <p>Title: run</p>
     * <p>Description:JDK线程类自带方法</p>
     *
     * @param @return 设定文件
     * @return boolean 返回类型
     * @throws
     */
    public void run() {
        this.isRun = true;
        while (!isDead) {
            while (true) {
                if (!isStop) {
                    if (preConditions()) execute();
                } else {
                    break;
                }
                sleep(time*1000);//缓解CPU压力，即唤醒线程需要至多256ms
            }
        }
        isRun = false;
        logger.info("线程:[" + this.getName() + "-" + this.getId() + "] 消亡");
    }

    /**
     * <p>Title: preConditions</p>
     * <p>Description:执行体前置条件</p>
     *
     * @param @return 设定文件
     * @return boolean 返回类型
     * @throws
     */
    protected boolean preConditions() {
        return true;
    }

    /**
     * <p>Title: execute</p>
     * <p>Description:线程执行体</p>
     *
     * @return void 返回类型
     * @throws
     */
    public void execute() {

    }

    /**
     * <p>Title: kill</p>
     * <p>Description:结束线程</p>
     *
     * @return void 返回类型
     * @throws
     */
    public void kill() {
        this.isStop = true;
        this.isDead = true;
        this.isRun = false;
        logger.info("线程:[" + this.getName() + "-" + this.getId() + "] 被终止");
    }

    /**
     * <p>Title: halt</p>
     * <p>Description:暂停进程，非休眠</p>
     *
     * @return void 返回类型
     * @throws
     */
    public void halt() {
        this.isStop = true;
        logger.info("线程:[" + this.getName() + "-" + this.getId() + "] 被暂停");
    }

    /**
     * <p>Title: reStart</p>
     * <p>Description:重新执行线程</p>
     *
     * @return void 返回类型
     * @throws
     */
    public void reStart() {
        this.isStop = false;
        logger.info("线程:[" + this.getName() + "-" + this.getId() + "] 被重新执行");
    }

    /**
     * <p>Title: isRun</p>
     * <p>Description:是否处于执行态</p>
     *
     * @param @return 设定文件
     * @return boolean 返回类型
     * @throws
     */
    public boolean isRun() {
        return isRun;
    }

    /**
     * <p>Title: isSleep</p>
     * <p>Description:是否处于休眠态</p>
     *
     * @param @return 设定文件
     * @return boolean 返回类型
     * @throws
     */
    public boolean isSleep() {
        return isSleep;
    }

    public boolean isDead() {
        return isDead;
    }

    /**
     * <p>Title: sleep</p>
     * <p>Description:休眠线程</p>
     * @param @param  millis
     * @return void 返回类型
     * @throws
     */
    public void sleep(int millis) {
        isSleep = true;
        try {
            Thread.sleep(millis);
            isSleep=false;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}