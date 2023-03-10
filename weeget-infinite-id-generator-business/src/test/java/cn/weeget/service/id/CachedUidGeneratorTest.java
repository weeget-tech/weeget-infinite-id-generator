package cn.weeget.service.id;


import cn.weeget.service.id.baidu.UidGenerator;
import org.apache.commons.lang.StringUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.atomic.AtomicInteger;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CachedUidGeneratorTest {

    private static final int SIZE = 7000000; // 700w
    private static final boolean VERBOSE = false;
    private static final int THREADS = Runtime.getRuntime().availableProcessors() << 1;

    @Resource
    @Qualifier("cachedUidGenerator")
    private UidGenerator uidGenerator;

    /**
     * Test for serially generate
     *
     * @throws IOException
     */
    @Test
    public void testSerialGenerate() throws IOException {
        // Generate UID serially
        long start = System.currentTimeMillis();
        Set<Long> uidSet = new HashSet<>(SIZE);
        for (int i = 0; i < SIZE; i++) {
            doGenerate(uidSet, i);
        }
        long costTime = System.currentTimeMillis() - start;
        System.out.println("耗时:" + costTime);
        // Check UIDs are all unique
        checkUniqueID(uidSet);
    }

    /**
     * Test for parallel generate
     *
     * @throws InterruptedException
     * @throws IOException
     */
    @Test
    public void testParallelGenerate() throws InterruptedException, IOException {
        long start = System.currentTimeMillis();
        AtomicInteger control = new AtomicInteger(-1);
        Set<Long> uidSet = new ConcurrentSkipListSet<>();

        // Initialize threads
        List<Thread> threadList = new ArrayList<>(THREADS);
        for (int i = 0; i < THREADS; i++) {
            Thread thread = new Thread(() -> workerRun(uidSet, control));
            thread.setName("UID-generator-" + i);

            threadList.add(thread);
            thread.start();
        }

        // Wait for worker done
        for (Thread thread : threadList) {
            thread.join();
        }

        long costTime = System.currentTimeMillis() - start;


        // Check generate 700w times
        Assert.assertEquals(SIZE, control.get());

        // Check UIDs are all unique
        checkUniqueID(uidSet);
        System.out.println("耗时:" + costTime);
    }

    /**
     * Woker run
     */
    private void workerRun(Set<Long> uidSet, AtomicInteger control) {
        for (;;) {
            int myPosition = control.updateAndGet(old -> (old == SIZE ? SIZE : old + 1));
            if (myPosition == SIZE) {
                return;
            }

            doGenerate(uidSet, myPosition);
        }
    }

    /**
     * Do generating
     */
    private void doGenerate(Set<Long> uidSet, int index) {
        long uid = uidGenerator.getUID();
        String parsedInfo = uidGenerator.parseUID(uid);
        boolean existed = !uidSet.add(uid);
        if (existed) {
            System.out.println("Found duplicate UID " + uid);
        }

        // Check UID is positive, and can be parsed
        Assert.assertTrue(uid > 0L);
        Assert.assertTrue(StringUtils.isNotBlank(parsedInfo));

        if (VERBOSE) {
            System.out.println(Thread.currentThread().getName() + " No." + index + " >>> " + parsedInfo);
        }
    }

    /**
     * Check UIDs are all unique
     */
    private void checkUniqueID(Set<Long> uidSet) throws IOException {
        System.out.println(uidSet.size());
        Assert.assertEquals(SIZE, uidSet.size());
    }


}
